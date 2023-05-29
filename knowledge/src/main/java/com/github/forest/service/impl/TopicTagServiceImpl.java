package com.github.forest.service.impl;

import com.github.forest.entity.TopicTag;
import com.github.forest.mapper.TopicTagMapper;
import com.github.forest.service.TopicTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 专题- 标签关联表  服务实现类
 * </p>
 *
 * @author sunzy
 * @since 2023-05-29
 */
@Service
public class TopicTagServiceImpl extends ServiceImpl<TopicTagMapper, TopicTag> implements TopicTagService {

}
