# ioGame17 - 更新日志


## 2024
### 2024-01-03 - v17.1.61
https://github.com/iohao/ioGame/releases/tag/17.1.61

**版本更新汇总**
> 1. [#223](https://github.com/iohao/ioGame/issues/223) 一天内 action 各小时的调用统计插件

<hr/>

## 2023
### 2023-12-10 - v17.1.60
https://github.com/iohao/ioGame/releases/tag/17.1.60

**版本更新汇总**
> 1. [#227](https://github.com/iohao/ioGame/issues/227) 增加调度、定时器相关便捷工具，使用 HashedWheelTimer 来模拟 ScheduledExecutorService 调度
> 2. [#225](https://github.com/iohao/ioGame/issues/225) 新增插件 - 业务线程监控
> 3. 废弃 InternalKit，使用 TaskKit 代替
> 4. HeadMetadata 新增 cloneAll 方法
> 5. 加 ThreadExecutorRegion 线程执行器管理域接口

### 2023-11-20 - v17.1.59
https://github.com/iohao/ioGame/releases/tag/17.1.59

**版本更新汇总**
> 1. TImeKit 新加时间更新策略，开发者可设置时间更新策略。
> 2. RandomKit 功能增强，随机得到数组中的一个元素。
> 3. 移除 SocketUserSessionHandler exceptionCaught 的日志打印。
> 4. [#221](https://github.com/iohao/ioGame/issues/221) 新增 action 调用统计插件 StatisticActionInOut
> 5. FlowContext 增加 inOutStartTime 和 getInOutTime 方法，用于记录插件的开始执行时间和结束时所消耗的时间。

### 2023-11-02 - v17.1.58
https://github.com/iohao/ioGame/releases/tag/17.1.58

**版本更新汇总**
> 1. [#194 ](https://github.com/iohao/ioGame/issues/194) 可能在 SpringBoot 集成 light-domain-event 时，启动报 java.lang.ClassNotFoundException
> 2. [#198 ](https://github.com/iohao/ioGame/issues/198)

### 2023-09-06 - v17.1.55
https://github.com/iohao/ioGame/releases/tag/17.1.55

**版本更新汇总**
> 1. [#186](https://github.com/iohao/ioGame/issues/186) 增强 ProtoDataCodec

### 2023-08-18 - v17.1.54
https://github.com/iohao/ioGame/releases/tag/17.1.54

**版本更新汇总**
> 1. [#174](https://github.com/iohao/ioGame/pull/174) **fix action 交给容器管理时，实例化两次的问题**
> 2. 游戏对外服新增 HttpRealIpHandler，用于获取玩家真实 ip 支持
> 3. 压测&模拟客户端请求模块，新增模块名标识

```java
public class BagInputCommandRegion extends AbstractInputCommandRegion {
    @Override
    public void initInputCommand() {
        this.inputCommandCreate.cmd = BagCmd.cmd;
        this.inputCommandCreate.cmdName = "Bag";
    }
}
```

### 2023-08-07 - v17.1.52
https://github.com/iohao/ioGame/releases/tag/17.1.52

**版本更新汇总**
1. [#172](https://github.com/iohao/ioGame/issues/172) 新增 webSocket token 鉴权、校验支持
2. 移除 light-log 模块，统一使用 lombok slf4j 相关注解
3. 模拟客户端新增 SplitParam，方便模拟测试时，解析控制台输入参数的获取

```java
private void useRequest() {
    InputRequestData inputRequestData = () -> {
        ScannerKit.log(() -> log.info("输入需要使用的背包物品，格式 [背包物品id-数量]"));
        String inputType = ScannerKit.nextLine("1-1");

        SplitParam param = new SplitParam(inputType);
        // 得到下标 0 的值
        String id = param.getString(0);
        // 得到下标 1 的值，如果值不存在，则使用默认的 1 代替
        int quantity = param.getInt(1, 1);
    };
}
```

### 2023-08-01 - v17.1.50
https://github.com/iohao/ioGame/releases/tag/17.1.50

**版本更新汇总**
> 1. DebugInout 在自定义 FlowContext 时的打印优化。
> 2. CmdInfo 新增 of 系列方法，用于代替 getCmdInfo 系列方法
> 3. 异常机制接口 MsgExceptionInfo，新增方法 assertTrueThrows、assertNonNull

### 2023-07-28 - v17.1.48
> 1. 文档生成增强，增加 action 参数注释说明.
> 2. 文档生成增强，返回值注释说明.
> 3. fix 在 pom 中引入本地 jar 时，文档解析的错误。

### 2023-07-21 - v17.1.47
> 1. [#160](https://github.com/iohao/ioGame/issues/160) 增加压测&模拟客户端请求模块

### 2023-07-07 - v17.1.45
> 1. [#157](https://github.com/iohao/ioGame/issues/157) fix 默认心跳钩子问题
> 2. [#122](https://github.com/iohao/ioGame/issues/122) 同进程亲和性

### 2023-06-14 - v17.1.44
> 1. [#133](https://github.com/iohao/ioGame/issues/133) 向指定对外服上的用户广播数据
> 2. [#131](https://github.com/iohao/ioGame/issues/131) 获取指定对外服上数据的接口

### 2023-06-08 - v17.1.43
> 1. [#115](https://github.com/iohao/ioGame/issues/115) 游戏对外服增加路由是否存在检测
> 2. [#114](https://github.com/iohao/ioGame/issues/114) 支持玩家与多个游戏逻辑服的动态绑定
> 3. [#113](https://github.com/iohao/ioGame/issues/113) 新版本游戏对外服

### 2023-05-29 - v17.1.42
> 1. [#127](https://github.com/iohao/ioGame/issues/127) DebugInOut，可限制某些 action 不输出 log
> 2. [#132](https://github.com/iohao/ioGame/issues/132) 集群重启后组网异常

### 2023-05-09 - v17.1.40
> 1. [#99](https://github.com/iohao/ioGame/issues/99) 增加 msgId 特性，只在 request/response 通讯方式下生效
> 2. [#102](https://github.com/iohao/ioGame/issues/102) 业务框架 BarSkeleton 类增加动态属性，方便扩展.
> 3. [#111](https://github.com/iohao/ioGame/issues/111) 新增文档解析、文档生成的控制选项
> 4. [#103](https://github.com/iohao/ioGame/issues/103) 业务框架新增 Runner 机制，增强扩展性、规范性
> 5. [#104](https://github.com/iohao/ioGame/issues/104) 新增实验性特性-脉冲通讯方式

### 2023-04-18 - v17.1.38
> 1. [#46](https://github.com/iohao/ioGame/issues/46) action 业务参数与返回值增加 List 支持
> 2. [#74](https://github.com/iohao/ioGame/issues/74) action 返回值增加 byte[] 扩展
> 3. [#79](https://github.com/iohao/ioGame/issues/79) light-jprotobuf 模块支持枚举
> 4. [#84](https://github.com/iohao/ioGame/issues/84) 生成proto文件时,内容的顺序总是产生变化
> 5. 将 IoGameGlobalSetting.me() 方法标记为废弃的，将 IoGameGlobalSetting 内的方法改为静态的。

### 2023-04-03 - v17.1.37
> 1. [#77](https://github.com/iohao/ioGame/issues/77) RequestMessageClientProcessorHook 提供新的默认实现

### 2023-03-27 - v17.1.35
> 1. [#62](https://github.com/iohao/ioGame/issues/62) RequestMessageClientProcessor 中的 FlowContext 增加路由、响应对象、用户id 相关的属性设置。
> 2. 领域事件部分代码整理

### 2023-03-02 - v17.1.34
> 1. [#47](https://github.com/iohao/ioGame/issues/47) 增加拒绝外部直接访问 action 的路由权限
> 2. [#45](https://github.com/iohao/ioGame/issues/45) 游戏对外服独立 UserSession 管理部份逻辑，做成单独的 UserSessionHandler
> 3. [#54](https://github.com/iohao/ioGame/issues/54) 动态属性接口增加消费操作

### 2023-02-13 - v17.1.33
> 1. [#38](https://github.com/iohao/ioGame/issues/38) 新增业务参数自动装箱、拆箱基础类型-boolean
> 2. [#39](https://github.com/iohao/ioGame/issues/39) 新增业务参数自动装箱、拆箱"基础"类型-String
> 3. [#42](https://github.com/iohao/ioGame/issues/42) 新增业务参数自动装箱、拆箱基础类型-int
> 4. [#43](https://github.com/iohao/ioGame/issues/43) 新增业务参数自动装箱、拆箱基础类型-long
> 5. 修复 ExternalServerBuilder.channelPipelineHook 设置业务编排钩子接口被默认实现覆盖的问题

### 2023-02-03 - v17.1.31
> 1. [#34](https://github.com/iohao/ioGame/issues/34) 日志模块增加
> 2. [#36](https://github.com/iohao/ioGame/issues/36) 增加 Banner 打印版本、内存占用、启动耗时等信息。
> 3. [#37](https://github.com/iohao/ioGame/issues/37) 缩小打包后的包体大小，移除一些第三方库
> 4. MethodParsers 增加 action 参数解析器的默认设置
> 5. 业务参数自动装箱、拆箱基础类型增强
> 6. 修复广播的数据为空时，广播虽然是成功的，但是打印广播日志报错的问题


### 2023-01-14 - v17.1.29
> 1. 修复文档生成时的路径问题

<hr/>

## 2022

### 2022-12-29 - v17.1.28
> 1. 增加版本标识，并在 DebugInOut 插件中显示的打印。

### 2022-12-14 - v17.1.27
> 1. 命令解析器与源码文档逻辑分离。
> 2. 优化命令对象，减少 action 类的实例化对象
> 3. [#30](https://github.com/iohao/ioGame/issues/30) 简化元附加信息的使用

### 2022-12-06 - v17.1.26
> 1. [#27](https://github.com/iohao/ioGame/issues/27) 业务框架与网络通信框架解耦
> 2. [#28](https://github.com/iohao/ioGame/issues/28) 新增 ChannelPipelineHook netty 业务编排的处理器钩子接口，用于游戏对外服。

### 2022-11-29 - v17.1.25
> 1. ActionCommandInfoBuilder 改名为 ActionCommandParser 命令解析器
> 2. BarSkeletonBuilderParamConfig 类的方法名变更
> 3. 支持多种协议：protobuf、json，并支持可扩展

### 2022-11-14 - v17.1.23
> 1. BrokerServerBuilder 游戏网关构建器中增加移除 UserProcessor 的方法
> 2. 提供 UserProcessor 用户线程池设置策略。分离 IO 线程池与用户线程池，这样服务器可以在同一时间内处理更多的请求。
> 3. 修复动态绑定游戏逻辑服不能取消，不能路由到其他游戏逻辑服的问题
> 4. 废弃 BrokerGlobalConfig ，由 IoGameGlobalConfig 代替。


### 2022-10-31 - v17.1.21
> 1. 移除游戏网关的 Spring 依赖，之前使用了 Spring 的日志彩色打印，改为使用 logback 提供的。
> 2. 现在 ioGame 的 JSR 校验支持 Jakarta 和 Javax 两种，基于 java SPI 实现
> 3. SimpleRunOne 中 startup 优化
> 4. 框架提供 cmd 路由对应的响应数据类型信息，方便后续做"模拟客户端" 支持

### 2022-09-26 - v17.1.20
> 1. light-timer-task，将任务延时器的任务数量默认值 2_000 --> 10_000
> 2. 将 MsgException 修改为运行时异常；下面两个 action 的业务逻辑处理是等价的，其中一个显示的声明了 throws。
> 3. JSR380 新增参数分组校验，分组校验在 web 开发中很常见，现在 ioGame 也支持这一特性了。
> 4. 修复：light-profile getInt getBool 无法获取数据
> 5. 移除 HeadMetadata 类的 cmd、subCmd 属性，进一步减少传输，因为 cmd、subCmd 属性数据可以通过 cmdMerge 计算出来；

假设你的游戏请求量可以达到每秒 100W 次时，至少可为服务器每秒节省大约
8 * 1000000 / 1024 / 1024 = 7.63 MB 的传输量；
当请通讯方式是 请求/响应 类型时，每秒节省大约 7.63 * 2 = 15.26 MB;

### 2022-09-08 - v17.1.18
领域事件变更默认配置
DomainEventContextParam.producerType 由 ProducerType.SINGLE 改为 ProducerType.MULTI
DomainEventContextParam.waitStrategy 由 BlockingWaitStrategy 改为 LiteBlockingWaitStrategy

### 2022-08-29 - v17.1.17
> 1. 新增集群日志开关 BrokerGlobalConfig.brokerClusterLog，默认为 true
> 2. 增强顶号强制下线的问题，在发送强制离线时，需要先给个客户端发个错误码。
> 3. 修复 JSR380 验证相关缺少判断

### 2022-08-23 - v17.1.13
> 1. 游戏逻辑服与同类型逻辑服通信，超时问题。新增 SyncRequestMessageClientProcessor、SyncRequestMessage 来处理超业务逻辑时间比较长的业务。
> 2. 支持自定义 FlowContext

### 2022-08-16 - v17.1.10
> 1. 逻辑服务之间通信相关的扩展增强方式
> 2. 游戏网关的扩展增强 - 元素选择器生产工厂
> 3. 游戏对外服、动态绑定逻辑服节点相关

### 2022-08-08 - v17.1.9
> 1. UserHook quit方法中调用ExternalKit.requestGateway(userSession, requestMessage); 抛出异常
> 2. 断言抛异常没有带异常信息
> 3. 顶号强制下线时，发送一个错误码给连接端
> 4. 框架内置可选模块 light-jprotobuf

### 2022-07-28 - v17.1.4
> 1. 新增：【游戏逻辑服】访问多个【游戏对外服】的上下文。
> 2. 新增：元信息 - 附加信息 - 在 action 中得到附加数据
> 3. 新增：严格登录，路由访问权限的控制

### 2022-07-19 - v17.1.3
> 1. 增强：相同路由可以用在 action 与广播上；当 action 返回值为 void 时，可以复用路由来作为广播的响应路由。
> 2. 广播推送添加新成员，新增顺序特性成员 BroadcastOrderContext 可以确保消息是严格顺序的。

### 2022-07-11 - v17.1.2
> 1. InvokeModuleContext，新增无参请求方法，单个逻辑服与单个逻辑服通信请求 invokeModuleMessage
> 2. BarMessage.dataClass 字段，由 Class 类型改为 String类型
> 3. 新增综合示例
> 4. 移除 JSR303 相关，使用符合 JSR380 标准的校验。

### 2022-07-06 - v17.1.1
ioGame 上传到中央仓库中。

ioGame 版本规则 x.y.z
- x 表示当前使用的 JDK 版本
- y 表示 ioGame API 变更版本 （但基本上不会变动，常规下是变动 x 才会变动 API）
- z 表示 ioGame 新特性、新功能、新模块、bugfix 相关

ioGame 的 x 会跟着最新的 JDK LTS 版本来走的，目的是确保 ioGame 的 API 不会发生很大的变化。
为了保持新活力与接受新鲜事物， ioGame 基本会用上最新的 JDK LTS，也就是说，下一个 x 将会是 21。
x 一般延后 2~4 个季度，给开发者一个缓冲，即下一个 JDK LTS 出来后，那么 ioGame 的 x 会在 2~4 个季度后跟上。


### 2022-06
> 1. 新增 ClientProcessorHooks 业务框架处理请求时，开发者可以自定义业务线程编排
> 2. 将模块之间的访问独立一个接口

### 2022-05
> - 新增 request/multiple_response 通讯模型。
> - 支持对外服的玩家绑定指定的游戏逻辑服（可以做到动态分配逻辑服资源）。
> - DebugInOut 新增设置最小触发打印时间、新增逻辑服id、逻辑服类型的打印信息。
> - action 的返回值支持 null。
> - 每个逻辑服都可以独立进程部署
> - 新增 Broker 概念与 BrokerClient 概念
> - 新增 Broker 集群，集群使用 gossip 协议
> - 新增 DataCodec 开发者可以自定义业务参数的编解码
> - 支持逻辑服（BrokerClient）与游戏网关（Broker）的数量扩展，并能很好的进行负载均衡。
> - 新增 分布式锁-基于 Redisson 的简单实现

### 2022-04
> - 新增 u3d、cocos 连接示例 和 tcp socket 的连接示例
> - 独立 bolt 网络通信框架到单独目录 net-bolt。

### 2022-03
> - 增加房间模块
> - 新增 Spring 集成
> - DefaultActionFactoryBean 新增创建 action 混合特性
> - 新增 UserSession
> - FlowContext 新增动态属性
> - 新增心跳相关设置 IdleProcessSetting
> - 新增 ExternalJoinEnum
> - 新增用户钩子接口 UserHook 上线时、下线时会触发
> - 简化对外服务器 - 构建器 ExternalServerBuilder
> - 业务框架新增 InOutInfo 管理插件相关
> - 新增简单的启动器 SimpleRunOne

### 2022-02
> - BarSkeletonBuilderParamConfig 新增构建 BarSkeletonBuilder 方法
> - 游戏框架对外服默认连接协议改为 WebSocket
> - 编写游戏框架文档
> - 新增 游戏（错误码）文档的生成
> - 新增 游戏（异常信息）文档的生成
> - 新增 BarSkeletonBuilderParamConfig 构建参数的配置
> - 新增动态属性 AttrOptionDynamic 接口

### 2022-01
> - 修复广播时的 bug （在逻辑服传输数据到网关时，response.data 对象如果没有实现 Serializable 会异常）。
> - 新增 light-jprotobuf 简化 jprotobuf 的编写方式。
> - 业务框架支持文档生成，java 代码既文档。
> - DebugInOut 可定位代码行数。
> - 新增路由错误码： 一般是客户端请求了不存在的路由引起的
> - 新增 websocket  编解码
> - 增加 websocket 数据压缩扩展
> - 网关新增路由检测
> - 业务框架 BarMessage 增加 rpcCommandType 字段
> - 新增 request/void 通讯模型。
> - DebugInOut 日志可以支持 JSR 303、JSR 349、JSR 380 验证规范 的日志.
> - 优化业务框架: Flow 流程，在开启业务参数验证规范功能时，业务参数如果验证不通过，则直接响应带有错误码的消息给调用端。
> - 业务框架支持 JSR 303、JSR 349、JSR 380 验证规范。
> - 业务框架新增 FlowContext 上下文，生命周期存在于当前执行流程。
> - 对外服新增接收并处理 来自网关的广播消息。
> - 业务框架加强规范异常处理
> - 提供异常全局统一处理规范
> - 领域事件新增默认异常处理
> - 游戏对外服务器 支持 websocket
> - 业务框架支持 proto
> - 编写对外服务器，对外服务器连接到网关。
> - 增加加载项 BootConfig
> - 动态属性

<hr/>

## 2021

### 2021-12
新增领域事件模块
初始化项目，编写业务框架
