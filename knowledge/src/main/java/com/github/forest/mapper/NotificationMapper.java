package com.github.forest.mapper;

import com.github.forest.dto.NotificationDTO;
import com.github.forest.entity.Notification;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 通知表  Mapper 接口
 * </p>
 *
 * @author sunzy
 * @since 2023-06-11
 */
public interface NotificationMapper extends BaseMapper<Notification> {
    /**
     * 获取未读通知数据
     *
     * @param idUser
     * @return
     */
    List<Notification> selectUnreadNotifications(@Param("idUser") Long idUser);

    /**
     * 获取消息数据
     *
     * @param idUser
     * @return
     */
    List<NotificationDTO> selectNotifications(@Param("idUser") Long idUser);

    /**
     * 获取消息数据
     *
     * @param idUser
     * @param dataId
     * @param dataType
     * @return
     */
    Notification selectNotification(@Param("idUser") Long idUser, @Param("dataId") Long dataId, @Param("dataType") String dataType);

    /**
     * 标记所有消息已读
     *
     * @param idUser
     * @return
     */
    Integer readAllNotification(@Param("idUser") Long idUser);
}
