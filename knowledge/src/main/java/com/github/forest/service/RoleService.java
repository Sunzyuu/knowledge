package com.github.forest.service;

import com.github.forest.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.forest.entity.User;
import com.github.forest.mapper.RoleMapper;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sunzy
 * @since 2023-05-28
 */
public interface RoleService extends IService<Role> {

    List<Role> selectRoleByUser(User sysUser);

}
