package com.github.forest.service.impl;

import com.github.forest.dto.TokenUser;
import com.github.forest.entity.User;
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
}