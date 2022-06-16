package com.example.tcp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tcp.pojo.UeFriend;
import com.example.tcp.pojo.UeUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Jay
 * @since 2022-05-29
 */
@Mapper
@Repository
public interface UeFriendMapper extends BaseMapper<UeFriend> {

    List<UeUser> getFriends(String account);

    List<UeUser> findFriends(String account,String keyword);
    List<Map<String,String>> myAdd(String account);
    List<Map<String,String>> othersAdd(String account);
}
