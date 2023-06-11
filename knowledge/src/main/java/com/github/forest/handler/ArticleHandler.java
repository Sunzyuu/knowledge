package com.github.forest.handler;

import com.github.forest.handler.event.ArticleEvent;
import com.github.forest.lucene.service.LuceneService;
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


    }
}
