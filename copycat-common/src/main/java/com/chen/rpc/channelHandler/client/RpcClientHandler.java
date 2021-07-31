package com.chen.rpc.channelHandler.client;

import com.alibaba.fastjson.JSONObject;
import com.chen.rpc.bean.Request;
import com.chen.rpc.bean.Response;
import com.chen.rpc.constants.Heartbeat;
import com.chen.rpc.loadBalance.LoadBalance;
import com.chen.rpc.loadBalance.RandomLoadBalance;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;

@ChannelHandler.Sharable
public class RpcClientHandler extends ChannelInboundHandlerAdapter {

    private String host;
    private int port;
    private Channel channel;
    private Map<String, Channel> channels = new ConcurrentHashMap<>();
    private Response response;

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcClientHandler.class);
    private static ConcurrentHashMap<String, SynchronousQueue<Response>> queueMap = new ConcurrentHashMap<>();

    public void doConnect(String host,int port){
        //if(channel != null){
        //    return;
        //}
        this.host = host;
        this.port = port;
        try {
            EventLoopGroup group = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ClientChannelInitializerImpl());
            ChannelFuture future = bootstrap.connect(host,port).sync();
            channel = future.channel();
            channels.put(String.valueOf(channel.remoteAddress()), channel);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public SynchronousQueue<Response> send(Request request){
        SynchronousQueue<Response> queue = new SynchronousQueue<>();
        queueMap.put(request.getRequestId(), queue);
        LoadBalance loadBalance = new RandomLoadBalance();
        Channel channel = loadBalance.select(channels);
        channel.writeAndFlush(request);
        return queue;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("与服务端{}连接成功",ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("与服务端{}断开连接",ctx.channel().remoteAddress());
        ctx.channel().close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Response response = JSONObject.parseObject(msg.toString(),Response.class);
        SynchronousQueue<Response> queue = queueMap.get(response.getRequestId());
        queue.put(response);
        queueMap.remove(response.getRequestId());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error(cause.getMessage());
        cause.printStackTrace();
        ctx.channel().close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if(idleStateEvent.state() == IdleState.ALL_IDLE){
                Request request = new Request();
                request.setRequestId(Heartbeat.REQUEST_ID_HEARTBEAT);
                ctx.channel().writeAndFlush(request);
            }else{
                super.userEventTriggered(ctx, evt);
            }
        }
    }
}
