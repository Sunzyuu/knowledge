package com.github.forest.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 银行账户表 
 * </p>
 *
 * @author sunzy
 * @since 2023-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("forest_bank_account")
public class BankAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long idBankAccount;

    /**
     * 所属银行
     */
    private Long idBank;

    /**
     * 银行账户
     */
    private String bankAccount;

    /**
     * 账户余额
     */
    private BigDecimal accountBalance;

    /**
     * 账户所有者
     */
    private Long accountOwner;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 0: 普通账户 1: 银行账户
     */
    private String accountType;


}
