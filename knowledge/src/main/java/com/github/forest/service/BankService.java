package com.github.forest.service;

import com.github.forest.dto.BankDTO;
import com.github.forest.entity.Bank;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 银行表  服务类
 * </p>
 *
 * @author sunzy
 * @since 2023-06-29
 */
public interface BankService extends IService<Bank> {
    List<BankDTO> findBanks();
}
