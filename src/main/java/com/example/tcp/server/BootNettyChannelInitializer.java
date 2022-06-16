package com.example.tcp.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 通道初始化
 */
public class BootNettyChannelInitializer<SocketChannel> extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {


        // ChannelOutboundHandler，依照逆序执行
        ch.pipeline().addLast("encoder", new StringEncoder());

        // 属于ChannelInboundHandler，依照顺序执行
        ch.pipeline().addLast("decoder", new StringDecoder());

        /*
         *
         *    自定义ChannelInboundHandlerAdapter
         *    该项在源码中放在了上面两项上面，实际上需要调整到后面
         */
        ch.pipeline().addLast(new BootNettyChannelInboundHandlerAdapter());

    }

}