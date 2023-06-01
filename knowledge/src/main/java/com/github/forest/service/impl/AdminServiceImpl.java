package com.github.forest.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.forest.core.result.GlobalResult;
import com.github.forest.core.result.GlobalResultGenerator;
import com.github.forest.dto.UserInfoDTO;
import com.github.forest.dto.UserSearchDTO;
import com.github.forest.entity.Topic;
import com.github.forest.entity.User;
import com.github.forest.mapper.TopicMapper;
import com.github.forest.service.AdminService;
import com.github.forest.service.TopicService;
import com.github.forest.service.UserService;
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
    private UserService userService;

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

    @Override
    public GlobalResult<PageInfo<UserInfoDTO>> getUsers(Integer page, Integer pageSize, UserSearchDTO userSearchDTO) {
        PageHelper.startPage(page, pageSize);
        List<UserInfoDTO> users = userService.findUsers(userSearchDTO);
        PageInfo<UserInfoDTO> pageInfo = new PageInfo<>(users);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }
}
