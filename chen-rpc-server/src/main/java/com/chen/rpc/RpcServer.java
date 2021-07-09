package com.chen.rpc;

import com.chen.rpc.register.RegisterService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RpcServer implements ApplicationContextAware, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);

    /**
     * 存放服务名称和对应的bean
     */
    private Map<String,Object> serviceBeanMap = new HashMap<String, Object>();

    private RegisterService registerService;

    /**
     * 服务提供者的地址
     */
    private String rpcAddress;

    /**
     * zookeeper的地址（注册中心）
     */
    private String zookeeperAddress;

    public RpcServer(String rpcAddress,String zookeeperAddress){
        this.rpcAddress = rpcAddress;
        this.zookeeperAddress = zookeeperAddress;
        this.registerService = new RegisterService(zookeeperAddress);
    }

    /**
     * 获取spring容器，spring会检查所有的bean，发现某个bean
     * 实现了ApplicationContextArare，会自动调用setApplicationContext（）方法
     * @param applicationContext
     * @throws BeansException
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //获取所有bean，并将bean放入serviceBeanMap中
        Map<String,Object> map = applicationContext.getBeansWithAnnotation(ChenRpcService.class);
        for(Object bean : map.values()){
            Class<?>[] interfaces =  bean.getClass().getInterfaces();
            for(Class<?> interfaceName : interfaces){
                serviceBeanMap.put(interfaceName.getName(),bean);
                LOGGER.info("服务："+interfaceName.getName()+"已被加载");
            }
        }
    }

    /**
     * 初始bean时执行
     * 执行start（）方法
     * 也就是rpc服务提供者的引导类
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {
        start();
    }

    /**
     * rpc服务提供者的引导类
     */
    private void start(){
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss,worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childOption(ChannelOption.TCP_NODELAY,true)
                    .childHandler(new ChannelInitializerImpl(serviceBeanMap));
            String[] addressArray = rpcAddress.split(String.valueOf(':'));
            String host = addressArray[0];
            int port = Integer.parseInt(addressArray[1]);
            ChannelFuture future = bootstrap.bind(host,port).sync();
            LOGGER.info("服务监听端口->"+port);
            //注册服务名称和地址
            for(String interfaceName : serviceBeanMap.keySet()){
                registerService.register(interfaceName,rpcAddress);
                LOGGER.info("服务->{}已被注册，该服务地址为->{}",interfaceName,rpcAddress);
            }
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }
}
