package com.github.forest.mapper;

import com.github.forest.dto.Author;
import com.github.forest.dto.CommentDTO;
import com.github.forest.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 评论表  Mapper 接口
 * </p>
 *
 * @author sunzy
 * @since 2023-06-12
 */
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 获取文章的所有评论
     * @param idArticle
     * @return
     */
    List<CommentDTO> selectArticleComments(@Param("idArticle") Integer idArticle);

    /**
     * 查询评论作者
     *
     * @param commentAuthorId
     * @return
     */
    Author selectAuthor(@Param("commentAuthorId") Integer commentAuthorId);

    /**
     * 查询父评论作者
     *
     * @param commentOriginalCommentId
     * @return
     */
    Author selectCommentOriginalAuthor(@Param("commentOriginalCommentId") Integer commentOriginalCommentId);

    /**
     * 更新文章评论分享链接
     *
     * @param idComment
     * @param commentSharpUrl
     * @return
     */
    Integer updateCommentSharpUrl(@Param("idComment") Long idComment, @Param("commentSharpUrl") String commentSharpUrl);

    /**
     * 获取评论列表数据
     *
     * @return
     */
    List<CommentDTO> selectComments();
}
