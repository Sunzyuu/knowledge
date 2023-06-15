package com.github.forest.config;

import com.github.forest.auth.JwtConstants;
import com.github.forest.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author sunzy
 * @date 2023/6/15 9:43
 */
@Slf4j
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    @Resource
    private UserService userService;

    @Autowired
    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }


    /**
     * 针对redis key失效事件，进行数据处理
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expireKey = message.toString();
        if (expireKey.contains(JwtConstants.LAST_ONLINE)) {
            String account = expireKey.replace(JwtConstants.LAST_ONLINE, "");
            log.info("拿到的过期数据： {}", expireKey);
            log.info("处理后的过期数据： {}", account);
            userService.updateLastOnlineTimeByAccount(account);
        }
        super.onMessage(message, pattern);
    }
}
