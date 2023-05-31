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
 * 主题表
 * </p>
 *
 * @author sunzy
 * @since 2023-05-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("forest_topic")
public class Topic implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 专题标题
     */
    private String topicTitle;

    /**
     * 专题路径
     */
    private String topicUri;

    /**
     * 专题描述
     */
    private String topicDescription;

    /**
     * 专题类型
     */
    private String topicType;

    /**
     * 专题序号 10
     */
    private Integer topicSort;

    /**
     * 专题图片路径
     */
    private String topicIconPath;

    /**
     * 0：作为导航1：不作为导航 0
     */
    private String topicNva;

    /**
     * 专题下标签总数 0
     */
    private Integer topicTagCount;

    /**
     * 0：正常1：禁用 0
     */
    private String topicStatus;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 专题描述 Html
     */
    private String topicDescriptionHtml;


}
