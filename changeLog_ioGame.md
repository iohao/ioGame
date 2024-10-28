文档与日志
- [ioGame javadoc api](https://iohao.github.io/javadoc/index.html)
- [框架版本更新日志 (yuque.com)](https://www.yuque.com/iohao/game/ab15oe)
- [ioGame 真.轻量级网络编程框架 - 在线使用文档 ](https://game.iohao.com/)
- <a target="_blank" href='https://app.codacy.com/gh/iohao/ioGame/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade'><img src="https://app.codacy.com/project/badge/Grade/4981fff112754686baad7442be998b17" alt="github star"/></a>


> ioGame 每月会发 1 ~ 2 个版本，通常在大版本内升级总是兼容的，如 21.1 升级到任意 21.x 的高版本。

------



### 2024-10

#### 2024-10-28 - v21.19

https://github.com/iohao/ioGame/releases/tag/21.19



**版本更新汇总**

> 1. [core] FlowContext provides the setUserId method to simplify the login operation.
> 2. [broker] Added RingElementSelector load balancing implementation and set it as default to replace RandomElementSelector
> 3. [core] [#386](https://github.com/iohao/ioGame/issues/386) Action supports constructor injection with parameters in Spring
> 4. Simplify the implementation class of ActionParserListener related to ProtoDataCodec. and #386
> 5. perf(i18n): 🐳 [#376](https://github.com/iohao/ioGame/issues/376) cmd check tips
> 6. refactor(external): simplify and improve externalCache


------

**[core]** FlowContext provides the setUserId method to simplify the login operation.
> FlowContext 提供登录方法以简化登录的使用

```java
@ActionController(LoginCmd.cmd)
public class TheLoginAction {
    ... ...
	@ActionMethod(LoginCmd.login)
    public UserInfo loginVerify(LoginVerify loginVerify, FlowContext flowContext) {
        long userId = ...;
        
        // Deprecated
		boolean success = UserIdSettingKit.settingUserId(flowContext, userId);
        // now
        boolean success = flowContext.setUserId(userId);

        return ...;
    }
}
```

---

**[core]** [#386](https://github.com/iohao/ioGame/issues/386) Action supports constructor injection with parameters in Spring
> 在 Spring 中，Action 支持构造函数注入

```java
// Action supports constructor injection in Spring.
@Component
@AllArgsConstructor
@ActionController(PersonCmd.cmd)
public class PersonAction {    
    final PersonService personService;
    ...
}
```

---

refactor(external): simplify and improve externalCache
> 简化与提升游戏对外服缓存

```java
// create externalCache
private static void extractedExternalCache() {
    // Deprecated
    DefaultExternalCmdCache externalCmdCache = new DefaultExternalCmdCache();
    // now
    var externalCmdCache = ExternalCmdCache.of();
}
```

------

**[其他更新]**

```xml
<netty.version>4.1.114.Final</netty.version>
```

------

#### 2024-10-09 - v21.18

https://github.com/iohao/ioGame/releases/tag/21.18



**版本更新汇总**

> - [external] [#375](https://github.com/iohao/ioGame/issues/375) Support for lightweight or embedded Linux distributions. 支持轻量级或嵌入式 Linux 发行版。
> - [core] [#376](https://github.com/iohao/ioGame/issues/376) Support i18n, such as logs and internal messages. 框架内的日志、内部消息支持 i18n。


------

**[core]**

 [#376](https://github.com/iohao/ioGame/issues/376) Support i18n, such as logs and internal messages. 框架内的日志、内部消息支持 i18n。

```java
public class DemoApplication {
    public static void main(String[] args) {
        // setting defaultLocale, such as US or CHINA
        Locale.setDefault(Locale.US);
        Locale.setDefault(Locale.CHINA);

        ... start ioGame
    }
}
```

------

**[其他更新]**

```
<scalecube.version>2.6.17</scalecube.version>
```

------



### 2024-09

#### 2024-09-25 - v21.17

https://github.com/iohao/ioGame/releases/tag/21.17

**版本更新汇总**

> - [core] 简化 TraceIdSupplier 默认实现（[全链路调用日志跟踪](https://www.yuque.com/iohao/game/zurusq)）
> - [core] FlowContext 提供用户（玩家）所关联的用户线程执行器信息及虚拟线程执行器信息方法

---

**[core]**

FlowContext 提供用户（玩家）所关联的用户线程执行器信息及虚拟线程执行器信息方法

```java
void testThreadExecutor(FlowContext flowContext) {
    // 获取 - 用户（玩家）所关联的用户线程执行器信息及虚拟线程执行器信息

    // 用户虚拟线程执行器信息
    ThreadExecutor virtualThreadExecutor = flowContext.getVirtualThreadExecutor();
    // 用户线程执行器信息
    ThreadExecutor threadExecutor = flowContext.getThreadExecutor();

    threadExecutor.execute(() -> {
        log.info("execute");
    });

    threadExecutor.executeTry(() -> {
        log.info("executeTry");
    });

    // get Executor
    Executor executor = threadExecutor.executor();
}
```

------



#### 2024-09-09 - v21.16

https://github.com/iohao/ioGame/releases/tag/21.16

**版本更新汇总**

> - [kit] [#291](https://github.com/iohao/ioGame/issues/291) 增加轻量可控的延时任务
> - [kit] 细分时间日期相关工具。
> - [Archive] [#363](https://github.com/iohao/ioGame/issues/363)  light-redis-lock 相关模块
> - [Archive] [#364](https://github.com/iohao/ioGame/issues/364) light-timer-task 相关模块
> - [core] 增加同一个 ActionController 相同的 action 方法名只允许存在一个的检测。
> - [core] Banner 增加启动时的错误数量提示。
> - [core] [#365](https://github.com/iohao/ioGame/issues/365) 支持对接文档生成时，可以根据路由访问权限来控制文档的生成

------

**[kit]**

[#291](https://github.com/iohao/ioGame/issues/291) 增加轻量可控的延时任务

 文档 - [轻量可控的延时任务 (yuque.com)](https://www.yuque.com/iohao/game/nykaacfzg4h1ynii)

 for example 

```java
@Test
public void example() {
    long timeMillis = System.currentTimeMillis();

    DelayTask delayTask = DelayTaskKit.of(() -> {
                long value = System.currentTimeMillis() - timeMillis;
                log.info("1 - 最终 {} ms 后，执行延时任务", value);
            })
            .plusTime(Duration.ofSeconds(1)) // 增加 1 秒的延时  
            .task(); // 启动任务

    delayTask.plusTimeMillis(500); // 增加 0.5 秒的延时
    delayTask.minusTimeMillis(500);// 减少 0.5 秒的延时时间

    // 因为 taskId 相同，所以会覆盖之前的延时任务
    String taskId = delayTask.getTaskId();
    delayTask = DelayTaskKit.of(taskId, () -> {
                long value = System.currentTimeMillis() - timeMillis;
                log.info("2 - 最终 {} ms 后，执行延时任务", value);
            })
            .plusTime(Duration.ofSeconds(1)) // 增加 1 秒的延时
            .task(); // 启动任务

    // 取消延时任务，下面两个方法是等价的
    delayTask.cancel();
    DelayTaskKit.cancel(taskId);

    // 可以通过 taskId 查找该延时任务
    Optional<DelayTask> optionalDelayTask = DelayTaskKit.optional(taskId);
    if (optionalDelayTask.isPresent()) {
        var delayTask = optionalDelayTask.get();
    }

    // 通过 taskId 查找延时任务，存在则执行给定逻辑
    DelayTaskKit.ifPresent(taskId, delayTask -> {
        delayTask.plusTimeMillis(500); // 增加 0.5 秒的延时时间
    });
}
```

------

细分时间日期相关工具。

see com.iohao.game.common.kit.time

------

**[Archive]**

[#363](https://github.com/iohao/ioGame/issues/363)  light-redis-lock 相关模块

将 light-redis-lock、light-redis-lock-spring-boot-starter 模块做归档。在过去的时间里，由于一直没有改动这些模块的相关内容，现决定将不再上传到 maven 库中，以节约公共资源。如果你使用了该模块的相关内容，请指定最后一个版本即可。如

```xml
<!-- https://mvnrepository.com/artifact/com.iohao.game/light-redis-lock -->
<dependency>
    <groupId>com.iohao.game</groupId>
    <artifactId>light-redis-lock</artifactId>
    <version>21.15</version>
</dependency>

<!-- https://mvnrepository.com/artifact/com.iohao.game/light-redis-lock-spring-boot-starter -->
<dependency>
    <groupId>com.iohao.game</groupId>
    <artifactId>light-redis-lock-spring-boot-starter</artifactId>
    <version>21.15</version>
</dependency>
```



模块相关文档 - [redis-lock 分布式锁 (yuque.com)](https://www.yuque.com/iohao/game/wz7af5)

------

[#364](https://github.com/iohao/ioGame/issues/364) light-timer-task 相关模块

将 light-timer-task 模块做归档。在过去的时间里，由于一直没有改动这些模块的相关内容；同时，也因为框架内置了类似的功能 #291 。现决定将不再上传到 maven 库中，以节约公共资源。如果你使用了该模块的相关内容，请指定最后一个版本即可。如

```xml
<!-- https://mvnrepository.com/artifact/com.iohao.game/light-timer-task -->
<dependency>
    <groupId>com.iohao.game</groupId>
    <artifactId>light-timer-task</artifactId>
    <version>21.15</version>
</dependency>
```

模块相关文档 - [timer-task 任务延时器 (yuque.com)](https://www.yuque.com/iohao/game/niflk0)

类似的代替 [轻量可控的延时任务 (yuque.com)](https://www.yuque.com/iohao/game/nykaacfzg4h1ynii)

------

**[core]**

[#365](https://github.com/iohao/ioGame/issues/365) 支持对接文档生成时，可以根据路由访问权限来控制文档的生成



生成相关代码的使用及相关文档

- `ExternalGlobalConfig.accessAuthenticationHook`，相关文档 - [路由访问权限控制 (yuque.com)](https://www.yuque.com/iohao/game/nap5y8p5fevhv99y)
- IoGameDocumentHelper，相关文档 - [游戏对接文档生成 (yuque.com)](https://www.yuque.com/iohao/game/irth38)

for example 

```java
public class MyExternalServer {
    public static void extractedAccess() {
        // https://www.yuque.com/iohao/game/nap5y8p5fevhv99y
        var accessAuthenticationHook = ExternalGlobalConfig.accessAuthenticationHook;
        ... 省略部分代码
        // 添加 - 拒绝玩家访问权限的控制
        accessAuthenticationHook.addRejectionCmd(RankCmd.cmd, RankCmd.internalUpdate);
    }
}

public class TestGenerate {
    ... 省略部分代码
    public static void main(String[] args) {
        // 对外服访问权限控制
        MyExternalServer.extractedAccess();
        // （复用）设置文档路由访问权限控制
      IoGameDocumentHelper.setDocumentAccessAuthentication(ExternalGlobalConfig.accessAuthenticationHook::reject);
        
        // ====== 生成对接文档、生成 proto ======
//        generateCsharp();
//        generateTypeScript();
        // 生成文档
        IoGameDocumentHelper.generateDocument();
        // .proto 文件生成
//        generateProtoFile();
    }
}
```



**预览 - 没有做控制前的生成**

```latex
==================== RankAction  ====================
路由: 4 - 1  --- 【listRank】 --- 【RankAction:48】【listRank】
    方法参数: StringValue 排行类型
    方法返回值: ByteValueList<RankUpdate> 玩家排行名次更新
 
路由: 4 - 10  --- 【玩家排行名次更新】 --- 【RankAction:60】【internalUpdate】
    方法参数: RankUpdate 玩家排行名次更新
    方法返回值: void 
```

**预览 - 加入了访问控制后的生成**

我们可以看见，路由为 4-10 的 action 方法没有生成到对接文档中。

```latex
==================== RankAction  ====================
路由: 4 - 1  --- 【listRank】 --- 【RankAction:48】【listRank】
    方法参数: StringValue 排行类型
    方法返回值: ByteValueList<RankUpdate> 玩家排行名次更新
```



提示：除了文档文档的访问权限控制外，还支持 SDK TypeScript、SDK C# ...等客户端代码生成的访问权限控制。



SDK 相关请阅读：[SDK&对接文档 (yuque.com)](https://www.yuque.com/iohao/game/mywnvkhemv8wm396)

------

**[其他更新]**

```xml
<netty.version>4.1.113.Final</netty.version>
```

------



### 2024-08

#### 2024-08-26 - v21.15

https://github.com/iohao/ioGame/releases/tag/21.15



**版本更新汇总**

> - [core] [#351](https://github.com/iohao/ioGame/issues/351)  增加 UserProcessor 线程执行器的选择策略扩展
> - [core] [#350](https://github.com/iohao/ioGame/issues/350) 修复请求消息在 Broker 环节乱序的问题
> - [core] [#353](https://github.com/iohao/ioGame/issues/353) 对接文档支持框架内置错误码的生成
> - [core] [#354](https://github.com/iohao/ioGame/issues/354) 日志打印调整
> - [core] [#359](https://github.com/iohao/ioGame/issues/359) [逻辑服-监听] 增加打印其他进程逻辑服的上线与下线信息
> - [core] 优化 ThreadExecutorRegion 相关实现类。
> - [external] UserSession 接口新增 ofRequestMessage 方法，简化玩家在游戏对外服中创建请求对象。

------

**[external]**

UserSession 接口新增 ofRequestMessage 方法，简化玩家在游戏对外服中创建请求对象。 for example

```java
var cmdInfo = CmdInfo.of(1, 1);
RequestMessage request = userSession.ofRequestMessage(cmdInfo);
```

------

**[core]**

[#359](https://github.com/iohao/ioGame/issues/359) [逻辑服-监听] 增加打印其他进程逻辑服的上线与下线信息

```java
public class MyLogicServer extends AbstractBrokerClientStartup {
    ...

    @Override
    public BrokerClientBuilder createBrokerClientBuilder() {
        BrokerClientBuilder builder = BrokerClient.newBuilder();
        ...
        // 添加监听 - 打印其他进程逻辑服的上线与下线信息
        builder.addListener(SimplePrintBrokerClientListener.me());
        return builder;
    }
}
```

------

[#351](https://github.com/iohao/ioGame/issues/351) 增加 UserProcessor 线程执行器的选择策略扩展

> for example，注意事项：当你的 UserProcessor 做了线程执行器的选择策略扩展，需要重写 CustomSerializer 接口的相关方法。

```java
// 为请求消息开启有序的、多线程处理的优化
IoGameGlobalConfig.enableUserProcessorExecutorSelector();
```





------

#### 2024-08-08 - v21.14

https://github.com/iohao/ioGame/releases/tag/21.14



**版本更新汇总**

> - [code quality] 提升代码质量，see [ioGame - Qodana Cloud](https://qodana.cloud/organizations/3k6Pm/teams/zxRGm)
> - [javadoc] 增强相关模块的 javadoc ：业务框架、压测与模拟客户端请求、领域事件、Room
> - [core]  [#346](https://github.com/iohao/ioGame/issues/346) 业务框架 InOutManager 提供扩展点
> - [core]  [#344](https://github.com/iohao/ioGame/issues/344) 登录时，如果 FlowContext 存在 userId 就不请求游戏对外服
> - [broker] fixed [#342](https://github.com/iohao/ioGame/issues/342)  非集群环境下，Broker 断开重启后，逻辑服没有将其重新加入到 BrokerClientManager 中所引发的 NPE。

------

**[core]**

 [#346](https://github.com/iohao/ioGame/issues/346) **业务框架 InOutManager 提供扩展点**

在构建器中配置 InOutManager 策略，框架内置了两个实现类，分别是

1. ofAbcAbc ：in ABC，out ABC 的顺序，即编排时的顺序。
2. ofPipeline：in ABC，out CBA 的顺序，类似的 netty Pipeline 。（默认策略，如果不做任何设置，将使用该策略）



for example 在构建器中配置 InOutManager 策略

```java
public void config() {
    BarSkeletonBuilder builder = ...;
    builder.setInOutManager(InOutManager.ofAbcAbc());
    builder.setInOutManager(InOutManager.ofPipeline());
}
```



------



### 2024-07

#### 2024-07-24 - v21.13

https://github.com/iohao/ioGame/releases/tag/21.13



**版本更新汇总**

- [external]  [#334](https://github.com/iohao/ioGame/issues/334) 顶号操作 bug，有概率触发并发问题
- [core]  FlowContext 新增 createRequestCollectExternalMessage 方法
- [javadoc] 源码 javadoc 增强

------



**[core]**

FlowContext 新增 createRequestCollectExternalMessage 方法，request 与游戏对外服交互。相关使用文档请阅读 [获取游戏对外服的数据与扩展 (yuque.com)](https://www.yuque.com/iohao/game/ivxsw5)

```java
... ... 省略部分代码
@ActionMethod(ExternalBizRegionCmd.listOnlineUserAll)
public List<Long> listOnlineUserAll(FlowContext flowContext) {

    // 创建 RequestCollectExternalMessage
    var request = flowContext
            .createRequestCollectExternalMessage(MyExternalBizCode.onlineUser);

    // 访问多个【游戏对外服】
    var collectExternalMessage = flowContext
            .invokeExternalModuleCollectMessage(request);

    return listUserId(collectExternalMessage);
}
```

------



**[其他更新]**

```xml
<netty.version>4.1.112.Final</netty.version>
<lombok.version>1.18.34</lombok.version>
```

------



#### 2024-07-08 - v21.12

https://github.com/iohao/ioGame/releases/tag/21.12



**版本更新汇总**

- [light-game-room]  [#326](https://github.com/iohao/ioGame/issues/326) GameFlowContext getRoom、getPlayer 方法返回值改成泛型
- [对接文档]  [#330](https://github.com/iohao/ioGame/issues/330) 增强，支持对接文档生成与扩展，包括文本文档生成、联调代码生成 ...等



当前版本，为之后生成联调代码做了充分的准备

------



**[light-game-room]**

[#326](https://github.com/iohao/ioGame/issues/326) GameFlowContext getRoom、getPlayer 方法返回值改成泛型

```java
GameFlowContext gameFlowContext = ...;
// FightRoomEntity 是自定义的 Room 对象

// Room、Player 在使用时，不需要强制转换了
FightRoomEntity room =  gameFlowContext.getRoom();
FightPlayerEntity player = gameFlowContext.getPlayer();
```

------



**[对接文档]** 

[#330](https://github.com/iohao/ioGame/issues/330) 增强，支持对接文档生成与扩展，包括文本文档生成、联调代码生成 ...等。开发者做更多个性化的扩展



在该版本中，我们已经新做了对接文档相关模块；该版本功能更加的强大，使用上也更加的简洁。新版本的对接文档模块，除了能提供文本文档的生成外，还能支持生成与客户端联调的代码、并且是可扩展的。通常，客户端联调代码有：

1. 支持生成 C# 客户端的联调代码，通常用在 Unity、Godot 客户端，具体可阅读 [SDK C# 代码生成](https://www.yuque.com/iohao/game/fgrizbhz4qqzd1vl)。
2. 支持生成 TypeScript 客户端的联调代码，通常用在 cocos、laya 客户端，具体可阅读 [SDK TypeScript 代码生成](https://www.yuque.com/iohao/game/mywnvkhemv8wm396)。



```java
public static void main(String[] args) {
    // 添加枚举错误码 class，用于生成错误码相关信息
    IoGameDocumentHelper.addErrorCodeClass(GameCode.class);

    // 添加文档生成器，文本文档
    IoGameDocumentHelper.addDocumentGenerate(new TextDocumentGenerate());
    // 添加文档生成器，Ts 联调代码生成
    IoGameDocumentHelper.addDocumentGenerate(new TypeScriptDocumentGenerate());
    // 生成文档
    IoGameDocumentHelper.generateDocument();
}
```

上述代码

- 添加了错误码的生成
- 添加了文本文档的生成
- 添加了 Ts 客户端联调代码的生成（包括 action、广播、错误码...相关代码的生成）， [SDK TypeScript 客户端代码生成；方便 CocosCeator、或其他支持 TypeScript 的客户端对接。 #329](https://github.com/iohao/ioGame/issues/329)



addDocumentGenerate 是可扩展的，这将意味着开发者可以扩展出 C#、GodotScript、Js ...等不同客户端的联调代码。默认，我们提供了一个文本文档，即 TextDocumentGenerate，如果默认的实现满足不了当下需求，开发者也可以定制个性化的文档，如 json 格式的。



更多内容请阅读 [游戏对接文档生成 (yuque.com)](https://www.yuque.com/iohao/game/irth38)



**新增 DocumentGenerate 接口**

开发者可利用该接口进行定制个性化的对接文档，如代码生成 ...等。

```java
/**
 * 对接文档生成接口，可扩展不同的实现
 */
public interface DocumentGenerate {
    /**
     * 生成文档
     *
     * @param ioGameDocument ioGameDocument
     */
    void generate(IoGameDocument ioGameDocument);
}

/**
 * 文档相关信息，如 action 相关、广播相关、错误码相关。
 */
@Getter
public final class IoGameDocument {
    /** 已经解析好的广播文档 */
    List<BroadcastDocument> broadcastDocumentList;
    /** 已经解析好的错误码文档 */
    List<ErrorCodeDocument> errorCodeDocumentList;
    /** 已经解析好的 action 文档 */
    List<ActionDoc> actionDocList;
}
```



开发者可以通过实现 DocumentGenerate 接口来扩展不同的文档生成，开发者可以扩展此接口来定制更多个性化的扩展，如

- html 版本的文档。
- json 版本的文档。
- 其他语言的联调文档 ...等。



```java
// 使用示例
private static void test() {
    var documentGenerate = new YourDocumentGenerate();
    IoGameDocumentHelper.addDocumentGenerate(documentGenerate);
}
```



------

其他：废弃旧版本对接文档相关类 DocActionSend、DocActionSends、ActionDocs、ActionSendDoc、ActionSendDocs、ActionSendDocsRegion、BarSkeletonDoc、BroadcastDoc、BroadcastDocBuilder、ErrorCodeDocs、ErrorCodeDocsRegion。

------

21.10 及之前版本的使用示例（对接文档）

```java
public static void main(String[] args) {
    ... 省略部分代码

    new NettyRunOne()
            ... ...
            .startup();

    // 生成对接文档
    BarSkeletonDoc.me().buildDoc();
}
```

------



### 2024-06

#### 2024-06-21 - v21.10（问题版本）

https://github.com/iohao/ioGame/releases/tag/21.10

------

**版本更新汇总**

- [core] [#315](https://github.com/iohao/ioGame/issues/315) ResponseMessage 增加协议碎片便捷获取，简化跨服调用时的使用
- [core] ActionCommand 增加 containAnnotation、getAnnotation 方法，简化获取 action 相关注解信息的使用。
- [kit] [动态属性]  增加 ifNull 方法，如果动态属性值为 null，则执行给定的操作，否则不执行任何操作。执行给定操作后将得到一个返回值，该返回值会设置到动态属性中。
- [kit]  TimeKit 增加 nowLocalDate 方法，可减少 LocalDate 对象的创建；优化 currentTimeMillis 方法的时间更新策略。同时，优化 nowLocalDate、currentTimeMillis 方法，不使用时将不会占用相关资源。
- [EventBus]  分布式事件总线增加 EventBusRunner 接口。EventBus 接口化，方便开发者自定义扩展。fix 订阅者使用自身所关联的 EventBus 处理相关事件。

------

**[core]** [315](https://github.com/iohao/ioGame/issues/315) ResponseMessage 增加协议碎片便捷获取，简化跨服调用时的使用

框架具备[协议碎片](https://www.yuque.com/iohao/game/ieimzn)特性。某些业务中，我们需要跨服访问其他游戏逻辑服，以获取某些业务数据；一些简单的数据，我们可以通过协议碎片来返回，从而避免定义过多的协议。



现为 ResponseMessage 增加协议碎片支持，简化跨服调用时的使用，新增的方法如下

```java
public void test() {
    ResponseMessage responseMessage = ...;

    // object
    responseMessage.getValue(Student.class);
    List<Student> listValue = responseMessage.listValue(Student.class);

    // int
    int intValue = responseMessage.getInt();
    List<Integer> listInt = responseMessage.listInt();

    // long
    long longValue = responseMessage.getLong();
    List<Long> listLong = responseMessage.listLong();

    // String
    String stringValue = responseMessage.getString();
    List<String> listString = responseMessage.listString();

    // boolean
    boolean boolValue = responseMessage.getBoolean();
    List<Boolean> listBoolean = responseMessage.listBoolean();
}
```



示例说明

- HomeAction 是 【Home 游戏逻辑服】提供的 action
- UserAction 是 【User 游戏逻辑服】提供的 action

两个逻辑服的交互如下，UserAction 使用跨服方式调用了【Home 游戏逻辑服】的几个方法，并通过 responseMessage 的协议碎片支持，简化跨服调用时的使用。



示例中演示了 string、string list、object list 的简化使用（协议碎片获取时的简化使用）。

```java
@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
public class Student {
    String name;
}

// home 游戏逻辑服提供的 action
public class HomeAction {
    @ActionMethod(HomeCmd.name)
    public String name() {
        return "a";
    }

    @ActionMethod(HomeCmd.listName)
    public List<String> listName() {
        return List.of("a", "b");
    }

    @ActionMethod(HomeCmd.listStudent)
    public List<Student> listStudent() {
        Student student = new Student();
        student.name = "a";

        Student student2 = new Student();
        student2.name = "b";

        return List.of(student, student2);
    }
}

@ActionController(UserCmd.cmd)
public class UserAction {
    @ActionMethod(UserCmd.userSleep)
    public void userSleep(FlowContext flowContext) {

        flowContext.invokeModuleMessageAsync(HomeCmd.of(HomeCmd.name), responseMessage -> {
            String name = responseMessage.getString();
            log.info("{}", name);
        });

        flowContext.invokeModuleMessageAsync(HomeCmd.of(HomeCmd.listName), responseMessage -> {
            var listName = responseMessage.listString();
            log.info("{}", listName);
        });

        flowContext.invokeModuleMessageAsync(HomeCmd.of(HomeCmd.listStudent), responseMessage -> {
            List<Student> studentList = responseMessage.listValue(Student.class);
            log.info("{}", studentList);
        });
    }
}
```



------

**[core]** ActionCommand 增加 containAnnotation、getAnnotation 方法，简化获取 action 相关注解信息的使用。

```java
ActionCommand actionCommand = flowContext.getActionCommand();

bool contain = actionCommand.containAnnotation(DisableDebugInout.class);
var annotation = actionCommand.getAnnotation(DisableDebugInout.class);
```



------

**[EventBus] 分布式事件总线**

1. [增强扩展] 将抽象类 AbstractEventBusRunner 标记为过时的，由接口 EventBusRunner 代替。
2. [增强扩展] 分布式事件总线 EventBus 接口化，方便开发者自定义扩展。增加[总线相关的 javadoc](https://iohao.github.io/javadoc/com/iohao/game/action/skeleton/eventbus/package-summary.html)。
3. [fix] 订阅者使用自身所关联的 EventBus 处理相关事件。



关于 fix 订阅者使用自身所关联的 EventBus 处理相关事件，在此之前可能引发 bug 的场景如下

1. 【游戏逻辑服 A】 发布事件。
2. 【游戏逻辑服 B】 订阅者接收事件并处理，在处理过程中又调用了【游戏逻辑服 A】 某个 action 方法。



该业务场景，会在多服单进程下会引发调用超时，但在多服多进程下则不会超时。



------

**[kit] TimeKit**

增强 TimeKit 增加 nowLocalDate 方法，可减少 LocalDate 对象的创建；

优化 currentTimeMillis 方法的时间更新策略。

优化 nowLocalDate、currentTimeMillis 不使用时将不会占用相关资源。

```csharp
@Test
public void test() {
    long millis = TimeKit.currentTimeMillis();
    Assert.assertTrue(millis > 0);

    LocalDate localDate = TimeKit.nowLocalDate();
    Assert.assertTrue(localDate.isEqual(LocalDate.now()));
}
```



------

**[kit] 动态属性**

[动态属性] 增加 ifNull 方法，如果动态属性值为 null，则执行给定的操作，否则不执行任何操作。执行给定操作后将得到一个返回值，该返回值会设置到动态属性中。

```csharp
public class AttrOptionDynamicTest {
    // 动态属性 key
    AttrOption<AttrCat> attrCatOption = AttrOption.valueOf("AttrCat");

    @Test
    public void ifNull() {
        var myAttrOptions = new MyAttrOptions();
        Assert.assertNull(myAttrOptions.option(attrCatOption));

        // 如果 catAttrOption 属性为 null，则创建 AttrCat 对象，并赋值到属性中
        myAttrOptions.ifNull(attrCatOption, AttrCat::new);
        Assert.assertNotNull(myAttrOptions.option(attrCatOption));
    }

    private static class AttrCat {
        String name;
    }

    @Getter
    private static class MyAttrOptions implements AttrOptionDynamic {
        final AttrOptions options = new AttrOptions();
    }
}
```



------

**[其他 - 相关库升级]**

<netty.version>4.1.111.Final</netty.version>

<jctools-core.version>4.0.5</jctools-core.version>

<jprotobuf.version>2.4.23</jprotobuf.version>



<br>

#### 2024-06-03 - v21.9（问题版本）

https://github.com/iohao/ioGame/releases/tag/21.9


---

**版本更新汇总**

- [core]  [#294](https://github.com/iohao/ioGame/issues/294) 增加范围内的广播接口 RangeBroadcaster，业务参数支持基础类型（[协议碎片](https://www.yuque.com/iohao/game/ieimzn)）的简化使用
- [core-对接文档]  [#293](https://github.com/iohao/ioGame/issues/293) 广播文档构建器支持对参数的单独描述
- [light-game-room]   [#297](https://github.com/iohao/ioGame/issues/297) 模拟系统创建房间，RoomCreateContext 的使用
- [light-game-room]   [#298](https://github.com/iohao/ioGame/issues/298) 模拟系统创建房间，GameFlowContext 的使用
- [core]   [#301](https://github.com/iohao/ioGame/issues/301) FlowContext 更新元信息后，需要立即生效（跨服调用时）
- [内置 kit] 开放 TaskListener 接口
- 为 SimpleRoom aggregationContext 属性提供默认值，移除 RoomCreateContext 接口的 getAggregationContext 方法，以免产生误导。


---



**[light-game-room]**

为 SimpleRoom aggregationContext 属性提供默认值

<br>

[#297](https://github.com/iohao/ioGame/issues/297)，模拟系统创建房间，RoomCreateContext 的使用

>  移除 RoomCreateContext 接口的 getAggregationContext 方法，以免产生误导。
>
> RoomCreateContext 增加默认重载

```java
RoomCreateContext.of(); // 无房间创建者，通常表示系统创建
RoomCreateContext.of(userId); // 房间创建者为 userId
```

<br>

 [#298](https://github.com/iohao/ioGame/issues/298) 模拟系统创建房间，GameFlowContext 的使用

```java
public void test() {
    Room room = ...;
    GameFlowContext context = GameFlowContext.of(room);
    ... 省略部分代码
}
```

---

**[core]**

[#294](https://github.com/iohao/ioGame/issues/294) 增加范围内的广播接口 RangeBroadcaster，业务参数支持基础类型（[协议碎片](https://www.yuque.com/iohao/game/ieimzn)）的简化使用

```java
public void testRangeBroadcaster(FlowContext flowContext) {
    // ------------ object ------------
    // 广播 object
    DemoBroadcastMessage message = new DemoBroadcastMessage();
    message.msg = "helloBroadcast --- 1";
    RangeBroadcaster.of(flowContext)
            .setResponseMessage(cmdInfo, message);
    // 广播 object list
    List<DemoBroadcastMessage> messageList = List.of(message);
    RangeBroadcaster.of(flowContext)
            .setResponseMessageList(cmdInfo, messageList);

    // ------------ int ------------
    // 广播 int
    int intValue = 1;
    RangeBroadcaster.of(flowContext)
            .setResponseMessage(cmdInfo, intValue);
    // 广播 int list
    List<Integer> intValueList = List.of(1, 2);
    RangeBroadcaster.of(flowContext)
            .setResponseMessageIntList(cmdInfo, intValueList);

    // ------------ long ------------
    // 广播 long
    long longValue = 1L;
    RangeBroadcaster.of(flowContext)
            .setResponseMessage(cmdInfo, longValue);
    // 广播 long list
    List<Long> longValueList = List.of(1L, 2L);
    RangeBroadcaster.of(flowContext)
            .setResponseMessageLongList(cmdInfo, longValueList);

    // ------------ String ------------
    // 广播 String
    String stringValue = "1";
    RangeBroadcaster.of(flowContext)
            .setResponseMessage(cmdInfo, stringValue);
    // 广播 String list
    List<String> stringValueList = List.of("1L", "2L");
    RangeBroadcaster.of(flowContext)
            .setResponseMessageStringList(cmdInfo, stringValueList);
            
    // ------------ boolean ------------
    // 广播 boolean
    boolean boolValue = true;
    RangeBroadcaster.of(flowContext)
            .setResponseMessage(cmdInfo, boolValue);
    // 广播 boolean list
    List<Boolean> boolValueList = List.of(true, false);
    RangeBroadcaster.of(flowContext)
            .setResponseMessageBoolList(cmdInfo, boolValueList);
}
```

<br>

[#301](https://github.com/iohao/ioGame/issues/301) FlowContext 更新元信息后，需要立即生效（跨服调用时）

> 在此之前，更新元信息后，并不会将元信息同步到 FlowContext 中，只会将元信息同步到游戏对外服中；所以在更新元信息后，紧接着执行跨服调用是不能获取新的元信息内容的。
>
> 当前 issues 会对这部分做增强，也就是在更新元信息后，会将元信息同步到 FlowContext 中；这样，在后续的跨服调用中也能获取到最新的元信息。

```java
void test1(FlowContext flowContext) {
    // 获取元信息
    MyAttachment attachment = flowContext.getAttachment(MyAttachment.class);
    attachment.nickname = "渔民小镇";

    // [同步]更新 - 将元信息同步到玩家所在的游戏对外服中
    flowContext.updateAttachment(attachment);

    // 跨服请求
    CmdInfo helloCmdInfo = CmdInfo.of(1, 1);
    flowContext.invokeModuleMessage(helloCmdInfo);
}

@ActionController(1)
public class DemoFightAction {
    @ActionMethod(1)
    void hello(FlowContext flowContext) {
        // 可以得到最新的元信息
        MyAttachment attachment = flowContext.getAttachment(MyAttachment.class);
        log.info("{}", attachment.nickname);
    }
}
```

<br>

 [#293](https://github.com/iohao/ioGame/issues/293) 广播文档构建器支持对参数的单独描述

```java
  private void extractedDco(BarSkeletonBuilder builder) {
      // UserCmd
      builder.addBroadcastDoc(BroadcastDoc.newBuilder(UserCmd.of(UserCmd.enterSquare))
              .setDataClass(SquarePlayer.class)
              .setDescription("新玩家加入房间，给房间内的其他玩家广播")
      ).addBroadcastDoc(BroadcastDoc.newBuilder(UserCmd.of(UserCmd.offline))
              .setDataClass(LongValue.class, "userId")
              .setDescription("有玩家下线了")
      );
}
```

>  下面是生成后的对接文档预览

```text
==================== 游戏文档格式说明 ====================
https://www.yuque.com/iohao/game/irth38#cJLdC

==================== FightHallAction 大厅（类似地图） ====================
 
路由: 1 - 2  --- 【进入大厅】 --- 【FightHallAction:94】【enterSquare】
    方法参数: EnterSquare enterSquare 进入大厅
    方法返回值: ByteValueList<SquarePlayer> 所有玩家
    广播推送: SquarePlayer ，(新玩家加入房间，给房间内的其他玩家广播)
 

路由: 1 - 5  --- 【玩家下线】 --- 【FightHallAction:154】【offline】
    方法返回值: void 
    广播推送: LongValue userId，(有玩家下线了)

```



**[内置 kit]** 

开放 TaskListener 接口，TaskListener 是 TaskKit 相关的任务监听接口。



TaskListener 任务监听回调，使用场景有：一次性延时任务、任务调度、轻量可控的延时任务、轻量的定时入库辅助功能 ...等其他扩展场景。这些使用场景都有一个共同特点，即监听回调。接口提供了 4 个方法，如下

1. CommonTaskListener.onUpdate()，监听回调
2. CommonTaskListener.triggerUpdate()，是否触发 CommonTaskListener.onUpdate() 监听回调方法
3. CommonTaskListener.onException(Throwable) ，异常回调。在执行 CommonTaskListener.triggerUpdate() 和 CommonTaskListener.onUpdate() 方法时，如果触发了异常，异常将被该方法捕获。
4. CommonTaskListener.getExecutor()，指定执行器来执行上述方法，目的是不占用业务线程。



更多介绍与使用，请阅读 [TaskKit (yuque.com)](https://www.yuque.com/iohao/game/gzsl8pg0si1l4bu3)

<br>

### 2024-05


#### 2024-05-19 - v21.8

https://github.com/iohao/ioGame/releases/tag/21.8


---

**版本更新汇总**

- [light-game-room]  [#278](https://github.com/iohao/ioGame/issues/278) 桌游类、房间类游戏的扩展模块，简化与规范化房间管理相关的、开始游戏流程相关的、玩法操作相关的相关扩展
- [core]  [#290](https://github.com/iohao/ioGame/issues/290) 新增广播文档构建器，简化生成广播对接文档
- [示例集合整理] 将 SimpleExample（文档中所有功能点的示例）、SpringBootExample（[综合示例](https://www.yuque.com/iohao/game/ruaqza)）、ioGameWeb2Game（[web 转游戏 - 示例理解篇](https://www.yuque.com/iohao/game/gpzmc8vadn4vl70z)）、fxglSimpleGame（[移动同步](https://www.yuque.com/iohao/game/bolt) FXGL + netty）合并成一个示例项目。

---



**[core]**  

[#290](https://github.com/iohao/ioGame/issues/290) 新增广播文档构建器，简化生成广播对接文档

下面是使用示例

```java
public class MyLogicServer extends AbstractBrokerClientStartup {
    @Override
    public BarSkeleton createBarSkeleton() {
        // 业务框架构建器
        BarSkeletonBuilder builder = ...
        
        // 错误码、广播、推送对接文档生成
        extractedDco(builder);

        return builder.build();
    }
    
    private void extractedDco(BarSkeletonBuilder builder) {
        // 错误码
        Arrays.stream(GameCode.values()).forEach(builder::addMsgExceptionInfo);

        // UserCmd
        builder.addBroadcastDoc(BroadcastDoc.newBuilder(UserCmd.of(UserCmd.enterSquare))
                .setDataClass(SquarePlayer.class)
                .setDescription("新玩家加入房间，给房间内的其他玩家广播")
        ).addBroadcastDoc(BroadcastDoc.newBuilder(UserCmd.of(UserCmd.move))
                .setDataClass(SquarePlayerMove.class)
                .setDescription("其他玩家的移动")
        ).addBroadcastDoc(BroadcastDoc.newBuilder(UserCmd.of(UserCmd.offline))
                .setDataClass(LongValue.class)
                .setDescription("有玩家下线了。userId")
        );

        // room
        builder.addBroadcastDoc(BroadcastDoc.newBuilder(RoomCmd.of(RoomCmd.roomUpdateBroadcast))
                .setDataClass(FightRoomNotice.class)
                .setDescription("房间更新通知")
        ).addBroadcastDoc(BroadcastDoc.newBuilder(RoomCmd.of(RoomCmd.playerEnterRoomBroadcast))
                .setDataClass(FightPlayer.class)
                .setDescription("有新玩家加入房间")
        ).addBroadcastDoc(BroadcastDoc.newBuilder(RoomCmd.of(RoomCmd.enterRoom))
                .setDataClass(FightEnterRoom.class)
                .setDescription("玩家自己进入房间")
        ).addBroadcastDoc(BroadcastDoc.newBuilder(RoomCmd.of(RoomCmd.dissolveRoomBroadcast))
                .setDescription("解散房间")
        ).addBroadcastDoc(BroadcastDoc.newBuilder(RoomCmd.of(RoomCmd.quitRoom))
                .setDataClass(LongValue.class)
                .setDescription("有玩家退出房间了。userId")
        ).addBroadcastDoc(BroadcastDoc.newBuilder(RoomCmd.of(RoomCmd.ready))
                .setDataClass(PlayerReady.class)
                .setDescription("有玩家准备或取消准备了")
        ).addBroadcastDoc(BroadcastDoc.newBuilder(RoomCmd.of(RoomCmd.nextRoundBroadcast))
                .setDataClass(IntValue.class)
                .setDescription("对局开始，通知玩家开始选择。round 当前对局数")
        ).addBroadcastDoc(BroadcastDoc.newBuilder(RoomCmd.of(RoomCmd.operationBroadcast))
                .setDataClass(LongValue.class)
                .setDescription("通知其他玩家，有玩家做了选择。userId")
        ).addBroadcastDoc(BroadcastDoc.newBuilder(RoomCmd.of(RoomCmd.littleSettleBroadcast))
                .setDataClassList(FightRoundPlayerScore.class)
                .setDescription("广播玩家对局分数")
        ).addBroadcastDoc(BroadcastDoc.newBuilder(RoomCmd.of(RoomCmd.gameOverBroadcast))
                .setDescription("游戏结束")
        );
    }
}
```



其他扩展阅读
- [游戏对接文档生成 (yuque.com)](https://www.yuque.com/iohao/game/irth38)
- 示例中关于错误码可阅读 [断言 + 异常机制 = 清晰简洁的代码 (yuque.com)](https://www.yuque.com/iohao/game/avlo99)
- [解决协议碎片 (yuque.com)](https://www.yuque.com/iohao/game/ieimzn)

下面是生成后的对接文档预览

```text
==================== 游戏文档格式说明 ====================
https://www.yuque.com/iohao/game/irth38#cJLdC

==================== FightHallAction 大厅（类似地图） ====================
路由: 1 - 1  --- 【登录】 --- 【FightHallAction:67】【loginVerify】
    方法参数: LoginVerify loginVerify 登录验证
    方法返回值: UserInfo 玩家信息
 
路由: 1 - 2  --- 【进入大厅】 --- 【FightHallAction:95】【enterSquare】
    方法参数: EnterSquare enterSquare 进入大厅
    方法返回值: ByteValueList<SquarePlayer> 所有玩家
    广播推送: SquarePlayer 新玩家加入房间，给房间内的其他玩家广播
 
路由: 1 - 4  --- 【玩家移动】 --- 【FightHallAction:131】【move】
    方法参数: SquarePlayerMove squarePlayerMove 玩家移动
    方法返回值: void 
    广播推送: SquarePlayerMove 其他玩家的移动
 
路由: 1 - 5  --- 【玩家下线】 --- 【FightHallAction:155】【offline】
    方法返回值: void 
    广播推送: LongValue 有玩家下线了。userId
 

==================== FightRoomAction  ====================
路由: 2 - 1  --- 【玩家创建新房间】 --- 【FightRoomAction:63】【createRoom】
    方法返回值: void 
 
路由: 2 - 2  --- 【玩家进入房间】 --- 【FightRoomAction:96】【enterRoom】
    方法参数: LongValue roomId      房间号
    方法返回值: void 房间信息
    广播推送: FightEnterRoom 玩家自己进入房间
 
路由: 2 - 3  --- 【玩家退出房间】 --- 【FightRoomAction:120】【quitRoom】
    方法返回值: void 
    广播推送: LongValue 有玩家退出房间了。userId
 
路由: 2 - 4  --- 【玩家准备】 --- 【FightRoomAction:146】【ready】
    方法参数: BoolValue ready       true 表示准备，false 则是取消准备
    方法返回值: void 
    广播推送: PlayerReady 有玩家准备或取消准备了
 
路由: 2 - 5  --- 【房间列表】 --- 【FightRoomAction:222】【listRoom】
    方法返回值: ByteValueList<FightRoomNotice> 房间列表
 
路由: 2 - 6  --- 【玩家在游戏中的操作】 --- 【FightRoomAction:191】【operation】
    方法参数: FightOperationCommand command     玩家操作数据
    方法返回值: void 
 
路由: 2 - 7  --- 【开始游戏】 --- 【FightRoomAction:162】【startGame】
    方法返回值: void 
 

==================== 其它广播推送 ====================
路由: 2 - 51  --- 广播推送: FightRoomNotice (房间更新通知)
路由: 2 - 50  --- 广播推送: FightPlayer (有新玩家加入房间)
路由: 2 - 52  --- 广播推送: IntValue (对局开始，通知玩家开始选择。round 当前对局数)
路由: 2 - 53  --- 广播推送: LongValue (通知其他玩家，有玩家做了选择。userId)
路由: 2 - 56  --- 广播推送: none (解散房间)
路由: 2 - 54  --- 广播推送: ByteValueList<FightRoundPlayerScore> (广播玩家对局分数)
路由: 2 - 55  --- 广播推送: none (游戏结束)
==================== 错误码 ====================
 -1008 : 绑定的游戏逻辑服不存在 
 -1007 : 强制玩家下线 
 -1006 : 数据不存在 
 -1005 : class 不存在 
 -1004 : 请先登录 
 -1003 : 心跳超时相关 
 -1002 : 路由错误 
 -1001 : 参数验错误 
 -1000 : 系统其它错误 
 1 : 玩家在房间里 
 3 : 房间不存在 
 4 : 非法操作 
 6 : 开始游戏需要的最小人数不足 
 7 : 请等待其他玩家准备 
 8 : 房间空间不足，人数已满 

```

---



**[light-game-room]**

room 模块相关文档 - [room 桌游、房间类 (yuque.com)](https://www.yuque.com/iohao/game/vtzbih)

[#278](https://github.com/iohao/ioGame/issues/278) 桌游类、房间类游戏的扩展模块，简化与规范化房间管理相关的、开始游戏流程相关的、玩法操作相关的相关扩展



light-game-room 房间，是 ioGame 提供的一个轻量小部件 - 可按需选择的模块。

> **light-game-room + 领域事件 + 内置 Kit  = 轻松搞定桌游类游戏**



该模块是桌游类、房间类游戏的解决方案。比较适合桌游类、房间类的游戏基础搭建，基于该模型可以做一些如，炉石传说、三国杀、斗地主、麻将 ...等类似的桌游。或者说只要是房间类的游戏，该模型都适用。比如，CS、泡泡堂、飞行棋、坦克大战 ...等。



如果你计划做一些桌游类的游戏，那么推荐你基于该模块做扩展。该模块遵循面向对象的设计原则，没有强耦合，可扩展性强。且帮助开发者屏蔽了很多重复性的工作，并可为项目中的功能模块结构、开发流程等进行清晰的组织定义，减少了后续的项目维护成本。



**主要解决的问题与职责**

桌游、房间类的游戏在功能职责上可以分为 3 大类，分别是

1. **房间管理相关的**
   1. 管理着所有的房间、查询房间列表、房间的添加、房间的删除、房间与玩家之间的关联、房间查找（通过 roomId 查找、通过 userId 查找）。
1. **开始游戏流程相关的**
   1. 通常桌游、房间类的游戏都有一些固定的流程，如创建房间、玩家进入房间、玩家退出房间、解散房间、玩家准备、开始游戏 ...等。
   2. 开始游戏时，需要做开始前的验证，如房间内的玩家是否符足够 ...等，当一切符合业务时，才是真正的开始游戏。
1. **玩法操作相关的**
   1. 游戏开始后，由于不同游戏之间的具体操作是不相同的。如坦克的射击，炉石的战前选牌、出牌，麻将的吃、碰、杠、过、胡，回合制游戏的普攻、防御、技能 ...等。
   2. 由于玩法操作的不同，所以我们的玩法操作需要是可扩展的，并用于处理具体的玩法操作。同时这种扩展方式更符合单一职责，使得我们后续的扩展与维护成本更低。

以上功能职责（房间管理相关、流程相关、玩法操作相关）属于相对通用的功能。如果每款游戏都重复的做这些工作，除了枯燥之外，还将浪费巨大的人力成本。

而当前模块则能很好的帮助开发者屏蔽这些重复性的工作，并可为项目中的功能模块结构、开发流程等进行清晰的组织定义，减少了后续的项目维护成本。更重要的是有相关文档，将来当你的团队有新进成员时，可以快速的上手。

---



**room 实战简介**

[room 桌游、房间类实战(yuque.com)](https://www.yuque.com/iohao/game/vtzbih#JX2i1)

文档中，我们基于该 room 模块做一个实战示例，该示例整体比较简单，多名玩家在房间里猜拳（石头、剪刀、布）得分。实战示例包括了前后端，前端使用 [FXGL](https://github.com/almasB/FXGL) 引擎，这样开发者在学习时，只需 JDK 环境就可以了，而不需要安装更多的环境。启动游戏后玩家会将加入大厅（类似地图），多名玩家相互可见，并且玩家可以在大厅内移动。



---



[示例集合整理] 

将 SimpleExample（文档中所有功能点的示例）、SpringBootExample（[综合示例](https://www.yuque.com/iohao/game/ruaqza)）、ioGameWeb2Game（[web 转游戏 - 示例理解篇](https://www.yuque.com/iohao/game/gpzmc8vadn4vl70z)）、fxglSimpleGame（[移动同步](https://www.yuque.com/iohao/game/bolt) FXGL + netty）合并成一个示例项目。



| github                                                     | gitee                                                     |
| ---------------------------------------------------------- | --------------------------------------------------------- |
| [ioGame 示例集合](https://github.com/iohao/ioGameExamples) | [ioGame 示例集合](https://gitee.com/iohao/ioGameExamples) |



<br>

#### 2024-05-11 - v21.7

https://github.com/iohao/ioGame/releases/tag/21.7



**版本更新汇总**

1. [core]  [#112](https://github.com/iohao/ioGame/issues/112) protobuf 协议类添加检测，通过 action 构建时的监听器实现
2. [core]  [#272](https://github.com/iohao/ioGame/issues/272) 业务框架 - 提供 action 构建时的监听回调
3. [core]  [#274](https://github.com/iohao/ioGame/issues/274) 优化、提速 - 预生成 jprotobuf 协议类的代理，通过 action 构建时的监听器实现
4. [broker] fix [#277](https://github.com/iohao/ioGame/issues/277) 、[#280](https://github.com/iohao/ioGame/issues/280) 偶现 BrokerClientType 为空
5. [external]  [#271](https://github.com/iohao/ioGame/issues/271) 游戏对外服 - 内置与可选 handler - log 相关的打印（触发异常、断开连接时）
6. [room] 简化命名:  AbstractPlayer --> Player、AbstractRoom --> Room
7. 其他优化：预先生成游戏对外服统一协议的代理类及内置的[协议碎片 (yuque.com)](https://www.yuque.com/iohao/game/ieimzn)相关代理类，优化 action 参数解析



**[external]**

[#271](https://github.com/iohao/ioGame/issues/271) 游戏对外服 - 内置与可选 handler - log 相关的打印（触发异常、断开连接时）

其他参考 [内置与可选的 Handler (yuque.com)](https://www.yuque.com/iohao/game/gqvf6cooowpo0ukp)



**[core]**

[#272](https://github.com/iohao/ioGame/issues/272) 业务框架 - 提供 action 构建时的监听回调

开发者可以利用 ActionParserListener 接口来观察 action 构建过程，或者做一些额外的扩展。



扩展示例参考

```java
// 简单打印
public final class YourActionParserListener implements ActionParserListener {
    @Override
    public void onActionCommand(ActionParserContext context) {
        ActionCommand actionCommand = context.getActionCommand();
        log.info(actionCommand);
    }
}

void test() {
    BarSkeletonBuilder builder = ...;
    builder.addActionParserListener(new YourActionParserListener());
}
```



[#112](https://github.com/iohao/ioGame/issues/112) protobuf 协议类添加检测，通过 action 构建时的监听器实现

如果当前使用的编解码器为 ProtoDataCodec 时，当 action 的参数或返回值的类没有添加 ProtobufClass 注解时（通常是忘记添加），给予一些警告提示。

```java
// 该协议类没有添加 ProtobufClass 注解
class Bird {
    public String name;
}

@ActionController(1)
public class MyAction {
    @ActionMethod(1)
    public Bird testObject() {
        return new Bird();
    }
}
======== 注意，协议类没有添加 ProtobufClass 注解 ========
class com.iohao.game.action.skeleton.core.action.Bird
```



[#274](https://github.com/iohao/ioGame/issues/274) 优化、提速 - 预生成 jprotobuf 协议类的代理，通过 action 构建时的监听器实现

如果当前使用的编解码器为 ProtoDataCodec 时，会在启动时就预先生成好 jprotobuf 协议类对应的代理类（用于 .proto 相关的 编码、解码），而不必等到用时在创建该代理类。从而达到整体优化提速的效果。

 

在此之前，在没做其他设置的情况下，首次访问 action 时，如果参数使用的 jprotobuf 协议类，那么在解码该参数时，会通过 `ProtobufProxy.create` 来创建对应的代理类（类似 .proto 相关的 编码、解码）。之后再访问时，才会从缓存中取到对应的代理类。

 

该优化默认开启，开发者可以不需要使用与配置跟 jprotobuf-precompile-plugin 插件相关的了。



已经预先生成的代理类有

- 游戏对外服[统一协议 ExternalMessage (yuque.com)](https://www.yuque.com/iohao/game/xeokui)
- 所有开发者定义的 action 的方法参数及返回值
- [解决协议碎片 (yuque.com)](https://www.yuque.com/iohao/game/ieimzn)相关，如 int、int list、String、String list、long、long list、ByteValueList ...等



**[room]**
简化命名:  AbstractPlayer --> Player、AbstractRoom --> Room



**其他优化**

优化 action 参数解析

<br>

### 2024-04

#### 2024-04-23 - v21.6

https://github.com/iohao/ioGame/releases/tag/21.6



**版本更新汇总**

1. [#264](https://github.com/iohao/ioGame/issues/264) 新增属性值变更监听特性
2. 模拟客户端新增与服务器断开连接的方法。模拟客户端新增是否活跃的状态属性。
3. [#265](https://github.com/iohao/ioGame/issues/265) 从游戏对外服中获取玩家相关数据 - 模拟玩家请求。
4. 任务相关：TaskListener 接口增加异常回调方法，用于接收异常信息；当 triggerUpdate 或 onUpdate 方法抛出异常时，将会传递到该回调方法中。
5. [#266](https://github.com/iohao/ioGame/issues/266) 新增 RangeBroadcast 范围内的广播功能，这个范围指的是，可指定某些用户进行广播。
6. AbstractRoom 增加 ifPlayerExist、ifPlayerNotExist 方法。

------

**属性监听特性**

[#264](https://github.com/iohao/ioGame/issues/264) 新增属性值变更监听特性

文档 : [属性监听 (yuque.com)](https://www.yuque.com/iohao/game/uqn84q41f58xe5f0)

属性可添加监听器，当某些属性值的发生变化时，触发监听器。



**使用场景举例**

比如玩家的血量低于一定值时，需要触发无敌状态；此时，我们就可以监听玩家的血量，并在该属性上添加一个对应的监听器来观察血量的变化，当达到预期值时就触发对应的业务。

 

类似的使用场景还有很多，这里就不过多的举例了。属性监听的特点在于属性变化后会触发监听器。



**属性监听特点**

- 可为属性添加监听器，用于观察属性值的变化。
- 属性可以添加多个监听器。
- 属性的监听器可以移除。



**框架已经内置了几个属性实现类，分别是：**

- IntegerProperty
- LongProperty
- StringProperty
- BooleanProperty
- ObjectProperty

------

for example - 添加监听器

BooleanProperty

当 BooleanProperty 对象的值发生改变时，触发监听器。

```java
var property = new BooleanProperty();
// 添加一个监听器。
property.addListener((observable, oldValue, newValue) -> {
   log.info("oldValue:{}, newValue:{}", oldValue, newValue);
});

property.get(); // value is false
property.set(true); // 值变更时，将会触发监听器
property.get(); // value is true
```



IntegerProperty

当 IntegerProperty 对象的值发生改变时，触发监听器。

```java
var property = new IntegerProperty();
// add listener monitor property object
property.addListener((observable, oldValue, newValue) -> {
   log.info("oldValue:{}, newValue:{}", oldValue, newValue);
});

property.get(); // value is 0
property.set(22); // When the value changes,listeners are triggered
property.get(); // value is 22

property.increment(); // value is 23. will trigger listeners
```

------

for example - 移除监听器

下面这个示例，我们将 property 初始值设置为 10，随后添加了一个监听器；当监听器观察到新值为 9 时，就从 observable 中移除自己（这个自己指的是监听器本身），而 observable 则是 IntegerProperty。

```java
@Test
public void remove1() {
    IntegerProperty property = new IntegerProperty(10);
    // 添加一个监听器
    property.addListener(new PropertyChangeListener<>() {
        @Override
        public void changed(PropertyValueObservable<? extends Number> observable, Number oldValue, Number newValue) {
            log.info("1 - newValue : {}", newValue);

            if (newValue.intValue() == 9) {
                // 移除当前监听器
                observable.removeListener(this);
            }
        }
    });

    property.decrement(); // value 是 9，并触发监听器
    property.decrement(); // value 是 8，由于监听器已经移除，所以不会触发任何事件。
}
```



下面的示例中，我们定义了一个监听器类 OnePropertyChangeListener 并实现了 PropertyChangeListener 监听器接口。示例中，我们通过 OnePropertyChangeListener 对象的引用来移除监听器。

```java
@Test
public void remove2() {
    // 监听器
    OnePropertyChangeListener onePropertyChangeListener = new OnePropertyChangeListener();
    
    // 属性
    IntegerProperty property = new IntegerProperty();
    // 添加监听器
    property.addListener(onePropertyChangeListener);

    property.increment(); // value == 1，并触发监听器
    property.removeListener(onePropertyChangeListener); // 移除监听器
    property.increment(); // value == 2，由于监听器已经移除，所以不会触发任何事件。
}

// 自定义的监听器
class OnePropertyChangeListener implements PropertyChangeListener<Number> {
    @Override
    public void changed(PropertyValueObservable<? extends Number> observable, Number oldValue, Number newValue) {
        log.info("oldValue:{}, newValue:{}, observable:{}", oldValue, newValue, observable);
    }
}
```

------

**属性监听 - 小结**

属性监听在使用上是简单的，如果你的业务中**有关于属性变化后需要触发某些事件的**，可以考虑引用该特性。框架为 int、long、boolean、Object、String 等基础类型提供了对应的属性监听。



属性监听特性支持添加多个监听器，支持移除监听器。

------



**模拟客户端相关**

- 模拟客户端新增与服务器断开连接的方法。
- 模拟客户端新增是否活跃的状态属性。

```java
ClientUser clientUser = ...;
// 是否活跃，true 表示玩家活跃
clientUser.isActive();
// 关闭模拟客户端连接
clientUser.getClientUserChannel().closeChannel();
```

------



**获取游戏对外服的数据与扩展相关**

文档 [获取游戏对外服的数据与扩展 (yuque.com)](https://www.yuque.com/iohao/game/ivxsw5)

RequestCollectExternalMessage 增加 userId 字段。



[#265](https://github.com/iohao/ioGame/issues/265) 模拟玩家请求时 - 从游戏对外服中获取在线玩家相关数据

新增  UserHeadMetadataExternalBizRegion，从用户（玩家）所在游戏对外服中获取用户自身的数据，如用户所绑定的游戏逻辑服、元信息 ...等

```java
@Slf4j
@RestController
@RequestMapping("other")
public class OtherController {
    static final AtomicLong msgId = GameManagerController.msgId;
    /** 为了方便测试，这里指定一个 userId 来模拟玩家 */
    static final long userId = GameManagerController.userId;

    @GetMapping("/notice")
    public String notice() {
        log.info("other notice");
        // 使用协议碎片特性 https://www.yuque.com/iohao/game/ieimzn
        StringValue data = StringValue.of("other GM web msg " + msgId.incrementAndGet());
        // 模拟请求 : 路由 - 业务数据
        RequestMessage requestMessage = BarMessageKit.createRequestMessage(ExchangeCmd.of(ExchangeCmd.notice), data);

        // 设置需要模拟的玩家
        HeadMetadata headMetadata = requestMessage.getHeadMetadata();
        headMetadata.setUserId(userId);

        // 从游戏对外服中获取一些用户（玩家的）自身的数据，如元信息、所绑定的游戏逻辑服 ...等
        Optional<HeadMetadata> headMetadataOptional = ExternalCommunicationKit.employHeadMetadata(requestMessage);

        if (headMetadataOptional.isPresent()) {
            // 发起模拟请求
            extractedRequestLogic(requestMessage);

            // 打印从游戏对外服获取的元信息
            byte[] attachmentData = headMetadata.getAttachmentData();
            ExchangeAttachment attachment = DataCodecKit.decode(attachmentData, ExchangeAttachment.class);
            return "other notice 玩家的元信息: %s - %s".formatted(attachment, msgId.get());
        } else {
            return "other notice 玩家 %s 不在线，无法获取玩家的元信息 - %s".formatted(userId, msgId.get());
        }
    }

    private void extractedRequestLogic(RequestMessage requestMessage) {
        // 向逻辑服发送请求，该模拟请求具备了玩家的元信息。
        BrokerClient brokerClient = MyKit.brokerClient;
        InvokeModuleContext invokeModuleContext = brokerClient.getInvokeModuleContext();
        invokeModuleContext.invokeModuleVoidMessage(requestMessage);
    }
}
```

------



**任务工具相关**

TaskListener 接口增加异常回调方法 `void onException(Throwable e)`，用于接收异常信息；当 triggerUpdate 或 onUpdate 方法抛出异常时，将会传递到该回调方法中。

```java
@Test
public void testException() throws InterruptedException {
    AtomicBoolean hasEx = new AtomicBoolean(false);
    TaskKit.runOnce(new OnceTaskListener() {
        @Override
        public void onUpdate() {
            // 模拟一个业务异常
            throw new RuntimeException("hello exception");
        }

        @Override
        public void onException(Throwable e) {
            hasEx.set(true);
            // 触发异常后，将来到这里
            log.error(e.getMessage(), e);
        }
    }, 10, TimeUnit.MILLISECONDS);

    TimeUnit.MILLISECONDS.sleep(200);
    Assert.assertTrue(hasEx.get()); // true
}
```

------



**业务框架相关 - [common-core]**

[#266](https://github.com/iohao/ioGame/issues/266) 新增 RangeBroadcast 范围内的广播功能，这个范围指的是，可指定某些用户进行广播。



在执行广播前，开发者可以自定义业务逻辑，如

- 添加一些需要广播的用户
- 删除一些不需要接收广播的用户
- 可通过重写 logic、trick 方法来做一些额外扩展

```java
// example - 1
new RangeBroadcast(flowContext)
        // 需要广播的数据
        .setResponseMessage(responseMessage)
        // 添加需要接收广播的用户
        .addUserId(1)
        .addUserId(2)
        .addUserId(List.of(3L, 4L, 5L))
        // 排除一些用户，被排除的用户将不会接收到广播
        .removeUserId(1)
        // 执行广播
        .execute();

// example - 2
new RangeBroadcast(flowContext)
        // 需要广播的数据
        .setResponseMessage(cmdInfo, playerReady)
        // 添加需要接收广播的用户
        .addUserId(1)
        // 执行广播
        .execute();
```

------

**[light-game-room] 房间模块**

- 移除 AbstractRoom broadcast 系列方法，开发者可使用 RoomBroadcastFlowContext 接口实现旧的兼容。
- 移除 AbstractRoom createSend 方法，开发者可使用 ofRangeBroadcast 系列来代替。AbstractRoom 新增 RoomBroadcastEnhance，实现房间内的广播增强，该系列在语义上更清晰。



```java
final RoomService roomService = ...;

@ActionMethod(RoomCmd.ready)
public void ready(boolean ready, FlowContext flowContext) {
    long userId = flowContext.getUserId();
    // 得到玩家所在的房间
    AbstractRoom room = this.roomService.getRoomByUserId(userId);
    
    // 准备
    PlayerReady playerReady = new PlayerReady();
    playerReady.userId = userId;
    playerReady.ready = ready;
  
    // 通知房间内的所有玩家，有玩家准备或取消准备了
    room.ofRangeBroadcast(flowContext)
            // 响应数据（路由、业务数据）
            .setResponseMessage(flowContext.getCmdInfo(), playerReady)
            .execute();
}

// 准备或取消准备
@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
public class PlayerReady {
    /** 当前操作的玩家 userId */
    long userId;
    /** true 表示准备 */
    boolean ready;
}
```

------



**AbstractRoom 增加 ifPlayerExist、ifPlayerNotExist 方法。**

**ifPlayerExist 方法**

如果玩家在房间内，就执行给定的操作，否则不执行任何操作。

```java
RoomService roomService = ...;
AbstractRoom room = ...;
// 如果玩家不在房间内，就创建一个玩家，并让玩家加入房间
room.ifPlayerNotExist(userId, () -> {
    // 玩家加入房间
    FightPlayerEntity newPlayer = new FightPlayerEntity();
    newPlayer.setId(userId);
    
    this.roomService.addPlayer(room, newPlayer);
});
```



**ifPlayerNotExist 方法**

如果玩家不在房间内，就执行给定的操作，否则不执行任何操作。

```java
AbstractRoom room = ...;
// 有新玩家加入房间，通知其他玩家
room.ifPlayerExist(userId, (FightPlayerEntity playerEntity) -> {
    FightPlayer fightPlayer = FightMapstruct.ME.convert(playerEntity);
    room.ofRangeBroadcast(flowContext)
            .setResponseMessage(RoomCmd.of(RoomCmd.playerEnterRoomBroadcast), fightPlayer)
            // 排除不需要通知的玩家（当前 userId 是自己）
            .removeUserId(userId)
            .execute();
});
```

<br>

#### 2024-04-16 - v21.5

https://github.com/iohao/ioGame/releases/tag/21.5



1.  增强 ClassScanner 类 
2.  优化模拟客户端 
3.  [#258](about:blank) 文档生成，兼容 gradle 编译路径 
4.  enhance jprotobuf，临时解决打包后不能在 linux java21 环境运行的问题，see [java21，springBoot3.2 打 jar 后使用异常 · Issue #211 · jhunters/jprotobuf (github.com)](https://github.com/jhunters/jprotobuf/issues/211) 
5.  生成 .proto 时，在最后打印文件路径 

1. [#255](https://github.com/iohao/ioGame/issues/255) 关于 Proto 生成排除属性问题

```java
/**
 * 动物
 */
@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
public class Animal {
    /** id */
    int id;
    /** 动物类型 - 枚举测试 */
    AnimalType animalType;
  	/** 年龄 - 忽略的属性*/
    @Ignore
    String age;
}
```



生成后的 .proto

```protobuf
// 动物
message Animal {
  // id
  int32 id = 1;
  // 动物类型 - 枚举测试
  AnimalType animalType = 2;
}
```

<br>

### 2024-03

#### 2024-03-28 - v21.4

https://github.com/iohao/ioGame/releases/tag/21.4




[ #253](https://github.com/iohao/ioGame/issues/253)

> CreateRoomInfo.createUserId int --> long



ExecutorRegion

> 1. 优化默认创建策略
> 2. 优化 ExecutorRegionKit，SimpleThreadExecutorRegion 默认使用全局单例，减少对象的创建。



proto 文档生成时，默认指定为 StandardCharsets.UTF_8

>  javaProjectBuilder.setEncoding(StandardCharsets.UTF_8.name());



玩家下线时，使用自身所关联的线程处理。

>  SocketUserSessions removeUserSession

```java
public void removeUserSession(SocketUserSession userSession) {

    if (Objects.isNull(userSession)) {
        return;
    }

    long userId = userSession.getUserId();
    ExecutorRegionKit.getSimpleThreadExecutor(userId)
            .executeTry(() -> internalRemoveUserSession(userSession));
}
```

<br>

#### 2024-03-11 - v21.3

https://github.com/game-town/ioGame/releases/tag/21.3



[#250](https://github.com/game-town/ioGame/issues/250) 游戏对外服 - 自定义编解码 - WebSocketMicroBootstrapFlow



文档 [游戏对外服-自定义编解码 (yuque.com)](https://www.yuque.com/iohao/game/ea6geg#Z8pbL)



重写 WebSocketMicroBootstrapFlow createExternalCodec 方法，用于创建开发者自定义的编解码，其他配置则使用 pipelineCodec 中的默认配置。

```java
DefaultExternalServerBuilder builder = ...;

builder.setting().setMicroBootstrapFlow(new WebSocketMicroBootstrapFlow() {
    @Override
    protected MessageToMessageCodec<BinaryWebSocketFrame, BarMessage> createExternalCodec() {
        // 开发者自定义的编解码实现类。
        return new YourWsExternalCodec();
    }
});
```



以下展示的是 WebSocketMicroBootstrapFlow pipelineCodec 相关代码

```java
public class WebSocketMicroBootstrapFlow extends SocketMicroBootstrapFlow {
    ... 省略部分代码
    @Override
    public void pipelineCodec(PipelineContext context) {
        // 添加 http 相关 handler
        this.httpHandler(context);

        // 建立连接前的验证 handler
        this.verifyHandler(context);

        // 添加 websocket 相关 handler
        this.websocketHandler(context);

        // websocket 编解码
        var externalCodec = this.createExternalCodec();
        context.addLast("codec", externalCodec);
    }

    @Override
    protected MessageToMessageCodec<BinaryWebSocketFrame, BarMessage> createExternalCodec() {
        // createExternalCodec 相当于一个钩子方法。
        return new WebSocketExternalCodec();
    }
};
```



[#249](https://github.com/game-town/ioGame/issues/249)
将集群启动顺序放到 Broker（游戏网关）之后。

集群增减和逻辑服 Connect 增减使用同一线程处理。

IoGameGlobalConfig brokerClusterLog 集群相关日志不开启。

<br>

### 2024-02

#### 2024-02-22 - v21.2

修复版本号显示错误问题（该版本没有功能上的更新与修改，不升级也不影响）

<br>

####  2024-02-21 - v21.1

https://github.com/game-town/ioGame/releases/tag/21.1



ioGame21 首发计划

| 功能支持                                                     | 完成 | 描述                   | issu                                                         |
| ------------------------------------------------------------ | ---- | ---------------------- | ------------------------------------------------------------ |
| [游戏对外服开放自定义协议](https://www.yuque.com/iohao/game/xeokui) | ✅    | 功能增强               | [#213](https://github.com/game-town/ioGame/issues/213)       |
| [游戏对外服缓存](https://www.yuque.com/iohao/game/khg23pvbh59a7spm) | ✅    | 功能增强、性能提升     | [#76](https://github.com/game-town/ioGame/issues/76)         |
| [FlowContext](https://www.yuque.com/iohao/game/zz8xiz#YQOZ5) 增加通信能力，提供同步、异步、异步回调的便捷使用 | ✅    | 功能增强               | [#235](https://github.com/game-town/ioGame/issues/235)       |
| 虚拟线程支持;  各逻辑服之间通信阻塞部分，改为使用虚拟线程，避免阻塞业务线程 | ✅    | 功能增强、性能提升     |                                                              |
| 默认不使用 bolt 线程池，减少上下文切换。  ioGame17：netty --> bolt 线程池 --> ioGame 线程池。  ioGame21： 1. netty --> ioGame 线程池。 2. 部分业务将直接在 netty 线程中消费业务。文档 - [ioGame 线程相关](https://www.yuque.com/iohao/game/eixd6x) | ✅    | 性能提升               |                                                              |
| [全链路调用日志跟踪](https://www.yuque.com/iohao/game/zurusq)；日志增强 traceId | ✅    | 功能增强               | [#230](https://github.com/game-town/ioGame/issues/230)       |
| [文档自动生成](https://www.yuque.com/iohao/game/irth38)，改为由开发者调用触发。 | ✅    | 整理                   |                                                              |
| 移除过期代码                                                 | ✅    | 整理                   | [#237](https://github.com/game-town/ioGame/issues/239)       |
| [分布式事件总线](https://www.yuque.com/iohao/game/gmxz33)  可以代替 redis pub sub 、 MQ ，并且具备全链路调用日志跟踪，这点是中间件产品做不到的。 | ✅    | 功能增强               | [#228](https://github.com/game-town/ioGame/issues/228)       |
| 日志库使用新版本 slf4j 2.0                                   | ✅    |                        |                                                              |
| [Fury](https://fury.apache.org/) 支持。  Fury 是一个基于JIT动态编译和零拷贝的高性能多语言序列化框架 |      | 在计划内，不一定会支持 | 因在发布 ioGame21 时，Fury 还未发布稳定版本，所以这里暂不支持。 |
| [心跳响应前的回调](https://www.yuque.com/iohao/game/lxqbnb#bJ6T8) | ✅    | 功能增强               | [#234](https://github.com/game-town/ioGame/issues/234)       |
| [FlowContext](https://www.yuque.com/iohao/game/zz8xiz#HQYmm) 增加更新、获取元信息的便捷使用 | ✅    | 功能增强               | [#236](https://github.com/game-town/ioGame/issues/236)       |



### 2024

#### ioGame21 首发内容简介

在 ioGame21 中，该版本做了数百项优化及史诗级增强。

- 文档方面
- 线程管理域方面的开放与统一、减少线程池上下文切换
- FlowContext 得到了**史诗级**的增强。
- 新增通讯方式 - 分布式事件总线
- 游戏对外服方面增强
- 全链路调用日志跟踪
- 各逻辑服之间通信阻塞部分，改为使用虚拟线程, 避免阻塞业务线程，从而使得框架的吞吐量得到了巨大的提升。



##### 游戏对外服相关

[#76](https://github.com/game-town/ioGame/issues/76) 游戏对外服缓存 

更多的介绍，请阅读[游戏对外服缓存](https://www.yuque.com/iohao/game/khg23pvbh59a7spm)文档。



游戏对外服缓存，可以将一些热点的业务数据缓存在游戏对外服中，玩家每次访问相关路由时，会直接从游戏对外服的内存中取数据。这样可以避免反复请求游戏逻辑服，从而达到性能的超级提升；

```java
private static void extractedExternalCache() {
    // 框架内置的缓存实现类
    DefaultExternalCmdCache externalCmdCache = new DefaultExternalCmdCache();
    // 添加到配置中
    ExternalGlobalConfig.externalCmdCache = externalCmdCache;
    // 配置缓存 3-1
    externalCmdCache.addCmd(3, 1);
}
```



[#213](https://github.com/game-town/ioGame/issues/213) 游戏对外服开放自定义协议 

更多的介绍，请阅读[游戏对外服的协议说明](https://www.yuque.com/iohao/game/xeokui)文档。

开发者可自定义游戏对外服协议，用于代替框架默认的 ExternalMessage 公共对外协议。



[#234](https://github.com/game-town/ioGame/issues/234) 心跳响应前的回调 

更多的介绍，请阅读[心跳设置与心跳钩子](https://www.yuque.com/iohao/game/uueq3i)文档。

在部分场景下，在响应心跳前可添加当前时间，使得客户端与服务器时间同步。

```java
@Slf4j
public class DemoIdleHook implements SocketIdleHook {
    ... ... 省略部分代码
    volatile byte[] timeBytes;

    public DemoIdleHook() {
        updateTime();
        // 每秒更新当前时间
        TaskKit.runInterval(this::updateTime, 1, TimeUnit.SECONDS);
    }

    private void updateTime() {
        LongValue data = LongValue.of(TimeKit.currentTimeMillis());
        // 避免重复序列化，这里提前序列化好时间数据
        timeBytes = DataCodecKit.encode(data);
    }

    @Override
    public void pongBefore(BarMessage idleMessage) {
        // 把当前时间戳给到心跳接收端
        idleMessage.setData(timeBytes);
    }
}
```



##### FlowContext - 跨服通信

[#235](https://github.com/game-town/ioGame/issues/235) FlowContext 增加通信能力，提供同步、异步、异步回调的便捷使用 

更多的介绍，请阅读 [FlowContext](https://www.yuque.com/iohao/game/zz8xiz) 文档。

```java
// 跨服请求 - 同步、异步回调演示
void invokeModuleMessage() {
    // 路由、请求参数
    ResponseMessage responseMessage = flowContext.invokeModuleMessage(cmdInfo, yourData);
    RoomNumMsg roomNumMsg = responseMessage.getData(RoomNumMsg.class);
    log.info("同步调用 : {}", roomNumMsg.roomCount);

    // --- 此回调写法，具备全链路调用日志跟踪 ---
    // 路由、请求参数、回调
    flowContext.invokeModuleMessageAsync(cmdInfo, yourData, responseMessage -> {
        RoomNumMsg roomNumMsg = responseMessage.getData(RoomNumMsg.class);
        log.info("异步回调 : {}", roomNumMsg.roomCount);
    });
}

// 广播
public void broadcast(FlowContext flowContext) {
    // 全服广播 - 路由、业务数据
    flowContext.broadcast(cmdInfo, yourData);

    // 广播消息给单个用户 - 路由、业务数据、userId
    long userId = 100;
    flowContext.broadcast(cmdInfo, yourData, userId);

    // 广播消息给指定用户列表 - 路由、业务数据、userIdList
    List<Long> userIdList = new ArrayList<>();
    userIdList.add(100L);
    userIdList.add(200L);
    flowContext.broadcast(cmdInfo, yourData, userIdList);

    // 给自己发送消息 - 路由、业务数据
    flowContext.broadcastMe(cmdInfo, yourData);

    // 给自己发送消息 - 业务数据
    // 路由则使用当前 action 的路由。
    flowContext.broadcastMe(yourData);
}
```



[#236](https://github.com/game-town/ioGame/issues/236) FlowContext 增加更新、获取元信息的便捷使用 

更多的介绍，请阅读 [FlowContext](https://www.yuque.com/iohao/game/zz8xiz) 文档。

```java
void test(MyFlowContext flowContext) {
    // 获取元信息
    MyAttachment attachment = flowContext.getAttachment();
    attachment.nickname = "渔民小镇";

    // [同步]更新 - 将元信息同步到玩家所在的游戏对外服中
    flowContext.updateAttachment();

    // [异步无阻塞]更新 - 将元信息同步到玩家所在的游戏对外服中
	flowContext.updateAttachmentAsync();
}

public class MyFlowContext extends FlowContext {
    MyAttachment attachment;

    @Override
    @SuppressWarnings("unchecked")
    public MyAttachment getAttachment() {
        if (Objects.isNull(attachment)) {
            this.attachment = this.getAttachment(MyAttachment.class);
        }

        return this.attachment;
    }
}
```



##### 线程相关 - 无锁高并发

更多的介绍，请阅读 [ioGame 线程相关](https://www.yuque.com/iohao/game/eixd6x)文档。



虚拟线程支持，各逻辑服之间通信阻塞部分使用虚拟线程来处理，避免阻塞业务线程。



默认不使用 bolt 线程池，减少上下文切换。ioGame21 业务消费的线程相关内容如下：

1. netty --> ioGame 线程池。
2. 部分业务将直接在 netty 线程中消费业务。



在 ioGame21 中，框架内置了 3 个线程执行器管理域，分别是

1. UserThreadExecutorRegion ，用户线程执行器管理域。
2. UserVirtualThreadExecutorRegion ，用户虚拟线程执行器管理域。
3. SimpleThreadExecutorRegion ，简单的线程执行器管理域。



**从工具类中得到与用户（玩家）所关联的线程执行器**

```java
@Test
public void userThreadExecutor() {
    long userId = 1;

    ThreadExecutor userThreadExecutor = ExecutorRegionKit.getUserThreadExecutor(userId);

    userThreadExecutor.execute(() -> {
        // print 1
        log.info("userThreadExecutor : 1");
    });

    userThreadExecutor.execute(() -> {
        // print 2
        log.info("userThreadExecutor : 2");
    });
}

@Test
public void getUserVirtualThreadExecutor() {
    long userId = 1;

    ThreadExecutor userVirtualThreadExecutor = ExecutorRegionKit.getUserVirtualThreadExecutor(userId);

    userVirtualThreadExecutor.execute(() -> {
        // print 1
        log.info("userVirtualThreadExecutor : 1");
    });

    userVirtualThreadExecutor.execute(() -> {
        // print 2
        log.info("userVirtualThreadExecutor : 2");
    });
}

@Test
public void getSimpleThreadExecutor() {
    long userId = 1;

    ThreadExecutor simpleThreadExecutor = ExecutorRegionKit.getSimpleThreadExecutor(userId);

    simpleThreadExecutor.execute(() -> {
        // print 1
        log.info("simpleThreadExecutor : 1");
    });

    simpleThreadExecutor.execute(() -> {
        // print 2
        log.info("simpleThreadExecutor : 2");
    });
}
```



**从 FlowContext 中得到与用户（玩家）所关联的线程执行器**

```java
void executor() {
    // 该方法具备全链路调用日志跟踪
    flowContext.execute(() -> {
        log.info("用户线程执行器");
    });

    // 正常提交任务到用户线程执行器中
    // getExecutor() 用户线程执行器
    flowContext.getExecutor().execute(() -> {
        log.info("用户线程执行器");
    });
}

void executeVirtual() {
    // 该方法具备全链路调用日志跟踪
    flowContext.executeVirtual(() -> {
        log.info("用户虚拟线程执行器");
    });

    // 正常提交任务到用户虚拟线程执行器中
    // getVirtualExecutor() 用户虚拟线程执行器
    flowContext.getVirtualExecutor().execute(() -> {
        log.info("用户虚拟线程执行器");
    });

    // 示例演示 - 更新元信息（可以使用虚拟线程执行完成一些耗时的操作）
    flowContext.executeVirtual(() -> {
        log.info("用户虚拟线程执行器");
        
        // 更新元信息
        flowContext.updateAttachment();
        
        // ... ... 其他业务逻辑
    });
}
```



##### 日志相关

日志库使用新版本 slf4j 2.x



[#230](https://github.com/game-town/ioGame/issues/230) 支持全链路调用日志跟踪；

更多的介绍，请阅读[全链路调用日志跟踪](https://www.yuque.com/iohao/game/zurusq)文档。



**开启 traceId 特性**

该配置需要在游戏对外服中设置，因为游戏对外服是玩家请求的入口。

```java
// true 表示开启 traceId 特性
IoGameGlobalConfig.openTraceId = true;
```



将[全链路调用日志跟踪插件](https://www.yuque.com/iohao/game/xhvpqy) TraceIdInOut 添加到业务框架中，表示该游戏逻辑服需要支持全链路调用日志跟踪。如果游戏逻辑服没有添加该插件的，表示不需要记录日志跟踪。

```java
BarSkeletonBuilder builder = ...;
// traceId
TraceIdInOut traceIdInOut = new TraceIdInOut();
builder.addInOut(traceIdInOut);
```



##### 分布式事件总线 - 跨服解耦

[#228](https://github.com/game-town/ioGame/issues/228) 分布式事件总线是新增的通讯方式，可以代替 redis pub sub 、 MQ ...等中间件产品；分布式事件总线具备全链路调用日志跟踪，这点是中间件产品所做不到的。



文档 - [分布式事件总线](https://www.yuque.com/iohao/game/gmxz33)



**ioGame 分布式事件总线，特点**

- 使用方式与 Guava EventBus 类似
- 具备**全链路调用日志跟踪**。（这点是中间件产品做不到的）
- 支持跨多个机器、多个进程通信
- 支持与多种不同类型的多个逻辑服通信
- 纯 javaSE，不依赖其他服务，耦合性低。（不需要安装任何中间件）
- 事件源和事件监听器之间通过事件进行通信，从而实现了模块之间的解耦
- 当没有任何远程订阅者时，**将不会触发网络请求**。（这点是中间件产品做不到的）



下面两个订阅者是分别在**不同的进程中**的，当事件发布后，这两个订阅者都能接收到 UserLoginEventMessage 消息。

```java
@ActionController(UserCmd.cmd)
public class UserAction {
	... 省略部分代码
    @ActionMethod(UserCmd.fireEvent)
    public String fireEventUser(FlowContext flowContext) {
        long userId = flowContext.getUserId();

        log.info("fire : {} ", userId);
        
        // 事件源
        var userLoginEventMessage = new UserLoginEventMessage(userId);
        // 发布事件
        flowContext.fire(userLoginEventMessage);

        return "fireEventUser";
    }
}

// 该订阅者在 【UserLogicStartup 逻辑服】进程中，与 UserAction 同在一个进程
@EventBusSubscriber
public class UserEventBusSubscriber {
    @EventSubscribe(ExecutorSelector.userExecutor)
    public void userLogin(UserLoginEventMessage message) {
        log.info("event - 玩家[{}]登录，记录登录时间", message.getUserId());
    }
}

// 该订阅者在 【EmailLogicStartup 逻辑服】进程中。
@EventBusSubscriber
public class EmailEventBusSubscriber {
    @EventSubscribe
    public void mail(UserLoginEventMessage message) {
        long userId = message.getUserId();
        log.info("event - 玩家[{}]登录，发放 email 奖励", userId);
    }
}
```



##### 小结

在 ioGame21 中，该版本做了数百项优化及史诗级增强。

- 在线文档方面
- 线程管理域方面的开放与统一、减少线程池上下文切换
- FlowContext 增强
- 新增通讯方式 - 分布式事件总线
- 游戏对外服方面增强
- 全链路调用日志跟踪



#### ioGame17 迁移到 ioGame21

文档：[17 迁移到 ioGame21](https://www.yuque.com/iohao/game/hcgsfobyoph9r74r) 



#### ioGame17 - 更新日志

see online [ioGame17 - 更新日志](https://www.yuque.com/iohao/game/ot5yruazqpe3uxre) 