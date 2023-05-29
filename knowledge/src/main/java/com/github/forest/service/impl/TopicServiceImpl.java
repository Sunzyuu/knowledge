package com.github.forest.service.impl;

import com.github.forest.entity.Topic;
import com.github.forest.mapper.TopicMapper;
import com.github.forest.service.TopicService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 主题表 服务实现类
 * </p>
 *
 * @author sunzy
 * @since 2023-05-29
 */
@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements TopicService {

}
