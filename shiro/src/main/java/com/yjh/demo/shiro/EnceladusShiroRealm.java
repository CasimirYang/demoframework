package com.yjh.demo.shiro;

import com.yjh.demo.shiro.dao.SysPermission;
import com.yjh.demo.shiro.dao.SysRole;
import com.yjh.demo.shiro.dao.User;
import com.yjh.demo.shiro.service.PasswordHelper;
import com.yjh.demo.shiro.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EnceladusShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;


    /**
     * 授权
     *
     *  获取角色，权限
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        String username = (String) principals.getPrimaryPrincipal();

        User user = userService.findUserByName(username);

        for (SysRole role : user.getRoles()) {
            authorizationInfo.addRole(role.getRole()); //增加角色
            for (SysPermission permission : role.getPermissions()) {
                authorizationInfo.addStringPermission(permission.getName());  //增加权限
            }
        }
        return authorizationInfo;
    }

    /**
     * 登陆认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        User user = userService.findUserByName(username);
        if (user == null) {
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user.getUserName(), user.getPassword(),
                ByteSource.Util.bytes(user.getCredentialsSalt()), getName());
        return authenticationInfo;
    }

}