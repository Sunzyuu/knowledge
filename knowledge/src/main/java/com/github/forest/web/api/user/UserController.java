package com.github.forest.web.api.user;


import com.github.forest.entity.User;
import com.github.forest.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户表  前端控制器
 * </p>
 *
 * @author sunzy
 * @since 2023-05-28
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Resource
    private UserService userService;


    @GetMapping
    public List<User> getUsers(){
        return userService.list();
    }

}
