package com.github.forest.service.impl;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.github.forest.entity.LoginRecord;
import com.github.forest.mapper.LoginRecordMapper;
import com.github.forest.service.LoginRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.forest.util.Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Retention;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 登录记录表 服务实现类
 * </p>
 *
 * @author sunzy
 * @since 2023-06-17
 */
@Service
public class LoginRecordServiceImpl extends ServiceImpl<LoginRecordMapper, LoginRecord> implements LoginRecordService {

    @Resource
    private LoginRecordMapper loginRecordMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginRecord saveLoginRecord(Long idUser) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String ipAddress = Utils.getIpAddress(request);
        String ua = request.getHeader("user-agent");
        String fingerprint = request.getHeader("fingerprint");
        LoginRecord loginRecord = new LoginRecord();
        loginRecord.setLoginIp(ipAddress);
        loginRecord.setIdUser(idUser);
        loginRecord.setLoginUa(ua);
        UserAgent userAgent = UserAgentUtil.parse(ua);
        loginRecord.setLoginDeviceId(fingerprint);
        loginRecord.setLoginOs(userAgent.getOs().toString());
        loginRecord.setLoginBrowser(userAgent.getBrowser().toString());
        loginRecord.setCreatedTime(new Date());
        save(loginRecord);
        return loginRecord;
    }

    @Override
    public List<LoginRecord> findLoginRecordByIdUser(Integer idUser) {
        return loginRecordMapper.selectLoginRecordByIdUser(idUser);
    }
}
