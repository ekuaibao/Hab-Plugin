package com.hosecloud.hab.plugin;

import com.hosecloud.hab.plugin.annotation.JsonSchemaProperty;

/**
 * 示例插件的返回值项
 */
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ExampleResultItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createTime='" + createTime + '\'' +
                ", status=" + status +
                '}';
    }
}