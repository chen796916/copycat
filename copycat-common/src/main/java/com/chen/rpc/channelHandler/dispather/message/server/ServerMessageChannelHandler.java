package com.chen.rpc.channelHandler.dispather.message.server;

import com.alibaba.fastjson.JSONObject;
import com.chen.rpc.ChannelEventRunnable;
import com.chen.rpc.channelHandler.Executor;
import com.chen.rpc.channelHandler.server.RpcServerHandler;
import com.chen.rpc.constants.Heartbeat;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

@ChannelHandler.Sharable
public class ServerMessageChannelHandler extends RpcServerHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerMessageChannelHandler.class);

    private ThreadPoolExecutor executor = Executor.getExecutor();

    public ServerMessageChannelHandler(Map serviceBeanMap) {
        super(serviceBeanMap);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        JSONObject message = (JSONObject) msg;
        String requestId = message.getString("requestId").toLowerCase();
        if (Heartbeat.REQUEST_ID_HEARTBEAT.equals(requestId)) {
            super.channelRead(ctx, msg);
        }
        try {
            executor.execute(new ChannelEventRunnable(msg, ctx, new RpcServerHandler()));
        }catch (Throwable T) {
            LOGGER.error("消息处理失败");
            T.printStackTrace();
        }
    }
}
