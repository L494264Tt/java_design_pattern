# 装饰器模式 - Spring参数解析器实现

## 概述

本示例通过扩展Spring MVC的参数解析器，演示了装饰器模式的经典应用。实现了在不修改原有RequestBody解析逻辑的基础上，为请求参数自动添加时间戳功能。

## 装饰器模式结构

### 角色映射
```
Component (组件接口) 
├── HandlerMethodArgumentResolver

ConcreteComponent (具体组件)
├── RequestResponseBodyMethodProcessor (Spring原生的@RequestBody处理器)

ConcreteDecorator (具体装饰器)
├── TimestampRequestBodyMethodProcessor (添加时间戳功能的装饰器)

注解标记
├── @TimestampRequestBody (标识需要添加时间戳的参数)
```

## 核心实现

### 1. 自定义注解
```java
@TimestampRequestBody
// 用于标识需要添加时间戳功能的参数
```

### 2. 装饰器类
```java
public class TimestampRequestBodyMethodProcessor implements HandlerMethodArgumentResolver {
    // 持有被装饰的对象
    private RequestResponseBodyMethodProcessor processor;
    
    // 委托 + 增强模式
    public Object resolveArgument(...) {
        // 委托：调用原始处理器
        Object result = processor.resolveArgument(...);
        
        // 增强：添加时间戳
        if (result instanceof Map) {
            ((Map) result).put("Timestamp", System.currentTimeMillis());
        }
        
        return result;
    }
}
```

## 执行流程详解

### 阶段1：Spring启动配置
```
1. 在WebMvcConfigurer中注册自定义参数解析器
2. Spring将装饰器加入到参数解析器链中
   [TimestampRequestBodyMethodProcessor, RequestResponseBodyMethodProcessor, ...]
```

### 阶段2：请求处理
```
客户端请求 → Spring DispatcherServlet → Controller映射 → 参数解析
```

### 阶段3：参数解析过程
```
1. Spring遍历参数解析器链
2. 检查每个解析器是否支持当前参数 (supportsParameter方法)
3. 找到支持的解析器后调用 resolveArgument方法
```

### 阶段4：装饰器工作流程
```java
// 1. 检查支持性
supportsParameter(parameter)
├── return parameter.hasParameterAnnotation(TimestampRequestBody.class);

// 2. 解析参数
resolveArgument(...)
├── setupProcessor()           // 获取原始处理器
├── processor.resolveArgument(...) // 委托给原始处理器
├── 添加时间戳到结果Map中        // 装饰功能
└── return 增强后的结果
```

### 阶段5：setupProcessor详细过程
```java
private void setupProcessor() {
    // 从Spring容器获取RequestMappingHandlerAdapter
    RequestMappingHandlerAdapter adapter = context.getBean(...);
    
    // 获取所有参数解析器
    List<HandlerMethodArgumentResolver> resolvers = adapter.getArgumentResolvers();
    
    // 遍历找到原始的RequestResponseBodyMethodProcessor
    for (HandlerMethodArgumentResolver resolver : resolvers) {
        if (resolver instanceof RequestResponseBodyMethodProcessor) {
            this.processor = (RequestResponseBodyMethodProcessor) resolver;
            break;
        }
    }
}
```

## 使用示例

### Controller代码
```java
@RestController
@RequestMapping("/api")
public class MyController {

    @PostMapping
    public Map<String,Object> origin(@TimestampRequestBody Map<String,Object> json){
        // json参数会自动包含timestamp字段
        return json;
    }
}
```

### 请求响应示例
```bash
# 请求
POST /api
Content-Type: application/json
{
    "name": "张三",
    "age": 25
}

# 响应 (自动添加了Timestamp字段)
{
    "name": "张三", 
    "age": 25,
    "Timestamp": 1693123456789
}
```

## 装饰器模式优势

### 1. 开闭原则
- ✅ 对扩展开放：可以添加新功能（时间戳）
- ✅ 对修改关闭：不需要修改Spring原有代码

### 2. 单一职责原则
- `RequestResponseBodyMethodProcessor`：专注JSON解析
- `TimestampRequestBodyMethodProcessor`：专注时间戳添加

### 3. 灵活组合
```java
// 可以继续叠加装饰器
LoggingDecorator → TimestampDecorator → RequestResponseBodyMethodProcessor
```

### 4. 透明性
- 对Spring框架：看起来就是普通的参数解析器
- 对Controller：参数自动包含了时间戳，无需额外处理

## 关键设计点

### 1. 组合而非继承
```java
// ✅ 使用组合
private RequestResponseBodyMethodProcessor processor;

// ❌ 不使用继承
// extends RequestResponseBodyMethodProcessor 
```

### 2. 接口一致性
```java
// 装饰器和被装饰对象实现相同接口
implements HandlerMethodArgumentResolver
```

### 3. 委托机制
```java
// 核心功能委托给被装饰对象
Object result = processor.resolveArgument(...);
// 只负责增强功能
((Map) result).put("Timestamp", System.currentTimeMillis());
```

## 文件结构
```
spring/
├── TimestampRequestBody.java                    # 自定义注解
├── TimestampRequestBodyMethodProcessor.java     # 装饰器实现
├── MyController.java                           # 使用示例
├── WebConfig.java                              # Spring配置
└── 装饰器模式-Spring参数解析器实现.md            # 本文档
```

## 总结

这个实现完美诠释了装饰器模式的核心思想：
- **保持接口一致**：实现相同的HandlerMethodArgumentResolver接口
- **功能叠加**：在原有JSON解析功能基础上添加时间戳功能
- **透明扩展**：对使用方（Spring框架和Controller）完全透明
- **职责分离**：原有功能和新增功能各自独立

通过这种方式，我们可以在不破坏现有代码的情况下，优雅地扩展系统功能。