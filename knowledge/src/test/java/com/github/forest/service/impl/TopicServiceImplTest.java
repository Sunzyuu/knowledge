package com.github.forest.service.impl;

import com.github.forest.entity.Topic;
import com.github.forest.service.TopicService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author sunzy
 * @date 2023/6/1 15:50
 */
@SpringBootTest
class TopicServiceImplTest {

    @Resource
    private TopicService topicService;

    @Test
    void findTopicNav() {
        List<Topic> topicNav = topicService.findTopicNav();
        topicNav.forEach(System.out::println);
    }
}