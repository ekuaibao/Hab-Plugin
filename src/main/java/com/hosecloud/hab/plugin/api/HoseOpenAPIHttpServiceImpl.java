package com.hosecloud.hab.plugin.api;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpRequestBase;

import java.util.List;
import java.util.Map;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * 该类仅用于本地测试，实际生产中请使用HoseHttpService
 */
public class HoseOpenAPIHttpServiceImpl implements HoseHttpService {
    private final String baseUrl;
    private final String appKey;
    private final String appSecurity;
    private final HttpClient httpClient;

    public HoseOpenAPIHttpServiceImpl(String baseUrl, String appKey, String appSecurity, HttpClient httpClient) {
        this.baseUrl = baseUrl;
        this.appKey = appKey;
        this.appSecurity = appSecurity;
        this.httpClient = httpClient;
    }

    @Override
    public <T> T doSend(String path, String method, List<Map<String, Object>> queryParams, List<Map<String, Object>> header, Map<String, Object> body, Class<T> aClass) {
        String accessToken = getAccessToken();
        if (accessToken == null) {
            throw new RuntimeException("获取access token失败");
        }

        String url = baseUrl + path;
        
        try {
            // 构建基础URL（包含accessToken）
            String baseUrlWithToken = url + "?accessToken=" + accessToken;
            
            // 如果有查询参数，添加到URL中
            if (queryParams != null && !queryParams.isEmpty()) {
                String queryString = queryParams.stream()
                    .map(param -> param.entrySet().stream()
                        .map(e -> e.getKey() + "=" + URLEncoder.encode(String.valueOf(e.getValue()), StandardCharsets.UTF_8))
                        .collect(Collectors.joining("&")))
                    .collect(Collectors.joining("&"));
                baseUrlWithToken += "&" + queryString;
            }

            // 根据method选择请求方法
            HttpRequestBase request;
            switch (method.toUpperCase()) {
                case "GET":
                    request = new HttpGet(baseUrlWithToken);
                    break;
                case "POST":
                    request = new HttpPost(baseUrlWithToken);
                    if (body != null) {
                        StringEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(body), StandardCharsets.UTF_8);
                        ((HttpPost) request).setEntity(entity);
                    }
                    break;
                case "PUT":
                    request = new HttpPut(baseUrlWithToken);
                    if (body != null) {
                        StringEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(body), StandardCharsets.UTF_8);
                        ((HttpPut) request).setEntity(entity);
                    }
                    break;
                case "DELETE":
                    request = new HttpDelete(baseUrlWithToken);
                    break;
                default:
                    throw new IllegalArgumentException("不支持的HTTP方法: " + method);
            }
            
            // 设置请求头
            request.setHeader("Content-Type", "application/json");
            
            // 如果有自定义header，添加到请求中
            if (header != null) {
                header.forEach(h -> h.forEach((key, value) -> 
                    request.setHeader(key, String.valueOf(value))));
            }
            
            // 发送请求
            HttpResponse response = httpClient.execute(request);
            
            // 读取响应
            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            
            // 检查响应状态码
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new RuntimeException("请求失败，状态码：" + statusCode + "，响应内容：" + responseBody);
            }
            
            // 解析响应JSON
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(responseBody, aClass);
            
        } catch (Exception e) {
            throw new RuntimeException("HTTP请求失败", e);
        }
    }

    protected String getAccessToken() {
        String authUrl = "/api/openapi/v1/auth/getAccessToken";
        String url = baseUrl + authUrl;
        
        try {
            // 构建请求
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json");
            
            // 构建请求体
            Map<String, Object> requestBody = Map.of(
                "appKey", appKey,
                "appSecurity", appSecurity
            );
            StringEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(requestBody), StandardCharsets.UTF_8);
            httpPost.setEntity(entity);
            
            // 发送请求
            HttpResponse response = httpClient.execute(httpPost);
            
            // 读取响应
            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            
            // 检查响应状态码
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new RuntimeException("获取access token失败，状态码：" + statusCode + "，响应内容：" + responseBody);
            }
            
            // 解析响应
            Map<String, Object> responseMap = new ObjectMapper().readValue(responseBody, Map.class);
            if (responseMap != null && responseMap.containsKey("value")) {
                Map<String, Object> value = (Map<String, Object>) responseMap.get("value");
                return (String) value.get("accessToken");
            }
            
            throw new RuntimeException("获取access token失败，响应格式错误：" + responseBody);
            
        } catch (Exception e) {
            throw new RuntimeException("获取access token失败", e);
        }
    }

    /**
     * 获取访问令牌
     * @return 访问令牌
     */
    public String fetchAccessToken() {
        return getAccessToken();
    }
}
