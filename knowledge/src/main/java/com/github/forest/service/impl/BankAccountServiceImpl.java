package com.github.forest.service.impl;

import com.github.forest.entity.BankAccount;
import com.github.forest.mapper.BankAccountMapper;
import com.github.forest.service.BankAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
