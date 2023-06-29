package com.github.forest.openai;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.forest.core.result.GlobalResult;
import com.github.forest.core.result.GlobalResultGenerator;
import com.github.forest.entity.User;
import com.github.forest.openai.service.OpenAiService;
import com.github.forest.openai.service.SseService;
import com.github.forest.util.UserUtils;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import okhttp3.OkHttpClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Retrofit;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;
import java.util.ArrayList;

import static com.github.forest.openai.service.OpenAiService.*;

/**
 * @author sunzy
 * @date 2023/6/27 12:48
 */
@RestController
@RequestMapping("/api/v1/openai")
public class OpenAiController {

    @Resource
    private SseService sseService;

    @Value("${openai.token}")
    private String token;

    @PostMapping("/chat")
    public GlobalResult chat(@RequestBody JSONObject jsonObject) {
        ObjectMapper mapper = defaultObjectMapper();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        OkHttpClient client =  defaultClient(token,Duration.ofSeconds(10000))
                .newBuilder()
                .proxy(proxy)
                .build();
        Retrofit retrofit = defaultRetrofit(client, mapper);
        OpenAiApi api = retrofit.create(OpenAiApi.class);
        String message = jsonObject.getString("message");
        if (StringUtils.isBlank(message)) {
            throw new IllegalArgumentException("参数异常！");
        }
        User user = UserUtils.getCurrentUserByToken();
        ChatMessage chatMessage = new ChatMessage("user", message);
        ArrayList<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(chatMessage);
        OpenAiService service = new OpenAiService(api);
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .stream(true)
                .messages(chatMessages)
                .build();
        service.streamChatCompletion(completionRequest).doOnError(Throwable::printStackTrace)
                .blockingForEach(chunk -> {
                    String text = chunk.getChoices().get(0).getMessage().getContent();
                    if (text == null) {
                        return;
                    }
                    System.out.print(text);
                    sseService.send(user.getId(), text);
                });
        service.shutdownExecutor();
        return GlobalResultGenerator.genSuccessResult();
    }

}
