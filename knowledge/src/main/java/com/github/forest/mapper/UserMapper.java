package com.github.forest.mapper;

import com.github.forest.dto.UserDTO;
import com.github.forest.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户表  Mapper 接口
 * </p>
 *
 * @author sunzy
 * @since 2023-05-28
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    User selectByAccount(@Param("account") String account);

    UserDTO selectUserDTOByAccount(@Param("account") String account);
}
