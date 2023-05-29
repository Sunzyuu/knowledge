package com.github.forest.web.api.common;

import com.github.forest.core.exception.AccountExistsException;
import com.github.forest.core.exception.ServiceException;
import com.github.forest.core.result.GlobalResult;
import com.github.forest.core.result.GlobalResultGenerator;
import com.github.forest.core.result.GlobalResultMessage;
import com.github.forest.dto.ForgetPasswordDTO;
import com.github.forest.dto.TokenUser;
import com.github.forest.dto.UserRegisterInfoDTO;
import com.github.forest.entity.User;
import com.github.forest.service.*;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 访问首页需要的api
 * @Author sunzy
 * @Date 2023/5/29 15:51
 */
@RestController
@RequestMapping("/api/v1/console")
public class CommonApiController {

    @Resource
    private JavaMailService javaMailService;
    @Resource
    private UserService userService;
    @Resource
    private ArticleService articleService;
    @Resource
    private PortfolioService portfolioService;
    @Resource
    private ProductService productService;


    /**
     * 注册时发送验证码
     * @param email
     * @return
     * @throws MessagingException
     */
    @GetMapping("/get-email-code")
    public GlobalResult<Map<String, String>> getEmailCode(@RequestParam("email") String email) throws MessagingException {
        Map<String, String> map = new HashMap<>(1);
        map.put("message", GlobalResultMessage.SEND_SUCCESS.getMessage());
        User user = userService.findByAccount(email);
        if (user != null) {
            throw new AccountExistsException("该邮箱已被注册!");
        } else {
            Integer result = javaMailService.sendEmailCode(email);
            if (result == 0) {
                map.put("message", GlobalResultMessage.SEND_FAIL.getMessage());
            }
        }
        return GlobalResultGenerator.genSuccessResult(map);
    }


    /**
     * 忘记密码的验证码发送
     * @param email
     * @return
     * @throws MessagingException
     * @throws ServiceException
     */
    @GetMapping("/get-forget-password-email")
    public GlobalResult getForgetPasswordEmail(@RequestParam("email") String email) throws MessagingException, ServiceException {
        User user = userService.findByAccount(email);
        if (user != null) {
            Integer result = javaMailService.sendForgetPasswordEmail(email);
            if (result == 0) {
                throw new ServiceException(GlobalResultMessage.SEND_FAIL.getMessage());
            }
        } else {
            throw new UnknownAccountException("未知账号");
        }
        return GlobalResultGenerator.genSuccessResult(GlobalResultMessage.SEND_SUCCESS.getMessage());
    }

    /**
     * 注册
     * @param registerInfo
     * @return
     */
    @PostMapping("/register")
    public GlobalResult<Boolean> register(@RequestBody UserRegisterInfoDTO registerInfo) {
        boolean flag = userService.register(registerInfo.getEmail(), registerInfo.getPassword(), registerInfo.getCode());
        return GlobalResultGenerator.genSuccessResult(flag);
    }


    /**
     * 用户登录
     * @param user
     * @return
     * @throws ServiceException
     */
    @PostMapping("/login")
    public GlobalResult<TokenUser> login(@RequestBody User user) throws ServiceException {
        TokenUser tokenUser = userService.login(user.getAccount(), user.getPassword());
        return GlobalResultGenerator.genSuccessResult(tokenUser);
    }

    @PatchMapping("/forget-password")
    public GlobalResult<Boolean> forgetPassword(@RequestBody ForgetPasswordDTO forgetPassword) throws ServiceException {
        boolean flag = userService.forgetPassword(forgetPassword.getCode(), forgetPassword.getPassword());
        return GlobalResultGenerator.genSuccessResult(flag);
    }

    @GetMapping("/heartbeat")
    public GlobalResult heartbeat() {
        return GlobalResultGenerator.genSuccessResult("heartbeat");
    }




}
