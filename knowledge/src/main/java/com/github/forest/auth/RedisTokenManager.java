package com.github.forest.auth;

import com.github.forest.handler.event.AccountEvent;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Author sunzy
 * @Date 2023/5/28 14:23
 */
@Component
public class RedisTokenManager implements TokenManager{
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public String createToken(String id) {
        String token = Jwts.builder().setId(id).setSubject(id).
                setIssuedAt(new Date()).
                signWith(SignatureAlgorithm.HS256, JwtConstants.JWT_SECRET).compact();
        // 存储到redis 并设置过期时间为15min
        stringRedisTemplate.boundValueOps(id).set(token, JwtConstants.TOKEN_EXPIRES_MINUTE, TimeUnit.MINUTES);
        return token;
    }

    @Override
    public boolean checkToken(TokenModel model) {
        if(model == null) {
            return false;
        }

        StringBuilder key = new StringBuilder();
        key.append(JwtConstants.LAST_ONLINE).append(model.getUsername());
        String result = stringRedisTemplate.boundValueOps(key.toString()).get();
        if (StringUtils.isBlank(result)) {
            // 更新最后在线时间
            applicationEventPublisher.publishEvent(new AccountEvent(model.getUsername()));
            stringRedisTemplate.boundValueOps(key.toString()).set(LocalDateTime.now().toString(), JwtConstants.LAST_ONLINE_EXPIRES_MINUTE, TimeUnit.MINUTES);
        }
        return true;
    }

    @Override
    public TokenModel getToken(String token, String account) {
        return new TokenModel(account, token);
    }


    @Override
    public void deleteToken(String account) {
        stringRedisTemplate.delete(account);
    }
}
