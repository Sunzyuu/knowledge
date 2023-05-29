package com.github.forest.service.impl;

import com.github.forest.dto.*;
import com.github.forest.entity.User;
import com.github.forest.entity.UserExtend;
import com.github.forest.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.thymeleaf.templateresource.StringTemplateResource;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author sunzy
 * @Date 2023/5/28 19:45
 */
@SpringBootTest
class UserServiceImplTest {

    @Resource
    private UserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void register() {
        User user = new User();
        user.setNickname("sunzy");
        user.setAccount("sunzy");
        user.setEmail("2632338423@qq.com");
        user.setPassword("123456");
        userService.register("2632338423@qq.com", "123456", "111111");
    }

    @Test
    void login() {
        TokenUser login = userService.login("08183039@cumt.edu.cn", "123321");
        System.out.println(login.getRefreshToken());
    }

    @Test
    void testRedis(){
        stringRedisTemplate.opsForValue().set("263233@qq.com", "123321");

        String code = stringRedisTemplate.boundValueOps("123321").get();
        System.out.println(code);
    }

    @Test
    void forgetPassword() {
        userService.forgetPassword("6q8qhtif4rd14wknqjva@25e64456b75962051f5c9359186b6d0e", "123321");
    }

    @Test
    void testRegister() {
    }

    @Test
    void testLogin() {
    }
    @Test
    void findByAccount() {
        System.out.println(userService.findByAccount("2632338423"));
    }



    @Test
    void findUserDTOByAccount() {
        System.out.println(userService.findUserDTOByAccount("2632338423"));
    }

    @Test
    void testForgetPassword() {

    }

    @Test
    void updateUserRole() {
        userService.updateUserRole(9l, 1l);
    }

    @Test
    void updateStatus() {
        userService.updateStatus(9l, "1");
    }

    @Test
    void findUserInfo() {
        System.out.println(userService.findUserInfo(9l));
    }

    @Test
    void updateUserInfo() {
        UserInfoDTO user = new UserInfoDTO();
        user.setOnlineStatus(1);
        user.setNickname("sunnnn");
        userService.updateUserInfo(user);
    }

    @Test
    void checkNicknameByIdUser() {
        System.out.println(userService.checkNicknameByIdUser(9l, "26323384231"));
    }

    @Test
    void findRoleWeightsByUser() {
        System.out.println(userService.findRoleWeightsByUser(9l));
    }

    @Test
    void selectAuthor() {
        System.out.println(userService.selectAuthor(9l));
    }

    @Test
    void updateUserExtend() {

        UserExtend user = new UserExtend();
        user.setBlog("2122313");
        user.setIdUser(9l);
        user.setQq("2632338423");
        userService.updateUserExtend(user);
    }

    @Test
    void selectUserExtendByAccount() {
        System.out.println(userService.selectUserExtendByAccount("2632338423"));
    }

    @Test
    void updateEmail() {
        ChangeEmailDTO changeDto = new ChangeEmailDTO();
        changeDto.setIdUser(9l);
        userService.updateEmail(changeDto);
    }

    @Test
    void updatePassword() {
        UpdatePasswordDTO updatePasswordDto = new UpdatePasswordDTO();
        updatePasswordDto.setIdUser(9l);
        updatePasswordDto.setPassword("123321");
        userService.updatePassword(updatePasswordDto);
    }

    @Test
    void findUsers() {
        UserSearchDTO searchUser = new UserSearchDTO();
        searchUser.setNickname("2632");
        userService.findUsers(searchUser);
    }

    @Test
    void updateLastOnlineTimeByAccount() {
        userService.updateLastOnlineTimeByAccount("2632338423");
    }

    @Test
    void findUserExtendInfo() {
        userService.findUserExtendInfo(9l);
    }

    @Test
    void refreshToken() {

    }

    @Test
    void findUserPermissions() {
        User user = new User();
        user.setId(9l);
        userService.findUserPermissions(user);
    }
}