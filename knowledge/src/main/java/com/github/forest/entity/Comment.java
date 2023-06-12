package com.github.forest.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 评论表 
 * </p>
 *
 * @author sunzy
 * @since 2023-06-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("forest_comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long idComment;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 作者 id
     */
    private Long commentAuthorId;

    /**
     * 文章 id
     */
    private Long commentArticleId;

    /**
     * 锚点 url
     */
    private String commentSharpUrl;

    /**
     * 父评论 id
     */
    private Long commentOriginalCommentId;

    /**
     * 状态
     */
    private String commentStatus;

    /**
     * 评论 IP
     */
    private String commentIp;

    /**
     * User-Agent
     */
    private String commentUa;

    /**
     * 0：公开回帖，1：匿名回帖
     */
    private String commentAnonymous;

    /**
     * 回帖计数
     */
    private Integer commentReplyCount;

    /**
     * 0：所有人可见，1：仅楼主和自己可见
     */
    private String commentVisible;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;


}
