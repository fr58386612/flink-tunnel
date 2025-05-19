#!/bin/bash

echo "Starting Flink Tunnel System..."

# 确保Docker服务正在运行
echo "1. Starting Docker containers..."
docker-compose down
docker-compose up -d

# 等待Flink启动
echo "2. Waiting for Flink to start..."
for i in {1..30}; do
    if curl -s http://localhost:8081/overview > /dev/null; then
        break
    fi
    echo "   Waiting... ($i/30)"
    sleep 1
done

# 启动代理客户端
echo "3. Starting proxy client..."
pkill -f "ProxyClient"
java -cp target/flink-tunnel-1.0-SNAPSHOT.jar com.example.ProxyClient &

# 等待代理客户端启动
echo "4. Waiting for proxy to start..."
sleep 2

# 上传并启动Flink任务
echo "5. Uploading and starting Flink job..."
UPLOAD_RESPONSE=$(curl -s -X POST -H "Expect:" -F "jarfile=@target/flink-tunnel-1.0-SNAPSHOT.jar" http://localhost:8081/jars/upload)
JAR_ID=$(echo $UPLOAD_RESPONSE | grep -o '"filename":"[^"]*' | cut -d'"' -f4 | xargs basename)
echo "   Uploaded JAR: $JAR_ID"

curl -s -X POST "http://localhost:8081/jars/$JAR_ID/run" -H "Content-Type: application/json" -d '{"allowNonRestoredState":true}'

# 启动健康检查
echo "6. Starting health check..."
./health_check.sh &

echo "
=========================================
Flink Tunnel System is running!
- Flink UI: http://localhost:8081
- SOCKS5 Proxy: 127.0.0.1:1080
- Health check is running in background
=========================================
"
