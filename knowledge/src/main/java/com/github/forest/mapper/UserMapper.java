package com.github.forest.mapper;

import com.github.forest.dto.UserDTO;
import com.github.forest.dto.UserInfoDTO;
import com.github.forest.dto.UserSearchDTO;
import com.github.forest.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    Integer findRoleWeightsByUser(@Param("idUser") Long idUser);

    /**
     * 查询用户数据
     *
     * @param searchDTO
     * @return
     */
    List<UserInfoDTO> selectUsers(@Param("searchDTO") UserSearchDTO searchDTO);
}
