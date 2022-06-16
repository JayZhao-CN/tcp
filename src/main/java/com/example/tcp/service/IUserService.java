package com.example.tcp.service;

import com.example.tcp.pojo.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface IUserService {

    Map loginCheck(User user, HttpServletResponse response) ;
}
