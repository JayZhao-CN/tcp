package com.example.tcp.util;



import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static com.example.tcp.server.BootNettyChannelInboundHandlerAdapter.*;

@Slf4j
public class MsgHandler {

    /**
     * 服务器发送信息方法
     * @param ctx
     * @param msg
     */
    public static void sendMsg(ChannelHandlerContext ctx, Object msg){
        try {
            JSONObject jsonObject = new JSONObject(msg.toString());

            Object typeCode = jsonObject.get("type");
            if (typeCode.equals("1")){
                String target = (String) jsonObject.get("target");
                Object msg1 = jsonObject.get("msg");
                ChannelHandlerContext ct = channelHandlerContextMap.get(target);
                ct.writeAndFlush(msg1.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 服务器处理收到的方法
     * @param type
     * @param ctx
     * @param jsonObject
     * @throws JSONException
     */
    public static void handlerMsg(String type, ChannelHandlerContext ctx, JSONObject jsonObject) throws JSONException {
        String key = getKey(channelHandlerContextMap, ctx);
        switch (type) {
            // 验证连接
            case "0":
                key = getKey(channelHandlerContextMapTemp,ctx);
                // 是否有此连接
                if (channelHandlerContextMapTemp.containsKey(key)) {
                    DecodedJWT token = TokenUtil.deTokenForTcp(jsonObject.get("token").toString());
                    String account = token.getClaim("account").asString();
                    // 正常流程
                    channelHandlerContextMapTemp.remove(key);
                    channelHandlerContextMap.put(account, ctx);
                } else {
                    // 非法连接
                    ctx.close();
                }
                redisMap.remove(key);
                break;
            // 推送消息
            case "1":
                try {
                    ctx.writeAndFlush(jsonObject.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            // 群组聊天
            case "2":
                // 群组：群组id
                log.info(key+"对群组"+jsonObject.get("group").toString()+"说：{}",jsonObject.get("msg"));

                ctx.writeAndFlush("type:2,msg:这是服务器发来的群组消息！");
                break;
            // 私聊
            case "3":
                // 私聊：私聊对象id {token:"",type:"",aim:"",msg:""} 》》 私聊
                String aim = jsonObject.get("aim").toString(); // 发送目标用户id
                JSONObject jsonObject1 = new JSONObject();
                if (channelHandlerContextMap.containsKey(aim)) {
                    DecodedJWT token = TokenUtil.deTokenForTcp(jsonObject.get("token").toString());
                    String account = token.getClaim("account").asString();
                    // 好友在线
                    jsonObject1.put("type","3");
                    jsonObject1.put("msg",jsonObject.get("msg"));
                    jsonObject1.put("aim",account);
                    channelHandlerContextMap.get(aim).writeAndFlush(jsonObject1.toString());
                } else {
                    jsonObject1.put("type","1");
                    jsonObject1.put("msg","好友不在线！");
                    jsonObject1.put("aim",jsonObject.get("aim"));
                    ctx.writeAndFlush(jsonObject1.toString());
                }
                break;
//            case "4":
//                break;
//            case "5":
//                break;
//            case "6":
//                break;
            // 默认返回
            default:
                ctx.close();
                break;

        }
    }

    // 根据键值获取key值
    public static String getKey(Map map, Object value){

        Set set = map.entrySet(); //通过entrySet()方法把map中的每个键值对变成对应成Set集合中的一个对象
        Iterator<Map.Entry<Object, Object>> iterator = set.iterator();
        ArrayList<Object> arrayList = new ArrayList();
        while(iterator.hasNext()){
            //Map.Entry是一种类型，指向map中的一个键值对组成的对象
            Map.Entry<Object, Object> entry = iterator.next();
            if(entry.getValue().equals(value)){
                return entry.getKey().toString();

            }
        }
        return "";
    }
}
