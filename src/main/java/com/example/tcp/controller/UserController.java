package com.example.tcp.controller;

import com.example.tcp.pojo.User;
import com.example.tcp.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private IUserService userService;


    @PostMapping("/loginCheck")
    public Map login(User user, HttpServletResponse response){

//        R result = userService.loginCheck(user, response);
        Map map = userService.loginCheck(user, response);
        return map;
    }
}


