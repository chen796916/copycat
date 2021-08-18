package com.chen.rpc.loadBalance;

import com.chen.rpc.spi.SPI;
import io.netty.channel.Channel;

import java.util.Map;

@SPI("RandomLoadBalance")
public interface LoadBalance {

    Channel select(Map<String, Channel> channels);
}
