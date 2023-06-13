package com.github.forest.handler;

import com.alibaba.fastjson.JSON;
import com.github.forest.core.constant.NotificationConstant;
import com.github.forest.handler.event.FollowEvent;
import com.github.forest.util.NotificationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.mail.MessagingException;

/**
 * @author sunzy
 * @date 2023/6/13 11:14
 */
@Slf4j
@Component
public class FollowHandler {

    @TransactionalEventListener
    public void processFollowEvent(FollowEvent followEvent) throws MessagingException {
        log.info(String.format("执行关注相关事件: [%s]", JSON.toJSONString(followEvent)));
        NotificationUtils.saveNotification(followEvent.getFollowingId(), followEvent.getIdFollow(), NotificationConstant.Follow, followEvent.getSummary());
        log.info("执行完成关注相关事件...");
    }
}