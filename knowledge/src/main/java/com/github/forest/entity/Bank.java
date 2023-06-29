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
 * 银行表 
 * </p>
 *
 * @author sunzy
 * @since 2023-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("forest_bank")
public class Bank implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long idBank;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 银行负责人
     */
    private Long bankOwner;

    /**
     * 银行描述
     */
    private String bankDescription;

    /**
     * 创建人
     */
    private Long createdBy;

    /**
     * 创建时间
     */
    private Date createdTime;


}
