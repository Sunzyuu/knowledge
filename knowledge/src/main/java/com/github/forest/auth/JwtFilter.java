package com.github.forest.auth;

import com.alibaba.fastjson2.JSONObject;
import com.github.forest.core.result.GlobalResultGenerator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * jwt认证过滤器
 * @author sunzy
 * @date 2023/5/31 13:35
 */
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {
    /**
     * 判断用户是否想要登入
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader(JwtConstants.AUTHORIZATION);
        if(StringUtils.isBlank(authorization)) {
            // 编辑器上传文件使用的 X-Upload-Token请求头传递token
            authorization = httpServletRequest.getHeader(JwtConstants.UPLOAD_TOKEN);
        }
        return authorization != null;
    }


    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader(JwtConstants.AUTHORIZATION);
        if(StringUtils.isBlank(authorization)) {
            // 编辑器上传文件使用的 X-Upload-Token请求头传递token
            authorization = httpServletRequest.getHeader(JwtConstants.UPLOAD_TOKEN);
        }
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(JwtConstants.JWT_SECRET).parseClaimsJws(authorization).getBody();
        } catch (final SignatureException e) {
            throw new UnauthenticatedException();
        }
        Object username = claims.getId();

        if(Objects.isNull(username)) {
            throw new UnauthenticatedException();
        }

        TokenModel token = new TokenModel(username.toString(), authorization);
        // 提交给realm进入登入，如果错误会抛出异常并捕获
        getSubject(request, response).login(token);
        // 如果没有抛出异常说明登录成功，返回true
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if(isLoginAttempt(request, response)) {
            try {
                return executeLogin(request, response);
            } catch (Exception e) {
                onLoginFail(response);
            }
        }
        onLoginFail(response);
        return false;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    /**
     * 解决跨域访问
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control" +
                "-Request-Headers"));
        // 跨域时会首先发送一个 option 请求，这里我们给 option 请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    private void onLoginFail(ServletResponse response) {
        try {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.setContentType("application/json");
            httpResponse.getOutputStream().write(JSONObject.toJSONString(GlobalResultGenerator.genErrorResult("未登录或已登录超时，请重新登录")).getBytes());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


}
