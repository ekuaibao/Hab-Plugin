package com.hosecloud.hab.plugin;

import com.hosecloud.hab.plugin.annotation.JsonSchemaProperty;

import java.util.List;

/**
 * 示例插件的返回值类型
 */
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

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<ExampleResultItem> getItems() {
        return items;
    }

    public void setItems(List<ExampleResultItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "ExampleResult{" +
                "success=" + success +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", total=" + total +
                ", items=" + items +
                '}';
    }
}