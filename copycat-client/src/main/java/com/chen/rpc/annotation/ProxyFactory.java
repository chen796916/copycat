package com.chen.rpc.annotation;

import com.chen.rpc.bean.Request;
import com.chen.rpc.channelHandler.dispather.message.client.ClientMessageChannelHandler;
import com.chen.rpc.discovery.DiscoveryService;
import io.netty.channel.Channel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ProxyFactory<T> implements InvocationHandler {

    private DiscoveryService discoveryService;
    private String zookeeperAddress;
    private ClientMessageChannelHandler rpcClient;

    public ProxyFactory(String zookeeperAddress){
        this.zookeeperAddress = zookeeperAddress;
        this.discoveryService = new DiscoveryService(zookeeperAddress);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<T> interfaceClazz = (Class<T>) method.getDeclaringClass();
        Request request = new Request();
        request.setClassName(interfaceClazz.getName());
        request.setMethodName(method.getName());
        request.setRequestId(String.valueOf(UUID.randomUUID()));
        request.setParameters(args);
        request.setParameterTypes(method.getParameterTypes());
        List<String> addressList = discoveryService.getAddressList(interfaceClazz.getName());
        // String serviceAddress = discoveryService.getAddress(interfaceClazz.getName());
        if(rpcClient == null){
            rpcClient = new ClientMessageChannelHandler();
        }
        Map<String, Channel> channels = rpcClient.getChannels();
        for (String serviceAddress : addressList) {
            if (channels.keySet().contains("/" + serviceAddress)) {
                continue;
            }
            String[] serviceAddressArray = serviceAddress.split(":");
            String host = serviceAddressArray[0];
            int port = Integer.parseInt(serviceAddressArray[1]);
            rpcClient.doConnect(host,port);
        }
        return rpcClient.send(request).take().getResult();
    }
}
