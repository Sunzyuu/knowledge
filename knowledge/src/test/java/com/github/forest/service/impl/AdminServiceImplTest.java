package com.github.forest.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.forest.core.result.GlobalResult;
import com.github.forest.entity.Topic;
import com.github.forest.service.AdminService;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author sunzy
 * @date 2023/5/31 21:54
 */
@SpringBootTest
class AdminServiceImplTest {

    @Resource
    private AdminService adminService;

    @Test
    void getTopics() {
        GlobalResult<PageInfo<Topic>> topics = adminService.getTopics(0, 10);
        System.out.println(topics.getData());
    }
}