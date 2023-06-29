package com.github.forest.service.impl;

import com.github.forest.entity.Bank;
import com.github.forest.mapper.BankMapper;
import com.github.forest.service.BankService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 银行表  服务实现类
 * </p>
 *
 * @author sunzy
 * @since 2023-06-29
 */
@Service
public class BankServiceImpl extends ServiceImpl<BankMapper, Bank> implements BankService {

}
