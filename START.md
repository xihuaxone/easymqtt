# EasyMQTT 启动指南

## 前置要求

1. **Java 环境**: 需要 JDK 8 或更高版本
2. **Maven**: 用于构建项目
3. **MQTT Broker**: 需要运行一个 MQTT 消息代理服务器

### 安装 MQTT Broker

如果还没有 MQTT broker，可以使用以下方式之一：

#### 方式1: 使用 Docker 运行 Mosquitto（推荐）
```bash
docker run -it -p 1883:1883 eclipse-mosquitto
```

#### 方式2: 使用 Homebrew 安装（macOS）
```bash
brew install mosquitto
brew services start mosquitto
```

#### 方式3: 下载 Mosquitto
访问 https://mosquitto.org/download/ 下载并安装

## 构建项目

```bash
# 编译项目
mvn clean compile

# 打包项目（生成 jar 文件）
mvn clean package
```

打包后会生成两个 jar 文件：
- `target/easymqtt-1.0-SNAPSHOT.jar` - 普通 jar（需要依赖）
- `target/easymqtt-1.0-SNAPSHOT-jar-with-dependencies.jar` - 包含所有依赖的 fat jar

## 启动方式

### 方式1: 运行示例代码（推荐）

#### 1. 启动服务端
在一个终端中运行：
```bash
# 使用 Maven 运行
mvn exec:java -Dexec.mainClass="com.xihua.easymqtt.example.SimpleServer"

# 或者使用 java 命令（需要先打包）
java -cp target/easymqtt-1.0-SNAPSHOT-jar-with-dependencies.jar com.xihua.easymqtt.example.SimpleServer
```

#### 2. 启动客户端
在另一个终端中运行：
```bash
# 使用 Maven 运行
mvn exec:java -Dexec.mainClass="com.xihua.easymqtt.example.SimpleClient"

# 或者使用 java 命令（需要先打包）
java -cp target/easymqtt-1.0-SNAPSHOT-jar-with-dependencies.jar com.xihua.easymqtt.example.SimpleClient
```

### 方式2: 运行测试代码

```bash
# 运行测试（注意：需要先修改测试代码中的 MQTT broker 地址）
mvn test -Dtest=ClientMqttTest
```

### 方式3: 在自己的项目中使用

1. 将项目打包并安装到本地 Maven 仓库：
```bash
mvn clean install
```

2. 在其他项目的 `pom.xml` 中添加依赖：
```xml
<dependency>
    <groupId>com.xihua</groupId>
    <artifactId>easymqtt</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

3. 在代码中使用：
```java
// 创建客户端
MClient client = new MClient(
    "tcp://localhost:1883",  // MQTT broker 地址
    "/client/topic",          // 订阅的 topic
    null,                     // 用户名
    null,                     // 密码
    "./persistence"           // 持久化目录
);

// 注册处理器（服务端）
client.registerHandler(YourHandler.class);

// 调用远程服务（客户端）
Message response = client.call("/server/topic", "/api/name", params);
```

## 配置说明

### MClient 构造函数参数

1. **brokerHost**: MQTT broker 地址，格式：`tcp://host:port`
2. **sourceTopic**: 本客户端订阅的 topic，用于接收响应
3. **username**: MQTT 用户名（可选，不需要认证时传 null）
4. **password**: MQTT 密码（可选，不需要认证时传 null）
5. **persistenceDir**: MQTT 消息持久化目录路径

### 示例配置

```java
// 无认证
MClient client = new MClient(
    "tcp://localhost:1883",
    "/client/1",
    "./mqtt-persistence"
);

// 有认证
MClient client = new MClient(
    "tcp://192.168.1.100:1883",
    "/client/1",
    "username",
    "password",
    "./mqtt-persistence"
);
```

## 常见问题

### 1. 连接失败
- 检查 MQTT broker 是否正在运行
- 检查 broker 地址和端口是否正确
- 检查防火墙设置

### 2. 等待响应超时
- 确保服务端已启动
- 确保服务端注册了对应的 API 处理器
- 检查 topic 是否正确

### 3. 找不到类
- 确保已执行 `mvn clean package`
- 使用 `-jar-with-dependencies.jar` 文件运行

## 快速测试

1. 启动 MQTT broker（如使用 Docker）：
```bash
docker run -it -p 1883:1883 eclipse-mosquitto
```

2. 在一个终端启动服务端：
```bash
mvn exec:java -Dexec.mainClass="com.xihua.easymqtt.example.SimpleServer"
```

3. 在另一个终端启动客户端：
```bash
mvn exec:java -Dexec.mainClass="com.xihua.easymqtt.example.SimpleClient"
```

你应该能看到客户端成功调用服务端并收到响应。

