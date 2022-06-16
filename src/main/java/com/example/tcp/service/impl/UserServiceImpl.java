package com.example.tcp.service.impl;

import com.example.tcp.pojo.User;
import com.example.tcp.service.IUserService;
import com.example.tcp.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: OnlyOne
 * @create: 2020-12-22 14:42
 * @description:
 **/
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    TokenUtil tokenUtil;

    @Override
    public Map loginCheck(User user, HttpServletResponse response) {
//        User user2 = userMapper.selectByName(user.getUsername());
        Map map = new HashMap();

        User user2 = new User();
        user2.setId(1);
        user2.setUsername("张三");
        user2.setPassword("123456");

        if (!user2.getPassword().equals(user.getPassword())) {
            map.put("result","密码错误！");
            return map;
        }
        String token = tokenUtil.generateToken(user2);
//        Cookie cookie = new Cookie("token", token);
//        设置cookie的作用域：为”/“时，以在webapp文件夹下的所有应用共享cookie
//        cookie.setPath("/");
//        response.addCookie(cookie);
        map.put("result","登录成功！");
        map.put("token",token);
        return map;
    }
}

