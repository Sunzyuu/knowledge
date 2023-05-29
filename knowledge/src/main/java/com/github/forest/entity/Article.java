package com.github.forest.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 文章表 
 * </p>
 *
 * @author sunzy
 * @since 2023-05-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("forest_article")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 文章缩略图
     */
    private String articleThumbnailUrl;

    /**
     * 文章作者id
     */
    private Long articleAuthorId;

    /**
     * 文章类型
     */
    private String articleType;

    /**
     * 文章标签
     */
    private String articleTags;

    /**
     * 浏览总数
     */
    private Integer articleViewCount;

    /**
     * 预览内容
     */
    private String articlePreviewContent;

    /**
     * 评论总数
     */
    private Integer articleCommentCount;

    /**
     * 文章永久链接
     */
    private String articlePermalink;

    /**
     * 站内链接
     */
    private String articleLink;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 0:非优选1：优选
     */
    private String articlePerfect;

    /**
     * 文章状态
     */
    private String articleStatus;

    /**
     * 点赞总数
     */
    private Integer articleThumbsUpCount;

    /**
     * 赞赏总数
     */
    private Integer articleSponsorCount;


}
