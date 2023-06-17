package com.github.forest.service;

import com.github.forest.dto.ProductDTO;
import com.github.forest.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 产品表 服务类
 * </p>
 *
 * @author sunzy
 * @since 2023-05-29
 */
public interface ProductService extends IService<Product> {

    /**
     * 查询产品列表
     *
     * @return
     */
    List<ProductDTO> findProducts();

    /**
     * 获取产品详情
     *
     * @param idProduct
     * @param type
     * @return
     */
    ProductDTO findProductDTOById(Integer idProduct, Integer type);

}
