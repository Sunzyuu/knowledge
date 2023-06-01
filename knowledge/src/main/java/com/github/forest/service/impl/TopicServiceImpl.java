package com.github.forest.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.forest.core.exception.BusinessException;
import com.github.forest.core.exception.ServiceException;
import com.github.forest.entity.Topic;
import com.github.forest.mapper.TopicMapper;
import com.github.forest.service.TopicService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.forest.util.XssUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

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
    @Resource
    private TopicMapper topicMapper;


    /**
     * 获取导航栏的topic
     * @return
     */
    @Override
    public List<Topic> findTopicNav() {
        LambdaUpdateWrapper<Topic> topicLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        topicLambdaUpdateWrapper.eq(true, Topic::getTopicNva, 0);
        topicLambdaUpdateWrapper.eq(true, Topic::getTopicStatus, 0);
        topicLambdaUpdateWrapper.orderBy(true, true, Topic::getTopicSort);
        return list(topicLambdaUpdateWrapper);
    }

    @Override
    public Topic findTopicByTopicUri(String topicUri) {
        LambdaUpdateWrapper<Topic> topicLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        topicLambdaUpdateWrapper.eq(true, Topic::getTopicUri, topicUri);
        topicLambdaUpdateWrapper.eq(true, Topic::getTopicStatus, 0);
        topicLambdaUpdateWrapper.orderBy(true, true, Topic::getTopicSort);
        return getOne(topicLambdaUpdateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Topic saveTopic(Topic topic) throws ServiceException {
        topic.setTopicDescriptionHtml(XssUtils.filterHtmlCode(topic.getTopicDescriptionHtml()));
        if(topic.getId() == null) {
            // 新增
            if (StringUtils.isBlank(topic.getTopicTitle())) {
                throw new IllegalArgumentException("标签名不能为空!");
            } else {
                Integer count = query().eq("topic_title", topic.getTopicTitle()).count();
                if(count > 0) {
                    throw new BusinessException("专题 '" + topic.getTopicTitle() + "' 已存在!");
                }
            }
            if(StringUtils.isNotBlank(topic.getTopicIconPath()) && topic.getTopicIconPath().contains("base64")) {
                // todo::保存base64格式的图片信息
            } else {
                topic.setTopicIconPath(topic.getTopicIconPath());
            }
            topic.setTopicNva(topic.getTopicNva());
            topic.setTopicStatus(topic.getTopicStatus());
            topic.setTopicSort(topic.getTopicSort());
            topic.setTopicDescription(topic.getTopicDescription());
            topic.setTopicDescriptionHtml(topic.getTopicDescriptionHtml());
            topic.setCreatedTime(new Date());
            topic.setUpdatedTime(topic.getCreatedTime());
        } else {
            // 更新
            if (StringUtils.isNotBlank(topic.getTopicIconPath()) && topic.getTopicIconPath().contains("base64")) {
                // todo::保存base64格式的图片信息
                // String topicIconPath = UploadController.uploadBase64File(topic.getTopicIconPath(), 3);
//                topic.setTopicIconPath(topicIconPath);
            }
            topic.setUpdatedTime(new Date());
        }
        if(!saveOrUpdate(topic)) {
            throw new ServiceException("操作失败");
        }
        return topic;
    }




}
