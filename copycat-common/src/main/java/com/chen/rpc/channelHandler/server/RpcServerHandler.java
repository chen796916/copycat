package com.chen.rpc.channelHandler.server;

import com.alibaba.fastjson.JSONObject;
import com.chen.rpc.bean.Request;
import com.chen.rpc.bean.Response;
import com.chen.rpc.constants.Heartbeat;
import com.chen.rpc.result.ResultCode;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

@ChannelHandler.Sharable
public class RpcServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServerHandler.class);

    private static Map serviceBeanMap;

    public RpcServerHandler(){}

    public RpcServerHandler(Map serviceBeanMap){
        this.serviceBeanMap = serviceBeanMap;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("与客户端{}连接成功",ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("与客户端{}断开连接",ctx.channel().remoteAddress());
        ctx.close();
    }

    /**
     * 读取入站消息并处理，返回response
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Request request = JSONObject.parseObject(msg.toString(),Request.class);
        //心跳消息，不做处理
        if(Heartbeat.REQUEST_ID_HEARTBEAT.equals(request.getRequestId().toLowerCase())){

        }else{
            Response response = new Response();
            response.setRequestId(request.getRequestId());
            try {
                Object result = handler(request);
                response.setResult(result);
                response.setCode(ResultCode.SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
                response.setException(e);
                response.setCode(ResultCode.FAIL);
            }
            ctx.writeAndFlush(response);
        }
    }

    /**
     * 从serviceBeanMap中找到服务并通过反射执行服务
     * @param request
     * @return
     * @throws Exception
     */
    private Object handler(Request request) throws Exception{
        String className = request.getClassName();
        String methodName = request.getMethodName();
        Class<?>[] parametersTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();
        //从serviceMapBean中找出bean
        Object serviceBean = serviceBeanMap.get(className);
        if(serviceBean != null){
            //反射调用
            Class<?> serviceClass = serviceBean.getClass();
            Method serviceMethod = serviceClass.getMethod(methodName,parametersTypes);
            serviceMethod.setAccessible(true);
            return serviceMethod.invoke(serviceBean,parameters);
        }else{
            return new Exception("找不到服务->"+className);
        }
    }

    /**
     * 超时处理
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            if(event.state() == IdleState.ALL_IDLE){
                LOGGER.info("客服端30秒内无发送心跳，链接断开");
                ctx.channel().close();
            }else{
                super.userEventTriggered(ctx, evt);
            }
        }
    }

    /**
     * 异常处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error(cause.getMessage());
        cause.printStackTrace();
        ctx.close();
    }
}
