package com.github.forest.mapper;

import com.github.forest.entity.Sponsor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
/**
 * <p>
 * 赞赏表  Mapper 接口
 * </p>
 *
 * @author sunzy
 * @since 2023-07-02
 */
public interface SponsorMapper extends BaseMapper<Sponsor> {

    /**
     * 更新文章赞赏数
     * @param idArticle
     * @return
     */
    Integer updateArticleSponsorCount(@Param("idArticle") Long idArticle);
}
