# Java设计模式项目

这是一个Java设计模式学习项目，包含了各种常用设计模式的实现和示例。

## 项目结构

```
src/
├── main/java/com/example/design_pattern/
│   ├── DesignPatternApplication.java          # Spring Boot主程序
│   └── decoration_pattern/                    # 装饰器模式
│       ├── input/
│       │   └── Main.java
│       └── set/
│           ├── HistorySet.java
│           └── Main.java
└── test/                                      # 测试文件
```

## 已实现的设计模式

### 装饰器模式 (Decorator Pattern)
- **位置**: `src/main/java/com/example/design_pattern/decoration_pattern/`
- **说明**: 装饰器模式允许向一个现有的对象添加新的功能，同时又不改变其结构
- **示例**: 
  - `HistorySet`: 为Set添加历史记录功能
  - 输入流装饰器示例

## 运行环境

- Java 21
- Spring Boot 3.5.5
- Gradle

## 如何运行

1. 克隆项目到本地
```bash
git clone https://github.com/L494264Tt/java_design_pattern.git
cd java_design_pattern
```

2. 使用Gradle运行项目
```bash
./gradlew bootRun
```

3. 运行特定的设计模式示例
```bash
./gradlew run --args="装饰器模式示例"
```

## 构建项目

```bash
# 编译项目
./gradlew build

# 运行测试
./gradlew test
```

## 贡献

欢迎提交Issue和Pull Request来完善这个项目！

## 许可证

本项目采用MIT许可证。