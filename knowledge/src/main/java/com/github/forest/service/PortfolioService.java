package com.github.forest.service;

import com.github.forest.dto.PortfolioDTO;
import com.github.forest.dto.UserDTO;
import com.github.forest.entity.Portfolio;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
