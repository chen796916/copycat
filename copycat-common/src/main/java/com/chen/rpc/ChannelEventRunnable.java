package com.chen.rpc;

import com.alibaba.fastjson.JSONObject;
import com.chen.rpc.constants.BeanType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelEventRunnable implements Runnable{

    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelEventRunnable.class);

    private Object message;
    private ChannelInboundHandlerAdapter handler;
    private ChannelHandlerContext ctx;

    public ChannelEventRunnable(Object message, ChannelHandlerContext ctx, ChannelInboundHandlerAdapter handler) {
        this.message = message;
        this.ctx = ctx;
        this.handler = handler;
    }

    @Override
    public void run() {
        JSONObject msg = (JSONObject) message;
        String type = msg.getString("type");
        if (type.equals(BeanType.REQUEST)) {
            try {
                handler.channelRead(ctx, message);
            } catch (Exception e) {
                LOGGER.error("发送消息失败");
                e.printStackTrace();
            }
        }
        if (type.equals(BeanType.RESPONSE)) {
            try {
                handler.channelRead(ctx, message);
            } catch (Exception e) {
                LOGGER.error("接受消息失败");
                e.printStackTrace();
            }
        }
    }
}
