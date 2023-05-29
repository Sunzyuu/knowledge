package com.github.forest.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 产品详情表
 * </p>
 *
 * @author sunzy
 * @since 2023-05-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("forest_product_content")
public class ProductContent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产品表主键
     */
    private Integer idProduct;

    /**
     * 产品详情原文
     */
    private String productContent;

    /**
     * 产品详情 Html
     */
    private String productContentHtml;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;


}
