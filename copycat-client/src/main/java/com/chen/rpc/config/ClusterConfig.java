package com.chen.rpc.config;

public class ClusterConfig {
    public static String loadBalance;

    public static void setLoadBalance(String loadBalance) {
        ClusterConfig.loadBalance = loadBalance;
    }

    public static String getLoadBalance() {
        return loadBalance;
    }
}
