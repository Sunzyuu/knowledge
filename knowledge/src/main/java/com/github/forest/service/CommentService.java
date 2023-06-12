package com.github.forest.service;

import com.github.forest.dto.CommentDTO;
import com.github.forest.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 评论表  服务类
 * </p>
 *
 * @author sunzy
 * @since 2023-06-12
 */
public interface CommentService extends IService<Comment> {

    /**
     * 获取文章评论数据
     * @param idArticle
     * @return
     */
    List<CommentDTO> getArticleComments(Integer idArticle);

    /**
     * 添加评论
     * @param comment
     * @param request
     * @return
     */
    Comment postComment(Comment comment, HttpServletRequest request);

    /**
     * 获取评论列表数据
     * @return
     */
    List<CommentDTO> findComments();
}
