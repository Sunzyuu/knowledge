package com.github.forest.service.impl;

import com.github.forest.entity.UserTag;
import com.github.forest.mapper.UserTagMapper;
import com.github.forest.service.UserTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户 - 标签关联表  服务实现类
 * </p>
 *
 * @author sunzy
 * @since 2023-05-28
 */
@Service
public class UserTagServiceImpl extends ServiceImpl<UserTagMapper, UserTag> implements UserTagService {

}
