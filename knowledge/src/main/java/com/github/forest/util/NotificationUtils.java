package com.github.forest.util;

import com.github.forest.core.constant.NotificationConstant;
import com.github.forest.dto.ArticleDTO;
import com.github.forest.dto.Author;
import com.github.forest.dto.NotificationDTO;
import com.github.forest.entity.Comment;
import com.github.forest.entity.Follow;
import com.github.forest.entity.Notification;
import com.github.forest.entity.User;
import com.github.forest.service.*;
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
    private static final FollowService followService = SpringContextHolder.getBean(FollowService.class);
    private static final JavaMailService mailService = SpringContextHolder.getBean(JavaMailService.class);
    private static final ArticleService articleService = SpringContextHolder.getBean(ArticleService.class);
    private static final CommentService commentService = SpringContextHolder.getBean(CommentService.class);

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

    public static void saveNotification(Long idUser, Long dataId, String dataType, String dataSummary) throws MessagingException{
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

    public static NotificationDTO genNotification(Notification notification) {
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        ArticleDTO article;
        Follow follow;
        Comment comment;
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
                // 关注提醒
                follow = followService.getById(notification.getDataId());
                notificationDTO.setDataTitle("关注提醒");
                if(Objects.nonNull(follow)) {
                    user = userService.getById(follow.getFollowerId());
                    notificationDTO.setDataUrl(getFollowLink(follow.getFollowingType(), user.getAccount()));
                    notificationDTO.setAuthor(genAuthor(user));
                }
                break;
            case "2" :
                // 回帖提醒
               comment = commentService.getById(notification.getDataId());
               if(Objects.nonNull(comment)) {
                   article = articleService.findArticleDTOById(comment.getCommentArticleId(), 0);
                   if(Objects.nonNull(article)) {
                       notificationDTO.setDataTitle(article.getArticleTitle());
                       notificationDTO.setDataUrl(comment.getCommentSharpUrl());
                       user = userService.getById(comment.getCommentAuthorId());
                       notificationDTO.setAuthor(genAuthor(user));
                   }
                   break;
               }
            case "3":
                // 关注用户发布文章
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

    private static String getFollowLink(String followingType, String id) {
        StringBuilder url = new StringBuilder();
        url.append(Utils.getProperty("resource.domain"));
        if("0".equals(followingType)) {
            url.append("/user/").append(id);
        } else {
            url.append("/notification");
        }
        return url.toString();
    }

    private static Author genAuthor(User user) {
        Author author = new Author();
        author.setIdUser(user.getId());
        author.setUserAvatarURL(user.getAvatarUrl());
        author.setUserNickname(user.getNickname());
        return author;
    }


    public static void sendArticlePush(Long dataId, String dataType, String dataSummary, Long articleAuthorId) {
        List<Follow> follows;
        if(NotificationConstant.PostArticle.equals(dataType)) {
            // 关注 用户通知
            follows = followService.findByFollowingId("0", articleAuthorId);
        } else {
            // 关注文章通知
            follows = followService.findByFollowingId("3", articleAuthorId);
        }
        follows.forEach(follow -> {
            try {
                saveNotification(follow.getFollowerId(), dataId, dataType, dataSummary);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
