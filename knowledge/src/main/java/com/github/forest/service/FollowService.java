package com.github.forest.service;

import com.github.forest.dto.UserDTO;
import com.github.forest.entity.Follow;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 关注表  服务类
 * </p>
 *
 * @author sunzy
 * @since 2023-06-12
 */
public interface FollowService extends IService<Follow> {
    /**
     * 判断是否关注
     *
     * @param followingId
     * @param followingType
     * @param idUser
     * @return
     */
    Boolean isFollow(Integer followingId, String followingType, Long idUser);

    /**
     * 关注操作
     *
     * @param follow
     * @param nickname
     * @return
     */
    Boolean follow(Follow follow, String nickname);


    /**
     * 取消关注操作
     *
     * @param follow
     * @return
     */
    Boolean cancelFollow(Follow follow);

    /**
     * 获取关注用户者数据
     *
     * @param followType
     * @param followingId
     * @return
     */
    List<Follow> findByFollowingId(String followType, Long followingId);


    /**
     * 查询用户粉丝
     *
     * @param userDTO
     * @return
     */
    List<UserDTO> findUserFollowersByUser(UserDTO userDTO);

    /**
     * 查询用户关注用户
     *
     * @param userDTO
     * @return
     */
    List<UserDTO> findUserFollowingsByUser(UserDTO userDTO);
}
