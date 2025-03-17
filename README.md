# HoseCloud Hab 插件框架

这是一个用于HoseCloud Hab系统的插件框架，允许开发者创建自定义任务插件。

## 项目结构

```
com.hosecloud.hab.plugin
├── annotation/ - 插件注解定义
│   ├── Execute.java - 标记插件执行方法的注解
│   ├── JsonSchemaDefinition.java - JSON Schema定义注解
│   ├── JsonSchemaOutput.java - 输出结果Schema注解
│   ├── JsonSchemaProperty.java - Schema属性注解
│   └── JsonSchemaResult.java - 结果Schema注解
├── cache/ - 缓存服务
│   └── CacheService.java - 缓存服务接口
├── model/ - 数据模型
│   ├── Log.java - 日志模型
│   └── LogLevel.java - 日志级别枚举
├── BaseTaskPlugin.java - 任务插件基类
├── PluginExecutor.java - 插件执行器
└── TaskPluginInterface.java - 任务插件接口
```

## 主要组件

### TaskPluginInterface

所有插件必须实现的接口，定义了插件的基本行为：

- `getName()` - 获取插件名称
- `execute()` - 执行插件任务
- `setCorporationId(String)` - 设置公司ID
- `setCacheService(CacheService)` - 设置缓存服务
- `setHttpClient(HttpClient)` - 设置HTTP客户端
- `getExecuteLogs()` - 获取执行日志
- `setParameters(Map<String, Object>)` - 设置插件参数

### BaseTaskPlugin

提供TaskPluginInterface的基本实现，插件可以继承此类以简化开发。

### 注解

- `@Execute` - 标记插件的执行方法
- `@JsonSchemaDefinition` - 定义JSON Schema
- `@JsonSchemaOutput` - 定义输出结果的Schema
- `@JsonSchemaProperty` - 定义Schema属性
- `@JsonSchemaResult` - 定义结果Schema

## 构建项目

使用Maven构建项目：

```bash
mvn clean package
```

## 发布到GitHub Packages

本项目使用GitHub Actions自动打包并发布到GitHub Packages。

### 设置GitHub Secrets

在GitHub仓库中，需要设置以下Secrets：

1. `GITHUB_TOKEN` - GitHub的访问令牌，用于发布包

### 触发发布

发布可以通过以下方式触发：

1. 推送到main分支
2. 手动触发GitHub Actions工作流

### 版本管理

在发布前，请确保更新`pom.xml`中的版本号：

- 对于开发版本，使用`-SNAPSHOT`后缀，例如`1.0-SNAPSHOT`
- 对于正式版本，移除`-SNAPSHOT`后缀，例如`1.0`

## 使用示例

### 创建一个简单的插件

```java
package com.example.plugin;

import com.hosecloud.hab.plugin.BaseTaskPlugin;
import com.hosecloud.hab.plugin.annotation.Execute;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

@Extension
public class ExamplePlugin extends BaseTaskPlugin {
    
    @Override
    public String getName() {
        return "示例插件";
    }
    
    @Execute(description = "执行示例任务")
    public String executeTask() {
        executeLogs.add(Log.info("开始执行任务"));
        // 执行任务逻辑
        executeLogs.add(Log.success("任务执行成功"));
        return "任务执行结果";
    }
}
```

### 使用插件

```java
TaskPluginInterface plugin = new ExamplePlugin();
plugin.setCorporationId("company123");
plugin.setCacheService(cacheService);
plugin.setHttpClient(httpClient);

// 执行插件
Object result = plugin.execute();

// 获取执行日志
List<Log> logs = plugin.getExecuteLogs();
```