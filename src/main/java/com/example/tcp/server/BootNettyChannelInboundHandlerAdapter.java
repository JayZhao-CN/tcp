package com.example.tcp.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;

import cn.hutool.core.util.IdUtil;
import com.example.tcp.util.MsgHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import static com.example.tcp.util.MsgHandler.getKey;

/**
 * I/O数据读写处理类
 */
@Slf4j
@Component
public class BootNettyChannelInboundHandlerAdapter extends ChannelInboundHandlerAdapter {

    // 仿云端存储
    public static Map<String, Long> redisMap = new HashMap<>();
    // 连接对象集合
    public static Map<String, ChannelHandlerContext> channelHandlerContextMap = new HashMap<>();
    // 临时连接对象集合
    public static Map<String, ChannelHandlerContext> channelHandlerContextMapTemp = new HashMap<>();

    // 群组对象集合
    public static Map<String, List<ChannelHandlerContext>> GroupMap = new HashMap<>();

    public static int index = 0;

    public static boolean thread = false;

    /**
     * 连接后 与 正式连接前逻辑
     */
    // 启用一个线程
    private void startCycle() {
        new Thread(new Runnable() {
            public void run() {
                // run方法具体重写
                while (true) {
                    for (Map.Entry<String, ChannelHandlerContext> stringChannelHandlerContextEntry : channelHandlerContextMapTemp.entrySet()) {
                        String key = stringChannelHandlerContextEntry.getKey();
                        ChannelHandlerContext value = stringChannelHandlerContextEntry.getValue();
                        if (redisMap.containsKey(key)) {
//                        long mill = redisUtil.get(key);
                            try {
                                long mill = redisMap.get(key);
                                long current = System.currentTimeMillis();
                                if (current - mill > 1000 * 3) {
                                    // 断开连接
                                    channelHandlerContextMapTemp.get(key).close();
                                    // 缓存区删除对象
                                    channelHandlerContextMapTemp.remove(key);
                                    // 清除云端缓存
//                            redisUtil.del(key);
                                    redisMap.remove(key);
                                }
                            } catch (Exception e) {
                                log.warn("循环检测出错！");
                            }
                        }
                    }
                    // 每一秒钟检测一次
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 从客户端收到新的数据时，这个方法会在收到消息时被调用
     *
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // {token:"",type:"",aim:"",msg:""} 》》 私聊
        // {token:"",type:"",group:"",msg:""} 》》 群聊

        // 是否携带token
        try {
            JSONObject jsonObject = new JSONObject(msg.toString());
            Object token = jsonObject.get("token");
            // 携带了token
            if (token != null) {
                String type = (String) jsonObject.get("type");
                // 处理消息
                MsgHandler.handlerMsg(type, ctx, jsonObject);
            }
        } catch (Exception e) {
            e.getStackTrace();
            log.warn("非法连接！");
            ctx.close();
        }

        // 不携带token，则去map中查找，没有的话直接关闭连接，有的话，对这条消息忽略
        // 携带token，则传给消息处理方法
        System.out.println("客户端 " + getKey(channelHandlerContextMap, ctx) + "：" + msg.toString());
        //回应客户端
//        ctx.write("服务器回复【客户端" + getKey(channelHandlerContextMap,ctx) + "】: 服务器已收到信息！");
//        MsgHandler.sendMsg(ctx, msg, channelHandlerContextMap);
    }

    /**
     * 从客户端收到新的数据、读取完成时调用
     *
     * @param ctx
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws IOException {
        System.out.println("系统消息：数据读取完毕！");
        ctx.flush();
    }

    /**
     * 当出现 Throwable 对象才会被调用，即当 Netty 由于 IO 错误或者处理器在处理事件时抛出的异常时
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws IOException {
        System.out.println("系统提示：IO出错! 断开【客户端 " + getKey(channelHandlerContextMap, ctx) + "】连接！");
        cause.printStackTrace();
        ctx.close();//抛出异常，断开与客户端的连接
    }

    /**
     * 客户端与服务端第一次建立连接时 执行
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ctx.channel().read();
        InetSocketAddress inSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = inSocket.getAddress().getHostAddress();

        String id = null;

        // 先校验是否是重复连接
        String key = getKey(channelHandlerContextMap, ctx);
        if (key != null && !key.equals("")) {
            // 有该连接存在
        } else {
            /**
             * 初次连接逻辑
             */
            // 新连接进来
            // 生成一个唯一key
            id = IdUtil.simpleUUID();
            // 将唯一 key 与 ctx 存入临时map中
            channelHandlerContextMapTemp.put(id, ctx);
            // 将唯一 key 与 currenTimeMills 存入redis
            redisMap.put(id,System.currentTimeMillis());
//            redisUtil.set(id, System.currentTimeMillis());
            // 此时服务器发送请求token的流
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type","0");
            jsonObject.put("msg","token");
            jsonObject.put("aim",id);
            System.out.println(jsonObject.toString());
            ctx.writeAndFlush(jsonObject.toString());
            // 此方法流程结束

            // 新连接+1
            index++;
        }

//        index++;
//        //此处不能使用ctx.close()，否则客户端始终无法与服务端建立连接
//        channelHandlerContextMap.put(String.valueOf(index),ctx);
        log.info("系统消息：新连接———》" + clientIp + " id为:" + id);
        log.info("当前连接数：" + channelHandlerContextMap.size());

        // 每当有新连接就验证一次该线程是否存在
        if (!thread) {
            startCycle();
        }
    }

    /**
     * 客户端与服务端 断连时 执行
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception, IOException {
        super.channelInactive(ctx);
        InetSocketAddress inSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = inSocket.getAddress().getHostAddress();

        ctx.close(); //断开连接时，必须关闭，否则造成资源浪费，并发量很大情况下可能造成宕机
        System.out.println("系统消息：【客户端 " + getKey(channelHandlerContextMap, ctx) + "】断开连接！");

        String key = getKey(channelHandlerContextMap, ctx);
        channelHandlerContextMap.remove(key);
    }


    /**
     * 服务端当read超时, 会调用这个方法
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception, IOException {
        super.userEventTriggered(ctx, evt);
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = insocket.getAddress().getHostAddress();
        System.out.println("系统提示：【客户端 " + getKey(channelHandlerContextMap, ctx) + "】读取超时！断开连接！");
        ctx.close();//超时时断开连接

        String key = getKey(channelHandlerContextMap, ctx);
        channelHandlerContextMap.remove(key);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接成功！");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("断开连接成功！");
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接写入状态改变！");
    }
}


