package com.yjh.demo.shiro;

import com.yjh.demo.shiro.service.PasswordHelper;
import net.sf.ehcache.CacheManager;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.apache.shiro.mgt.SecurityManager;

import java.util.HashMap;
import java.util.Map;

@Component
public class ShiroConfig {

    /**
     * 通过securityManager配置shiro 的过滤链
     *
     * 常用的过滤器如下：
     *  authc：所有已登陆用户可访问
     *  roles：有指定角色的用户可访问，通过[ ]指定具体角色
     *  perms：有指定权限的用户可访问，通过[ ]指定具体权限
     *  anon：所有用户可访问
     *
     *权限mapper可以通过注解的方式注解方法控制权限
     *  @RequiresAuthentication
     *  @RequiresGuest
     *  @RequiresRoles("administrator")
     *  @RequiresPermissions("account:create")
     *  @RequiresUser
     *      验证用户是否被记忆，user有两种含义：
     *          一种是成功登录的（subject.isAuthenticated() 结果为true）,另外一种是被记忆的（subject.isRemembered()结果为true）。
     */
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, String> filterChainDefinitionMap = new HashMap<>();
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthc");
        shiroFilterFactoryBean.setSuccessUrl("/index");

        filterChainDefinitionMap.put("/*", "authc");
        filterChainDefinitionMap.put("/authc/index", "authc");
        filterChainDefinitionMap.put("/authc/admin", "roles[admin]");
        filterChainDefinitionMap.put("/authc/renewable", "perms[Create,Update]");
        filterChainDefinitionMap.put("/authc/removable", "perms[Delete]");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     *
     * Matcher -> Realm                              :  CredentialsMatcher是一个接口，功能就是用来匹配token 和 subject
     * Realm -> SecurityManager                      :  AuthorizingRealm是shiro Realm的其中一种
     * SecurityManager -> ShiroFilterFactoryBean
     *
     */
    @Bean
    public SecurityManager securityManager(@Autowired EnceladusShiroRealm authorizingRealm,@Autowired EhCacheManager ehCacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        securityManager.setSubjectDAO();
//        securityManager.setCacheManager();
//        securityManager.setSessionManager();

        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        CachingSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
        defaultWebSessionManager.setSessionDAO(sessionDAO);
        securityManager.setSessionManager(defaultWebSessionManager);

        authorizingRealm.setCredentialsMatcher(hashedCredentialsMatcher()); // subject 的mapper

        securityManager.setCacheManager(ehCacheManager);
        securityManager.setRealm(authorizingRealm);

        return securityManager;
    }

    /**
     * CredentialsMatcher是一个接口，功能就是用来匹配token 和 subject; 凭证匹配器
     */
    private HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName(PasswordHelper.ALGORITHM_NAME); // 散列算法
        hashedCredentialsMatcher.setHashIterations(PasswordHelper.HASH_ITERATIONS); // 散列次数
        return hashedCredentialsMatcher;
    }

    @Bean
    public EhCacheManager getEhCacheManager(){
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.init();
        return ehCacheManager;
    }


}
