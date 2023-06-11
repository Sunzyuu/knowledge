package com.github.forest.service;

import com.github.forest.dto.NotificationDTO;
import com.github.forest.entity.Notification;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 通知表  服务类
 * </p>
 *
 * @author sunzy
 * @since 2023-06-11
 */
public interface NotificationService extends IService<Notification> {
    /**
     * 获取未读消息数据
     *
     * @param idUser
     * @return
     */
    List<Notification> findUnreadNotifications(Long idUser);

    /**
     * 获取消息数据
     *
     * @param idUser
     * @return
     */
    List<NotificationDTO> findNotifications(Long idUser);

    /**
     * 获取消息数据
     *
     * @param idUser
     * @param dataId
     * @param dataType
     * @return
     */
    Notification findNotification(Long idUser, Long dataId, String dataType);

    /**
     * 创建系统通知
     *
     * @param idUser
     * @param dataId
     * @param dataType
     * @param dataSummary
     * @return
     */
    Integer save(Long idUser, Long dataId, String dataType, String dataSummary);

    /**
     * 标记消息已读
     *
     * @param id
     * @param idUser
     * @return
     */
    Integer readNotification(Long id, Long idUser);

    /**
     * 标记所有消息已读
     *
     * @return
     */
    Integer readAllNotification(Long idUser);

    /**
     * 删除相关未读消息
     *
     * @param dataId
     * @param dataType
     * @return
     */
    Integer deleteUnreadNotification(Long dataId, String dataType);

}
