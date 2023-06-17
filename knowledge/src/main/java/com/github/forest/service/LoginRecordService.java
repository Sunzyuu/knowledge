package com.github.forest.service;

import com.github.forest.entity.LoginRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 登录记录表 服务类
 * </p>
 *
 * @author sunzy
 * @since 2023-06-17
 */
public interface LoginRecordService extends IService<LoginRecord> {

    /**
     * 保存登录记录
     *
     * @param idUser
     * @return
     */
    LoginRecord saveLoginRecord(Long idUser);

    /**
     * 获取用户登录记录
     *
     * @param idUser
     * @return
     */
    List<LoginRecord> findLoginRecordByIdUser(Integer idUser);

}
