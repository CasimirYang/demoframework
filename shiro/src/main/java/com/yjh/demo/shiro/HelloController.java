package com.yjh.demo.shiro;

import com.yjh.demo.shiro.dao.User;
import com.yjh.demo.shiro.service.PasswordHelper;
import com.yjh.demo.shiro.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private UserService userService;

    @RequestMapping("doLogin")
    public Object doLogin(@RequestParam String username, @RequestParam String password) {
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            Session session = subject.getSession();
            session.setAttribute("bili","bbbc");
        } catch (IncorrectCredentialsException ice) {
            return "password error!";
        } catch (UnknownAccountException uae) {
            return "username error!";
        }
        User user = userService.findUserByName(username);
        subject.getSession().setAttribute("user", user);
        return "SUCCESS";
    }

    @RequestMapping("access")
    public Object register() {
        User user = User.getDefault();
        Subject subject = SecurityUtils.getSubject();
        boolean isAuthenticated = subject.isAuthenticated();
        System.out.println(isAuthenticated);
        Session session = subject.getSession();
        Object object = session.getAttribute("bili");
        System.out.println(object.toString());
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(), user.getPassword());

        return "SUCCESS";
    }

}
