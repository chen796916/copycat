package com.chen.rpc.channelHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Executor {

    private volatile static ThreadPoolExecutor executor = null;

    private Executor() {}

    public static ThreadPoolExecutor getExecutor() {
        if (executor == null) {
            synchronized (Executor.class) {
                if (executor == null) {
                    executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(20);
                }
            }
        }
        return executor;
    }
}
