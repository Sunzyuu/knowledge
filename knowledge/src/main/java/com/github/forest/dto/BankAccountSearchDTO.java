package com.github.forest.dto;

import lombok.Data;

/**
 * @author sunzy
 * @date 2023/7/9 16:10
 */
@Data
public class BankAccountSearchDTO {
    /**
     * 所属银行名称
     */
    private String bankName;
    /**
     * 银行账户
     */
    private String bankAccount;
    /**
     * 账户所有者姓名
     */
    private String accountOwnerName;

}