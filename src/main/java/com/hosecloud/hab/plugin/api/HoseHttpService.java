package com.hosecloud.hab.plugin.api;

import java.util.List;
import java.util.Map;

public interface HoseHttpService {
    /**
     * 合思OpenAPI请求
     * @param path 请求路径
     * @param method 请求方法
     * @param queryParams 请求参数
     * @param header 请求头信息
     * @param aClass 返回类型
     * @return 返回结果
     */
    <T> T doSend(String path, String method, List<Map<String, Object>> queryParams,
                 List<Map<String, Object>> header,
                 Class<T> aClass);
}
