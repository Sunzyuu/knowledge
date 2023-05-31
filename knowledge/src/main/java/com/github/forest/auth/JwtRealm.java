package com.github.forest.auth;

import com.github.forest.dto.TokenUser;
import com.github.forest.entity.Role;
import com.github.forest.entity.User;
import com.github.forest.service.RoleService;
import com.github.forest.util.UserUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sunzy
 * @date 2023/5/31 13:03
 */
public class JwtRealm extends AuthorizingRealm {
    @Resource
    private RoleService roleService;

    @Resource
    private TokenManager tokenManager;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof TokenModel;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String accessToken = (String) principalCollection.getPrimaryPrincipal();
        TokenUser tokenUser = UserUtils.getTokenUser(accessToken);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user = new User();
        user.setId(tokenUser.getIdUser());
        try {
            List<Role> roles = roleService.selectRoleByUser(user);
            for (Role role : roles) {
                if(StringUtils.isNotBlank(role.getInputCode())) {
                    authorizationInfo.addRole(role.getInputCode());
                    authorizationInfo.addStringPermission(role.getInputCode());
                }
            }
            authorizationInfo.addStringPermission("user");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        TokenModel token = (TokenModel) authenticationToken;
        if(!tokenManager.checkToken(token)) {
            throw new UnauthorizedException();
        }
        return new SimpleAuthenticationInfo(token.getPrincipal(), token.getCredentials(), this.getName());
    }
}
