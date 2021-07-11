package com.chen.rpc.codec;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class RpcJSONDecoder extends LengthFieldBasedFrameDecoder {

    /**
     * 数据包最大长度128kb，长度域为4kb，长度域的值为数据的长度（也就是不包含长度域的长度），并接受后丢弃长度域
     */
    public RpcJSONDecoder(){
        super(1195725860, 0, 4,0,4);
    }

    @Override
    protected Object decode(ChannelHandlerContext context, ByteBuf in) throws Exception{
        ByteBuf byteBuf = (ByteBuf) super.decode(context, in);
        if (byteBuf == null){
            return null;
        }
        int length = byteBuf.readableBytes();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        return JSON.parse(bytes);
    }
}

