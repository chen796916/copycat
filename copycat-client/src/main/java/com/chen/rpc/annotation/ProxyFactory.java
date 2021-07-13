package com.chen.rpc.annotation;

import com.chen.rpc.bean.Request;
import com.chen.rpc.discovery.DiscoveryService;
import com.chen.rpc.nettyClient.RpcClientHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

@Component
public class ProxyFactory<T> implements InvocationHandler {

    private DiscoveryService discoveryService;
    private String zookeeperAddress;
    private RpcClientHandler rpcClient;

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
        String serviceAddress = discoveryService.getAddress(interfaceClazz.getName());
        String[] serviceAddressArray = serviceAddress.split(":");
        String host = serviceAddressArray[0];
        int port = Integer.parseInt(serviceAddressArray[1]);
        if(rpcClient == null){
            rpcClient = new RpcClientHandler();
        }
        rpcClient.doConnect(host,port);
        return rpcClient.send(request).take().getResult();
    }
}
