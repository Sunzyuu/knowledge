package com.github.forest.util;

import com.github.forest.core.constant.NotificationConstant;
import com.github.forest.dto.NotificationDTO;
import com.github.forest.entity.Notification;
import com.github.forest.entity.User;
import com.github.forest.service.ArticleService;
import com.github.forest.service.JavaMailService;
import com.github.forest.service.NotificationService;
import com.github.forest.service.UserService;
import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;
import java.util.List;

/**
 * @author sunzy
 * @date 2023/6/11 22:06
 */
@Slf4j
public class NotificationUtils {

    private static final NotificationService notificationService = SpringContextHolder.getBean(NotificationService.class);
    private static final UserService userService = SpringContextHolder.getBean(UserService.class);
//    private static final FollowService followService = SpringContextHolder.getBean(FollowService.class);
    private static final JavaMailService mailService = SpringContextHolder.getBean(JavaMailService.class);
    private static final ArticleService articleService = SpringContextHolder.getBean(ArticleService.class);
//    private static final CommentService commentService = SpringContextHolder.getBean(CommentService.class);

    public static void sendAnnouncement(Long dataId, String dataType, String dataSummary) {
        List<User> users = userService.list();
        users.forEach(user -> {
            try {
                saveNotification(user.getId(), dataId, dataType, dataSummary);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void saveNotification(Long idUser, Long dataId, String dataType, String dataSummary) throws MessagingException{
        Notification notification = notificationService.findNotification(idUser, dataId, dataType);
        if(notification == null || NotificationConstant.UpdateArticle.equals(dataType)) {
            log.info("------------------- 开始执行消息通知 ------------------");
            Integer result = notificationService.save(idUser, dataId, dataType, dataSummary);
            if(result == 0) {
                // TODO 记录失败数据
            }
        }
        if(NotificationConstant.Comment.equals(dataType)) {
            notification = notificationService.findNotification(idUser, dataId, dataType);
            NotificationDTO notificationDTO = genNotification(notification);
            // mailService send notification
        }

    }

    private static NotificationDTO genNotification(Notification notification) {
        return null;
    }


}
