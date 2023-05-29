package com.github.forest.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 作品集与文章关系表
 * </p>
 *
 * @author sunzy
 * @since 2023-05-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("forest_portfolio_article")
public class PortfolioArticle implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 作品集表主键
     */
    private Long idPortfolio;

    /**
     * 文章表主键
     */
    private Long idArticle;

    /**
     * 排序号
     */
    private Integer sortNo;


}
