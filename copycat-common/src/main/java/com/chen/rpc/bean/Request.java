package com.chen.rpc.bean;

import com.chen.rpc.constants.BeanType;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private String requestId;
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;
    private String type = BeanType.REQUEST;
    private Map<String, Object> config = new HashMap<>();

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public String getType() {
        return type;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    public Map<String, Object> getConfig() {
        return config;
    }
}
