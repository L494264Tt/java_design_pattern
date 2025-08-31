# Gradle Dependencies 系统教程

## 目录
1. [基础概念](#基础概念)
2. [依赖配置类型](#依赖配置类型)
3. [依赖声明格式](#依赖声明格式)
4. [仓库配置](#仓库配置)
5. [版本管理](#版本管理)
6. [依赖分析工具](#依赖分析工具)
7. [高级特性](#高级特性)
8. [实际示例](#实际示例)
9. [最佳实践](#最佳实践)

## 基础概念

### 什么是Dependencies？
Dependencies（依赖）是您的项目需要的外部库或模块。Gradle通过依赖管理系统自动下载、管理这些库。

### 当前项目的Dependencies解释
```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter'
    testImplementation 'org.springframework.boot:spring-boot-starter-test' 
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
```

- **implementation**: 编译时和运行时都需要的依赖
- **testImplementation**: 仅测试编译和运行时需要的依赖
- **testRuntimeOnly**: 仅测试运行时需要的依赖

## 依赖配置类型

### 主要配置类型

| 配置类型 | 用途 | 何时使用 |
|---------|------|----------|
| `implementation` | 编译和运行时都需要 | 大部分依赖使用此配置 |
| `api` | 对外暴露的API依赖 | 当依赖需要被使用者访问时 |
| `compileOnly` | 仅编译时需要 | 注解、编译时工具 |
| `runtimeOnly` | 仅运行时需要 | 数据库驱动、日志实现 |
| `testImplementation` | 测试编译和运行时 | 测试框架和工具 |
| `testCompileOnly` | 仅测试编译时 | 测试注解处理器 |
| `testRuntimeOnly` | 仅测试运行时 | 测试运行时依赖 |
| `annotationProcessor` | 注解处理器 | Lombok、MapStruct等 |

### 配置示例
```gradle
dependencies {
    // 核心依赖
    implementation 'org.springframework.boot:spring-boot-starter-web'
    api 'com.fasterxml.jackson.core:jackson-core:2.15.2'
    
    // 编译时依赖
    compileOnly 'org.projectlombok:lombok:1.18.28'
    annotationProcessor 'org.projectlombok:lombok:1.18.28'
    
    // 运行时依赖
    runtimeOnly 'com.mysql:mysql-connector-j'
    
    // 测试依赖
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
```

## 依赖声明格式

### 1. 标准格式（推荐）
```gradle
dependencies {
    // 格式: 'group:name:version'
    implementation 'org.apache.commons:commons-lang3:3.12.0'
    implementation 'com.google.guava:guava:32.1.1-jre'
}
```

### 2. Map格式
```gradle
dependencies {
    implementation group: 'org.apache.commons', 
                  name: 'commons-lang3', 
                  version: '3.12.0'
}
```

### 3. 版本范围
```gradle
dependencies {
    // 最新的3.x版本
    implementation 'org.apache.commons:commons-lang3:3.+'
    
    // 版本范围：3.0到4.0之间（不包含4.0）
    implementation 'org.apache.commons:commons-lang3:[3.0,4.0)'
    
    // 最新版本（不推荐）
    implementation 'org.apache.commons:commons-lang3:+'
}
```

### 4. 排除传递依赖
```gradle
dependencies {
    implementation('org.springframework:spring-core:6.0.11') {
        exclude group: 'commons-logging', module: 'commons-logging'
    }
    
    // 排除所有传递依赖
    implementation('some.group:some-module:1.0') {
        transitive = false
    }
}
```

### 5. 强制版本
```gradle
dependencies {
    implementation('org.apache.commons:commons-lang3:3.12.0') {
        force = true
    }
}
```

## 仓库配置

### 常用仓库
```gradle
repositories {
    // Maven中央仓库（必备）
    mavenCentral()
    
    // Google Maven仓库（Android项目必备）
    google()
    
    // Gradle插件仓库
    gradlePluginPortal()
    
    // 本地Maven仓库
    mavenLocal()
}
```

### 自定义仓库
```gradle
repositories {
    // 自定义Maven仓库
    maven {
        name 'SpringMilestone'
        url 'https://repo.spring.io/milestone'
    }
    
    // 需要认证的仓库
    maven {
        url 'https://private.repo.com/maven'
        credentials {
            username = project.findProperty('repoUser') ?: 'defaultUser'
            password = project.findProperty('repoPassword') ?: 'defaultPassword'
        }
    }
    
    // 文件系统仓库
    flatDir {
        dirs 'libs', 'lib'
    }
}
```

## 版本管理

### 1. 使用BOM (Bill of Materials)
```gradle
dependencies {
    // 导入BOM
    implementation platform('org.springframework.boot:spring-boot-dependencies:3.1.2')
    implementation platform('org.springframework.cloud:spring-cloud-dependencies:2022.0.4')
    
    // 使用BOM管理的版本（无需指定版本号）
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
}
```

### 2. 版本约束
```gradle
dependencies {
    constraints {
        implementation 'org.apache.commons:commons-lang3:3.12.0'
        implementation 'com.google.guava:guava:32.1.1-jre'
    }
    
    // 实际依赖声明（版本由约束控制）
    implementation 'org.apache.commons:commons-lang3'
    implementation 'com.google.guava:guava'
}
```

### 3. 版本目录（Gradle 7+）
**gradle/libs.versions.toml**:
```toml
[versions]
spring-boot = "3.1.2"
junit = "5.10.0"

[libraries]
spring-boot-starter = { module = "org.springframework.boot:spring-boot-starter", version.ref = "spring-boot" }
junit-jupiter = { module = "org.junit.jupiter:junit-jupiter", version.ref = "junit" }

[bundles]
testing = ["junit-jupiter", "mockito-core"]
```

**build.gradle**:
```gradle
dependencies {
    implementation libs.spring.boot.starter
    testImplementation libs.bundles.testing
}
```

## 依赖分析工具

### 1. 查看依赖树
```bash
# 查看所有配置的依赖树
./gradlew dependencies

# 查看特定配置的依赖树
./gradlew dependencies --configuration implementation
./gradlew dependencies --configuration testImplementation
```

### 2. 依赖洞察
```bash
# 查看特定依赖的来源
./gradlew dependencyInsight --dependency commons-lang3

# 查看特定组的所有依赖
./gradlew dependencyInsight --dependency org.springframework.boot
```

### 3. 依赖报告
```bash
# 生成HTML依赖报告
./gradlew htmlDependencyReport

# 项目报告
./gradlew projectReport
```

### 4. 查找依赖冲突
```bash
# 查看依赖冲突
./gradlew dependencies | grep "FAILED"

# 详细冲突信息
./gradlew dependencyInsight --dependency <conflicting-dependency>
```

## 高级特性

### 1. 条件依赖
```gradle
dependencies {
    if (project.hasProperty('useMySQL')) {
        runtimeOnly 'mysql:mysql-connector-java:8.0.33'
    } else {
        runtimeOnly 'com.h2database:h2'
    }
    
    // 基于环境的依赖
    if (project.findProperty('env') == 'dev') {
        implementation 'org.springframework.boot:spring-boot-devtools'
    }
}
```

### 2. 项目间依赖
```gradle
dependencies {
    // 依赖同一构建中的其他项目
    implementation project(':common')
    implementation project(path: ':web', configuration: 'shadow')
}
```

### 3. 文件依赖
```gradle
dependencies {
    // 单个JAR文件
    implementation files('libs/custom.jar')
    
    // 多个文件
    implementation files('libs/a.jar', 'libs/b.jar')
    
    // 文件树
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation fileTree(dir: 'libs', include: ['*.jar'], exclude: ['*-sources.jar'])
}
```

### 4. 配置依赖替换
```gradle
configurations.all {
    resolutionStrategy {
        // 替换模块
        dependencySubstitution {
            substitute module('org.slf4j:slf4j-log4j12') using module('org.slf4j:slf4j-nop:1.7.36')
        }
        
        // 强制特定版本
        force 'org.apache.commons:commons-lang3:3.12.0'
        
        // 缓存策略
        cacheChangingModulesFor 0, 'seconds'
        cacheDynamicVersionsFor 10, 'minutes'
        
        // 依赖解析策略
        preferProjectModules()
    }
}
```

### 5. 自定义配置
```gradle
configurations {
    integrationTestImplementation.extendsFrom testImplementation
    integrationTestRuntimeOnly.extendsFrom testRuntimeOnly
}

dependencies {
    integrationTestImplementation 'org.testcontainers:junit-jupiter:1.18.3'
    integrationTestImplementation 'org.testcontainers:mysql:1.18.3'
}
```

## 实际示例

### Spring Boot Web应用
```gradle
dependencies {
    // Web starter
    implementation 'org.springframework.boot:spring-boot-starter-web'
    
    // 数据访问
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    
    // 安全
    implementation 'org.springframework.boot:spring-boot-starter-security'
    
    // 验证
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    
    // 数据库驱动
    runtimeOnly 'com.mysql:mysql-connector-j'
    
    // 开发工具
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
    
    // Lombok
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
    
    // 测试
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.springframework.security:spring-security-test'
}
```

### 微服务应用
```gradle
dependencies {
    // Spring Cloud BOM
    implementation platform('org.springframework.cloud:spring-cloud-dependencies:2022.0.4')
    
    // Web
    implementation 'org.springframework.boot:spring-boot-starter-web'
    
    // 服务发现
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
    
    // 配置中心
    implementation 'org.springframework.cloud:spring-cloud-starter-config'
    
    // 断路器
    implementation 'org.springframework.cloud:spring-cloud-starter-circuitbreaker-hystrix'
    
    // 链路追踪
    implementation 'org.springframework.cloud:spring-cloud-starter-sleuth'
    
    // API文档
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0'
}
```

### Android应用
```gradle
dependencies {
    // Android核心
    implementation 'androidx.core:core-ktx:1.10.1'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    
    // UI
    implementation 'com.google.android.material:material:1.9.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    
    // 架构组件
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1'
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.6.1'
    
    // 网络
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    
    // 图片加载
    implementation 'com.github.bumptech.glide:glide:4.15.1'
    
    // 测试
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
}
```

## 最佳实践

### 1. 版本管理
- ✅ 使用BOM管理版本一致性
- ✅ 明确指定版本号，避免使用 `+`
- ✅ 定期更新依赖版本
- ❌ 不要使用动态版本（如 `1.+`）

### 2. 依赖选择
- ✅ 优先使用 `implementation` 而不是 `api`
- ✅ 合理使用 `compileOnly` 和 `runtimeOnly`
- ✅ 测试依赖使用 `testImplementation`
- ❌ 避免不必要的传递依赖

### 3. 仓库配置
- ✅ 优先使用 `mavenCentral()`
- ✅ 按需添加仓库
- ✅ 使用HTTPS协议
- ❌ 避免使用不安全的仓库

### 4. 性能优化
- ✅ 使用Gradle依赖缓存
- ✅ 启用并行构建
- ✅ 使用构建缓存
- ✅ 定期清理未使用的依赖

### 5. 安全考虑
- ✅ 定期检查依赖安全漏洞
- ✅ 使用依赖检查工具
- ✅ 避免下载未知来源的依赖
- ❌ 不要在代码中硬编码凭证

### 6. 团队协作
- ✅ 使用 `gradle.lockfile` 锁定版本
- ✅ 在版本控制中包含 `gradle/wrapper/`
- ✅ 文档化自定义依赖配置
- ✅ 统一团队的依赖管理策略

## 常见问题和解决方案

### 1. 依赖冲突
```gradle
// 问题：多个版本的同一依赖
configurations.all {
    resolutionStrategy {
        // 解决方案：强制使用特定版本
        force 'org.slf4j:slf4j-api:1.7.36'
        
        // 或者使用最新版本
        eachDependency { details ->
            if (details.requested.group == 'org.slf4j') {
                details.useVersion '1.7.36'
            }
        }
    }
}
```

### 2. 排除不需要的传递依赖
```gradle
dependencies {
    implementation('org.springframework.boot:spring-boot-starter-web') {
        exclude group: 'org.springframework.boot', module: 'spring-boot-starter-tomcat'
    }
    implementation 'org.springframework.boot:spring-boot-starter-jetty'
}
```

### 3. 多模块项目依赖管理
```gradle
// 根项目的build.gradle
subprojects {
    dependencies {
        // 公共依赖
        implementation 'org.slf4j:slf4j-api:1.7.36'
        testImplementation 'org.junit.jupiter:junit-jupiter:5.9.3'
    }
}
```

## 多模块项目结构和依赖管理

### 1. 多模块项目结构
```
my-project/
├── build.gradle                 # 根项目构建文件
├── settings.gradle             # 项目设置文件
├── gradle.properties           # 全局属性文件
├── common/                     # 公共模块
│   └── build.gradle           # 子模块构建文件
├── web/                       # Web模块  
│   └── build.gradle
├── service/                   # 服务模块
│   └── build.gradle
└── data/                      # 数据访问模块
    └── build.gradle
```

### 2. 根项目配置 (Root build.gradle)

**作用**: 定义全局配置、插件、版本管理和公共依赖

```gradle
// ========== 根项目 build.gradle ==========

plugins {
    id 'java' apply false
    id 'org.springframework.boot' version '3.1.2' apply false
    id 'io.spring.dependency-management' version '1.1.3' apply false
}

// 全局变量
ext {
    springBootVersion = '3.1.2'
    springCloudVersion = '2022.0.4'
    junitVersion = '5.10.0'
    lombokVersion = '1.18.28'
}

// 所有项目的公共配置
allprojects {
    group = 'com.example'
    version = '1.0.0'
    
    repositories {
        mavenCentral()
        maven { url 'https://repo.spring.io/milestone' }
    }
}

// 所有子项目的公共配置
subprojects {
    apply plugin: 'java'
    apply plugin: 'org.springframework.boot'
    apply plugin: 'io.spring.dependency-management'
    
    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    // BOM管理
    dependencyManagement {
        imports {
            mavenBom "org.springframework.boot:spring-boot-dependencies:${springBootVersion}"
            mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
        }
    }
    
    // 所有子项目的公共依赖
    dependencies {
        // 公共依赖
        implementation 'org.springframework.boot:spring-boot-starter'
        implementation 'org.springframework.boot:spring-boot-starter-actuator'
        
        // Lombok
        compileOnly "org.projectlombok:lombok:${lombokVersion}"
        annotationProcessor "org.projectlombok:lombok:${lombokVersion}"
        
        // 测试依赖
        testImplementation 'org.springframework.boot:spring-boot-starter-test'
        testImplementation "org.junit.jupiter:junit-jupiter:${junitVersion}"
    }
    
    // 通用测试配置
    test {
        useJUnitPlatform()
    }
}

// 特定子项目配置
project(':web') {
    dependencies {
        implementation project(':service')
        implementation project(':common')
    }
}

project(':service') {
    dependencies {
        implementation project(':data')
        implementation project(':common')
    }
}
```

### 3. 项目设置文件 (settings.gradle)

**作用**: 定义项目名称和包含的子模块

```gradle
// ========== settings.gradle ==========

rootProject.name = 'my-project'

// 包含子模块
include 'common'
include 'web' 
include 'service'
include 'data'

// 可选：自定义子项目目录
project(':common').projectDir = file('modules/common')
project(':web').projectDir = file('modules/web-app')
```

### 4. 子模块配置示例

#### Common模块 (common/build.gradle)
```gradle
// ========== common/build.gradle ==========

// 如果是纯Java库，不需要Spring Boot插件
plugins {
    id 'java-library'  // 使用java-library插件而不是java插件
}

// 不需要bootJar任务
jar {
    enabled = true
    archiveClassifier = '' // 移除classifier
}

dependencies {
    // 工具类依赖
    api 'org.apache.commons:commons-lang3:3.12.0'
    api 'com.google.guava:guava:32.1.1-jre'
    
    // JSON处理
    api 'com.fasterxml.jackson.core:jackson-databind'
    
    // 日志
    api 'org.slf4j:slf4j-api'
}
```

#### Web模块 (web/build.gradle)  
```gradle
// ========== web/build.gradle ==========

dependencies {
    // 项目内依赖
    implementation project(':service')
    implementation project(':common')
    
    // Web相关依赖
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    
    // 模板引擎
    implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
    
    // API文档
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0'
    
    // 开发工具
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
    
    // 测试
    testImplementation 'org.springframework.security:spring-security-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

#### Service模块 (service/build.gradle)
```gradle
// ========== service/build.gradle ==========

// 服务层通常不是可执行的，所以禁用bootJar
bootJar {
    enabled = false
}

jar {
    enabled = true
}

dependencies {
    // 项目内依赖
    implementation project(':data')
    implementation project(':common')
    
    // 业务逻辑相关
    implementation 'org.springframework.boot:spring-boot-starter'
    implementation 'org.springframework:spring-context'
    implementation 'org.springframework:spring-tx'
    
    // 缓存
    implementation 'org.springframework.boot:spring-boot-starter-cache'
    implementation 'org.springframework.boot:spring-boot-starter-data-redis'
    
    // 消息队列
    implementation 'org.springframework.boot:spring-boot-starter-amqp'
}
```

#### Data模块 (data/build.gradle)
```gradle  
// ========== data/build.gradle ==========

// 数据访问层也不是可执行的
bootJar {
    enabled = false
}

jar {
    enabled = true
}

dependencies {
    // 项目内依赖
    implementation project(':common')
    
    // 数据访问
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-data-redis'
    
    // 数据库驱动
    runtimeOnly 'com.mysql:mysql-connector-j'
    runtimeOnly 'com.h2database:h2'
    
    // 数据库迁移
    implementation 'org.flywaydb:flyway-core'
    implementation 'org.flywaydb:flyway-mysql'
    
    // 连接池
    implementation 'com.zaxxer:HikariCP'
    
    // 测试
    testImplementation 'org.testcontainers:junit-jupiter'
    testImplementation 'org.testcontainers:mysql'
}
```

### 5. 全局属性文件 (gradle.properties)

```properties
# ========== gradle.properties ==========

# 组织信息
group=com.example
version=1.0.0

# 构建优化
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configureondemand=true

# JVM配置
org.gradle.jvmargs=-Xmx2g -XX:MaxMetaspaceSize=512m

# 依赖版本（可选）
springBootVersion=3.1.2
springCloudVersion=2022.0.4
```

### 6. 多模块依赖关系图

```
┌─────────────────┐
│      web        │ ← 用户接口层
│   (Spring MVC)  │
└─────┬───────────┘
      │ depends on
      ▼
┌─────────────────┐
│    service      │ ← 业务逻辑层  
│  (Spring Core)  │
└─────┬───────────┘
      │ depends on
      ▼
┌─────────────────┐
│      data       │ ← 数据访问层
│   (Spring JPA)  │  
└─────┬───────────┘
      │ depends on
      ▼
┌─────────────────┐
│     common      │ ← 公共工具层
│    (Utils)      │
└─────────────────┘
```

### 7. 构建和依赖管理命令

```bash
# 查看项目结构
./gradlew projects

# 查看特定模块依赖
./gradlew :web:dependencies
./gradlew :service:dependencies

# 构建所有模块
./gradlew build

# 构建特定模块
./gradlew :web:build
./gradlew :service:jar

# 运行特定模块  
./gradlew :web:bootRun

# 查看模块间依赖关系
./gradlew :web:dependencyInsight --dependency service
```

### 8. 最佳实践

#### 依赖方向
- ✅ Web → Service → Data → Common (单向依赖)
- ❌ 避免循环依赖

#### 插件使用
- ✅ 根项目用 `apply false` 声明插件
- ✅ 子模块根据需要应用插件
- ✅ 公共库使用 `java-library` 插件

#### JAR配置
- ✅ 只有启动模块启用 `bootJar`
- ✅ 库模块启用普通 `jar`，禁用 `bootJar`

#### 版本管理
- ✅ 在根项目统一管理版本
- ✅ 使用 `ext` 或 `gradle.properties` 定义版本
- ✅ 子模块引用父级定义的版本

#### 依赖声明
- ✅ 公共依赖在根项目 `subprojects` 中定义
- ✅ 特定依赖在各自模块中定义
- ✅ 使用 `api` 暴露需要传递的依赖

通过这种多模块结构，您可以实现代码的模块化、复用性和可维护性，同时保持清晰的依赖关系。