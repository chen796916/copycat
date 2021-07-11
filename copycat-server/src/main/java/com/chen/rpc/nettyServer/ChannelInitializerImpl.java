package com.chen.rpc.nettyServer;

import com.chen.rpc.codec.RpcJSONDecoder;
import com.chen.rpc.codec.RpcJSONEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ChannelInitializerImpl extends ChannelInitializer<SocketChannel> {

    private Map serviceBeanMap;

    public ChannelInitializerImpl(Map serviceBeanMap){
        this.serviceBeanMap = serviceBeanMap;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new IdleStateHandler(0,0,60, TimeUnit.SECONDS));
        pipeline.addLast(new RpcJSONDecoder());
        pipeline.addLast(new RpcJSONEncoder());
        pipeline.addLast(new RpcServerHandler(serviceBeanMap));
    }
}
