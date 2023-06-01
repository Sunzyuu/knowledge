package com.github.forest.dto;

import lombok.Data;

/**
 * @author sunzy
 * @date 2023/6/1 15:37
 */
@Data
public class TagDTO {

    private Integer idTag;

    private String tagTitle;

    private String tagUri;

    private String tagDescription;

    private String tagIconPath;

    private Integer tagAuthorId;

    private Author tagAuthor;

}
