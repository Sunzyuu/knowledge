package com.github.forest.mapper;

import com.github.forest.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sunzy
 * @since 2023-05-28
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> selectRoleByIdUser(@Param("id") Long id);
}
