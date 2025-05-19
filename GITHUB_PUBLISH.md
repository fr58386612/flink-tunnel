发布到GitHub步骤说明
=================

1. 准备工作
----------
```bash
# 配置Git
git config --global user.name "你的GitHub用户名"
git config --global user.email "你的GitHub邮箱"

# 初始化仓库
git init
git add .
git commit -m "Initial commit"
```

2. 创建GitHub仓库
---------------
1. 访问 https://github.com
2. 点击 "New repository"
3. 仓库名称填写: flink-tunnel
4. 描述: Apache Flink based SOCKS5 proxy tunnel tool
5. 选择 "Public" 仓库
6. 确保不勾选 "Initialize this repository with a README"
7. 点击 "Create repository"

3. 推送代码
---------
```bash
# 添加远程仓库
git remote add origin https://github.com/你的用户名/flink-tunnel.git

# 推送代码
git push -u origin main

# 如果默认分支是master，使用：
# git push -u origin master
```

4. 验证发布
---------
1. 访问 https://github.com/你的用户名/flink-tunnel
2. 确认所有文件已正确上传
3. 检查README.md是否正确显示
4. 验证LICENSE文件存在

5. 后续维护
---------
1. 及时回复Issue和Pull Request
2. 定期更新依赖版本
3. 维护文档的时效性
4. 添加CI/CD配置（可选）

6. 文件清单
---------
确保以下文件都已包含：
- src/main/java/com/example/
  - TunnelJob.java
  - ProxyClient.java
- docker-compose.yml
- pom.xml
- start_tunnel.sh
- health_check.sh
- test_proxy.sh
- README.md
- 使用说明.txt
- LICENSE
- .gitignore

7. 安全提示
---------
1. 确保没有包含敏感信息
2. 避免提交编译后的文件
3. 检查.gitignore是否正确配置
4. 注意代码中的注释内容
