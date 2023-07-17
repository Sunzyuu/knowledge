package com.github.forest.web.api.auth;

import com.alibaba.fastjson2.JSONObject;
import com.github.forest.auth.TokenManager;
import com.github.forest.core.result.GlobalResult;
import com.github.forest.core.result.GlobalResultGenerator;
import com.github.forest.dto.BankAccountDTO;
import com.github.forest.dto.TokenUser;
import com.github.forest.entity.User;
import com.github.forest.service.BankAccountService;
import com.github.forest.service.UserService;
import com.github.forest.util.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

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

    @Resource
    private BankAccountService bankAccountService;

    @PostMapping("/login")
    public GlobalResult<TokenUser> login(@RequestBody User user){
        TokenUser tokenUser = userService.login(user.getAccount(), user.getPassword());
        return GlobalResultGenerator.genSuccessResult(tokenUser);
    }


    @PostMapping("/refresh-token")
    public GlobalResult<TokenUser> refreshToken(@RequestBody TokenUser tokenUser) {
        tokenUser = userService.refreshToken(tokenUser.getRefreshToken());
        return GlobalResultGenerator.genSuccessResult(tokenUser);
    }

    @PostMapping("/logout")
    public GlobalResult logout() {
        User user = UserUtils.getCurrentUserByToken();
        if (Objects.nonNull(user)) {
            tokenManager.deleteToken(user.getAccount());
        }
        return GlobalResultGenerator.genSuccessResult();
    }

    @GetMapping("/user")
    public GlobalResult<JSONObject> user() {
        User user = UserUtils.getCurrentUserByToken();
        TokenUser tokenUser = new TokenUser();
        BeanUtils.copyProperties(user, tokenUser);
        tokenUser.setScope(userService.findUserPermissions(user));
        BankAccountDTO bankAccountDTO = bankAccountService.findBankAccountByIdUser(user.getId());

        if (Objects.nonNull(bankAccountDTO)) {
            tokenUser.setBankAccount(bankAccountDTO.getBankAccount());
        }
        JSONObject object = new JSONObject();
        object.put("user", tokenUser);
        return GlobalResultGenerator.genSuccessResult(object);
    }

}
