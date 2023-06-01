package com.github.forest.service;

import com.github.forest.core.exception.ServiceException;
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

    /**
     * 查询用户角色
     *
     * @param idUser
     * @return
     */
    List<Role> findByIdUser(Long idUser);

    /**
     * 更新用户状态
     *
     * @param idRole
     * @param status
     * @return
     * @throws ServiceException
     */
    boolean updateStatus(Long idRole, String status) throws ServiceException;

    /**
     * 添加/更新角色
     *
     * @param role
     * @return
     * @throws ServiceException
     */
    boolean saveRole(Role role) throws ServiceException;

}
