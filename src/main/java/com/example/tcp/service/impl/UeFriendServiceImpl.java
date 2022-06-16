package com.example.tcp.service.impl;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.tcp.dao.UeFriendMapper;
import com.example.tcp.pojo.UeFriend;
import com.example.tcp.pojo.UeUser;
import com.example.tcp.service.UeFriendService;
import com.example.tcp.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jay
 * @since 2022-05-29
 */
@Service
public class UeFriendServiceImpl extends ServiceImpl<UeFriendMapper, UeFriend> implements UeFriendService {

    @Autowired
    UeFriendMapper ueFriendMapper;

    @Override
    public List<UeUser> getFriend(String token) {
        DecodedJWT decodedJWT = TokenUtil.deTokenForTcp(token);
        String account = decodedJWT.getClaim("account").asString();

        return ueFriendMapper.getFriends(account);
    }

    @Override
    public List<UeUser> findFriends(String account,String username,String keyword) {
        List<UeUser> friends = ueFriendMapper.findFriends(account, keyword);
        List<Map<String, String>> my = ueFriendMapper.myAdd(account);
        List<Map<String, String>> others = ueFriendMapper.othersAdd(account);

        UeUser ueUser = new UeUser();
        ueUser.setAccount(account);
        ueUser.setUsername(username);
        friends.remove(ueUser);

        List<UeUser> tempMap = new ArrayList<>();

        if (friends.size() > 0)
        friends.forEach(friend->{
            String rawAccount = friend.getAccount();
            Map<String,String> map = new HashMap<>();
            map.put("account",rawAccount);
            if (my.contains(map) || others.contains(map)) {
                tempMap.add(friend);
            }
        });
        if (tempMap.size() > 0)
        tempMap.forEach(friends::remove);
        return friends;
    }
}
