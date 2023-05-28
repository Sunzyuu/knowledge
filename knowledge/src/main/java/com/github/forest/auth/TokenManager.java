package com.github.forest.auth;

import org.springframework.stereotype.Component;

/**
 * @Author sunzy
 * @Date 2023/5/28 14:22
 */

public interface TokenManager {
    /**
     * 创建一个token关联上指定用户
     *
     * @param id
     * @return 生成的token
     */
    public String createToken(String id);

    /**
     * 检查token是否有效
     *
     * @param model token
     * @return 是否有效
     */
    public boolean checkToken(TokenModel model);

    /**
     * 从字符串中解析token
     *
     * @param token
     * @param account
     * @return
     */
    public TokenModel getToken(String token, String account);

    /**
     * 清除token
     *
     * @param account 登录用户账号
     */
    public void deleteToken(String account);
}
