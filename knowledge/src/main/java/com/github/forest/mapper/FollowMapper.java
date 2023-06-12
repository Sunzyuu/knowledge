package com.github.forest.mapper;

import com.github.forest.dto.UserDTO;
import com.github.forest.entity.Follow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 关注表  Mapper 接口
 * </p>
 *
 * @author sunzy
 * @since 2023-06-12
 */
public interface FollowMapper extends BaseMapper<Follow> {
    /**
     * 判断是否关注
     * @param followingId
     * @param followerId
     * @param followingType
     * @return
     */
    Boolean isFollow(@Param("followingId") Integer followingId, @Param("followerId") Long followerId, @Param("followingType") String followingType);

    /**
     * 查询用户粉丝
     *
     * @param idUser
     * @return
     */
    List<UserDTO> selectUserFollowersByUser(@Param("idUser") Long idUser);

    /**
     * 查询用户关注用户
     *
     * @param idUser
     * @return
     */
    List<UserDTO> selectUserFollowingsByUser(@Param("idUser") Long idUser);
}
