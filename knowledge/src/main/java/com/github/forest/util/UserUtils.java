package com.github.forest.util;

import com.github.forest.auth.JwtConstants;
import com.github.forest.auth.TokenManager;
import com.github.forest.auth.TokenModel;
import com.github.forest.dto.TokenUser;
import com.github.forest.entity.User;
import com.github.forest.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

/**
 * @author sunzy
 * @date 2023/5/31 11:14
 */
public class UserUtils {
    public static final UserMapper userMapper = SpringContextHolder.getBean(UserMapper.class);
    private static final TokenManager tokenManager = SpringContextHolder.getBean(TokenManager.class);


    public static User getCurrentUserByToken(){
        String authHeader = ContextHolderUtils.getRequest().getHeader(JwtConstants.AUTHORIZATION);
        if(authHeader == null) {
            throw new UnauthenticatedException();
        }

        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(JwtConstants.JWT_SECRET).parseClaimsJws(authHeader).getBody();
        } catch (final SignatureException e) {
            // jwt token 解析失败说明token无效
            throw new UnauthenticatedException();
        }

        Object account = claims.getId();
        if(StringUtils.isNotBlank(Objects.toString(account, ""))) {
            TokenModel model = tokenManager.getToken(authHeader, account.toString());
            if(tokenManager.checkToken(model)) {
                User user = userMapper.selectByAccount(account.toString());
                if(Objects.nonNull(user)) {
                    return user;
                }
            }
        }
        throw new UnauthenticatedException();
    }

    public static TokenUser getTokenUser(String token) {
        if(StringUtils.isNotBlank(token)) {
            // 验证token
            Claims claims;
            try {
                claims = Jwts.parser().setSigningKey(JwtConstants.JWT_SECRET).parseClaimsJws(token).getBody();
            } catch (final SignatureException e) {
                // jwt token 解析失败说明token无效
                throw new UnauthenticatedException();
            }

            Object account = claims.getId();
            if(StringUtils.isNotBlank(Objects.toString(account, ""))) {
                TokenModel model = tokenManager.getToken(token, account.toString());
                if(tokenManager.checkToken(model)) {
                    User user = userMapper.selectByAccount(account.toString());
                    if(Objects.nonNull(user)) {
                        TokenUser tokenUser = new TokenUser();
                        BeanUtils.copyProperties(user, tokenUser);
                        tokenUser.setAccount(user.getEmail());
                        tokenUser.setToken(token);
                        return tokenUser;
                    }
                }
            }
        }

        throw new UnauthenticatedException();
    }

}
