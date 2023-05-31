package com.github.forest.service.impl;

import com.github.forest.entity.Role;
import com.github.forest.entity.User;
import com.github.forest.mapper.RoleMapper;
import com.github.forest.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sunzy
 * @since 2023-05-28
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<Role> selectRoleByUser(User sysUser) {
        return roleMapper.selectRoleByIdUser(sysUser.getId());
    }
}
