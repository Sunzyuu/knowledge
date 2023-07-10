package com.github.forest.service.impl;

import com.github.forest.dto.BankAccountDTO;
import com.github.forest.dto.BankAccountSearchDTO;
import com.github.forest.dto.TransactionRecordDTO;
import com.github.forest.entity.BankAccount;
import com.github.forest.mapper.BankAccountMapper;
import com.github.forest.service.BankAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 银行账户表  服务实现类
 * </p>
 *
 * @author sunzy
 * @since 2023-06-29
 */
@Service
public class BankAccountServiceImpl extends ServiceImpl<BankAccountMapper, BankAccount> implements BankAccountService {


    @Resource
    private BankAccountMapper bankAccountMapper;


    @Override
    public List<BankAccountDTO> findBankAccounts(BankAccountSearchDTO bankAccountSearchDTO) {
        return null;
    }

    @Override
    public BankAccountDTO findBankAccountByIdUser(Long idUser) {
        return null;
    }

    @Override
    public BankAccountDTO findByBankAccount(String bankAccount) {
        return null;
    }

    @Override
    public BankAccount findSystemBankAccount() {
        return null;
    }

    @Override
    public BankAccount findInfoByBankAccount(String formBankAccount) {
        return null;
    }

    @Override
    public List<TransactionRecordDTO> findUserTransactionRecords(String bankAccount, String startDate, String endDate) {
        return null;
    }

    @Override
    public BankAccount createBankAccount(Long idUser) {
        return null;
    }
}
