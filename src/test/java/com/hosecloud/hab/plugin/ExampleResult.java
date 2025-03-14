package com.hosecloud.hab.plugin.example;

import com.hosecloud.hab.plugin.annotation.JsonSchemaProperty;
import lombok.Data;

/**
 * 示例插件的返回值类型
 */
@Data
public class ExampleResult {
    
    @JsonSchemaProperty(
        title = "成功标志",
        description = "操作是否成功",
        example = "true"
    )
    private Boolean success;
    
    @JsonSchemaProperty(
        title = "结果代码",
        description = "操作结果代码",
        example = "200"
    )
    private Integer code;
    
    @JsonSchemaProperty(
        title = "结果消息",
        description = "操作结果消息",
        example = "操作成功"
    )
    private String message;
    
    @JsonSchemaProperty(
        title = "数据总数",
        description = "查询结果的总记录数",
        example = "100"
    )
    private Integer total;
    
    @JsonSchemaProperty(
        title = "数据列表",
        description = "查询结果的数据列表"
    )
    private java.util.List<ExampleResultItem> items;
} 