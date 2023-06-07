package com.github.forest.service.impl;

import com.github.forest.dto.Author;
import com.github.forest.dto.PortfolioDTO;
import com.github.forest.dto.UserDTO;
import com.github.forest.entity.Portfolio;
import com.github.forest.lucene.model.PortfolioLucene;
import com.github.forest.lucene.util.PortfolioIndexUtil;
import com.github.forest.mapper.PortfolioMapper;
import com.github.forest.service.ArticleService;
import com.github.forest.service.PortfolioService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.forest.service.UserService;
import com.github.forest.util.XssUtils;
import com.github.forest.web.api.common.UploadController;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 作品集表 服务实现类
 * </p>
 *
 * @author sunzy
 * @since 2023-05-29
 */
@Service
public class PortfolioServiceImpl extends ServiceImpl<PortfolioMapper, Portfolio> implements PortfolioService {

    @Resource
    private PortfolioMapper portfolioMapper;

    @Resource
    private UserService userService;

    @Resource
    private ArticleService articleService;

    @Override
    public List<PortfolioDTO> findUserPortfoliosByUser(UserDTO userDTO) {
        List<PortfolioDTO> list = portfolioMapper.selectUserPortfoliosByIdUser(userDTO.getId());
        Author author = userService.selectAuthor(userDTO.getId());
        // 将作品信息绑定到作品集
        list.forEach(portfolioDTO -> {
            genPortfolioAuthor(portfolioDTO, author);
            Integer articleNumber = portfolioMapper.selectCountArticleNumber(author.getIdUser());
            portfolioDTO.setArticleNumber(articleNumber);
        });

        return list;
    }

    private PortfolioDTO genPortfolioAuthor(PortfolioDTO portfolioDTO, Author author) {
        portfolioDTO.setPortfolioAuthorAvatarUrl(author.getUserAvatarURL());
        portfolioDTO.setPortfolioAuthorName(author.getUserNickname());
        portfolioDTO.setPortfolioAuthorId(author.getIdUser());
        portfolioDTO.setPortfolioAuthor(author);
        return portfolioDTO;
    }

    @Override
    public PortfolioDTO findPortfolioDTPById(Long idPortfolio, Integer type) {
        PortfolioDTO portfolio = portfolioMapper.selectPortfolioDTOById(idPortfolio, type);
        if(portfolio == null) {
            return new PortfolioDTO();
        }

        Author author = userService.selectAuthor(portfolio.getPortfolioAuthorId());
        genPortfolioAuthor(portfolio, author);
        Integer articleNumber = portfolioMapper.selectCountArticleNumber(portfolio.getIdPortfolio());
        portfolio.setArticleNumber(articleNumber);
        return portfolio;
    }

    @Override
    public Portfolio postPortfolio(Portfolio portfolio) {
        if(StringUtils.isNotBlank(portfolio.getPortfolioHeadImgUrl())) {
            // 将网络图片转换成本地图片
            String headImageUrl = UploadController.uploadBase64File(portfolio.getPortfolioHeadImgUrl(), 0);
            portfolio.setPortfolioHeadImgUrl(headImageUrl);
        }
        if(portfolio.getIdPortfolio() == null || portfolio.getIdPortfolio() == 0) {
            portfolio.setCreatedTime(new Date());
            portfolio.setUpdatedTime(portfolio.getCreatedTime());
            portfolio.setPortfolioDescriptionHtml(XssUtils.filterHtmlCode(portfolio.getPortfolioDescription()));
            // 添加到全文索引中
            PortfolioIndexUtil.updateIndex(
                    PortfolioLucene.builder()
                            .idPortfolio(portfolio.getIdPortfolio())
                            .portfolioTitle(portfolio.getPortfolioTitle())
                            .portfolioDescription(portfolio.getPortfolioDescription())
                            .build());
        } else {
            portfolio.setUpdatedTime(new Date());
            // 添加到全文索引中
            PortfolioIndexUtil.updateIndex(
                    PortfolioLucene.builder()
                            .idPortfolio(portfolio.getIdPortfolio())
                            .portfolioTitle(portfolio.getPortfolioTitle())
                            .portfolioDescription(portfolio.getPortfolioDescription())
                            .build());
        }
        saveOrUpdate(portfolio);
        return null;
    }
}
