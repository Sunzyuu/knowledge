package com.github.forest.openai.impl;

import com.github.forest.openai.service.SseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sunzy
 * @date 2023/6/27 12:44
 */
@Slf4j
@Service
public class SseServiceImpl implements SseService {
    private static final Map<Long, SseEmitter> sessionMap = new ConcurrentHashMap<>();

    /**
     * 该方法用于建立SSE连接
     * @param idUser
     * @return
     */
    @Override
    public SseEmitter connect(Long idUser) {
        if (existsUser(idUser)) {
            removeUser(idUser);
        }
        SseEmitter sseEmitter = new SseEmitter(0L);
        sseEmitter.onError((err) -> {
            log.error("type: SseSession Error, msg: {} session Id : {}", err.getMessage(), idUser);
            onError(idUser, err);
        });

        sseEmitter.onTimeout(() -> {
            log.info("type: SseSession Timeout, session Id : {}", idUser);
            removeUser(idUser);
        });

        sseEmitter.onCompletion(() -> {
            log.info("type: SseSession Completion, session Id : {}", idUser);
            removeUser(idUser);
        });
        addUser(idUser, sseEmitter);
        return sseEmitter;
    }

    /**
     * 用于向指定用户发送消息
     * 首先，该方法会检查用户是否存在于sessionMap中。
     * 如果存在，则调用相应用户的SseEmitter对象的send方法发送消息内容；
     * 如果不存在，则抛出IllegalArgumentException异常。
     * @param idUser
     * @param content
     * @return
     */
    @Override
    public boolean send(Long idUser, String content) {
        if (existsUser(idUser)) {
            try {
                sendMessage(idUser, content);
                return true;
            } catch (IOException exception) {
                log.error("type: SseSession send Error:IOException, msg: {} session Id : {}", exception.getMessage(), idUser);
            }
        } else {
            throw new IllegalArgumentException("User Id " + idUser + " not Found");
        }
        return false;
    }

    /**
     * 该方法用于关闭指定用户的SSE连接
     *
     * @param idUser
     */
    @Override
    public void close(Long idUser) {
        log.info("type: SseSession Close, session Id : {}", idUser);
        removeUser(idUser);
    }

    private void addUser(Long idUser, SseEmitter sseEmitter) {
        sessionMap.put(idUser, sseEmitter);
    }

    /**
     * 该方法用于处理SSE连接出现错误的情况
     * @param sessionKey
     * @param throwable
     */
    private void onError(Long sessionKey, Throwable throwable) {
        SseEmitter sseEmitter = sessionMap.get(sessionKey);
        if (sseEmitter != null) {
            sseEmitter.completeWithError(throwable);
        }
    }

    /**
     * 移除会话中的用户
     * @param idUser
     */
    private void removeUser(Long idUser) {
        sessionMap.remove(idUser);
    }

    private boolean existsUser(Long idUser) {
        return sessionMap.containsKey(idUser);
    }

    /**
     * 向用户发送消息
     * @param idUser
     * @param content
     * @throws IOException
     */
    private void sendMessage(Long idUser, String content) throws IOException {
        sessionMap.get(idUser).send(content);
    }
}
