package com.github.forest.service;

import com.github.forest.core.exception.ServiceException;
import com.github.forest.dto.ArticleDTO;
import com.github.forest.dto.PortfolioArticleDTO;
import com.github.forest.dto.PortfolioDTO;
import com.github.forest.dto.UserDTO;
import com.github.forest.entity.Article;
import com.github.forest.entity.Portfolio;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * <p>
 * 作品集表 服务类
 * </p>
 *
 * @author sunzy
 * @since 2023-05-29
 */
public interface PortfolioService extends IService<Portfolio> {

    /**
     * 查询用户作品集
     * @param userDTO
     * @return
     */
    List<PortfolioDTO> findUserPortfoliosByUser(UserDTO userDTO);

    /**
     * 根据id查询作品集
     * @param idPortfolio
     * @param type
     * @return
     */
    PortfolioDTO findPortfolioDTPById(Long idPortfolio, Integer type);


    /**
     * 新增/更新作品集
     * @param port
     * @return
     */
    Portfolio postPortfolio(Portfolio port);

    /**
     * 查询作品集下未绑定文章
     * @param page
     * @param rows
     * @param searchText
     * @param idPortfolio
     * @param idUser
     * @return
     */
    PageInfo<ArticleDTO> findUnbindArticles(Integer page, Integer rows, String searchText, Long idPortfolio, Long idUser);

    /**
     * 绑定文章
     *
     * @param portfolioArticle
     * @return
     * @throws ServiceException
     */
    boolean bindArticle(PortfolioArticleDTO portfolioArticle) throws ServiceException;

    /**
     * 更新文章排序号
     *
     * @param portfolioArticle
     * @return
     * @throws ServiceException
     */
    boolean updateArticleSortNo(PortfolioArticleDTO portfolioArticle) throws ServiceException;

    /**
     * 取消绑定文章
     *
     * @param idPortfolio
     * @param idArticle
     * @return
     * @throws ServiceException
     */
    boolean unbindArticle(Long idPortfolio, Long idArticle) throws ServiceException;

    /**
     * 删除作品集
     *
     * @param idPortfolio
     * @param idUser
     * @param roleWeights
     * @return
     */
    boolean deletePortfolio(Long idPortfolio, Long idUser, Integer roleWeights);


    /**
     * 获取作品集列表数据
     *
     * @return
     */
    List<PortfolioDTO> findPortfolios();
}
