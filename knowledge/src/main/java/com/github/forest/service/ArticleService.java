package com.github.forest.service;

import com.github.forest.dto.ArticleDTO;
import com.github.forest.dto.ArticleSearchDTO;
import com.github.forest.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 文章表  服务类
 * </p>
 *
 * @author sunzy
 * @since 2023-05-29
 */
public interface ArticleService extends IService<Article> {
    /**
     * 根据检索内容/标签查询文章列表
     *
     * @param searchDTO
     * @return
     */
    List<ArticleDTO> findArticles(ArticleSearchDTO searchDTO);

    /**
     * 查询文章详情信息
     *
     * @param id
     * @param type
     * @return
     */
    ArticleDTO findArticleDTOById(Long id, Integer type);

    /**
     * 查询主题下文章列表
     *
     * @param name
     * @return
     */
    List<ArticleDTO> findArticlesByTopicUri(String name);


    /**
     * 查询标签下文章列表
     *
     * @param name
     * @return
     */
    List<ArticleDTO> findArticlesByTagName(String name);


    /**
     * 查询未绑定作品集的文章
     * @param idPortfolio
     * @param searchText
     * @param idUser
     * @return
     */
    List<ArticleDTO> selectUnbindArticles(Long idPortfolio, String searchText, Long idUser);

}
