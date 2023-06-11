package com.github.forest.util;

import com.github.forest.core.constant.NotificationConstant;
import com.github.forest.dto.ArticleDTO;
import com.github.forest.dto.Author;
import com.github.forest.dto.NotificationDTO;
import com.github.forest.entity.Notification;
import com.github.forest.entity.User;
import com.github.forest.service.ArticleService;
import com.github.forest.service.JavaMailService;
import com.github.forest.service.NotificationService;
import com.github.forest.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Objects;

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
            mailService.sendNotification(notificationDTO);
        }

    }

    private static NotificationDTO genNotification(Notification notification) {
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        ArticleDTO article;
        User user;
        switch (notification.getDataType()) {
            case "0":
                // 系统公告/帖子
                article = articleService.findArticleDTOById(notification.getDataId(), 0);
                if (Objects.nonNull(article)) {
                    notificationDTO.setDataTitle("系统公告");
                    notificationDTO.setDataUrl(article.getArticlePermalink());
                    user = userService.getById(article.getArticleAuthorId());
                    notificationDTO.setAuthor(genAuthor(user));
                }
                break;
            case "1" :
                // Todo::关注提醒
            case "2" :
                // todo::回帖提醒
            case "4":
                // 关注文章更新
                article = articleService.findArticleDTOById(notification.getDataId(), 0);
                if (Objects.nonNull(article)) {
                    notificationDTO.setDataTitle("关注通知");
                    notificationDTO.setDataUrl(article.getArticlePermalink());
                    user = userService.getById(article.getArticleAuthorId());
                    notificationDTO.setAuthor(genAuthor(user));
                }
                break;
            default :
                break;
        }
        return notificationDTO;
    }

    private static Author genAuthor(User user) {
        Author author = new Author();
        author.setIdUser(user.getId());
        author.setUserAvatarURL(user.getAvatarUrl());
        author.setUserNickname(user.getNickname());
        return author;
    }


}
