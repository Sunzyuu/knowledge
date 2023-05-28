package com.github.forest.service;

import com.github.forest.core.exception.ServiceException;
import com.github.forest.dto.TokenUser;
import com.github.forest.dto.UserDTO;
import com.github.forest.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.exceptions.TooManyResultsException;

/**
 * <p>
 * 用户表  服务类
 * </p>
 *
 * @author sunzy
 * @since 2023-05-28
 */
public interface UserService extends IService<User> {

    /**
     * 通过账号查询用户信息
     *
     * @param account
     * @return User
     * @throws TooManyResultsException
     */
    User findByAccount(String account) throws TooManyResultsException;


    /**
     * 注册接口
     *
     * @param email    邮箱
     * @param password 密码
     * @param code     验证码
     * @return Map
     */
    boolean register(String email, String password, String code);

    /**
     * 登录接口
     *
     * @param account  邮箱
     * @param password 密码
     * @return Map
     */
    TokenUser login(String account, String password);


    /**
     * 通过 account 获取用户信息接口
     *
     * @param account 昵称
     * @return UserDTO
     */
    UserDTO findUserDTOByAccount(String account);

    /**
     * 找回密码接口
     *
     * @param code     验证码
     * @param password 密码
     * @return Map
     * @throws ServiceException
     */
    boolean forgetPassword(String code, String password) throws ServiceException;


    /**
     * 更新用户角色接口
     *
     * @param idUser 用户 id
     * @param idRole 角色 id
     * @return Map
     * @throws ServiceException
     */
    boolean updateUserRole(Long idUser, Long idRole) throws ServiceException;






}
