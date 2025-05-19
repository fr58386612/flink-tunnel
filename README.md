# Flink内网渗透工具

这是一个基于Apache Flink的内网渗透工具，利用Flink的Web UI端口(8081)建立SOCKS5代理隧道，实现内网穿透。

## 特性

- 复用Flink管理端口，无需额外开放端口
- 支持SOCKS5代理协议
- 自动健康检查和故障恢复
- 易于部署和使用

## 快速开始

### 方式一：一键启动（推荐）

```bash
# 克隆仓库后执行：
./start_tunnel.sh
```

这将自动完成以下步骤：
1. 启动Flink集群
2. 启动代理客户端
3. 部署隧道任务
4. 启动健康检查

### 方式二：手动部署

1. 解压发布包：
```bash
tar xzf flink-tunnel.tar.gz
cd flink-tunnel-release
```

2. 部署服务端：
- 访问目标Flink服务器的Web UI
- 上传`flink-tunnel-1.0-SNAPSHOT.jar`并启动任务

3. 启动本地代理：
```bash
./start.sh
```

4. 测试连接：
```bash
./test_proxy.sh
```

## 配置代理

- SOCKS5代理地址：127.0.0.1
- 代理端口：1080

详细说明请参考`使用说明.txt`。

## 系统组件

- `flink-tunnel-1.0-SNAPSHOT.jar`: 主程序
- `start_tunnel.sh`: 一键启动脚本
- `health_check.sh`: 健康检查脚本
- `start.sh`: 代理客户端启动脚本
- `test_proxy.sh`: 连接测试脚本
- `使用说明.txt`: 详细使用文档

## 故障排除

1. 代理无法连接
   - 检查Flink任务是否正常运行
   - 确认8081端口是否可访问
   - 查看health_check.sh的输出日志

2. 连接速度慢
   - 检查网络状况
   - 确认目标服务器负载
   - 考虑调整JVM参数优化性能

## 维护说明

使用完成后，请及时：
1. 停止本地代理服务
2. 清理Flink任务
3. 删除上传的jar文件

可以使用以下命令清理：
```bash
docker-compose down   # 停止Flink集群
pkill -f ProxyClient # 停止代理客户端
```

## 安全说明

本工具仅用于授权的渗透测试活动，使用前请确保遵守相关法律法规。

## 参与贡献

欢迎贡献代码改进这个项目！参与方式：

1. Fork本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交修改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启Pull Request

### 改进方向

1. 性能优化
   - 优化数据传输效率
   - 减少内存占用
   - 提高并发处理能力

2. 功能增强
   - 支持更多代理协议
   - 添加加密传输
   - 实现流量控制

3. 安全性改进
   - 添加认证机制
   - 实现数据加密
   - 增加审计日志

## 联系方式

如有问题或建议，请通过以下方式联系：

- 提交Issue
- 发送Pull Request
- 参与讨论

感谢您的贡献！
