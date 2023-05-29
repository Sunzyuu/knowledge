package com.github.forest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.f4b6a3.ulid.UlidCreator;
import com.github.forest.auth.JwtConstants;
import com.github.forest.auth.TokenManager;
import com.github.forest.core.exception.AccountExistsException;
import com.github.forest.core.exception.CaptchaException;
import com.github.forest.core.exception.NicknameOccupyException;
import com.github.forest.core.exception.ServiceException;
import com.github.forest.dto.*;
import com.github.forest.entity.Role;
import com.github.forest.entity.User;
import com.github.forest.entity.UserExtend;
import com.github.forest.entity.UserRole;
import com.github.forest.mapper.RoleMapper;
import com.github.forest.mapper.UserExtendMapper;
import com.github.forest.mapper.UserMapper;
import com.github.forest.service.RoleService;
import com.github.forest.service.UserExtendService;
import com.github.forest.service.UserRoleService;
import com.github.forest.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.forest.util.PasswordEncoder;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
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
    private RoleMapper roleMapper;

    @Resource
    private UserExtendService userExtendService;

    @Resource
    private UserExtendMapper userExtendMapper;

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
        return account;
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

    /**
     * 更新用户状态
     * @param idUser 用户id
     * @param status 状态
     * @return
     * @throws ServiceException
     */
    @Override
    public boolean updateStatus(Long idUser, String status) throws ServiceException {
        User user = query().eq("id", idUser).one();
        if(user == null){
            return false;
        }
        user.setStatus(status);
        return updateById(user);
    }

    /**
     * 获起用户信息
     * @param idUser
     * @return
     */
    @Override
    public UserInfoDTO findUserInfo(Long idUser) {
        User user = getById(idUser);
        if(user == null){
            return null;
        }
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(user, userInfoDTO);
        userInfoDTO.setIdUser(user.getId());
        return userInfoDTO;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfoDTO updateUserInfo(UserInfoDTO user) throws ServiceException {
        user.setNickname(formatNickname(user.getNickname()));
        LambdaQueryWrapper<User> checkNicknameQueryWrapper = new LambdaQueryWrapper<>();
        checkNicknameQueryWrapper.eq(true, User::getNickname, user.getNickname());
        checkNicknameQueryWrapper.ne(true, User::getId, user.getIdUser());
        int count = count(checkNicknameQueryWrapper);
        if(count > 0){
            throw new NicknameOccupyException("该昵称已使用!");
        }
        // TODO::文件上传接口实现
        if(StringUtils.isNotBlank(user.getAvatarType()) && AVATAR_SVG_TYPE.equals(user.getAvatarType())) {

        }

        return null;
    }

    @Override
    public boolean checkNicknameByIdUser(Long idUser, String nickname) {
        LambdaQueryWrapper<User> checkNicknameQueryWrapper = new LambdaQueryWrapper<>();
        checkNicknameQueryWrapper.eq(true, User::getNickname,nickname);
        checkNicknameQueryWrapper.ne(true, User::getId, idUser);
        int count = count(checkNicknameQueryWrapper);
        if(count > 0){
            return false;
        }
        return true;
    }

    /**
     * 获取用户权限权重
     * @param idUser
     * @return
     */
    @Override
    public Integer findRoleWeightsByUser(Long idUser) {
        return userMapper.findRoleWeightsByUser(idUser);
    }

    /**
     * 获取文章作者信息
     * @param idUser
     * @return
     */
    @Override
    public Author selectAuthor(Long idUser) {
        User user = getById(idUser);
        if(user == null){
            return null;
        }
        Author author = new Author();
        BeanUtils.copyProperties(user, author);
        author.setIdUser(user.getId());
        return author;
    }

    /**
     * 更新用户扩展信息
     * @param userExtend
     * @return
     * @throws ServiceException
     */
    @Override
    public UserExtend updateUserExtend(UserExtend userExtend) throws ServiceException {
        boolean result = userExtendService.saveOrUpdate(userExtend);
        if(!result) {
            throw new ServiceException("更新失败");
        }
        return userExtend;
    }

    /**
     * 根据用户账号获取用户扩展信息
     * @param account
     * @return
     */
    @Override
    public UserExtend selectUserExtendByAccount(String account) {
        return userExtendMapper.selectUserExtendByAccount(account);
    }

    @Override
    public boolean updateEmail(ChangeEmailDTO changeEmailDTO) throws ServiceException {
        String code = changeEmailDTO.getCode();
        Long idUser = changeEmailDTO.getIdUser();
        String email = changeEmailDTO.getEmail();

        String vCode = stringRedisTemplate.boundValueOps(email).get();
        if (StringUtils.isNotBlank(vCode) && StringUtils.isNotBlank(code) && vCode.equals(code)) {
            User user = getById(idUser);
            user.setEmail(email);
            boolean result = updateById(user);
            if (!result) {
                throw new ServiceException("修改邮箱失败!");
            }
            return true;
        }
        throw new CaptchaException();
    }

    @Override
    public boolean updatePassword(UpdatePasswordDTO updatePasswordDTO) {
        Long idUser = updatePasswordDTO.getIdUser();
        User user = getById(idUser);
        user.setPassword(PasswordEncoder.encode(updatePasswordDTO.getPassword()));
        updateById(user);
        return true;
    }

    @Override
    public List<UserInfoDTO> findUsers(UserSearchDTO searchDTO) {
        List<UserInfoDTO> userInfoDTOS = userMapper.selectUsers(searchDTO);
        userInfoDTOS.forEach(user -> user.setOnlineStatus(getUserOnlineStatus(user.getAccount())));
        return userInfoDTOS;
    }

    private Integer getUserOnlineStatus(String account) {
        String lastOnlineTime = stringRedisTemplate.boundValueOps(JwtConstants.LAST_ONLINE + account).get();
        if (StringUtils.isBlank(lastOnlineTime)) {
            return 0;
        }
        return 1;
    }

    @Override
    public Integer updateLastOnlineTimeByAccount(String account) {
        User user = query().eq("account", account).one();
        if(user == null){
            return 0;
        }
        user.setLastOnlineTime(new Date());
        updateById(user);
        return 1;
    }

    @Override
    public UserExtend findUserExtendInfo(Long idUser) {
        UserExtend userExtend = userExtendService.getById(idUser);
        if(Objects.isNull(userExtend)){
            userExtend = new UserExtend();
            userExtend.setIdUser(idUser);
            userExtendService.save(userExtend);
        }
        return userExtend;
    }

    /**
     * 刷新token
     * @param refreshToken
     * @return
     */
    @Override
    public TokenUser refreshToken(String refreshToken) {
        String account = stringRedisTemplate.boundValueOps(refreshToken).get();
        if (StringUtils.isNotBlank(account)) {
            User nucleicUser = userMapper.selectByAccount(account);
            if (nucleicUser != null) {
                TokenUser tokenUser = new TokenUser();
                tokenUser.setToken(tokenManager.createToken(nucleicUser.getAccount()));
                tokenUser.setRefreshToken(UlidCreator.getUlid().toString());
                stringRedisTemplate.boundValueOps(tokenUser.getRefreshToken()).set(account, JwtConstants.REFRESH_TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
                stringRedisTemplate.delete(refreshToken);
                return tokenUser;
            }
        }
        throw new UnauthenticatedException();
    }

    /**
     * 获取用户权限
     * @param user
     * @return
     */
    @Override
    public Set<String> findUserPermissions(User user) {
        Set<String> permissions = new HashSet<>();
        List<Role> roles = roleMapper.selectRoleByIdUser(user.getId());
        for (Role role : roles) {
            if (StringUtils.isNotBlank(role.getInputCode())) {
                permissions.add(role.getInputCode());
            }
        }
        permissions.add("user");
        return permissions;
    }
}
