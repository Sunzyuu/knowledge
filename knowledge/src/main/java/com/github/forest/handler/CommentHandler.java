package com.github.forest.handler;

import com.alibaba.fastjson.JSON;
import com.github.forest.core.constant.NotificationConstant;
import com.github.forest.entity.Comment;
import com.github.forest.handler.event.CommentEvent;
import com.github.forest.mapper.CommentMapper;
import com.github.forest.util.Html2TextUtil;
import com.github.forest.util.NotificationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.Resource;
import javax.mail.MessagingException;

/**
 * @author sunzy
 * @date 2023/6/13 10:47
 */
@Slf4j
@Component
public class CommentHandler {
    private static final int MAX_PREVIEW = 200;

    @Resource
    private CommentMapper commentMapper;

    @TransactionalEventListener
    public void processCommentCreatedEvent(CommentEvent commentEvent) throws MessagingException {
        log.info(String.format("开始执行评论发布事件：[%s]", JSON.toJSONString(commentEvent)));
        int length = commentEvent.getContent().length();
        if( length > MAX_PREVIEW) {
            length = MAX_PREVIEW;
        }

        String commentPreviewContent = commentEvent.getContent().substring(0, length);
        String commentContent = Html2TextUtil.getContent(commentPreviewContent);
        // 判断是否回复消息
        if (commentEvent.getCommentOriginalCommentId() != null && commentEvent.getCommentOriginalCommentId() != 0) {
            Comment originalComment = commentMapper.selectById(commentEvent.getCommentOriginalCommentId());
            // 回复消息时， 评论者不是上级评论作者则进行消息通知
            if(!commentEvent.getCommentAuthorId().equals(originalComment.getCommentAuthorId())) {
                NotificationUtils.saveNotification(originalComment.getCommentAuthorId(), commentEvent.getIdComment(), NotificationConstant.Comment, commentContent);
            }
        } else {
            // 评论者不是作者本人则进行消息通知
            if (!commentEvent.getCommentAuthorId().equals(commentEvent.getArticleAuthorId())) {
                NotificationUtils.saveNotification(commentEvent.getArticleAuthorId(), commentEvent.getIdComment(), NotificationConstant.Comment, commentContent);
            }
        }
    }
}
