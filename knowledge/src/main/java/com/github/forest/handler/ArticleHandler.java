package com.github.forest.handler;

import com.alibaba.fastjson.JSON;
import com.github.forest.core.constant.NotificationConstant;
import com.github.forest.handler.event.ArticleEvent;
import com.github.forest.lucene.service.LuceneService;
import com.github.forest.util.NotificationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.Resource;

/**
 * @author sunzy
 * @date 2023/6/11 16:59
 */
@Slf4j
@Component
public class ArticleHandler {
    @Resource
    private LuceneService luceneService;

    @TransactionalEventListener
    public void processArticlePostEvent(ArticleEvent articleEvent) {
        log.info(String.format("执行文章发布相关事件：[%s]", JSON.toJSONString(articleEvent)));
        // 系统通知
        if(articleEvent.getNotification()) {
            NotificationUtils.sendAnnouncement(articleEvent.getIdArticle(), NotificationConstant.Article, articleEvent.getArticleTitle())
        } else {
            // 发送关注通知
            StringBuilder dataSummary = new StringBuilder();
            if(articleEvent.getIsUpdate()) {
                dataSummary.append(articleEvent.getNickname()).append("更新了文章: ").append(articleEvent.getArticleTitle());
                NotificationUtils.sendArticlePush(articleEvent.getIdArticle(), NotificationConstant.UpdateArticle, dataSummary.toString(), articleEvent.getArticleAuthorId());

            }
        }

    }
}
