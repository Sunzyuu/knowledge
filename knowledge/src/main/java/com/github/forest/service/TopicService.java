package com.github.forest.service;

import com.github.forest.core.exception.ServiceException;
import com.github.forest.entity.Topic;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.forest.mapper.TopicMapper;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 主题表 服务类
 * </p>
 *
 * @author sunzy
 * @since 2023-05-29
 */
public interface TopicService extends IService<Topic> {

    /**
     * 获取导航主题数据
     *
     * @return
     */
    List<Topic> findTopicNav();

    /**
     * 根据 topicUri 获取主题信息及旗下标签数据
     *
     * @param topicUri 主题 URI
     * @return
     */
    Topic findTopicByTopicUri(String topicUri);

    /**
     * 添加新主题
     * @param topic
     * @return
     * @throws ServiceException
     */
    Topic saveTopic(Topic topic) throws ServiceException;


}
