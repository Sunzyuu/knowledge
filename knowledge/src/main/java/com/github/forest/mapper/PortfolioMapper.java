package com.github.forest.mapper;

import com.github.forest.dto.PortfolioDTO;
import com.github.forest.entity.Portfolio;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 作品集表 Mapper 接口
 * </p>
 *
 * @author sunzy
 * @since 2023-05-29
 */
public interface PortfolioMapper extends BaseMapper<Portfolio> {

    /**
     * 查询用户作品集
     *
     * @param idUser
     * @return
     */
    List<PortfolioDTO> selectUserPortfoliosByIdUser(@Param("idUser") Long idUser);


    /**
     * 查询作品集
     *
     * @param id
     * @param type
     * @return
     */
    PortfolioDTO selectPortfolioDTOById(@Param("id") Long id, @Param("type") Integer type);

    /**
     * 统计作品集下的文章数
     * @param id
     * @return
     */
    Integer selectCountArticleNumber(@Param("idArticle") Long id);


    /**
     * 查询文章是否已绑定
     * @param idArticle
     * @param idPortfolio
     * @return
     */
    Integer selectCountPortfolioArticle(@Param("idArticle") Long idArticle, @Param("idPortfolio") Long idPortfolio);


    /**
     * 插入文章与作品绑定数据
     * @param idArticle
     * @param idPortfolio
     * @param maxSortNo
     * @return
     */
    Integer insertPortfolioArticle(@Param("idArticle") Long idArticle, @Param("idPortfolio") Long idPortfolio, @Param("maxSortNo") Integer maxSortNo);
}
