package com.hosecloud.hab.plugin.example;

import com.hosecloud.hab.plugin.annotation.JsonSchemaProperty;
import lombok.Data;

/**
 * 示例插件的返回值项
 */
@Data
public class ExampleResultItem {
    
    @JsonSchemaProperty(
        title = "ID",
        description = "记录ID",
        example = "1001"
    )
    private String id;
    
    @JsonSchemaProperty(
        title = "名称",
        description = "记录名称",
        example = "示例记录"
    )
    private String name;
    
    @JsonSchemaProperty(
        title = "创建时间",
        description = "记录创建时间",
        example = "2023-01-01 12:00:00"
    )
    private String createTime;
    
    @JsonSchemaProperty(
        title = "状态",
        description = "记录状态",
        example = "1"
    )
    private Integer status;
} 