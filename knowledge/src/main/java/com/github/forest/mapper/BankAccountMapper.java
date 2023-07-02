package com.github.forest.mapper;

import com.github.forest.dto.BankAccountDTO;
import com.github.forest.entity.BankAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 银行账户表  Mapper 接口
 * </p>
 *
 * @author sunzy
 * @since 2023-06-29
 */
public interface BankAccountMapper extends BaseMapper<BankAccount> {

    /**
     * 查询银行账户
     *
     * @param bankName
     * @param accountOwnerName
     * @param bankAccount
     * @return
     */
    List<BankAccountDTO> selectBankAccounts(@Param("bankName") String bankName, @Param("accountOwnerName") String accountOwnerName, @Param("bankAccount") String bankAccount);

    /**
     * 获取银行账户信息
     * @param idBank
     * @return
     */
    BankAccountDTO selectBankAccount(@Param("idBank") Long idBank);


    /**
     * 获取当前最大卡号
     *
     * @return
     */
    String selectMaxBankAccount();
    /**
     * 根据卡号获取银行账号信息
     *
     * @param bankAccount
     * @return
     */
    BankAccountDTO selectByBankAccount(@Param("bankAccount") String bankAccount);



    /**
     * 查询用户个人银行账户信息
     *
     * @param idUser
     * @return
     */
    BankAccountDTO findPersonBankAccountByIdUser(@Param("idUser") Long idUser);
}
