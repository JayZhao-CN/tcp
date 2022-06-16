package com.example.tcp.intercept;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.tcp.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private TokenUtil tokenUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
//        查看请求中是否存在token，如果不存在直接跳转到登陆页面
        String token = tokenUtil.getToken(request);

        if (token == null || token.isEmpty()) {
//            response.sendRedirect("/login");
            response.getWriter().write("{ \"result\":false }");
            return false;
        }else {
//            String username = TokenUtil.get(token, "username");

//            DecodedJWT decodedJWT = TokenUtil.deToken(token);
//            System.out.println(decodedJWT.getClaims().get("username").asString());
//            if (!username.equals("张三")){
//                response.getWriter().write("{result:false}");
//                return false;
//            }
            return true;
        }
    }
}

