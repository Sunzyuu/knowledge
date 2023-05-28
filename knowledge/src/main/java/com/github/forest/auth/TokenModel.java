package com.github.forest.auth;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @Author sunzy
 * @Date 2023/5/28 14:18
 */
public class TokenModel implements AuthenticationToken {

    private String username;

    private String token;


    public TokenModel(String username, String token) {
        this.username = username;
        this.token = token;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
