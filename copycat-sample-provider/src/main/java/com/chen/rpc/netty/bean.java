package com.chen.rpc.netty;

import com.chen.rpc.nettyServer.RpcServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class bean {

    @Bean
    public RpcServer setNetty(
            @Value("${rpc.address}") String rpcAddress,
            @Value("${zookeeper.address}") String zookeeperAddress){
       return new RpcServer(rpcAddress,zookeeperAddress);
    }
}
