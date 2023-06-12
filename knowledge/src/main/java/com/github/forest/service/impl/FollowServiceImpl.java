package com.github.forest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.forest.dto.UserDTO;
import com.github.forest.entity.Follow;
import com.github.forest.mapper.FollowMapper;
import com.github.forest.service.FollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 关注表  服务实现类
 * </p>
 *
 * @author sunzy
 * @since 2023-06-12
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements FollowService {

    @Resource
    private FollowMapper followMapper;


    @Override
    public Boolean isFollow(Integer followingId, String followingType, Long idUser) {
        return followMapper.isFollow(followingId, idUser, followingType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean follow(Follow follow, String nickname) {
        boolean result = save(follow);
        if (result) {
            // todo :: 关注消息提醒
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancelFollow(Follow follow) {
        LambdaQueryWrapper<Follow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(true, Follow::getFollowerId, follow.getFollowerId());
        queryWrapper.eq(true, Follow::getFollowingId, follow.getFollowingId());
        queryWrapper.eq(true, Follow::getFollowingType, follow.getFollowingType());
        return remove(queryWrapper);
    }

    @Override
    public List<Follow> findByFollowingId(String followType, Long followingId) {
        LambdaQueryWrapper<Follow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(true, Follow::getFollowingId, followingId);
        queryWrapper.eq(true, Follow::getFollowingType, followType);
        return list(queryWrapper);
    }

    @Override
    public List<UserDTO> findUserFollowersByUser(UserDTO userDTO) {
        return followMapper.selectUserFollowersByUser(userDTO.getIdUser());
    }

    @Override
    public List<UserDTO> findUserFollowingsByUser(UserDTO userDTO) {
        return followMapper.selectUserFollowingsByUser(userDTO.getIdUser());
    }

}
