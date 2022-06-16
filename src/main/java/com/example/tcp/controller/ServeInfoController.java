package com.example.tcp.controller;

import com.example.tcp.server.BootNettyChannelInboundHandlerAdapter;
import com.example.tcp.util.RedisUtil;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ServeInfoController {

    @Autowired
    RedisUtil redisUtil;

    @GetMapping("/getLinks")
    public Map getLinks(){

        int size = BootNettyChannelInboundHandlerAdapter.channelHandlerContextMap.size();

        Map map = new HashMap();
        map.put("result","当前在线人数：" + size);
        return map;
    }

    @GetMapping("/getCount")
    public Map getCount(){

        Map map = new HashMap();
        map.put("result",redisUtil.get("currentCount"));
        return map;
    }
}
