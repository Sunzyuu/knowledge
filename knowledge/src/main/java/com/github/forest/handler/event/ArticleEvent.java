package com.github.forest.handler.event;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author sunzy
 * @date 2023/6/11 17:24
 */
@Data
@AllArgsConstructor
public class ArticleEvent {

    private Long idArticle;

    private String articleTitle;

    private Boolean isUpdate;

    private Boolean notification;

    private String nickname;

    private Long articleAuthorId;
}
