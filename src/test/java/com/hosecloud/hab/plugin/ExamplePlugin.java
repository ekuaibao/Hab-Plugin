package com.hosecloud.hab.plugin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hosecloud.hab.plugin.annotation.Execute;
import com.hosecloud.hab.plugin.annotation.JsonSchemaDefinition;
import com.hosecloud.hab.plugin.annotation.JsonSchemaProperty;
import com.hosecloud.hab.plugin.annotation.JsonSchemaResult;
import com.hosecloud.hab.plugin.model.Log;
import com.hosecloud.hab.plugin.cache.CacheService;
import com.hosecloud.hab.plugin.example.ExampleResult;
import com.hosecloud.hab.plugin.example.ExampleResultItem;
import com.hosecloud.hab.plugin.model.OpenApiDocument;
import org.junit.jupiter.api.Assertions;
import org.pf4j.Extension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 示例插件，展示如何使用JSON Schema注解和@Execute注解
 */
@Extension
@JsonSchemaDefinition(
    title = "示例插件",
    description = "这是一个示例插件，用于展示如何使用JSON Schema注解和@Execute注解"
)
@JsonSchemaResult(
    value = ExampleResult.class,
    description = "查询结果",
    example = "{\"success\":true,\"code\":200,\"message\":\"操作成功\",\"total\":2,\"items\":[{\"id\":\"1001\",\"name\":\"示例记录1\",\"createTime\":\"2023-01-01 12:00:00\",\"status\":1},{\"id\":\"1002\",\"name\":\"示例记录2\",\"createTime\":\"2023-01-02 12:00:00\",\"status\":0}]}"
)
public class ExamplePlugin extends BaseTaskPlugin {
    
    @JsonSchemaProperty(
        title = "查询关键字",
        description = "用于搜索的关键字",
        minLength = 2,
        maxLength = 50,
        example = "关键字"
    )
    private String keyword;
    
    @JsonSchemaProperty(
        title = "页码",
        description = "查询结果的页码",
        minimum = 1,
        maximum = 100,
        defaultValue = "1"
    )
    private Integer page;
    
    @JsonSchemaProperty(
        title = "每页数量",
        description = "每页显示的记录数量",
        minimum = 10,
        maximum = 100,
        defaultValue = "20"
    )
    private Integer pageSize;
    
    @JsonSchemaProperty(
        title = "是否包含已删除",
        description = "是否在查询结果中包含已删除的记录",
        example = "false"
    )
    private Boolean includeDeleted;
    
    @JsonSchemaProperty(description = "输入参数")
    private String input;
    
    @Override
    public String getName() {
        return "ExamplePlugin";
    }
    
    /**
     * 使用@Execute注解标记的方法将作为插件的执行入口
     * 方法名可以自定义，不必是execute
     * 
     * @return 执行结果
     */
    @Execute(
        description = "执行示例插件查询操作",
        operationId = "queryRecords",
        tags = {"example", "query"},
        outputClass = ExampleResult.class,
        outputDescription = "查询结果",
        outputExample = "{\"success\":true,\"code\":200,\"message\":\"操作成功\",\"total\":2,\"items\":[{\"id\":\"1001\",\"name\":\"示例记录1\",\"createTime\":\"2023-01-01 12:00:00\",\"status\":1},{\"id\":\"1002\",\"name\":\"示例记录2\",\"createTime\":\"2023-01-02 12:00:00\",\"status\":0}]}"
    )
    public ExampleResult runPlugin() {
        // 示例实现，实际插件应该实现具体的业务逻辑
        cacheService.set("token", "1234567890", 3600);
        ExampleResult result = new ExampleResult();
        result.setSuccess(true);
        result.setCode(200);
        result.setMessage("操作成功");
        result.setTotal(2);
        
        List<ExampleResultItem> items = new ArrayList<>();
        
        ExampleResultItem item1 = new ExampleResultItem();
        item1.setId("1001");
        item1.setName("示例记录1" + (keyword != null ? " - " + keyword : ""));
        item1.setCreateTime("2023-01-01 12:00:00");
        item1.setStatus(1);
        items.add(item1);
        
        ExampleResultItem item2 = new ExampleResultItem();
        item2.setId("1002");
        item2.setName("示例记录2" + (keyword != null ? " - " + keyword : ""));
        item2.setCreateTime("2023-01-02 12:00:00");
        item2.setStatus(0);
        items.add(item2);

        String token = cacheService.get("token");
        Assertions.assertEquals("1234567890", token);

        result.setItems(items);
        executeLogs.add(Log.success("执行示例插件查询操作成功"));
        return result;
    }
    
    /**
     * 测试方法，用于生成并打印JSON Schema和OpenAPI文档
     */
    public static void main(String[] args) throws JsonProcessingException {
        ExamplePlugin plugin = new ExamplePlugin();
        plugin.setCacheService(new CacheService() {
            Map<String, String> cache = new HashMap<>();
            @Override
            public String get(String key) {
                return cache.get(key);
            }

            @Override
            public void set(String key, String value, int expireSeconds) {
                cache.put(key, value);
            }
        });
        ObjectMapper mapper = new ObjectMapper();
        
        System.out.println("\n=== OpenAPI ===");
        OpenApiDocument openApi = plugin.getOpenApi();
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(openApi));
        plugin.execute();
        System.out.println(plugin.getExecuteLogs());;
    }
} 