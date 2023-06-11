package com.github.forest.service.impl;

import com.github.forest.entity.Notification;
import com.github.forest.mapper.NotificationMapper;
import com.github.forest.service.NotificationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 通知表  服务实现类
 * </p>
 *
 * @author sunzy
 * @since 2023-06-11
 */
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

}
