# HoseCloud 示例项目

这是一个简单的Maven项目示例，可以打包成jar包。

## 项目结构

```
com.hosecloud.example
├── App.java - 主应用程序类
└── GreetingService.java - 问候服务类
```

## 构建项目

使用Maven构建项目：

```bash
mvn clean package
```

## 运行项目

构建完成后，可以使用以下命令运行：

```bash
java -jar target/example-1.0-SNAPSHOT.jar
```

## 测试

运行测试：

```bash
mvn test
```

## 发布到Maven中央仓库

本项目使用GitHub Actions自动打包并发布到Maven中央仓库。

### 前提条件

1. 在Sonatype OSSRH (OSS Repository Hosting)注册账号：https://s01.oss.sonatype.org/
2. 创建并验证一个新的项目
3. 生成GPG密钥对用于签名

### 设置GitHub Secrets

在GitHub仓库中，需要设置以下Secrets：

1. `OSSRH_USERNAME` - Sonatype OSSRH的用户名
2. `OSSRH_TOKEN` - Sonatype OSSRH的密码
3. `GPG_PRIVATE_KEY` - GPG私钥，使用以下命令导出：
   ```
   gpg --export-secret-keys --armor <KEY_ID>
   ```
4. `GPG_PASSPHRASE` - GPG密钥的密码

### 触发发布

发布可以通过以下两种方式触发：

1. 创建一个新的GitHub Release
2. 手动触发GitHub Actions工作流

### 版本管理

在发布前，请确保更新`pom.xml`中的版本号：

- 对于开发版本，使用`-SNAPSHOT`后缀，例如`1.0-SNAPSHOT`
- 对于正式版本，移除`-SNAPSHOT`后缀，例如`1.0`