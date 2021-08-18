package com.chen.rpc.util;

import com.chen.rpc.bean.Request;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class CommonUtilsTest {

    @Test
    public void testConvertToMapInObject() {
        Request request = new Request();
        request.setRequestId("this is requestId");
        request.setClassName("this is className");
        request.setMethodName("this is methodName");
        Map<String, Object> map = CommonUtils.convertToMap(request);
        for (Map.Entry entry : map.entrySet()) {
            System.out.println("key ->" + entry.getKey() + ", val ->" + entry.getValue());
        }
    }

    @Test
    public void testConvertToMapInClass() {
        Map<String, Object> map = CommonUtils.convertToMap(Bean.class);
        for (Map.Entry entry : map.entrySet()) {
            System.out.println("key ->" + entry.getKey() + ", val ->" + entry.getValue());
        }
    }
}
