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
 * 作品集表
 * </p>
 *
 * @author sunzy
 * @since 2023-05-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("forest_portfolio")
public class Portfolio implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long idPortfolio;

    /**
     * 作品集头像
     */
    private String portfolioHeadImgUrl;

    /**
     * 作品集名称
     */
    private String portfolioTitle;

    /**
     * 作品集作者
     */
    private Long portfolioAuthorId;

    /**
     * 作品集介绍
     */
    private String portfolioDescription;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     *  作品集介绍HTML
     */
    private String portfolioDescriptionHtml;


}
