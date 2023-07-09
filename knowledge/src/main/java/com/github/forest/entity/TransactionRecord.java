package com.github.forest.entity;

import java.math.BigDecimal;
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
 * 交易记录表 
 * </p>
 *
 * @author sunzy
 * @since 2023-07-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("forest_transaction_record")
public class TransactionRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 交易主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 交易流水号
     */
    private String transactionNo;

    /**
     * 款项
     */
    private String funds;

    /**
     * 交易发起方
     */
    private String formBankAccount;

    /**
     * 交易收款方
     */
    private String toBankAccount;

    /**
     * 交易金额
     */
    private BigDecimal money;

    /**
     * 交易类型
     */
    private String transactionType;

    /**
     * 交易时间
     */
    private Date transactionTime;


}
