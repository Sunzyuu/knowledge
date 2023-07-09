package com.github.forest.mapper;

import com.github.forest.dto.TransactionRecordDTO;
import com.github.forest.entity.TransactionRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 交易记录表  Mapper 接口
 * </p>
 *
 * @author sunzy
 * @since 2023-07-02
 */
public interface TransactionRecordMapper extends BaseMapper<TransactionRecord> {
    /**
     * 交易
     * @param formBankAccount
     * @param money
     * @return
     */
    Integer debit(@Param("formBankAccount") String formBankAccount, @Param("money") BigDecimal money);

    /**
     * 查询指定账户的交易记录
     * @param bankAccount
     * @param startDate
     * @param endDate
     * @return
     */
    List<TransactionRecordDTO> selectTransactionRecords(@Param("bankAccount") String bankAccount, @Param("startDate") String startDate, @Param("endDate") String endDate);


    /**
     * 校验今日是否已发放答题奖励
     *
     * @param bankAccount
     * @param funds
     * @return
     */
    Boolean existsWithBankAccountAndFunds(@Param("bankAccount") String bankAccount, @Param("funds") String funds);


    /**
     * 查询是否已发放
     *
     * @param bankAccount
     * @return
     */
    Boolean existsWithNewbieRewards(@Param("bankAccount") String bankAccount);


    /**
     *
     * @param toBankAccount
     * @param money
     * @return
     */
    Integer credit(@Param("toBankAccount") String toBankAccount, @Param("money") BigDecimal money);

}
