#!/bin/bash

echo "正在测试代理连接..."

# 测试代理是否在运行
nc -zv 127.0.0.1 1080 2>/dev/null
if [ $? -ne 0 ]; then
    echo "错误: 代理服务未运行在1080端口"
    exit 1
fi

# 测试HTTP代理连接
echo "测试HTTP连接..."
curl --socks5 127.0.0.1:1080 -s -o /dev/null -w "%{http_code}" http://www.baidu.com
if [ $? -eq 0 ]; then
    echo "代理连接测试成功!"
else
    echo "错误: 无法通过代理访问外网"
    exit 1
fi

# 测试目标Flink服务器
echo "测试Flink服务器连接..."
curl -s -o /dev/null -w "%{http_code}" http://target-server:8081
if [ $? -eq 0 ]; then
    echo "Flink服务器连接正常"
else
    echo "警告: 无法连接到Flink服务器"
fi

echo "测试完成"
