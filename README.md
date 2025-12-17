# 🛡️ ZhiMing - 智能网络安全扫描工具

[![Java](https://img.shields.io/badge/Java-11+-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Platform](https://img.shields.io/badge/Platform-Windows%7CmacOS%7CLinux-green.svg)]()

## 📋 项目概述

**ZhiMing**（智明）是一款基于Java开发的智能网络安全扫描工具，专为安全研究人员和系统管理员设计。它提供了直观的图形界面，支持多种攻击向量的模拟测试，帮助用户识别和修复系统安全漏洞。

### 🎯 核心功能

- **多协议支持**: TCP、HTTP、HTTPS等多种协议扫描
- **密码暴力破解**: 集成rockyou.txt等密码字典
- **实时进度显示**: 图形化进度条和详细日志
- **智能警告系统**: 针对敏感域名的安全提醒
- **跨平台支持**: Windows、macOS、Linux全平台兼容

### 🛠️ 技术栈

| 技术组件 | 版本 | 用途 |
|---------|------|------|
| **Java** | 11+ | 核心开发语言 |
| **Swing** | 内置 | 图形用户界面 |
| **Maven** | 3.6+ | 项目构建管理 |
| **Hibernate** | 5.x | 数据持久化 |
| **JUnit** | 5.x | 单元测试框架 |

### 💻 系统要求

- **操作系统**: Windows 10+, macOS 10.14+, Ubuntu 18.04+
- **Java环境**: JDK 11 或更高版本
- **内存**: 最低 2GB RAM，推荐 4GB+
- **存储**: 至少 500MB 可用磁盘空间

## 🚀 安装指南

### 1. 环境准备

#### 检查Java版本
```bash
# Windows
java -version

# macOS/Linux
java --version
```

如果未安装Java 11+，请前往 [Oracle官网](https://www.oracle.com/java/technologies/downloads/) 下载安装。

#### 安装Maven（可选）
```bash
# Windows (使用Chocolatey)
choco install maven

# macOS (使用Homebrew)
brew install maven

# Linux
sudo apt install maven  # Ubuntu/Debian
sudo yum install maven  # CentOS/RHEL
```

### 2. 项目获取

```bash
# 克隆项目
git clone https://github.com/ctkqiang/ZhiMing.git
cd ZhiMing

# 或者下载ZIP包
wget https://github.com/ctkqiang/ZhiMing/archive/main.zip
unzip main.zip
cd ZhiMing-main
```

### 3. 依赖安装

```bash
# 使用Maven安装依赖
mvn clean install

# 或者直接编译
mvn compile
```

### 4. 配置文件

项目使用默认配置即可运行，如需自定义配置，请编辑：
- `src/main/resources/hibernate.cfg.xml` - 数据库配置
- `src/main/java/xin/ctkqiang/constant/ConstantsString.java` - 常量配置

## 📖 使用说明

### 🏃 快速启动

#### 开发模式
```bash
# 编译项目
mvn compile

# 运行主程序
mvn exec:java -Dexec.mainClass="xin.ctkqiang.ZhiMing"
```

#### 生产模式
```bash
# 打包可执行JAR
mvn package

# 运行JAR包
java -jar target/ZhiMing-1.0-SNAPSHOT.jar
```

#### 命令行参数
```bash
# 指定窗口大小
java -jar ZhiMing.jar --width=1024 --height=768

# 调试模式
java -jar ZhiMing.jar --debug=true
```

### 🎮 功能使用

#### 1. 基本扫描
1. 启动应用
2. 在"目标坐标"输入框中输入目标URL或IP
3. 选择攻击类型（TCP80、HTTP等）
4. 点击"开始攻击"

#### 2. 密码列表下载
1. 点击菜单栏"帮助"
2. 选择"Download Default Password List"
3. 确认下载目录
4. 等待下载完成

#### 3. 密码管理
1. 点击"文件" → "导入密码"
2. 选择.txt格式的密码文件
3. 查看"文件" → "查看密码列表"

### ⚙️ 配置选项

#### 系统配置
```properties
# 应用配置
app.name=ZhiMing
app.version=1.0.0
app.debug=false

# 网络配置
network.timeout=30000
network.maxRetries=3

# 存储配置
storage.downloadDir=~/Downloads
storage.passwordFile=password_list.txt
```

## 🔧 开发指南

### 📁 项目结构

```
ZhiMing/
├── src/main/java/xin/ctkqiang/
│   ├── common/           # 公共组件
│   ├── constant/         # 常量定义
│   ├── controller/       # 控制器层
│   ├── database/         # 数据库操作
│   ├── exceptions/       # 异常处理
│   ├── interfaces/       # 接口定义
│   ├── model/            # 数据模型
│   ├── service/          # 业务服务层
│   ├── test/             # 测试代码
│   ├── ui/               # 用户界面
│   └── utilities/        # 工具类
├── src/main/resources/   # 资源文件
├── docs/                 # 文档
└── target/              # 构建输出
```

### 🏗️ 开发环境搭建

#### 1. IDE配置（推荐IntelliJ IDEA）
```bash
# 导入项目
File → Open → 选择pom.xml

# 配置运行
Run → Edit Configurations → Application
Main class: xin.ctkqiang.ZhiMing
VM options: -Dfile.encoding=UTF-8
```

#### 2. 调试配置
```bash
# 启用调试模式
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -jar ZhiMing.jar
```

### 🧪 测试方法

#### 单元测试
```bash
# 运行所有测试
mvn test

# 运行特定测试
mvn test -Dtest=DownloadIntegrationTest

# 生成测试报告
mvn surefire-report:report
```

#### 集成测试
```bash
# 运行集成测试
java xin.ctkqiang.test.DownloadIntegrationTest

# 运行菜单集成测试
java xin.ctkqiang.test.MenuIntegrationTest
```

### 📝 代码规范

#### Java代码风格
```java
// 类命名使用PascalCase
public class NetworkScanner {
    // 方法命名使用camelCase
    public void startScan(String target) {
        // 代码注释使用中文
        // 处理目标地址
    }
}
```

#### 提交规范
```bash
# 提交消息格式
type(scope): description

# 示例
feat(ui): 添加下载默认密码列表功能
fix(download): 修复网络超时问题
docs(readme): 更新安装指南
```

## 🤝 贡献指南

### 🔄 开发流程

1. **Fork项目**
   ```bash
   git fork https://github.com/ctkqiang/ZhiMing.git
   ```

2. **创建功能分支**
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **提交更改**
   ```bash
   git add .
   git commit -m "feat: 添加新功能描述"
   git push origin feature/your-feature-name
   ```

4. **创建Pull Request**
   - 在GitHub上创建PR
   - 描述更改内容
   - 等待代码审查

### 📋 代码审查标准

- ✅ 代码符合Java编码规范
- ✅ 包含适当的单元测试
- ✅ 文档已更新
- ✅ 无安全漏洞
- ✅ 性能影响评估

### 🐛 问题反馈

#### 报告Bug
请使用以下模板创建Issue：

```markdown
**问题描述**
清晰描述遇到的问题

**复现步骤**
1. 步骤1
2. 步骤2
3. 步骤3

**期望行为**
描述期望的结果

**环境信息**
- 操作系统: [Windows/macOS/Linux]
- Java版本: [11/17/21]
- 应用版本: [1.0.0]

**截图**
添加相关截图
```

#### 功能请求
```markdown
**功能描述**
描述希望添加的功能

**使用场景**
描述该功能的使用场景

**实现建议**
提供可能的实现思路
```

## 📄 许可证信息

### 📋 MIT许可证

```
MIT License

Copyright (c) 2026 钟智强

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```


## 📞 联系方式

- **项目主页**: [GitHub](https://github.com/ctkqiang/ZhiMing)
- **问题反馈**: [Issues](https://github.com/ctkqiang/ZhiMing/issues)
- **邮件联系**: johnmelodyml@qq.com
- **社区讨论**: [Discussions](https://github.com/ctkqiang/ZhiMing/discussions)

## 🙏 致谢

感谢所有为ZhiMing项目做出贡献的开发者和测试人员，特别感谢开源社区的支持！

---

<div align="center">
    <p><strong>🛡️ 用技术守护安全，用智慧照亮未来 🛡️</strong></p>
    <p><em>ZhiMing - 让网络安全更简单</em></p>
</div>




</div>

--- 

### 🌐 全球捐赠通道

#### 国内用户

<div align="center" style="margin: 40px 0">

<div align="center">
<table>
<tr>
<td align="center" width="300">
<img src="https://github.com/ctkqiang/ctkqiang/blob/main/assets/IMG_9863.jpg?raw=true" width="200" />
<br />
<strong>🔵 支付宝</strong>（小企鹅在收金币哟~）
</td>
<td align="center" width="300">
<img src="https://github.com/ctkqiang/ctkqiang/blob/main/assets/IMG_9859.JPG?raw=true" width="200" />
<br />
<strong>🟢 微信支付</strong>（小绿龙在收金币哟~）
</td>
</tr>
</table>
</div>
</div>

#### 国际用户

<div align="center" style="margin: 40px 0">
  <a href="https://qr.alipay.com/fkx19369scgxdrkv8mxso92" target="_blank">
    <img src="https://img.shields.io/badge/Alipay-全球支付-00A1E9?style=flat-square&logo=alipay&logoColor=white&labelColor=008CD7">
  </a>
  
  <a href="https://ko-fi.com/F1F5VCZJU" target="_blank">
    <img src="https://img.shields.io/badge/Ko--fi-买杯咖啡-FF5E5B?style=flat-square&logo=ko-fi&logoColor=white">
  </a>
  
  <a href="https://www.paypal.com/paypalme/ctkqiang" target="_blank">
    <img src="https://img.shields.io/badge/PayPal-安全支付-00457C?style=flat-square&logo=paypal&logoColor=white">
  </a>
  
  <a href="https://donate.stripe.com/00gg2nefu6TK1LqeUY" target="_blank">
    <img src="https://img.shields.io/badge/Stripe-企业级支付-626CD9?style=flat-square&logo=stripe&logoColor=white">
  </a>
</div>

---

### 📌 开发者社交图谱

#### 技术交流

<div align="center" style="margin: 20px 0">
  <a href="https://github.com/ctkqiang" target="_blank">
    <img src="https://img.shields.io/badge/GitHub-开源仓库-181717?style=for-the-badge&logo=github">
  </a>
  
  <a href="https://stackoverflow.com/users/10758321/%e9%92%9f%e6%99%ba%e5%bc%ba" target="_blank">
    <img src="https://img.shields.io/badge/Stack_Overflow-技术问答-F58025?style=for-the-badge&logo=stackoverflow">
  </a>
  
  <a href="https://www.linkedin.com/in/ctkqiang/" target="_blank">
    <img src="https://img.shields.io/badge/LinkedIn-职业网络-0A66C2?style=for-the-badge&logo=linkedin">
  </a>
</div>

#### 社交互动

<div align="center" style="margin: 20px 0">
  <a href="https://www.instagram.com/ctkqiang" target="_blank">
    <img src="https://img.shields.io/badge/Instagram-生活瞬间-E4405F?style=for-the-badge&logo=instagram">
  </a>
  
  <a href="https://twitch.tv/ctkqiang" target="_blank">
    <img src="https://img.shields.io/badge/Twitch-技术直播-9146FF?style=for-the-badge&logo=twitch">
  </a>
  
  <a href="https://github.com/ctkqiang/ctkqiang/blob/main/assets/IMG_9245.JPG?raw=true" target="_blank">
    <img src="https://img.shields.io/badge/微信公众号-钟智强-07C160?style=for-the-badge&logo=wechat">
  </a>
</div>