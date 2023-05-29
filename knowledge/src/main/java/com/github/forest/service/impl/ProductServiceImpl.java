package com.github.forest.service.impl;

import com.github.forest.entity.Product;
import com.github.forest.mapper.ProductMapper;
import com.github.forest.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品表 服务实现类
 * </p>
 *
 * @author sunzy
 * @since 2023-05-29
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

}
