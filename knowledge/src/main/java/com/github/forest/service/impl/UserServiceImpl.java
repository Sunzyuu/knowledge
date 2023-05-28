package com.github.forest.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.github.f4b6a3.ulid.UlidCreator;
import com.github.forest.auth.JwtConstants;
import com.github.forest.auth.TokenManager;
import com.github.forest.core.exception.AccountExistsException;
import com.github.forest.core.exception.CaptchaException;
import com.github.forest.core.exception.ServiceException;
import com.github.forest.dto.TokenUser;
import com.github.forest.dto.UserDTO;
import com.github.forest.entity.Role;
import com.github.forest.entity.User;
import com.github.forest.entity.UserRole;
import com.github.forest.mapper.UserMapper;
import com.github.forest.service.RoleService;
import com.github.forest.service.UserRoleService;
import com.github.forest.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.forest.util.PasswordEncoder;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.shiro.authc.AccountException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户表  服务实现类
 * </p>
 *
 * @author sunzy
 * @since 2023-05-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private RoleService roleService;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private TokenManager tokenManager;

    private final static String AVATAR_SVG_TYPE = "1";

    private final static String DEFAULT_AVATAR = "https://static.rymcu.com/article/1578475481946.png";

    @Override
    public User findByAccount(String account) throws TooManyResultsException {
        return userMapper.selectByAccount(account);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean register(String email, String password, String code) {
//        String vCode = stringRedisTemplate.boundValueOps(email).get();
        // 测试
        String vCode = "111111";
        if(StringUtils.isNotBlank(vCode)) {
            // 验证短信验证码
            if(vCode.equals(code)) {
                User user = userMapper.selectByAccount(email);
                if(user != null) {
                    throw new AccountExistsException("该邮箱已被注册！");
                } else {
                    user = new User();
                    String nickname = email.split("@")[0];
                    user.setNickname(checkNickname(nickname));
                    user.setAccount(checkAccount(nickname));
                    user.setEmail(email);
                    user.setPassword(PasswordEncoder.encode(password));
                    user.setCreatedTime(new Date());
                    user.setUpdatedTime(user.getCreatedTime());
                    user.setAvatarUrl(DEFAULT_AVATAR);
                    save(user);
                    // 获取用户角色信息
                    Role role = roleService.query().eq("input_code", "user").one();
                    // 保存用户的角色信息
                    UserRole userRole = new UserRole();
                    userRole.setIdUser(user.getId());
                    userRole.setIdRole(role.getIdRole());
                    userRole.setCreatedTime(new Date());
                    userRoleService.save(userRole);
                    // todo 向Lucene插入用户信息
//                    stringRedisTemplate.delete(email);
                    return true;
                }
            }
        }
        throw new CaptchaException();
    }

    private String checkAccount(String account) {
        account = formatNickname(account);
        Integer result = query().eq("account", account).count();
        if(result > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            return checkNickname(stringBuilder.append("_").append(System.currentTimeMillis()).toString());
        }
        return null;
    }

    private String checkNickname(String nickname) {
        nickname = formatNickname(nickname);
        Integer result = query().eq("nickname", nickname).count();
        if(result > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            return checkNickname(stringBuilder.append("_").append(System.currentTimeMillis()).toString());

        }
        return nickname;
    }

    private String formatNickname(String nickname) {
        return nickname.replaceAll("\\.", "");
    }


    @Override
    public TokenUser login(String account, String password) {
        User user = userMapper.selectByAccount(account);
        if(user != null){
            // 判断密码是否正确
            if (PasswordEncoder.matches(user.getPassword(), password)) {
                user.setLastLoginTime(new Date());
                user.setLastOnlineTime(new Date());
                // 更新用户登录信息
                updateById(user);
                TokenUser tokenUser = new TokenUser();
                tokenUser.setToken(tokenManager.createToken(account));
                tokenUser.setRefreshToken(UlidCreator.getUlid().toString());
                stringRedisTemplate.boundValueOps(tokenUser.getRefreshToken()).set(account, JwtConstants.REFRESH_TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
                // 保存登录日志
                return tokenUser;
            }
        }
        throw new AccountException();
    }

    @Override
    public UserDTO findUserDTOByAccount(String account) {
        return userMapper.selectUserDTOByAccount(account);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean forgetPassword(String code, String password) throws ServiceException {
        // 忘记密码的验证码保存形式为 加密后的code : email
        String email = stringRedisTemplate.boundValueOps(code).get();
        if(StringUtils.isBlank(email)) {
            throw new ServiceException("链接已失效");
        } else {
            String encodePassword = PasswordEncoder.encode(password);
            User user = query().eq("email", email).one();
            user.setPassword(encodePassword);
            boolean result = updateById(user);
            if(!result) {
                throw new ServiceException("修改密码失败！");
            }
            return true;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserRole(Long idUser, Long idRole) throws ServiceException {
        UserRole userRole = userRoleService.query().eq("id_user", idUser).one();
        userRole.setIdRole(idRole);
        boolean result = userRoleService.updateById(userRole);
        if(!result) {
            throw new ServiceException("更新失败!");
        }
        return true;
    }
}
