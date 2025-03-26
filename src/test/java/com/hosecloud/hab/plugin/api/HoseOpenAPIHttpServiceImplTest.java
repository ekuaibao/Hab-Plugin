package com.hosecloud.hab.plugin.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HoseOpenAPIHttpServiceImplTest {

    @Mock
    private CloseableHttpClient httpClient;

    @Mock
    private CloseableHttpResponse httpResponse;

    @Mock
    private StatusLine statusLine;

    @Mock
    private HttpEntity httpEntity;

    private HoseOpenAPIHttpServiceImpl service;
    private final String baseUrl = "https://test.api.com";
    private final String appKey = "test-app-key";
    private final String appSecurity = "test-app-security";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws Exception {
        service = new HoseOpenAPIHttpServiceImpl(baseUrl, appKey, appSecurity, httpClient);
        when(httpClient.execute(any(HttpPost.class))).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(httpResponse.getEntity()).thenReturn(httpEntity);
    }

    @Test
    void testGetAccessToken() throws Exception {
        // 准备测试数据
        Map<String, Object> tokenResponse = new HashMap<>();
        Map<String, Object> value = new HashMap<>();
        value.put("accessToken", "test-token");
        value.put("refreshToken", "test-refresh-token");
        value.put("expireTime", System.currentTimeMillis() + 7200000);
        value.put("corporationId", "test-corp-id");
        tokenResponse.put("value", value);

        String responseJson = objectMapper.writeValueAsString(tokenResponse);
        when(httpEntity.getContent()).thenReturn(new ByteArrayInputStream(responseJson.getBytes()));

        // 执行测试
        String token = service.getAccessToken();

        // 验证结果
        assertNotNull(token);
        assertEquals("test-token", token);

        // 验证请求
        verify(httpClient).execute(any(HttpPost.class));
        
        // 验证请求参数
        ArgumentCaptor<HttpPost> requestCaptor = ArgumentCaptor.forClass(HttpPost.class);
        verify(httpClient).execute(requestCaptor.capture());
        HttpPost capturedRequest = requestCaptor.getValue();
        
        // 验证URL
        assertEquals(baseUrl + "/api/openapi/v1/auth/getAccessToken", capturedRequest.getURI().toString());
        
        // 验证请求头
        assertEquals("application/json", capturedRequest.getFirstHeader("Content-Type").getValue());
        
        // 验证请求体
        String requestBody = EntityUtils.toString(capturedRequest.getEntity());
        Map<String, Object> requestBodyMap = objectMapper.readValue(requestBody, Map.class);
        assertEquals(appKey, requestBodyMap.get("appKey"));
        assertEquals(appSecurity, requestBodyMap.get("appSecurity"));
    }

    @Test
    void testDoSend() throws Exception {
        // 准备测试数据
        String path = "/test/path";
        List<Map<String, Object>> queryParams = Arrays.asList(
            Map.of("key1", "value1"),
            Map.of("key2", "value2")
        );
        List<Map<String, Object>> headers = Arrays.asList(
            Map.of("Custom-Header", "value")
        );
        Map<String, Object> body = Map.of("test", "data");

        // 设置 getAccessToken 的 mock 响应
        Map<String, Object> tokenResponse = new HashMap<>();
        Map<String, Object> value = new HashMap<>();
        value.put("accessToken", "test-token");
        value.put("refreshToken", "test-refresh-token");
        value.put("expireTime", System.currentTimeMillis() + 7200000);
        value.put("corporationId", "test-corp-id");
        tokenResponse.put("value", value);

        String tokenResponseJson = objectMapper.writeValueAsString(tokenResponse);
        String expectedResponseJson = objectMapper.writeValueAsString(Map.of("result", "success"));

        // 设置链式响应
        when(httpEntity.getContent())
            .thenReturn(new ByteArrayInputStream(tokenResponseJson.getBytes()))
            .thenReturn(new ByteArrayInputStream(expectedResponseJson.getBytes()));

        // 执行测试
        Map<String, Object> response = service.doSend(path, "POST", queryParams, headers, body, Map.class);

        // 验证结果
        assertNotNull(response);
        assertEquals("success", response.get("result"));

        // 验证请求
        ArgumentCaptor<HttpPost> requestCaptor = ArgumentCaptor.forClass(HttpPost.class);
        verify(httpClient, times(2)).execute(requestCaptor.capture());
        List<HttpPost> capturedRequests = requestCaptor.getAllValues();
        
        // 验证第一个请求（获取 token）
        HttpPost tokenRequest = capturedRequests.get(0);
        assertEquals(baseUrl + "/api/openapi/v1/auth/getAccessToken", tokenRequest.getURI().toString());
        assertEquals("application/json", tokenRequest.getFirstHeader("Content-Type").getValue());
        
        // 验证第二个请求（实际 API 调用）
        HttpPost apiRequest = capturedRequests.get(1);
        String expectedUrl = baseUrl + path + "?accessToken=test-token&key1=value1&key2=value2";
        assertEquals(expectedUrl, apiRequest.getURI().toString());
        assertEquals("application/json", apiRequest.getFirstHeader("Content-Type").getValue());
        assertEquals("value", apiRequest.getFirstHeader("Custom-Header").getValue());
        
        // 验证请求体
        String requestBody = EntityUtils.toString(apiRequest.getEntity());
        Map<String, Object> requestBodyMap = objectMapper.readValue(requestBody, Map.class);
        assertEquals("data", requestBodyMap.get("test"));
    }

    @Test
    void testDoSendWithError() throws Exception {
        // 准备测试数据
        when(statusLine.getStatusCode()).thenReturn(400);
        when(httpEntity.getContent()).thenReturn(new ByteArrayInputStream("{\"error\":\"Bad Request\"}".getBytes()));

        // 执行测试并验证异常
        assertThrows(RuntimeException.class, () -> 
            service.doSend("/test/path", "POST", null, null, null, Map.class)
        );
    }

    @Test
    void testGetAccessTokenWithError() throws Exception {
        // 准备测试数据
        when(statusLine.getStatusCode()).thenReturn(403);
        when(httpEntity.getContent()).thenReturn(new ByteArrayInputStream("{\"error\":\"Invalid credentials\"}".getBytes()));

        // 执行测试并验证异常
        assertThrows(RuntimeException.class, () -> service.getAccessToken());
    }
} 