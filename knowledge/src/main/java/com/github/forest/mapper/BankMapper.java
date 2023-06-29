package com.github.forest.mapper;

import com.github.forest.dto.BankDTO;
import com.github.forest.entity.Bank;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 银行表  Mapper 接口
 * </p>
 *
 * @author sunzy
 * @since 2023-06-29
 */
public interface BankMapper extends BaseMapper<Bank> {

    /**
     * 查询银行列表数据
     *
     * @return
     */
    List<BankDTO> selectBanks();
}
