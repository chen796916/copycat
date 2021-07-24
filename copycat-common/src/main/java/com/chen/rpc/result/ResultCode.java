package com.chen.rpc.result;

/**
 * response响应码
 */
public interface ResultCode {

    /**
     * 成功执行服务
     */
    int SUCCESS = 200;

    /**
     * 执行服务出错
     */
    int FAIL = 400;
}
