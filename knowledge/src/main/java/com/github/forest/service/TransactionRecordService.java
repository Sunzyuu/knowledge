package com.github.forest.service;

import com.github.forest.dto.TransactionRecordDTO;
import com.github.forest.entity.TransactionRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.forest.enumerate.TransactionEnum;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 交易记录表  服务类
 * </p>
 *
 * @author sunzy
 * @since 2023-07-02
 */
public interface TransactionRecordService extends IService<TransactionRecord> {
    /**
     * 交易
     *
     * @param transactionRecord
     * @return
     */
    TransactionRecord transfer(TransactionRecord transactionRecord);

    /**
     * 查询指定账户的交易记录
     *
     * @param bankAccount
     * @param startDate
     * @param endDate
     * @return
     */
    List<TransactionRecordDTO> findTransactionRecords(String bankAccount, String startDate, String endDate);

    /**
     * 根据用户主键进行交易
     *
     * @param toUserId
     * @param formUserId
     * @param transactionType
     * @return
     */
    TransactionRecord userTransfer(Long toUserId, Long formUserId, TransactionEnum transactionType);

    /**
     * 社区银行转账/奖励发放
     *
     * @param idUser
     * @param transactionType
     * @return
     */
    TransactionRecord bankTransfer(Long idUser, TransactionEnum transactionType);

    /**
     * 发放新手奖励
     *
     * @param transactionRecord
     * @return
     */
    TransactionRecord newbieRewards(TransactionRecord transactionRecord);
}
