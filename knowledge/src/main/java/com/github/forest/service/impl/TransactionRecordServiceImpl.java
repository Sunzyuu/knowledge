package com.github.forest.service.impl;

import com.github.forest.entity.TransactionRecord;
import com.github.forest.mapper.TransactionRecordMapper;
import com.github.forest.service.TransactionRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 交易记录表  服务实现类
 * </p>
 *
 * @author sunzy
 * @since 2023-07-02
 */
@Service
public class TransactionRecordServiceImpl extends ServiceImpl<TransactionRecordMapper, TransactionRecord> implements TransactionRecordService {

}
