package com.github.forest.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.forest.core.result.GlobalResult;
import com.github.forest.core.result.GlobalResultGenerator;
import com.github.forest.entity.Topic;
import com.github.forest.mapper.TopicMapper;
import com.github.forest.service.AdminService;
import com.github.forest.service.TopicService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sunzy
 * @date 2023/5/31 21:41
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private TopicService topicService;

    @Resource
    private TopicMapper topicMapper;

    @Override
    public GlobalResult<PageInfo<Topic>> getTopics(Integer page, Integer pageSize) {
//        IPage<Topic> pageInfo = new Page<>(page, pageSize);
        PageHelper.startPage(page, pageSize);
        List<Topic> topics = topicService.list();
        PageInfo<Topic> pageInfo = new PageInfo<>(topics);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }
}
