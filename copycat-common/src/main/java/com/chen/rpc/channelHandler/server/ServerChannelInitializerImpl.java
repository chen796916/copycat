package com.chen.rpc.channelHandler.server;

import com.chen.rpc.channelHandler.dispather.message.server.ServerMessageChannelHandler;
import com.chen.rpc.codec.RpcJSONDecoder;
import com.chen.rpc.codec.RpcJSONEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ServerChannelInitializerImpl extends ChannelInitializer<SocketChannel> {

    private Map serviceBeanMap;

    public ServerChannelInitializerImpl(Map serviceBeanMap){
        this.serviceBeanMap = serviceBeanMap;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new IdleStateHandler(0,0,60, TimeUnit.SECONDS));
        pipeline.addLast(new RpcJSONDecoder());
        pipeline.addLast(new RpcJSONEncoder());
        pipeline.addLast(new ServerMessageChannelHandler(serviceBeanMap));
    }
}
