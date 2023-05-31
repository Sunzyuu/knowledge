package com.github.forest.mapper;

import com.github.forest.dto.ArticleDTO;
import com.github.forest.dto.ArticleTagDTO;
import com.github.forest.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.forest.entity.ArticleContent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 文章表  Mapper 接口
 * </p>
 *
 * @author sunzy
 * @since 2023-05-29
 */
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 获取文章列表
     *
     * @param searchText
     * @param tag
     * @param topicUri
     * @return
     */
    List<ArticleDTO> selectArticles(@Param("searchText") String searchText, @Param("tag") String tag, @Param("topicUri") String topicUri);

    /**
     * 获取主题下文章列表
     *
     * @param topicName
     * @return
     */
    List<ArticleDTO> selectArticlesByTopicUri(@Param("topicName") String topicName);

    /**
     * 根据文章 ID 查询文章
     *
     * @param id
     * @param type
     * @return
     */
    ArticleDTO selectArticleDTOById(@Param("id") Long id, @Param("type") int type);

    /**
     * 获取标签下文章列表
     *
     * @param tagName
     * @return
     */
    List<ArticleDTO> selectArticlesByTagName(@Param("tagName") String tagName);


    /**
     * 获取文章标签列表
     *
     * @param idArticle
     * @return
     */
    List<ArticleTagDTO> selectTags(@Param("idArticle") Long idArticle);


    /**
     * 获取文章正文内容
     *
     * @param idArticle
     * @return
     */
//    ArticleContent selectArticleContent(@Param("idArticle") Long idArticle);


}
