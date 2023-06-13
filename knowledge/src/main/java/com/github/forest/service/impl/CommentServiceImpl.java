package com.github.forest.service.impl;

import com.github.forest.core.exception.ContentNotExistException;
import com.github.forest.dto.Author;
import com.github.forest.dto.CommentDTO;
import com.github.forest.entity.Article;
import com.github.forest.entity.Comment;
import com.github.forest.handler.event.CommentEvent;
import com.github.forest.mapper.CommentMapper;
import com.github.forest.service.ArticleService;
import com.github.forest.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.forest.util.Utils;
import com.github.forest.util.XssUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 评论表  服务实现类
 * </p>
 *
 * @author sunzy
 * @since 2023-06-12
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private ArticleService articleService;

    @Resource
    private ApplicationEventPublisher publisher;

    @Override
    public List<CommentDTO> getArticleComments(Integer idArticle) {
        List<CommentDTO> commentDTOList = commentMapper.selectArticleComments(idArticle);
        return genComments(commentDTOList);
    }

    private List<CommentDTO> genComments(List<CommentDTO> commentDTOList) {
        commentDTOList.forEach(commentDTO -> {
            commentDTO.setTimeAgo(Utils.getTimeAgo(commentDTO.getCreatedTime()));
            commentDTO.setCommentContent(XssUtils.filterHtmlCode(commentDTO.getCommentContent()));
            if (commentDTO.getCommentAuthorId() != null) {
                Author author = commentMapper.selectAuthor(commentDTO.getCommentAuthorId());
                if(author != null) {
                    commentDTO.setCommenter(author);
                }
            }
            if(commentDTO.getCommentOriginalCommentId() != null && commentDTO.getCommentOriginalCommentId() > 0) {
                Author commentOriginalAuthor = commentMapper.selectCommentOriginalAuthor(commentDTO.getCommentOriginalCommentId());
                if(commentOriginalAuthor != null) {
                    commentDTO.setCommentOriginalAuthorThumbnailURL(commentOriginalAuthor.getUserAvatarURL());
                    commentDTO.setCommentOriginalAuthorNickname(commentOriginalAuthor.getUserNickname());
                }
                Comment comment = getById(commentDTO.getCommentOriginalCommentId());
                commentDTO.setCommentOriginalContent(comment.getCommentContent());

            }
        });
        return commentDTOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Comment postComment(Comment comment, HttpServletRequest request) {
        if(comment.getCommentArticleId() == null) {
            throw new IllegalArgumentException("非法访问，文章长主键异常");
        }
        if(comment.getCommentAuthorId() == null) {
            throw new IllegalArgumentException("非法访问，用户未登录");
        }
        if(StringUtils.isBlank(comment.getCommentContent())) {
            throw new IllegalArgumentException("回帖内容不能为空");
        }
        Article article = articleService.getById(comment.getCommentArticleId());
        if(article == null) {
            throw new ContentNotExistException("文章不存在！");
        }
        String ip = Utils.getIpAddress(request);
        String ua = request.getHeader("user-agent");
        comment.setCommentIp(ip);
        comment.setCommentUa(ua);
        comment.setCreatedTime(new Date());
        comment.setCommentContent(XssUtils.filterHtmlCode(comment.getCommentContent()));
        save(comment);
        String commentSharpUrl = article.getArticlePermalink() + "#comment-" + comment.getIdComment();
        comment.setCommentSharpUrl(commentSharpUrl);
        commentMapper.updateCommentSharpUrl(comment.getIdComment(), commentSharpUrl);

        String commentContent = comment.getCommentContent();
        if(StringUtils.isNotBlank(commentContent)) {
            publisher.publishEvent(new CommentEvent(comment.getIdComment(), article.getArticleAuthorId(), comment.getCommentAuthorId(), commentContent, comment.getCommentOriginalCommentId()));
        }
        return comment;
    }

    @Override
    public List<CommentDTO> findComments() {
        List<CommentDTO> commentDTOList = commentMapper.selectComments();
        return genComments(commentDTOList);
    }
}
