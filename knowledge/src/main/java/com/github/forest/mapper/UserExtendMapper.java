package com.github.forest.mapper;

import com.github.forest.entity.UserExtend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户扩展表  Mapper 接口
 * </p>
 *
 * @author sunzy
 * @since 2023-05-28
 */
public interface UserExtendMapper extends BaseMapper<UserExtend> {

    /**
     * 获取用户扩展信息
     *
     * @param account
     * @return
     */
    UserExtend selectUserExtendByAccount(@Param("account") String account);

}
