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

## 发布到Maven中央仓库

### 准备工作

1. 确保已安装以下工具：
   - Python 3.x
   - Maven
   - GPG (用于签名)
   - zip (用于打包)

2. 确保已配置GPG密钥并上传到公钥服务器

### 使用打包脚本

项目提供了一个Python脚本 `create-bundle.py` 用于创建Maven中央仓库所需的发布包。

#### 脚本功能

- 清理并构建项目
- 创建符合Maven中央仓库要求的目录结构
- 复制所需文件（jar、源码、文档、pom）
- 生成MD5和SHA1校验和
- 生成GPG签名
- 打包成zip文件

#### 使用方法

1. 确保脚本有执行权限：
```bash
chmod +x create-bundle.py
```

2. 运行脚本：

基本用法（使用pom.xml中的版本号）：
```bash
./create-bundle.py
```

指定版本号（必须与pom.xml中的版本号匹配）：
```bash
./create-bundle.py --version 1.1.0
```

完整参数说明：
```bash
./create-bundle.py --version 1.1.0 --group-id com.hosecloud.hab --artifact-id plugin
```

或使用简写形式：
```bash
./create-bundle.py -v 1.1.0 -g com.hosecloud.hab -a plugin
```

可用参数：
- `--version`, `-v`: 版本号（可选，默认使用pom.xml中的版本号）
- `--group-id`, `-g`: Group ID（默认：com.hosecloud.hab）
- `--artifact-id`, `-a`: Artifact ID（默认：plugin）

注意：如果指定了`--version`参数，其值必须与pom.xml中的版本号匹配，否则脚本将报错。建议在更新版本时，先修改pom.xml中的版本号，然后运行脚本时不指定版本号参数。

3. 脚本执行完成后会生成 `plugin-{version}-bundle.zip` 文件，其中包含：
   - 主jar包
   - 源码jar包
   - javadoc jar包
   - pom文件
   - 所有文件的MD5和SHA1校验和
   - 所有文件的GPG签名

#### 自定义配置

脚本支持通过命令行参数自定义配置，无需修改代码。所有参数都有默认值，可以根据需要覆盖：

```bash
# 示例：使用pom.xml中的版本号
./create-bundle.py

# 示例：指定版本号（必须与pom.xml匹配）
./create-bundle.py --version 1.1.0

# 示例：完全自定义
./create-bundle.py --version 1.1.0 --group-id com.example --artifact-id custom-plugin
```

### 故障排除

如果遇到以下问题：

1. GPG签名失败
   - 确保已正确配置GPG密钥
   - 确保GPG密钥未过期

2. 校验和不匹配
   - 脚本使用Python的hashlib库生成标准的MD5和SHA1校验和
   - 确保文件在生成校验和时未被修改

3. 目录结构错误
   - 检查生成的zip文件内容是否符合Maven中央仓库的要求
   - 确保所有必需的文件都已包含

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