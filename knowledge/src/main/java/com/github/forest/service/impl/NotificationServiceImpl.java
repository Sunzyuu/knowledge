package com.github.forest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.forest.core.exception.BusinessException;
import com.github.forest.dto.NotificationDTO;
import com.github.forest.entity.Notification;
import com.github.forest.mapper.NotificationMapper;
import com.github.forest.service.NotificationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.forest.util.NotificationUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

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
    @Resource
    private NotificationMapper notificationMapper;

    private final static String UN_READ = "0";

    @Override
    public List<Notification> findUnreadNotifications(Long idUser) {
        return notificationMapper.selectUnreadNotifications(idUser);
    }

    @Override
    public List<NotificationDTO> findNotifications(Long idUser) {
        List<NotificationDTO> list = notificationMapper.selectNotifications(idUser);
        list.forEach(notification -> {
            NotificationDTO notificationDTO = NotificationUtils.genNotification(notification);
            // 判断关联数据是否已删除
            if(Objects.nonNull(notification.getAuthor())) {
                BeanUtils.copyProperties(notification, notificationDTO);
            } else {
                // 关联数据已删除,且未读
                if(UN_READ.equals(notification.getHasRead())) {
                    readNotification(notification.getIdNotification(), idUser);
                }
                NotificationDTO dto = new NotificationDTO();
                dto.setDataSummary("该消息已被撤销!");
                dto.setDataType("-1");
                dto.setHasRead("1");
                dto.setCreatedTime(notification.getCreatedTime());
                BeanUtils.copyProperties(notification, notificationDTO);
            }
        });
        return list;
    }

    @Override
    public Notification findNotification(Long idUser, Long dataId, String dataType) {
        return notificationMapper.selectNotification(idUser, dataId, dataType);
    }

    @Override
    public Integer save(Long idUser, Long dataId, String dataType, String dataSummary) {
        Notification notification = new Notification();
        notification.setDataId(dataId);
        notification.setDataType(dataType);
        notification.setIdUser(idUser);
        notification.setDataSummary(dataSummary);
        boolean result = save(notification);
        if(!result) {
            throw new BusinessException("添加通知失败");
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer readNotification(Long id, Long idUser) {
        LambdaUpdateWrapper<Notification> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(true, Notification::getIdNotification, id);
        updateWrapper.eq(true, Notification::getIdUser, idUser);
        updateWrapper.set(true, Notification::getHasRead, "1");
        boolean result = update(updateWrapper);
        if(!result) {
            throw new BusinessException("读取通知失败");
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer readAllNotification(Long idUser) {
        return notificationMapper.readAllNotification(idUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteUnreadNotification(Long dataId, String dataType) {
        return notificationMapper.deleteUnreadNotification(dataId, dataType);
    }
}
