package com.github.forest.service;

import com.github.forest.dto.BankAccountDTO;
import com.github.forest.dto.BankAccountSearchDTO;
import com.github.forest.dto.TransactionRecordDTO;
import com.github.forest.entity.BankAccount;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 银行账户表  服务类
 * </p>
 *
 * @author sunzy
 * @since 2023-06-29
 */
public interface BankAccountService extends IService<BankAccount> {
    /**
     * 查询银行账户列表
     *
     * @param bankAccountSearchDTO
     * @return
     */
    List<BankAccountDTO> findBankAccounts(BankAccountSearchDTO bankAccountSearchDTO);

    /**
     * 查询用户银行账户
     *
     * @param idUser
     * @return
     */
    BankAccountDTO findBankAccountByIdUser(Long idUser);

    /**
     * 根据账户查询银行账户信息
     *
     * @param bankAccount
     * @return
     */
    BankAccountDTO findByBankAccount(String bankAccount);

    /**
     * 查询系统社区银行
     *
     * @return
     */
    BankAccount findSystemBankAccount();

    /**
     * 查询账号信息
     *
     * @param formBankAccount
     * @return
     */
    BankAccount findInfoByBankAccount(String formBankAccount);

    /**
     * 根据时间查询账号交易记录
     *
     * @param bankAccount
     * @param startDate
     * @param endDate
     * @return
     */
    List<TransactionRecordDTO> findUserTransactionRecords(String bankAccount, String startDate, String endDate);

    /**
     * 创建钱包账号
     *
     * @param idUser
     * @return
     */
    BankAccount createBankAccount(Long idUser);
}
