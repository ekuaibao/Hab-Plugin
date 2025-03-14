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