package com.chen.rpc.register;

import com.chen.rpc.zookeeper.Constant;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterService {

    /**
     * zookeeper根节点
     */
    private static final String PATH = Constant.PATH;
    private static final int SESSION_TIMEOUT = Constant.SESSION_TIMEOUT;
    private static final int CONNECTION_TIMEOUT = Constant.CONNECTION_TIMEOUT;
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterService.class);

    private String zookeeperAddress;
    private ZkClient zkClient;

    public RegisterService(String zookeeperAddress){
        this.zookeeperAddress = zookeeperAddress;
        zkClient = new ZkClient(zookeeperAddress,SESSION_TIMEOUT,CONNECTION_TIMEOUT);
        LOGGER.info("服务提供者连接zookeeper");
    }

    public void register(String serviceName,String serviceAddress){
        if(!zkClient.exists(PATH)){
            zkClient.createPersistent(PATH);
            LOGGER.info("创建根节点->" + PATH);
        }
        String serviceNamePath = PATH + "/" + serviceName;
        if(!zkClient.exists(serviceNamePath)){
            zkClient.createPersistent(serviceNamePath);
            LOGGER.info("创建服务的根节点->" + serviceNamePath);
        }
        String serviceNameNodePath = serviceNamePath + "/no-";
        zkClient.createEphemeralSequential(serviceNameNodePath,serviceAddress);
        LOGGER.info("创建服务节点，服务地址为->"+serviceAddress);
    }
}
