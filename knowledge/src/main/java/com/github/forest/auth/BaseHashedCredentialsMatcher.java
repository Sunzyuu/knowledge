package com.github.forest.auth;

import com.github.forest.util.UserUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

/**
 * @author sunzy
 * @date 2023/5/31 13:20
 */
public class BaseHashedCredentialsMatcher extends HashedCredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UserUtils.getTokenUser(token.getCredentials().toString());
        return true;
    }
}
