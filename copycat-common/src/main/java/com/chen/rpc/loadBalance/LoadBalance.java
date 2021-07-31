package com.chen.rpc.loadBalance;

import io.netty.channel.Channel;

import java.util.Map;

public abstract class LoadBalance {

    public abstract Channel select(Map<String, Channel> channels);
}
