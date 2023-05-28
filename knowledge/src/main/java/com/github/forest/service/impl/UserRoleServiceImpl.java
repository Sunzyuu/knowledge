package com.github.forest.service.impl;

import com.github.forest.entity.UserRole;
import com.github.forest.mapper.UserRoleMapper;
import com.github.forest.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户权限表  服务实现类
 * </p>
 *
 * @author sunzy
 * @since 2023-05-28
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
