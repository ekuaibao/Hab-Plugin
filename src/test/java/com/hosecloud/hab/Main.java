package com.hosecloud.hab;

import com.hosecloud.hab.plugin.api.HoseOpenAPIHttpServiceImpl;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Main {
    public static void main(String[] args) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HoseOpenAPIHttpServiceImpl service = new HoseOpenAPIHttpServiceImpl(
                "https://app.ekuaibao.com", 
                "xxxx", 
                "xxxx", 
                httpClient
            );
            String token = service.fetchAccessToken();
            System.out.println("获取到的token: " + token);
            Object result = service.doSend("/api/openapi/v1/corporations", "GET", null, null, null, Object.class);
            System.out.println("获取到的结果: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

