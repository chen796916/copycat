package com.chen.rpc.loadBalance;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Random;

public class RandomLoadBalance extends LoadBalance {

    private static final Logger LOGGER = LoggerFactory.getLogger(RandomLoadBalance.class);

    public Channel select(Map<String, Channel> channels) {
        int randomIndex = new Random().nextInt(channels.size());
        Channel target = null;
        int i = 0;
        for (Channel channel : channels.values()) {
            target = channel;
            if (i == randomIndex) {
                return target;
            }
            i++;
        }
        LOGGER.error("无法找到Channel");
        return null;
    }
}
