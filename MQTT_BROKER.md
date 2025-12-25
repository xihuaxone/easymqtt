# MQTT Broker 管理指南

## 当前状态

MQTT broker 已通过 Docker 启动，容器名称：`easymqtt-broker`

## 常用命令

### 查看 broker 状态
```bash
docker ps | grep easymqtt-broker
```

### 查看 broker 日志
```bash
docker logs easymqtt-broker
```

### 停止 broker
```bash
docker stop easymqtt-broker
```

### 启动 broker（如果已停止）
```bash
docker start easymqtt-broker
```

### 重启 broker
```bash
docker restart easymqtt-broker
```

### 删除 broker 容器
```bash
docker stop easymqtt-broker
docker rm easymqtt-broker
```

### 重新创建 broker（如果需要）
```bash
docker stop easymqtt-broker 2>/dev/null
docker rm easymqtt-broker 2>/dev/null
docker run -d --name easymqtt-broker -p 1883:1883 eclipse-mosquitto
```

## 验证连接

可以使用以下命令测试 broker 是否正常工作：

```bash
# 检查端口是否监听
lsof -i :1883

# 或使用 telnet 测试（如果已安装）
telnet localhost 1883
```

## 使用其他 MQTT Broker

如果不想使用 Docker，可以：

1. **安装 Mosquitto（macOS）**：
   ```bash
   brew install mosquitto
   brew services start mosquitto
   ```

2. **使用在线 MQTT Broker**（测试用）：
   - 修改代码中的 broker 地址为：`tcp://test.mosquitto.org:1883`
   - 注意：公共 broker 可能不稳定，仅用于测试

3. **使用其他 MQTT 服务**：
   - 修改 `SimpleServer.java` 和 `SimpleClient.java` 中的 broker 地址

## 故障排查

### 连接被拒绝
- 检查 broker 是否运行：`docker ps | grep easymqtt-broker`
- 检查端口是否被占用：`lsof -i :1883`
- 查看 broker 日志：`docker logs easymqtt-broker`

### 端口已被占用
如果 1883 端口被其他程序占用，可以：
1. 停止占用端口的程序
2. 或使用其他端口，例如：
   ```bash
   docker run -d --name easymqtt-broker -p 1884:1883 eclipse-mosquitto
   ```
   然后修改代码中的端口为 1884

