package com.chen.rpc.channelHandler.dispather.message.client;

import com.chen.rpc.ChannelEventRunnable;
import com.chen.rpc.channelHandler.Executor;
import com.chen.rpc.channelHandler.client.RpcClientHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;

@ChannelHandler.Sharable
public class ClientMessageChannelHandler extends RpcClientHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientMessageChannelHandler.class);

    private ThreadPoolExecutor executor = Executor.getExecutor();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            executor.execute(new ChannelEventRunnable(msg, ctx, new RpcClientHandler()));
        }catch (Throwable T) {
            LOGGER.error("消息处理失败");
            T.printStackTrace();
        }
    }
}
