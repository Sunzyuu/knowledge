package com.github.forest.lucene.mapper;

import com.github.forest.dto.ArticleDTO;
import com.github.forest.lucene.model.ArticleLucene;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ArticleLuceneMapper
 */
@Mapper
public interface ArticleLuceneMapper {

    /**
     * 加载所有文章内容
     *
     * @return
     */
    List<ArticleLucene> getAllArticleLucene();

    /**
     * 加载所有文章内容
     *
     * @param ids 文章id(半角逗号分隔)
     * @return
     */
    List<ArticleDTO> getArticlesByIds(@Param("ids") Long[] ids);


    /**
     * 加载文章内容
     *
     * @param id 文章id
     * @return
     */
    ArticleLucene getById(@Param("id") Long id);

}
