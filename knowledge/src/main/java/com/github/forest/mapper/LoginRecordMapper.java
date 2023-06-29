package com.github.forest.mapper;

import com.github.forest.entity.LoginRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 登录记录表 Mapper 接口
 * </p>
 *
 * @author sunzy
 * @since 2023-06-19
 */
public interface LoginRecordMapper extends BaseMapper<LoginRecord> {

    /**
     *
     * @param idUser
     * @return
     */
    List<LoginRecord> selectLoginRecordByIdUser(@Param("idUser") Integer idUser);
}
