package com.example.tcp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.tcp.pojo.UeFriend;
import com.example.tcp.pojo.UeUser;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jay
 * @since 2022-05-29
 */
public interface UeFriendService extends IService<UeFriend> {

    List<UeUser> getFriend(String token);

    List<UeUser> findFriends(String account,String username,String keyword);
}
