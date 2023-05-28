package com.github.forest.web.api.auth;

import com.github.forest.auth.TokenManager;
import com.github.forest.core.result.GlobalResult;
import com.github.forest.core.result.GlobalResultGenerator;
import com.github.forest.dto.TokenUser;
import com.github.forest.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author sunzy
 * @Date 2023/5/28 14:16
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {


    @Resource
    private UserService userService;

    @Resource
    TokenManager tokenManager;

    @PostMapping("/login")
    public GlobalResult<TokenUser> login(){


        return null;
    }

}
