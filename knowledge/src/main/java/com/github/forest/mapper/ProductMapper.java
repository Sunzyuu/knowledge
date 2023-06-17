package com.github.forest.mapper;

import com.github.forest.dto.ProductDTO;
import com.github.forest.entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 产品表 Mapper 接口
 * </p>
 *
 * @author sunzy
 * @since 2023-05-29
 */
public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 查询产品列表
     * @return
     */
    List<ProductDTO> selectProducts();

    /**
     * 获取产品详细信息
     * @param idProduct
     * @param type
     * @return
     */
    ProductDTO selectProductDTOById(@Param("idProduct") Integer idProduct, @Param("type") Integer type);

}
