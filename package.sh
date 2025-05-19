#!/bin/bash

echo "打包Flink内网渗透工具..."

# 检查Maven构建
echo "构建项目..."
mvn clean package
if [ $? -ne 0 ]; then
    echo "构建失败!"
    exit 1
fi

# 创建发布目录
release_dir="flink-tunnel-release"
echo "创建发布包: $release_dir"
rm -rf $release_dir
mkdir -p $release_dir

# 复制必要文件
cp target/flink-tunnel-1.0-SNAPSHOT.jar $release_dir/
cp test_proxy.sh $release_dir/
cp 使用说明.txt $release_dir/
cp README.md $release_dir/

# 创建快速启动脚本
cat > $release_dir/start.sh << 'EOF'
#!/bin/bash
java -jar flink-tunnel-1.0-SNAPSHOT.jar
EOF

chmod +x $release_dir/start.sh
chmod +x $release_dir/test_proxy.sh

# 打包
echo "创建压缩包..."
tar czf flink-tunnel.tar.gz $release_dir

echo "清理临时文件..."
rm -rf $release_dir

echo "打包完成: flink-tunnel.tar.gz"
echo "使用方法："
echo "1. 解压: tar xzf flink-tunnel.tar.gz"
echo "2. 进入目录: cd $release_dir"
echo "3. 查看说明: cat README.md"
echo "4. 启动代理: ./start.sh"
echo "5. 测试连接: ./test_proxy.sh"
