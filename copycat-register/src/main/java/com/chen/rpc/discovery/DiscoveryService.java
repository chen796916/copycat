package com.chen.rpc.discovery;

import com.chen.rpc.zookeeper.Constant;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

public class DiscoveryService {

    /**
     * zookeeper根节点
     */
    private static final String PATH = Constant.PATH;
    private static final int SESSION_TIMEOUT = Constant.SESSION_TIMEOUT;
    private static final int CONNECTION_TIMEOUT = Constant.CONNECTION_TIMEOUT;
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscoveryService.class);

    private String zookeeperAddress;
    private ZkClient zkClient;

    public DiscoveryService(String zookeeperAddress){
        this.zookeeperAddress = zookeeperAddress;
        zkClient = new ZkClient(zookeeperAddress,SESSION_TIMEOUT,CONNECTION_TIMEOUT);
        LOGGER.info("服务调用者连接zookeeper");
    }

    public String getAddress(String serviceName){
        String servicePath = PATH + "/" + serviceName;
        if(!zkClient.exists(servicePath)){
            LOGGER.error("zookeeper中无此服务");
        }
        List<String> nodeList = zkClient.getChildren(servicePath);
        if(nodeList.isEmpty()){
            LOGGER.error("zookeeper中无此服务地址");
        }
        // 从地址列表中随机拿一个地址
        int index = new Random().nextInt(nodeList.size());
        String node = nodeList.get(index);
        String address = servicePath + "/" + node;
        return zkClient.readData(address);
    }
}
