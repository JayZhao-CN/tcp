package com.example.tcp.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.tcp.pojo.UeFriend;
import com.example.tcp.pojo.UeUser;
import com.example.tcp.service.UeFriendService;
import com.example.tcp.service.UeUserService;
import com.example.tcp.util.MsgHandler;
import com.example.tcp.util.TokenUtil;
import io.netty.channel.ChannelHandlerContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.tcp.server.BootNettyChannelInboundHandlerAdapter.channelHandlerContextMap;

@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    UeUserService userService;
    @Autowired
    UeFriendService ueFriendService;

    @PostMapping("/token")
    public Map getToken(@RequestParam("account")String account, @RequestParam("password")String password){
        QueryWrapper<UeUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account",account);

        UeUser one = userService.getOne(queryWrapper);
        if (one.getPassword().equals(password)){
            UeUser ueUser = new UeUser();
            ueUser.setUsername(one.getUsername());
            ueUser.setAccount(account);
            ueUser.setPassword(password);
            System.out.println(TokenUtil.generateTokenForTcp(ueUser));
            Map map = new HashMap();
            map.put("code",1);
            map.put("token",TokenUtil.generateTokenForTcp(ueUser));
            Map map1 = new HashMap();
            map1.put("result",map);
            return map1;
        }else {
            Map map = new HashMap();
            map.put("code",0);
            Map map1 = new HashMap();
            map1.put("result",map);
            return map1;
        }
    }

    @PostMapping("/friends")
    public Map getFriendsList(@RequestParam("token")String token){
        List<UeUser> friend = ueFriendService.getFriend(token);
        Map map = new HashMap();
        map.put("result",friend);
        return map;
    }

    @PostMapping("/findFriends")
    public Map findFriends(@RequestParam("keyword")String keyword, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("token");
        DecodedJWT decodedJWT = TokenUtil.deTokenForTcp(token);
        String account = decodedJWT.getClaim("account").asString();
        String username = decodedJWT.getClaim("username").asString();

        List<UeUser> friends = ueFriendService.findFriends(account,username, "%"+keyword+"%");
        Map map = new HashMap();
        map.put("result",friends);

        return map;
    }

    @PostMapping("/addFriend")
    public Map addFriend(@RequestParam("friend_account")String friend_account, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("token");
        DecodedJWT decodedJWT = TokenUtil.deTokenForTcp(token);
        String account = decodedJWT.getClaim("account").asString();
        UeFriend ueFriend = new UeFriend();
        ueFriend.setUserAccount(account);
        ueFriend.setFriendAccount(friend_account);
        boolean save = ueFriendService.save(ueFriend);

        if (save) {
            try {
                ChannelHandlerContext ctx = channelHandlerContextMap.get(friend_account);
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("type","1");
                jsonObject1.put("msg",account + "已加您为好友！");
                jsonObject1.put("aim",account);
                MsgHandler.handlerMsg("1",ctx,jsonObject1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Map map = new HashMap();
        map.put("result",save);

        return map;
    }

}
