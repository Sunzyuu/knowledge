package com.github.forest.service.impl;

import com.github.forest.entity.Follow;
import com.github.forest.mapper.FollowMapper;
import com.github.forest.service.FollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 关注表  服务实现类
 * </p>
 *
 * @author sunzy
 * @since 2023-06-12
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements FollowService {

}
