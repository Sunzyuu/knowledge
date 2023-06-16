package com.github.forest.config;

import com.github.forest.auth.BaseHashedCredentialsMatcher;
import com.github.forest.auth.JwtFilter;
import com.github.forest.auth.JwtRealm;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author sunzy
 * @date 2023/6/16 10:06
 */
@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 添加自定义过滤器
        LinkedHashMap<String, Filter> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("jwt", new JwtFilter());
        shiroFilterFactoryBean.setFilters(linkedHashMap);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/actuator/**", "anon");
        filterChainDefinitionMap.put("/api/v1/console/**", "anon");
        filterChainDefinitionMap.put("/api/v1/article/detail/**", "anon");
        filterChainDefinitionMap.put("/api/v1/topic/**", "anon");
        filterChainDefinitionMap.put("/api/v1/user/**", "anon");
        filterChainDefinitionMap.put("/api/v1/article/*/comments", "anon");
        filterChainDefinitionMap.put("/api/v1/rule/currency/**", "anon");
        filterChainDefinitionMap.put("/api/v1/lucene/**", "anon");
        filterChainDefinitionMap.put("/api/v1/open-data/**", "anon");
        filterChainDefinitionMap.put("/api/v1/auth/login/**", "anon");
        filterChainDefinitionMap.put("/api/v1/auth/logout/**", "anon");
        filterChainDefinitionMap.put("/api/v1/auth/refresh-token/**", "anon");
        filterChainDefinitionMap.put("/api/v1/sse/**", "anon");
        filterChainDefinitionMap.put("/**", "jwt");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }

    @Bean
    public JwtRealm baseShiroRealm() {
        JwtRealm jwtRealm = new JwtRealm();
        jwtRealm.setCredentialsMatcher(new BaseHashedCredentialsMatcher());
        return jwtRealm;
    }

    /**
     * 配置安全管理器
     * @return
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(baseShiroRealm());
        defaultWebSecurityManager.setRememberMeManager(null);
        return defaultWebSecurityManager;
    }

    /**
     * 开启shiro aop 注解支持
     * 使用代理方式
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * shiro生命周期处理器
     * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

}
