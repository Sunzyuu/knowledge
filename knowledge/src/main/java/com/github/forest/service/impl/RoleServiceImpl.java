package com.github.forest.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.forest.core.exception.ServiceException;
import com.github.forest.entity.Role;
import com.github.forest.entity.User;
import com.github.forest.mapper.RoleMapper;
import com.github.forest.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.forest.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
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

    @Resource
    private UserService userService;

    @Override
    public List<Role> selectRoleByUser(User sysUser) {
        return roleMapper.selectRoleByIdUser(sysUser.getId());
    }

    @Override
    public List<Role> findByIdUser(Long idUser) {
        // select vr.* from forest_role vr join forest_user vu on vr.id_user = ur.id
        return roleMapper.selectRoleByIdUser(idUser);
    }

    @Override
    public boolean updateStatus(Long idRole, String status) throws ServiceException {
        LambdaUpdateWrapper<Role> roleLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        roleLambdaUpdateWrapper.eq(true, Role::getIdRole, idRole);
        roleLambdaUpdateWrapper.set(true, Role::getStatus, status);
        roleLambdaUpdateWrapper.set(true, Role::getUpdatedTime, new Date());
        boolean isSuccess = update(roleLambdaUpdateWrapper);
        if(!isSuccess) {
            throw new ServiceException("更新失败");
        }
        return true;
    }

    @Override
    public boolean saveRole(Role role) throws ServiceException {
        Boolean result = false;
        if(role.getIdRole() == null) {
            role.setCreatedTime(new Date());
            role.setUpdatedTime(role.getCreatedTime());
        } else {
            role.setUpdatedTime(new Date());
        }
        result = saveOrUpdate(role);
        if(!result) {
            throw new ServiceException("添加失败！");
        }
        return true;
    }
}
