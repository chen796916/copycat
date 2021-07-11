package com.chen.rpc.proxy;

import com.chen.rpc.bean.Request;
import com.chen.rpc.discovery.DiscoveryService;
import com.chen.rpc.nettyClient.RpcClientHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.UUID;

@Component
public class RpcProxy{

    private DiscoveryService discoveryService;
    private String zookeeperAddress;
    private RpcClientHandler rpcClient;

    public RpcProxy(String zookeeperAddress){
        this.zookeeperAddress = zookeeperAddress;
        this.discoveryService = new DiscoveryService(zookeeperAddress);
    }

    public <T> T create(Class<T> interfaceClazz){
        return (T) Proxy.newProxyInstance(interfaceClazz.getClassLoader(),
                new Class[]{interfaceClazz},
                (proxy, method, args) -> {
                    Request request = new Request();
                    request.setClassName(interfaceClazz.getName());
                    request.setMethodName(method.getName());
                    request.setRequestId(String.valueOf(UUID.randomUUID()));
                    request.setParameters(args);
                    request.setParameterTypes(method.getParameterTypes());
                    String serviceAddress = discoveryService.getAddress(interfaceClazz.getName());
                    String[] serviceAddressArray = serviceAddress.split(":");
                    String host = serviceAddressArray[0];
                    int port = Integer.parseInt(serviceAddressArray[1]);
                    if(rpcClient == null){
                        rpcClient = new RpcClientHandler();
                    }
                    rpcClient.doConnect(host,port);
                    return rpcClient.send(request).take().getResult();
                });
    }
}
