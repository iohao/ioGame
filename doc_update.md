更新日志在线文档
https://www.yuque.com/iohao/game/ab15oe





#### 2023-09-06 - v17.1.55

详细 https://github.com/game-town/ioGame/releases/tag/17.1.55



[[#186](https://github.com/game-town/ioGame/issues/186)] 增强 ProtoDataCodec

当 ExternalMessage.data 为 null 时，使用空数组来解析序列化；可以确保 action 参数不会为 null，使得开发者拥有更好的编码体验。

将 ActionCommandTryHandler 逻辑合并到 ActionCommandHandler

DevConfig.me 标记为过期，并将 DevConfig 改为静态类；

把 MethodParsers.me 方法标记为过期，并将 MethodParsers 改为静态类；



**其他更新**

<netty.version>4.1.97.Final</netty.version>

<lombok.version>1.18.28</lombok.version>



#### 2023-08-18 17.1.54

[[#174](https://github.com/game-town/ioGame/pull/174)]  **fix action 交给容器管理时，实例化两次的问题**



[获取游戏对外服的数据与扩展](https://www.yuque.com/iohao/game/ivxsw5)，获取ResponseCollectExternalMessage 新增 optionalAnySuccess 方法，方便得到成功的 optional

```java
    public String getUserIp() {

        ResponseCollectExternalMessage message = ...

        return message
                .optionalAnySuccess()
                // 得到返回值
                .map(ResponseCollectExternalItemMessage::getData)
                // 将为 String
                .map(Objects::toString)
                // 如果没获取到给个空串，调用方就不需要做 null 判断了。
                .orElse("");
    }
```



[压测&模拟客户端请求](https://www.yuque.com/iohao/game/tc83ud)模块，新增模块名标识

```java
public class BagInputCommandRegion extends AbstractInputCommandRegion {
    @Override
    public void initInputCommand() {
        this.inputCommandCreate.cmd = BagCmd.cmd;
        this.inputCommandCreate.cmdName = "背包模块";
    }
}
```



[新游戏对外服](https://www.yuque.com/iohao/game/ea6geg)新增 HttpRealIpHandler，用于获取玩家真实 ip 支持

游戏对外服 webSocket 使用 nginx 代理，也能获取真实的玩家 ip



```java
public class MyExternalServer {
    ... ...省略部分代码
    public ExternalServer createExternalServer(int externalPort) {
    	... ...省略部分代码
        // 游戏对外服 - 构建器
        DefaultExternalServerBuilder builder = ...

        builder.setting().setMicroBootstrapFlow(new WebSocketMicroBootstrapFlow() {
            @Override
            protected void httpHandler(PipelineContext context) {
                super.httpHandler(context);
                /*
                 * HttpRealIpHandler 是框架内置的一个 handler。
                 * 添加上后，即使是通过 nginx 转发，也可以得到玩家真实的 ip
                 */
                context.addLast("HttpRealIpHandler", new HttpRealIpHandler());
            }
        });

        // 构建游戏对外服 https://www.yuque.com/iohao/game/ea6geg
        return builder.build();
    }
}
```



#### 2023-08-07 17.1.52

详细 https://github.com/game-town/ioGame/releases/tag/17.1.52



[[#172](https://github.com/game-town/ioGame/issues/172)] **新增 webSocket token 鉴权、校验支持**

有时，我们需要在 WebSocket 建立连接前做 token 相关鉴权、校验的业务。ioGame 支持此类业务的扩展，我们可以在游戏对外服部分做相关扩展；



简单的说，如果校验没通过，我们就不建立 ws 连接了，在 http 阶段就结束所有流程，可以有效的减少恶意长连接。



相关文档与使用示例 https://www.yuque.com/iohao/game/tb1126szmgfu6u55



**日志相关调整**
移除 light-log 模块，统一使用 lombok slf4j 相关注解



**压测&模拟客户端增强**

新增 SplitParam，方便模拟测试时，解析控制台输入参数的获取

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

            ... 省略部分代码
        };

        ofCommand(BagCmd.use).callback(BoolValue.class, result -> {
            var value = result.getValue();
            log.info("value : {}", value);
        }).setDescription("使用背包物品").setInputRequestData(inputRequestData);
    }
```



#### 2023-08-01 17.1.50

详细 https://github.com/game-town/ioGame/releases/tag/17.1.50



1 DebugInout 在自定义 FlowContext 时的打印优化。



2 cmdInfo 新增 of 系列方法，用于代替 getCmdInfo 系列方法



3 异常机制接口 MsgExceptionInfo，新增方法

// 断言为 true, 就抛出异常，可自定义消息

void assertTrueThrows(boolean v1, String msg)

// 断言值 value 不能为 null, 否则就抛出异常，可自定义消息

void assertNonNull(Object value, String msg)



4 将旧版的游戏对外服标记为过时的

请使用[新游戏对外服](https://www.yuque.com/iohao/game/ea6geg)



5 模拟客户端新增成功回调触发，模拟命令域 InputCommandRegion 新增 loginSuccessCallback 成功回调方法，当模拟玩家登录成功后会调用此方法

```java
public class MapInputCommandRegion extends AbstractInputCommandRegion {
    ... ... 省略部分代码

    @Override
    public void loginSuccessCallback() {
        // 进入地图，根据地图 id
        EnterMapReq enterMapReq = new EnterMapReq();
        enterMapReq.mapId = 1;
        ofRequestCommand(MapCmd.enterMap).request(enterMapReq);
    }
}
```



6 ClientUser 新增 callbackInputCommandRegion 方法。一般在登录模拟请求的回调中主动的调用，开发者可在登录成功后，调用此方法。使其触发所有的 InputCommandRegion.loginSuccessCallback 方法；

```java
@Slf4j
public class LoginInputCommandRegion extends AbstractInputCommandRegion {
    ... ... 省略部分代码
    @Override
    public void initInputCommand() {
        InputRequestData inputRequestData = () -> {
            LoginVerify loginVerify = new LoginVerify();
            loginVerify.jwt = clientUser.getJwt();
            return loginVerify;
        };
        
        ofCommand(LoginCmd.loginVerify).callback(UserInfo.class, result -> {
            UserInfo userInfo = result.getValue();
            log.info("登录成功 : {}", userInfo);
            clientUser.setUserId(userInfo.id);
            clientUser.setNickname(userInfo.nickname);
            // ------------ 关键代码 ------------
            clientUser.callbackInputCommandRegion();
        }).setDescription("登录").setInputRequestData(inputRequestData);
    }
}
```



7 [压测&模拟客户端请求](https://www.yuque.com/iohao/game/tc83ud)

新增重复上一次命令的支持




#### 2023-07-28 17.1.48

文档生成增强，增加 action 参数注释说明.

文档生成增强，返回值注释说明.

fix 在 pom 中引入本地 jar 时，文档解析的错误。



#### 2023-07-21 17.1.47

[[160](https://github.com/game-town/ioGame/issues/160)] **轻量小部件 - 压测&模拟客户端请求模块**

文档：https://www.yuque.com/iohao/game/tc83ud



**介绍**

此模块是用于模拟客户端，简化模拟工作量，只需要编写对应请求与回调。

使用该模块后，当我们与前端同学联调某个功能时，不需要跟前端哥们说：在点一下、在点一下、在点一下了。这种“在点一下”的交流联调方式将成为过去式。

除了可以模拟简单的请求外，通常还可以做一些复杂的请求编排，并支持复杂业务的压测。模拟测试的过程是可互动的，但也支持测试自动化。

与单元测试不同的是，该模块可以模拟真实的网络环境，并且在模拟测试的过程中与服务器交互是可持续的、可互动的。

可互动模式是用于调试测试某些功能。在互动的过程中，开发者可以在控制台中指定执行某个模拟请求命令，并且支持在控制台中输入一些动态的请求参数，从而让我们轻松的测试不同的业务逻辑走向。



**特点**

- 使用简单
- 压测支持
- 可以模拟客户端请求
- 可以模拟真实的网络环境
- 可以编排复杂的业务请求
- 同样的模拟测试用例，支持在多种连接方式下工作（tcp、udp、websocket）
- 可持续的与服务器交互，模拟测试的过程是可互动的，但也支持测试自动化



**其他优化**

文档生成增强，增加 action 参数注释说明、返回值注释说明。



#### 2023-07-07 17.1.45
[[#159](https://github.com/iohao/ioGame/issues/159)] **同进程同时支持多种连接方式方式的技巧**

```java
public class MyApplication {
    ... ... 省略部分代码
    static int externalCorePort = 10100;

    public static void main(String[] args) {
        // 游戏对外服列表
        List<ExternalServer> externalServerList = listExternalServer();
        new NettyRunOne()
                .setExternalServerList(externalServerList)
                .startup();
    }

    static List<ExternalServer> listExternalServer() {
        return List.of(
                // 连接方式；WEBSOCKET
                createExternalServer(ExternalJoinEnum.WEBSOCKET)
                // 连接方式；TCP
                , createExternalServer(ExternalJoinEnum.TCP)
                // 连接方式；UDP
                , createExternalServer(ExternalJoinEnum.UDP)
        );
    }

    static ExternalServer createExternalServer(ExternalJoinEnum join) {
        int port = externalCorePort;
        port = join.cocPort(port);
        DefaultExternalServerBuilder builder = DefaultExternalServer
                .newBuilder(port)
                // 连接方式
                .externalJoinEnum(join)
                // 与 Broker （游戏网关）的连接地址
                .brokerAddress(new BrokerAddress("127.0.0.1", IoGameGlobalConfig.brokerPort));

        return builder.build();
    }
}
```



[[#157](https://github.com/iohao/ioGame/issues/157)] fix **默认心跳钩子问题**

[[#122](https://github.com/iohao/ioGame/issues/122)] **同进程亲和性**

文档：https://www.yuque.com/iohao/game/unp26u



同进程内不同 Netty 实例通信时，是通过内存进行传输的，不需要经过网络传输，数据传输速度极快。也就是说，如果我们将游戏对外服、Broker（游戏网关）、游戏逻辑服部署在同一个进程中（也就是单体应用），那么各服务器之间是在内存中通信的。甚至可以简单的理解为在同一 JVM 中的 a 方法调用了 b 方法，b 方法调用了 c 方法。



同进程亲和性是 ioGame 的特性之一，可以让同一进程内的 Netty 实例拥有相互访问优先权。说人话就是，如果你在同一进程内启动了游戏对外服、Broker（游戏网关）、游戏逻辑服，当有请求需要处理时：

- 即使你启动了多个 Broker（游戏网关），游戏对外服会优先将请求交给同进程内的 Broker（游戏网关）来处理。
- 即使你启动了多个相同的游戏逻辑服，Broker（游戏网关）会优先将请求交给同进程的游戏逻辑服来处理。
- 同样的，游戏逻辑服处理完请求后，会优先将响应交给同进程内的 Broker（游戏网关）。



#### 2023-06-14 17.1.44

[[#138](https://github.com/iohao/ioGame/issues/138)] **提供协议碎片的工具类，方便协议碎片在广播时的使用**

代码中演示了协议碎片相关的使用，通过工具类，可以让一些基础类型在使用上更简便。
对应的包装类中，都提供了静态 of 方法；

框架支持的包装类可到 [框架支持的自动装箱、拆箱基础类型](https://www.yuque.com/iohao/game/ieimzn#EJVsp) 查询。

```java
... ... 省略部分代码
private static void test() {
  // 给客户端广播一个 int 值 : 1
  var bizData = WrapperKit.of(1);

  // 广播上下文
  CmdInfo cmdInfo = CmdInfo.getCmdInfo(DemoBroadcastCmd.cmd, DemoBroadcastCmd.broadcastMsg);
  BroadcastContext broadcastContext = BrokerClientHelper.getBroadcastContext();
  broadcastContext.broadcast(cmdInfo, bizData);

  // 给客户端广播一个 bool 值 : true
  var bizDataBoolean = WrapperKit.of(true);
  broadcastContext.broadcast(cmdInfo, bizDataBoolean);

  // 对象列表演示
  DemoBroadcastMessage broadcastMessage = new DemoBroadcastMessage();
  broadcastMessage.msg = "broadcast hello，" + counter.longValue();
  List<DemoBroadcastMessage> list = new ArrayList<>();
  list.add(broadcastMessage);
  var bizDataList = WrapperKit.ofListByteValue(list);
  broadcastContext.broadcast(cmdInfo, bizDataList);

  // int 列表
  var bizDataIntList = IntValueList.of(List.of(1, 3, 5, 7));
  broadcastContext.broadcast(cmdInfo, bizDataIntList);

  ... ... 省略部分代码
  其他类同，不全部介绍了。
}
```



[[#133](https://github.com/iohao/ioGame/issues/133)] **向指定对外服上的用户广播数据**

```java
... ...省略部分代码
private static void extracted(String externalId) {
    var bizData = new DemoBroadcastMessage();
    broadcastMessage.msg = "broadcast hello！" ;

    // 广播消息的路由
    CmdInfo cmdInfo = ...;
    ResponseMessage responseMessage = BarMessageKit.createResponseMessage(cmdInfo, bizData);
    
    // 指定游戏对外服广播
    HeadMetadata headMetadata = responseMessage.getHeadMetadata();
    int sourceClientId = MurmurHash3.hash32(externalId);
    headMetadata.setSourceClientId(sourceClientId);

    // 广播上下文
    BroadcastContext broadcastContext = BrokerClientHelper.getBroadcastContext();
    broadcastContext.broadcast(responseMessage);
}
```



容错设置

IoGameGlobalConfig.brokerSniperToggleAK47 = boolean；

```plain
Broker（游戏网关）转发消息容错配置
      游戏逻辑服与游戏对外服通信时，如果没有明确指定要通信游戏对外服，游戏网关则会将消息转发到所有的游戏对外服上。
      如果指定了游戏对外服的，游戏网关则会将消息转发到该游戏对外服上，而不会将消息转发到所有的对外服上。
 
      当为 true 时，开启容错机制
          表示开发者在发送消息时，如果指定了游戏对外服的，
          但【游戏网关】中没有找到所指定的【游戏对外服】，则会将消息转发到所有的游戏对外服上，
          这么做的目的是，即使开发者填错了指定的游戏对外服，也能保证消息可以送达到游戏对外服。
 
      当为 false 时，关闭容错机制
          表示在【游戏网关】中找不到指定的【游戏对外服】时，则不管了。
 
      支持的通讯方式场景
          广播、推送 
          获取游戏对外服的数据与扩展 
  
另一种叙述版本
      作用：
          在游戏逻辑服发送广播时，支持指定游戏对外服来广播；
          如果你能事先知道所要广播的游戏对外服，那么在广播时通过指定游戏对外服，可以避免一些无效的转发。
 
          为了更好的理解的这个配置的作用，这里将作一些比喻：
          1. 将广播时指定的游戏对外服，看作是目标
          2. 将发送广播的游戏逻辑服，看作是命令
          3. 而 Broker（游戏网关）职责是对消息做转发，可看成是一名射击员；射击员手上有两把枪，分别是狙击枪和 AK47。
 
          狙击枪的作用是单点目标，而 AK47 的作用则是扫射多个目标（就是所有的游戏对外服）。
 
      场景一：
          当设置为 true 时，表示射击员可以将手中的狙击切换为 AK47，什么意思呢？
          意思就是如果在【游戏网关】中没有找到所指定的【游戏对外服】，则将广播数据发送给【所有的游戏对外服】。（换 AK 来扫射）
          这么做的目的是，即使开发者填错了指定的游戏对外服，也能保证消息可以送达到游戏对外服。
 
      场景二：
          当设置为 false 时，表示找不到指定的【游戏对外服】时，则不管了。
```



[[#131](https://github.com/iohao/ioGame/issues/131)] **获取指定对外服上数据的接口**

参考使用示例，通过 RequestCollectExternalMessage 请求对象，可以指定游戏对外服id；

```java
@UtilityClass
public class ExternalCommunicationKit {
    /**
     * 设置元信息到游戏对外服
     * 
<pre>
     *     之后所有 action 的 FlowContext 中会携带上这个元信息对象，
     *     不建议在元信息保存过多的信息，因为会每次传递。
     * </pre>
*
     * @param attachment  元信息
     * @param flowContext flowContext
     */
    public void setAttachment(Attachment attachment, FlowContext flowContext) {
        // 不做 null 判断，只做个 userId 的检测
        long userId = attachment.getUserId();

        if (userId <= 0) {
            throw new RuntimeException("userId <= 0");
        }

        // 得到游戏对外服 id
        RequestMessage request = flowContext.getRequest();
        HeadMetadata headMetadata = request.getHeadMetadata();
        int sourceClientId = headMetadata.getSourceClientId();

        var requestCollectExternalMessage = new RequestCollectExternalMessage()
                // 根据业务码，调用游戏对外服与业务码对应的业务实现类 （AttachmentDataExternalBizRegion）
                .setBizCode(ExternalBizCodeCont.attachment)
                // 元信息
                .setData(attachment)
                // 指定游戏对外服
                .setSourceClientId(sourceClientId);

        BrokerClientHelper
                // 【游戏逻辑服】与【游戏对外服】通讯上下文
                .getInvokeExternalModuleContext()
                .invokeExternalModuleCollectMessage(requestCollectExternalMessage);
    }
}
```



容错设置

IoGameGlobalConfig.brokerSniperToggleAK47 = boolean；



#### 2023-06-08 17.1.43（重要版本）

**[**[**#115**](https://github.com/iohao/ioGame/issues/115)**] 游戏对外服增加路由是否存在检测**

参考：https://www.yuque.com/iohao/game/ea6geg#EeWiH

新游戏对外服中增加路由存在检测。当路由不存在时，可以起到抵挡的作用，而不必经过其他服务器。



**[**[**#114**](https://github.com/iohao/ioGame/issues/114)**] 支持玩家与多个游戏逻辑服的动态绑定**

文档：[动态绑定游戏逻辑服](https://www.yuque.com/iohao/game/idl1wm)

动态绑定游戏逻辑服，指的是玩家与游戏逻辑服绑定后，之后的请求都由该游戏逻辑服来处理。

玩家动态绑定逻辑服节点后，之后的请求都由这个绑定的游戏逻辑服来处理，可以实现类似 LOL、王者荣耀匹配后动态分配房间的效果。

支持玩家与多个游戏逻辑服的动态绑定。





**使用场景**

跨服活动、跨服战斗等。



动态绑定游戏逻辑服可以解决玩家增量的问题，我们都知道一台机器所能承载的运算是有上限的；当上限达到时，就需要增加新机器来分摊请求量；如果你开发的游戏是有状态的，那么你如何解决请求分配的问题呢？在比如让你做一个类似 LOL、王者荣耀的匹配，将匹配好的玩家分配到一个房间中，之后这些玩家的请求都能在同一个游戏逻辑服上处理，这种业务你该如何实现呢？



使用框架提供的动态绑定逻辑服节点可以轻松解决此类问题，而且还可以根据业务规则，计算出当前空闲最多的游戏逻辑服，并将此游戏逻辑服与玩家做绑定，从而做到均衡的利用机器资源，来防止请求倾斜的问题。



**[**[**#113**](https://github.com/iohao/ioGame/issues/113)**]** **新版本游戏对外服**

文档：[新游戏对外服使用](https://www.yuque.com/iohao/game/ea6geg)

迁移指南 ：[迁移到新版游戏对外服](https://www.yuque.com/iohao/game/vu0hrkn8xqq0x9a5)



【新版游戏对外服】用于取代【旧的游戏对外服】，如果条件允许，请尽可能做迁移，整体工作量很少。旧的游戏对外服将不在做功能上的新增，如果存在 bug 将会继续修复，维护期会持续到下个大版本前。



从架构简图中，我们知道了整体架构由三部分组成 1.游戏对外服、2.游戏网关、3.游戏逻辑服。本篇将介绍游戏对外服这部分，及功能扩展等相关的。



**游戏对外服的职责**

1. 保持与用户（玩家）长的连接
2. 帮助开发者屏蔽通信细节、与连接方式的细节
3. 连接方式支持：WebSocket、TCP、UDP
4. 将用户（玩家）请求转发到游戏网关
5. 可动态增减扩展机器
6. 功能扩展，如：路由存在检测、路由权限、UserSession 管理、心跳，及后续要提供但还未提供的熔断、限流、降载、用户流量统计等功能。



**扩展场景**

游戏对外服主要负责与用户（玩家）的连接。假设一台硬件支持最多建立 5000 个用户连接，当用户量达到 7000 人时，我们可以增加一个游戏对外服来进行流量控制和减压。



由于游戏对外服的扩展性和灵活性，可以支持同时在线玩家从几千人到数千万人不等。这是因为，通过增加游戏对外服的数量，可以有效地进行连接的负载均衡和流量控制，使得系统能够更好地承受高并发的压力。



**连接方式的切换、支持、扩展**

ioGame 已提供了 TCP、WebSocket、UDP 连接方式的支持，并提供了灵活的方式来实现连接方式的切换。可以将 TCP、WebSocket、UDP 连接方式与业务代码进行无缝衔接。开发者可以用一套业务代码，无需任何改动，同时支持多种通信协议。



如果想要切换到不同的连接方式，只需要更改相应的枚举即可，非常简单。在不使用 ioGame 时，将连接方式从 TCP 改为 WebSocket 或 UDP 等，需要进行大量的调整和改动。然而，在 ioGame 中，实现这些转换是非常简单的。此外，不仅可以轻松切换各种连接方式，而且可以同时支持多种连接方式，并使它们在同一应用程序中共存。



连接方式是可扩展的，而且扩展也简单，这意味着之后如果支持了 KCP，那么将已有项目的连接方式，如 TCP、WebSocket、UDP 切换成 KCP 也是简单的。



需要再次强调的是，连接方式的切换对业务代码没有任何影响，无需做出任何改动即可实现连接方式的更改。



**游戏对外服由两部分构成**

1. ExternalCore：与真实玩家连接的 ExternalCore 服务器
2. ExternalBrokerClientStartup：负责内部通信，与 Broker（游戏网关）通信



我们只需要关注 ExternalCore 这部分。



新版游戏对外服总体来说只有四个核心接口，如果你只打算做功能扩展，只需要关注 MicroBootstrapFlow 接口就好了。

| 接口名             | 描述                                                         |
| ------------------ | ------------------------------------------------------------ |
| ExternalServer     | 由 ExternalCore 和 ExternalBrokerClientStartup 组成的一个整体。职责：是启动 ExternalCore 和 ExternalBrokerClientStartup 。 |
| ExternalCore       | 与真实玩家连接的服务器，也是通信框架屏蔽接口。ExternalCore 帮助开发者**屏蔽各通信框架**的细节，如 Netty、mina、smart-socket 等通信框。ioGame 默认提供了基于 Netty 的实现。职责：与真实玩家连接的服务器 |
| MicroBootstrap     | 与真实玩家连接的服务器，服务器的创建由 MicroBootstrap 完成，实际上 ExternalCore 是一个类似代理类的角色。MicroBootstrap 帮助开发者**屏蔽连接方式**的细节，如 TCP、WebSocket、UDP 等。 职责：与真实玩家连接的【真实】服务器 |
| MicroBootstrapFlow | 与真实玩家连接【真实】服务器的启动流程，专为 MicroBootstrap 服务。开发者可通过此接口对服务器做编排，编排分为：构建时、新建连接时两种。框架提供了 TCP、WebSocket、UDP 的实现；开发者可以选择性的重写流程方法，来定制符合自身项目的业务。职责：业务编排，也是开发者在扩展时接触最多的一个接口。 |



**其他更新**

<scalecube-cluster.version>2.6.15</scalecube-cluster.version>

<netty.version>4.1.93.Final</netty.version>



#### 2023-05-29 - v17.1.42

**[**[**#127**](https://github.com/iohao/ioGame/issues/127)**] DebugInOut，可限制某些 action 不输出 log**

使用文档 https://www.yuque.com/iohao/game/pf3sx0#esnXX ，文档中提供了两种参考示例：

- 使用硬编码的方式
- 使用自定义注解的方式来扩展

```java
@ActionController(1)
public class DemoAction {
    @ActionMethod(3)
    @IgnoreDebugInout
    public String hello() {
        // 给 action 添加上自定义注解 IgnoreDebugInout 后，将不会打印 debug 信息
        return "hello";
    }
}
```



**[**[**#132**](https://github.com/iohao/ioGame/issues/132)**] 集群重启后组网异常**

导致集群数量对不上，触发的事件顺序可能是 ADDED、REMOVED，也可能是REMOVED、ADDED，现已修复。



**其他更新**

<scalecube-cluster.version>2.6.14</scalecube-cluster.version>



#### 2023-05-09 - v17.1.40

**[**[**#99**](https://github.com/iohao/ioGame/issues/99)**] 增加 msgId 特性，默认只在 request/response 通讯方式下生效**

游戏前端可以给游戏对外服协议添加一个 msgId，当 ioGame 接收到请求并处理完请求后，会在响应时将 msgId 回传给请求端；类似透传参数。

使用时请更新游戏对外服协议 https://www.yuque.com/iohao/game/xeokui



**[**[**#102**](https://github.com/iohao/ioGame/issues/102)**] 业务框架 BarSkeleton 类增加动态属性，方便扩展.**



**[**[**#111**](https://github.com/iohao/ioGame/issues/111)**] 新增文档解析、文档生成的控制选项**

在 windows 系统下开发时，如果 action 类过多，可能会导致启动慢；类 Linux 系统没有此问题，框架增加两个设置，可以让开发者决定是否启用相关功能。

```java
public class DemoLogicServer extends AbstractBrokerClientStartup {
    ... ... 省略部分代码
    @Override
    public BarSkeleton createBarSkeleton() {

        // 业务框架构建器
        BarSkeletonBuilder builder = ...;

        BarSkeletonSetting setting = builder.getSetting();
        // 不生成文档
        setting.setGenerateDoc(false);
        // 不解析文档
        setting.setParseDoc(false);

       ...

        return builder.build();
    }

}
```



**[**[**#103**](https://github.com/iohao/ioGame/issues/103)**] 业务框架新增 Runner 机制，增强扩展性、规范性**

详细的使用文档：[Runner 机制 - 文档](https://www.yuque.com/iohao/game/dpwe6r6sqwwtrh1q)

Runner 机制类似于 Spring CommandLineRunner 的启动项，它能够在逻辑服务器启动之后调用一次 Runner 接口实现类，让开发者能够通过实现 Runner 接口来扩展自身的系统。



使用 Runner 机制，开发者可以通过扩展已有模块的功能或提供配置相关的功能来实现自定义扩展。



Runner 机制不仅可以让我们将已有模块的功能以 Runner 形式进行扩展，也可以通过 Runner 机制来提供配置相关的功能，避免配置过于零散。



事实上，Runner 机制的可扩展性远远不止于此。通过 onStart 方法中的业务框架对象 BarSkeleton，开发者可以实现相关隔离并利用其动态属性来扩展特殊业务数据。一个业务框架对象对应一个逻辑服。



**[**[**#104**](https://github.com/iohao/ioGame/issues/104)**] 新增实验性特性-脉冲通讯方式**

详细的使用文档：[脉冲通讯方式-文档](https://www.yuque.com/iohao/game/zgaldoxz6zgg0tgn)

脉冲通讯与发布订阅类似，但是它除了具备发布订阅的无需反馈的方式，还增加了接收消息响应的动作，这是它与发布订阅的重要区别。



需要注意的是，脉冲通讯只是一种通讯方式，不能完全取代发布订阅，而是适用于一些特殊的业务场景。虽然在理论上，这些特殊的业务场景可以使用发布订阅来完成，但这会让代码变得复杂。



脉冲通讯在特定的场景下有巨大的优势！关于脉冲通讯更多的应用场景发挥，如同[业务框架插件](https://www.yuque.com/iohao/game/gmxz33)一样，取决于开发者的想象力。



**注意事项**

开源协议更改为 GPL2.0



**其他更新**

BrokerClient 添加 AwareInject

<netty.version>4.1.92.Final</netty.version>

<jctools-core.version>4.0.1</jctools-core.version>

<qdox.version>2.0.3</qdox.version>

<protobuf-java.version>3.22.4</protobuf-java.version>



#### 2023-04-18 - v17.1.38

此版本有 2 位开发者参与贡献

[miaozhiyan](https://github.com/miaozhiyan) 贡献的 [#79](https://github.com/iohao/ioGame/issues/79)

[hia](https://github.com/hia) 贡献的 [#84](https://github.com/iohao/ioGame/issues/84)



[#46](https://github.com/iohao/ioGame/issues/46) action 业务参数与返回值增加 List 支持

详细使用文档可参考：https://www.yuque.com/iohao/game/ieimzn#Aqc1C



action 支持 List 参数与返回值，可以有效的减少协议碎片、减少工作量等。在没有支持 List 之前的代码，如果想要传输一个列表的数据，通常需要将 pb 对象包装到另一个 pb 响应对象中。



让我们先看一个示例，这个示例中 action 方法的的逻辑很简单，将查询到的数据列表给到请求端。由于之前不支持 List 返回值，开发者想要将列表中的数据给到请求端，还需要额外的定义一个与之对应的响应类，只有这样才能将列表数据给到请求端。



我们可以想象一下，如果你的系统中有很多固定的配置数据，比如装备、道具、活动信息、英雄人物介绍、敌人相关信息、地图信息、技能信息、宠物基本信息...等等，通常会有几十、上百个这样的响应对象。



为了将这些固定的配置数据给到请求端，而建立与之对应的响应对象，想想这是一件多么无聊的一件事情。这些多出来的响应对象，就是协议碎片，是一种可有可无的协议；此外还有如下缺点：

- 将会变成干扰项
- 增加维护成本
- 增加工作量（每次有新的配置表都要新建、在每个 action 中，都要创建这个响应对象）



```java
@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
public class Animal {
    /** id */
    int id;
}

@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
public class AnimalResponse {
    List<Animal> animals;
}

@ActionController(3)
public class HallAction {
    @ActionMethod(10)
    public AnimalResponse listAnimal1() {
        // 查询出列表
        var list = IntStream.range(1, 4).mapToObj(id -> {
            Animal animal = new Animal();
            animal.id = id;
            return animal;
        }).collect(Collectors.toList());

        // 将列表存放到 Animal 的响应对象中
        AnimalResponse animalResponse = new AnimalResponse();
        animalResponse.animals = list;

        return animalResponse;
    }
}
```



通过上面的介绍，知道协议碎片是多么恐怖的一件事了把。其实我们的需求也很简单，只是想把列表中的数据给到请求端就可以了。此时，我们可以利用 action 将列表数据通过 List 直接返回，这样可以避免上面所说的各种缺点。同时，还可以让我们的代码更加的简洁，这种方式可以使前端与后端都受益。



用更少的代码实现了同样的功能，减少了工作量，避免了协议碎片。这样，开发者就不在需要额外的建立一个与之对应的响应协议了；当使用了框架提供的 List 返回值后，可以帮助你的系统减少几十、上百个类似 xxxResponse 的协议。



来，让我们看看修改后的代码是有多么简洁的吧。这种编码方式，即使你是一个新手，也能快速的看懂；

```java
@ActionController(3)
public class HallAction {
    @ActionMethod(9)
    public List<Animal> listAnimal() {
        // 查询出列表
        return IntStream.range(1, 4).mapToObj(id -> {
            Animal animal = new Animal();
            animal.id = id;
            return animal;
        }).collect(Collectors.toList());
    }
}
```



[#74](https://github.com/iohao/ioGame/issues/74) action 返回值增加 byte[] 扩展



<details class="lake-collapse"><summary id="u9cdd38f3"><span class="ne-text">框架已经取消该功能，取消原因--点我展开</span></summary><p id="ud1f58c5f" class="ne-p" style="margin: 0; padding: 0; min-height: 24px"><span class="ne-text">经过思考，决定取消 action 返回值 byte[] 类型的支持。<br></span><span class="ne-text">因为这会导致在生成文档时，不知道其具体的类型，从而增加沟通负担；</span></p><p id="u1aee9384" class="ne-p" style="margin: 0; padding: 0; min-height: 24px"><br></p><pre data-language="latex" id="2e6234d1" class="ne-codeblock language-latex" style="border: 1px solid #e8e8e8; border-radius: 2px; background: #f9f9f9; padding: 16px; font-size: 13px; color: #595959">路由: 1 - 4  --- 【】 --- 【DemoAction:120】【testByte】
    方法参数: IntValue
    方法返回值: [B</pre><p id="u1567634c" class="ne-p" style="margin: 0; padding: 0; min-height: 24px"><br></p><h3 id="2c4fadc2" style="font-size: 20; line-height: 28px; margin: 16px 0 5px 0"><span class="ne-text">可扩展</span></h3><p id="uf9516962" class="ne-p" style="margin: 0; padding: 0; min-height: 24px"><br></p><p id="u465ccb28" class="ne-p" style="margin: 0; padding: 0; min-height: 24px"><span class="ne-text">如果开发者有需要，可以自行扩展，扩展协议参考 </span><a href="https://www.yuque.com/iohao/game/uq2zrltrc7to27bt" data-href="https://www.yuque.com/iohao/game/uq2zrltrc7to27bt" target="_blank" class="ne-link"><span class="ne-text">https://www.yuque.com/iohao/game/uq2zrltrc7to27bt</span></a></p><p id="u68685372" class="ne-p" style="margin: 0; padding: 0; min-height: 24px"><br></p><p id="u5a6ef47c" class="ne-p" style="margin: 0; padding: 0; min-height: 24px"><span class="ne-text">以 proto 编解码器为例，只需要在 encode 方法添加如下逻辑</span></p><p id="u23a1d06f" class="ne-p" style="margin: 0; padding: 0; min-height: 24px"><br></p><pre data-language="java" id="0c90482d" class="ne-codeblock language-java" style="border: 1px solid #e8e8e8; border-radius: 2px; background: #f9f9f9; padding: 16px; font-size: 13px; color: #595959">public final class MyProtoDataCodec implements DataCodec {
    @Override
    public byte[] encode(Object data) {
        if (data instanceof byte[] bytes) {
            return bytes;
        }

        return ProtoKit.toBytes(data);
    }
    
    ... 省略部分代码
}</pre><p id="u932c9f85" class="ne-p" style="margin: 0; padding: 0; min-height: 24px"><br></p><p id="u4ec3c8c9" class="ne-p" style="margin: 0; padding: 0; min-height: 24px"><span class="ne-text">将自定义的，添加到框架中</span></p><p id="u2e4f885a" class="ne-p" style="margin: 0; padding: 0; min-height: 24px"><br></p><pre data-language="java" id="96d4e3d3" class="ne-codeblock language-java" style="border: 1px solid #e8e8e8; border-radius: 2px; background: #f9f9f9; padding: 16px; font-size: 13px; color: #595959">// 设置自定义的编解码。如果不做设置，默认使用 jprotobuf
IoGameGlobalSetting.setDataCodec(new MyProtoDataCodec());</pre><p id="ube57b01c" class="ne-p" style="margin: 0; padding: 0; min-height: 24px"><br></p><p id="u9ff96ed5" class="ne-p" style="margin: 0; padding: 0; min-height: 24px"><span class="ne-text">action byte[] 使用展示</span></p><p id="u93f9b4ba" class="ne-p" style="margin: 0; padding: 0; min-height: 24px"><br></p><pre data-language="java" id="3e546c5d" class="ne-codeblock language-java" style="border: 1px solid #e8e8e8; border-radius: 2px; background: #f9f9f9; padding: 16px; font-size: 13px; color: #595959">@Slf4j
@ActionController(1)
public class DemoAction {
    static byte[] helloReqData;

    static {
        // 这里模拟缓存数据，不用每次序列化
        HelloReq helloReq = new HelloReq();
        helloReq.theIndex = 100;
        helloReq.name = "to byte[]";
        helloReqData = DataCodecKit.encode(helloReq);
    }
    
    @ActionMethod(4)
    public byte[] testByte(int id) {
        return helloReqData;
    }
}</pre><p id="udfba6099" class="ne-p" style="margin: 0; padding: 0; min-height: 24px"><br></p><h3 id="fb57a6b6" style="font-size: 20; line-height: 28px; margin: 16px 0 5px 0"><span class="ne-text">原用意</span></h3><p id="ueb2cc0b5" class="ne-p" style="margin: 0; padding: 0; min-height: 24px"><br></p><p id="ue08fa132" class="ne-p" style="margin: 0; padding: 0; min-height: 24px"><span class="ne-text">之前是为了少一次序列化操作，节约一些 cpu，特别是在请求访问一些固定配置数据表时；但这样的编码格式增加了以后的维护成本，也增加了沟通成本，综合下来决定取消支持。另外一个原因是，找到了一种更加彻底的方式，就是游戏对外服缓存特性；</span></p><p id="u16e6e051" class="ne-p" style="margin: 0; padding: 0; min-height: 24px"><br></p><p id="u27879819" class="ne-p" style="margin: 0; padding: 0; min-height: 24px"><span class="ne-text">关于游戏对外服缓存特性可阅读 #76</span></p><p id="u39c95061" class="ne-p" style="margin: 0; padding: 0; min-height: 24px"><br></p><p id="ucb2ae8c6" class="ne-p" style="margin: 0; padding: 0; min-height: 24px"><span class="ne-text">这种方式不但能节约 CPU，还能减少传输、请求次数、无形中帮助游戏逻辑服做了一些防护性措施等，并且不破坏代码可读性。</span></p></details>

```java
@ActionController(1)
public class DemoAction {
    static byte[] helloReqData;

    static {
        // 这里模拟缓存数据，不用每次序列化
        HelloReq helloReq = new HelloReq();
        helloReq.theIndex = 100;
        helloReq.name = "to byte[]";
        helloReqData = DataCodecKit.encode(helloReq);
    }

    @ActionMethod(4)
    public byte[] testByte() {
        return helloReqData;
    }
}
```



[#79](https://github.com/iohao/ioGame/issues/79) light-jprotobuf 模块支持枚举

```protobuf
// 动物
message Animal {
  // id
  int32 id = 1;
  // 动物类型 - 枚举测试
  AnimalType animalType = 2;
}

// 动物类型
enum AnimalType {
  // 鸟
  BIRD = 0;
  // 猫
  CAT = 1;
}
```



对应的 java 代码

```java
@ProtobufClass
public enum AnimalType {
    /** 鸟 */
    BIRD,
    /** 猫 */
    CAT;
}

@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
public class Animal {
    /** id */
    int id;
    /** 动物类型 - 枚举测试 */
    AnimalType animalType;
}
```



[#84](https://github.com/iohao/ioGame/issues/84) 生成proto文件时,内容的顺序总是产生变化

实际使用过程中发现一个问题如下：当协议没有任何变化时，生成的协议文件依然会产生顺序变化。

例如：当前的协议定义有 A，B，C 三个类。如果对协议进行多次生成,产生的proto文件内的定义顺序可能是：A，C，B 或者 B，C，A。







**标记废弃的**

将 IoGameGlobalSetting.me() 方法标记为废弃的，将 IoGameGlobalSetting 内的方法改为静态的。



**其他更新**

<netty.version>4.1.91.Final</netty.version>

优化文档生成、控制台打印。

ExternalMessage.proto 新增 message ByteValueList，用于接收与发送 List 类型的数据

```java
... 省略部分代码
// pb 对象 list 包装类
message ByteValueList {
  // pb 对象 List、pb 对象 Array
  repeated bytes values = 1;
}
```



**注意事项**

此版本在游戏对外服协议中新增了 message ByteValueList 来支持 action 返回 List 类型，因此需要前端同学同步一下协议文件。

对外服的协议说明： https://www.yuque.com/iohao/game/xeokui



#### 2023-04-03 - v17.1.37

UserProcessor 如果自身提供了 Executor ，将不会使用框架的 Executor 创建策略。



调整 DefaultUserProcessorExecutorStrategy 创建参数。



**DebugInOut** 增加当前执行业务的线程名打印



[[#77](https://github.com/iohao/ioGame/issues/77)] RequestMessageClientProcessorHook 提供新的默认实现 DefaultRequestMessageClientProcessorHook，通过 userId 取得对应的线程来处理业务逻辑，避免并发问题；



重新编写 [ioGame 线程相关的文档](https://www.yuque.com/iohao/game/eixd6x)。详细的介绍了网络部分的线程及扩展、业务框架部分的线程及扩展；单玩家如何避免多线程并发的、同一房间内多个玩家如何避免多线程并发的。



#### 2023-03-27 - v17.1.35

https://www.yuque.com/iohao/game/ab15oe#xsmCW

移除废弃的 UserProcessor ChangeUserIdMessageBrokerSyncProcessor

[#62] RequestMessageClientProcessor 中的 FlowContext 增加路由、响应对象、用户id 相关的属性设置。

UserProcessor DefaultUserProcessorExecutorStrategy 用户线程池策略变更:
UserProcessor RequestMessageClientProcessor 请求处理单独一个池
其他的 UserProcessor 共用一个池

领域事件部分代码整理

将 BrokerClientHelper.me() 标记为 Deprecated
请使用 BrokerClientHelper 的静态方法，如 BrokerClientHelper.me().xxx() 改为 BrokerClientHelper.xxx()

#### 2023-03-02 - v17.1.34

[#47] 增加拒绝外部直接访问 action 的路由权限
有些 action 只能内部访问，比如增加金币、敏感数值的增加等。这些 action 是不能由外部直接访问的，这里说的外部指的连接的玩家。

[#45] 游戏对外服独立 UserSession 管理部份逻辑，做成单独的 Handler
[#54] 动态属性接口增加消费操作

废弃 
将 ExternalBizHandler 标记为废弃，由 UserSessionHandler、AccessAuthenticationHandler、RequestBrokerHandler 三个 Handler 代替。其一，这样更符合单一职责；其二，之前会有开发者将 ExternalBizHandler 重写或替换后，没有添加用户管理与路由访问权限管理的相关逻辑，导致逻辑与预期不符；

查看 https://www.yuque.com/iohao/game/ea6geg#noCqn

#### 2023-02-13 - v17.1.33

https://www.yuque.com/iohao/game/ab15oe#w9uZB

action 业务参数自动装箱、拆箱增强。
支持 action 接收与返回基础类型：int、long、boolean 和字符串 String。
支持 action 接收与返回多个基础类型：List<Integer>、List<Long>、List<Boolean> 和 List<String>

关于 action 业务参数、返回值的自动装箱、拆箱更详细的使用文档，可以参考：业务参数自动装箱、拆箱基础类型

[#38] 新增业务参数自动装箱、拆箱基础类型-boolean

[#39] 新增业务参数自动装箱、拆箱"基础"类型-String
[#42] 新增业务参数自动装箱、拆箱基础类型-int
[#43] 新增业务参数自动装箱、拆箱基础类型-long
修复 ExternalServerBuilder.channelPipelineHook 设置业务编排钩子接口被默认实现覆盖的问题

【废弃与替换】
标记废弃 IntPb ，由 IntValue 代替
标记废弃 IntListPb ，由 IntValueList 代替
标记废弃 LongPb ，由 LongValue 代替
标记废弃 LongListPb ，由 LongValueList 代替

废弃原因-1：属性名称不统一
废弃原因-2：命名以 pb 结尾，这会导致在使用非 pb 协议时，会产生歧义

不兼容说明
如果开发者在之前的版本中的 action 使用的是 int、long 作为接收与返回参数的，此次版本不会有兼容问题。
如果使用的是 IntPb、IntListPb、LongPb、LongListPb 将会有不兼容的情况，因为当前版本已将 int、long 基础类型装箱、拆箱的类型解析器，替换为新版本的解析器。

如果想兼容旧版本的，可以通过下面的代码来做兼容。如果条件允许，请使用新版本，因为这得到了统一。
注意：由于此版本更新了 pb 原文件，需要前端同学重新生成一次协议。pb 原文件可查看：对外服协议说明。
理论上前端不做更改问题也不大，只是生成的对接文档上有差异，如果后端使用了基础类型做接收参数或返回值的，原 IntPb 由 IntValue 代替，原 LongPb 由 LongValue 代替。
其次影响是如果将来打算换成 json 协议来对接，会导致不兼容的情况，如果条件允许，请前端同学尽早做变更。

#### 2023-02-03 - v17.1.31

https://www.yuque.com/iohao/game/ab15oe#dPVPB

#37 缩小打包后的包体大小，ioGame 打 jar 包后大约 15MB，演示查看 快速从零编写服务器完整示例。

#36 增加 Banner 打印版本、内存占用、启动耗时等信息。
ioGame 在内存占用、启动速度、打包等方面也是优秀的。
- 内存方面：内存占用小。
- 启动速度方面：应用通常会在 0.x 秒内完成启动。
- 打包方面：打 jar 包后大约 15MB ，详细请看 快速从零编写服务器完整示例。

#34 日志模块增加
用于将框架日志单独存放，与开发者项目的日志分离。
日志支持 log4j2、logback

#I6BE6J
MethodParsers 增加 action 参数解析器的默认设置

#I6C0UF
业务参数自动装箱、拆箱基础类型增强

#I6B76X
修复广播的数据为空时，广播虽然是成功的，但是打印广播日志报错的问题

移除一些第三方库
为缩小打包，将 hutool 依赖移除、将 fastjson2 依赖配置中的 scope 改为 provided；如有使用到相关的，需要开发者自行引入。
移除项目中 json 相关的类文件 ToJson。

#### 2023-01-14 - v17.1.29

https://www.yuque.com/iohao/game/ab15oe#pqHj1
修复文档生成时的路径问题

#### 2022-12-29 - v17.1.28

https://www.yuque.com/iohao/game/ab15oe#Y38Hx

增加版本标识
增加版本标识，并在 DebugInOut 插件中显示的打印。

业务框架处理器
增加 ActionCommandTryHandler 并作为默认的业务框架处理器。
业务框架默认是在 bolt 线程运行，如果有异常会被 bolt 捕获到日志文件中。由于控制台没有显示的打印异常信息，开发者如果对 bolt 不熟悉，是不知道有该日志文件的。当出现异常时，将会浪费开发人员的时间来排查问题，为了避免这种情况，业务框架先做捕获，并打印到控制台中，在向上抛 ex。

bolt 日志文件相关 https://www.yuque.com/iohao/game/derl0laiu2v0k104#likQv

#### 2022-12-14 - v17.1.27

https://www.yuque.com/iohao/game/ab15oe#KR00k

命令解析器与源码文档逻辑分离。
优化命令对象，减少 action 类的实例化对象

#30 简化元附加信息的使用 使用文档

在处理 action 时，我们可以通过 FlowContext.userId 可以很方便得到当前用户（玩家）id。

如果开发者想在处理 action 时，携带上一些自定义的信息时，可以通过元附加信息特性来完成。比如保存当前玩家的英雄角色 id，或者玩家的昵称，又或者是你的项目的 userId 是 string 或其他类型则可以通过元信息这一特性来兼容。

简单的说，就是你想在 FlowContext 中获取一些用户（玩家）特有的信息数据时，可以通过这个特性来实现。

#### 2022-12-06 - v17.1.26

https://www.yuque.com/iohao/game/ab15oe#Qzog2

#27
业务框架与网络通信框架解耦
新增 ChannelContext 通信通道接口，用于对 bolt AsyncContext、netty Channel 的包装，这样可以使得业务框架与网络通信框架解耦，为将来 ioGame 实现绳量级架构的使用做准备。

移除 FlowAttr.asyncContext 动态属性，由 FlowAttr.channelContext 代替。
业务框架模块移除网络相关的依赖 bolt、netty 等。

#28
游戏对外服 netty 编排业务钩子接口
新增 ChannelPipelineHook netty 业务编排的处理器钩子接口，用于游戏对外服。

ExternalServerBuilder 新增 channelPipelineHook 属性 ，用于自定义 netty 业务编排的处理器设置。
废弃标记 ExternalServerBuilder.channelHandlerProcessors 属性及其相关地方为过期，由 ChannelPipelineHook 来代替，使用示例如下。

#### 2022-11-29 - v17.1.25

 https://www.yuque.com/iohao/game/ab15oe#VghQ6

ActionCommandInfoBuilder 改名为 ActionCommandParser 命令解析器
将业务框架中的部分类设置为 final 权限
废弃动态属性 FlowAttr.data ，由 FlowAttr.actionBizParam 代替

#I63L7V BarSkeletonBuilderParamConfig 类的方法名变更
addActionController 标记为废弃，请使用 scanActionPackage。
addActionSend 标记为废弃，请使用 scanActionSendPackage。

因为 addActionController 、addActionSend 的方法名，会给部分开发者带来混淆。这导致部分开发者在使用时 addActionController 方法时，会多次添加 action 类。实际上只需要随意配置一个 action 类就可以了，即使有一万个action。

#I63L89 标准 action 规则
1. 业务方法上添加注解 ActionMethod
2. 业务方法的访问权限必须是：public
3. 业务方法不能是：static
4. 业务方法需要是在 action 类中声明的方法
简单的说，标准的 action 是非静态的，且访问权限为 public 的方法。
术语说明：在 action 类中提供的业务方法通常称为 action

#I6307A 支持多种协议：protobuf、json，并支持可扩展
现在 ioGame 支持同样的一套业务代码，无需开发者做代码的变更，就能支持多种协议的切换，如：protobuf、json。协议的切换是简单的，只需要一行代码。

简单点说，如果开发者的项目之前使用的是 json 数据来传输的，以后想改用 protobuf 来传输，是不需要改变业务方法的。框架除了内置支持的 protobuf、json 协议外，开发者还可以对协议进行扩展。

#### 2022-11-14 - v17.1.23

#I6032U
BrokerServerBuilder 游戏网关构建器中增加移除 UserProcessor 的方法

#I60L94
提供 UserProcessor 用户线程池设置策略。分离 IO 线程池与用户线程池，这样服务器可以在同一时间内处理更多的请求。

框架提供 UserProcessorExecutorStrategy 接口，主要用于给 UserProcessor 构建 Executor 的策略，这样更具有灵活性；开发者可以根据自身业务来做定制。

see IoGameGlobalConfig.userProcessorExecutorStrategy

框架会在启动时，如果检测到 UserProcessor 实现了 UserProcessorExecutorAware 接口，就会触发一次。

#I60R41
修复动态绑定游戏逻辑服不能取消，不能路由到其他游戏逻辑服的问题

#I60R3A
废弃 BrokerGlobalConfig ，由 IoGameGlobalConfig 代替。
当前大版本会兼容 BrokerGlobalConfig 配置，下个大版本将会移除 BrokerGlobalConfig；
BrokerGlobalConfig 有点表示游戏网关全局配置的意思，名字不太理想。

#### 2022-11-08 - v17.1.22

#I5YM30
提供 action 调试工具-模拟客户端的需要数据
具体 https://www.oschina.net/news/216923/iogame-17-1-22-released

2022-10-31
v17.1.21
（#I5YEZ5 、#I5VIGE 、#I5VHPC 、#I5U89Q 、#I5SLGJ 、#I5RXOZ 、#I5QZU7 、#I5LY33 、#I5KQT5 、#I5CYK7 、#I5BDO7 、#I5YEZU ）

#I5YEZ5
移除游戏网关的 spring 依赖，之前使用了 spring 的日志彩色打印，改为使用 logback 提供的。

#I5SLGJ
现在 ioGame 的 JSR 校验支持 Jakarta 和 Javax 两种，基于 java SPI 实现；
支持 Javax 是为了兼容一些老项目迁移到 ioGame 中；
新项目推荐使用 Jakarta，通常在 ioGame 的项目中，开启 JSR380 验证规范时，默认使用的是 Jakarta；

在示例目录中，单独的增加了 Jakarta 和 Javax 的使用示例。

#I5RXOZ
SimpleRunOne 中 startup 优化

#I5YEZU
框架提供 cmd 路由对应的响应数据类型信息，方便后续做"模拟客户端" 支持，这样可以做到不需要手动编写测试代码，
也可以对业务方法 action 进行测试，这将大量减少游戏开发者的测试时间；

大概意思是，开发者编写了 10 个模块，平均每个模块下有 8 个 action ，这一共是 80 个 action；
通常我们为了测试这 80 个 action ，会需要模拟客户端，写 80 个相关的请求端来访问我们的 action。
当使用上“模拟客户端” 将不在需要编写这些，这大量的降低的开发者的工作量；

#### 2022-09-26 - v17.1.20

（#I5SLRA、#I5S8QC、#I5SG8T、#I5RXMY、#I5SS3C）

#I5SLRA
移除 HeadMetadata 类的 cmd、subCmd 属性，进一步减少传输，因为 cmd、subCmd 属性数据可以通过 cmdMerge 计算出来；

假设你的游戏请求量可以达到每秒 100W 次时，至少可为服务器每秒节省大约 8 * 1000000 / 1024 / 1024 = 7.63 MB 的传输量；
当请通讯方式是 请求/响应 类型时，每秒节省大约 7.63 * 2 = 15.26 MB;

#I5S8QC
light-timer-task，将任务延时器的任务数量默认值 2_000 --> 10_000

#I5SG8T
将 MsgException 修改为运行时异常；下面两个 action 的业务逻辑处理是等价的，其中一个显示的声明了 throws。
https://www.yuque.com/iohao/game/avlo99#fTn4A

#I5RXMY
JSR380 新增参数分组校验，分组校验在 web 开发中很常见，现在 ioGame 也支持这一特性了。
https://www.yuque.com/iohao/game/ghng6g#DMPBH

#I5SS3C
修复：light-profile getInt getBool 无法获取数据

#### 2022-09-08 - v17.1.18

（#I5PU5R、#I5PFGP、#I5NDEG、#I5Q1AO、#I5Q89K）

#I5PFGP
新增 unity 与 ioGame 综合示例的联调示例，此 unity 连接示例由 licheng 提供。之前赵少提供的 unity 示例，由于我没 fork, 估计项目被删除了（原因是 git 仓库满了，就删除了）
u3d 连接示例语雀在线文档 https://www.yuque.com/iohao/game/syv5mm

#I5Q1AO
语雀在线文档 https://www.yuque.com/iohao/game/gmfy1
领域事件变更默认配置
DomainEventContextParam.producerType 由 ProducerType.SINGLE 改为 ProducerType.MULTI
DomainEventContextParam.waitStrategy 由 BlockingWaitStrategy 改为 LiteBlockingWaitStrategy

#### 2022-08-29 - v17.1.17

#I5O5QH
新增集群日志开关 BrokerGlobalConfig.brokerClusterLog，默认为 true

#I5NUGF
之前框架只会在玩家没有登录的时候，临时的使用了一下这个属性来存放 netty 的 channelId。
原因是不想为了这一次的使用，而特意声明一个变量来存放 netty 的 channelId。
简单点说就是在玩家没有登录时，开发者是不能使用该属性的。
只登录了的玩家，框架才不会占用该属性来存放 netty 的 channelId。

现在改为即使没有登录，框架也不占用 HeadMetadata.attachmentData 属性了，开发者可以放心的使用了；
比如，用于在登录时，拿到玩家IP，用于封号相关和其他的（比如日志、等）

具体使用可参考 https://www.yuque.com/iohao/game/sw1y8u

#I5K3WE
增强顶号强制下线的问题，在发送强制离线时，需要先给个客户端发个错误码。
防止客户端会自动进行重试再次连接。
这里顶号服务端主动断开连接，如果服务器不发送错误码，会导致2个客户端来回顶号的情况。

#I5OB8T
修复 JSR380 验证相关缺少判断

#### 2022-08-23 - v17.1.13

#I5N97K
(可跨进程通信) 游戏逻辑服与同类型逻辑服通信，超时问题；
在之前的版本中，使用通讯方式 -- 游戏逻辑服与同类型逻辑服通信时，当目标游戏逻辑服中的业务处理时间过长时，框架不能按开发者的预期值返回（相当于超时了），现在改为默认是在调用端进行阻塞，直到得到目标游戏逻辑服返回的结果为止；
新增 SyncRequestMessageClientProcessor、SyncRequestMessage 来处理超业务逻辑时间比较长的业务。
这个改进对开发者是无感知的；
使用的处理方式还是： CompletableFuture、ForkJoinPool 等，即总耗时为最长那个业务逻辑的耗时；如，A 用时 1 秒、B 用时 2 秒、C 用时 2.2 秒，那么总耗时大概是 2.2 秒；
但如果超过在 BrokerGlobalConfig.timeoutMillis 中设置的时间，仍然会做报错的提示，防止开发者因为编写业务逻辑时引发的问题；

#I5N97H
自定义 FlowContext
开发者可以自定义 FlowContext 方便做一些方法上的扩展，这些定制可以更好的服务于开发者的项目；业务框架新增 FlowContextFactory 业务框架 flow 上下文工厂，用于创建自定义的 FlowContext。通常使用自定义的 FlowContext，一般用于为 FlowContext 添加上一些方法；
框架默认提供的 FlowContext 已经将 invokeXXX 系列方法给标记为 Deprecated ，因为这是前期的一个 API 错误设计，invokeXXX 系列方法严格来说不算是 FlowContext 的职责；但在实际的业务开发中，开发者自定义的 FlowContext 是可以这么做的；
对于更详细的描述与具体使用，可以参考 https://www.yuque.com/iohao/game/zz8xiz#sLySn 在线文档

#### 2022-08-16 - v17.1.10

#I5LL08
逻辑服务之间通信相关的扩展增强方式

#I5MDBA
游戏网关的扩展增强 - 元素选择器生产工厂

#I5KQXM、#I5L1P
游戏对外服、动态绑定逻辑服节点相关

2022-08-08
v17.1.9

#I5KR7T
UserHook quit方法中调用ExternalKit.requestGateway(userSession, requestMessage); 抛出异常
https://gitee.com/iohao/iogame/issues/I5KR7T

#I5KZTP
断言抛异常没有带异常信息
https://gitee.com/iohao/iogame/issues/I5KZTP

#### 2022-08-04 - v17.1.5 ~ v17.1.8

（#I5K3WE、#I5KLED、#I5KLDA、#I5KMXT、#I5KMYK）

#I5K3WE
顶号强制下线时，发送一个错误码给连接端


#I5KLDA
新增：原生 PB 与 jprotobuf 互转示例，具体查看示例源码的 spring-websocket-native-pb-client 模块
示例文档：https://www.yuque.com/iohao/game/ruaqza



#I5KMXT
框架内置可选模块 light-jprotobuf
变更 light-jprotobuf 生成 .proto 文件时，sint64、sint32 改为 int64、int32。
使用 sint64、sint32 类型，需要使用原生的 jprotobuf 。light-jprotobuf 暂时不打算支持。
文档地址： https://www.yuque.com/iohao/game/vpe2t6


#I5KLED
CocosCreator 与综合示例联调（登录）
前端示例代码：https://gitee.com/iohao/io-game-cocos
综合示例代码：https://www.yuque.com/iohao/game/ruaqza#SWzpv
服务器示例启动类： SpringGameOneApplication


#I5KMYK
更新对外服数据协议 ExternalMessage

使用 jprotobuf 明确协议类型

重要：如果项目开发中，出现协议数据对不上的，建议升级到这个版本，前端的哥们也需要更新一下 .proto 文件。具体看 issu 描述。


打 jar、docker 部署、全局重复路由检测

移除示例中 EnableZigZap 注解的使用，减少相关理解。

#### 2022-07-28 - v17.1.4 

（#I5J96X、#I5J7JU、#I5J7HG、#I5J7GI、#I5J7G5、#I5IS7V、#I5IEXO）

-- 3类通讯方式相关 --
新增：【游戏逻辑服】访问多个【游戏对外服】的上下文。
具体可以查看 https://gitee.com/iohao/iogame/issues/I5J7JU

新增：游戏对外服扩展
游戏对外服新增 ExternalBizRegion 接口，开发者可以更好地扩展游戏对外服，通过这个接口与本次新增的上下文配合使用；开发者可以通过实现这个接口，向游戏逻辑服提供一些，如
1. 只存在于游戏对外服中的数据
2. 只有游戏对外服可以做的事

框架通过这一扩展，在不到 15 行的有效代码中，就轻松实现了重复登录、顶号功能；
● ExistUserExternalBizRegion 查询用户（玩家）是否在线
● ForcedOfflineExternalBizRegion 强制用户（玩家）下线
重复登录 具体查看 https://gitee.com/iohao/iogame/issues/I5J7G5
顶号 具体查看 https://gitee.com/iohao/iogame/issues/I5J7GI
ExternalBizRegion 扩展具体查看 https://www.yuque.com/iohao/game/ivxsw5


新增：元信息 - 附加信息 - 在 action 中得到附加数据
废弃 HeadMetadata.extJsonField 字段，由 attachment 字段代替，attachment 为 byte[] 类型。

在处理 action 时，我们可以通过 FlowContext.userId 可以很方便得到当前用户（玩家）id，userId 实际的保存地方是游戏对外服的 UserSession 中。每次请求时会在游戏对外服中，将当前 userId 设置到 RequestMessage 的元信息对象中；如果开发者想在处理 action 时，携带上一些自定义的信息时，可以通过 HeadMetadata.attachmentData 属性来完成。

假如我们想要把 channel 的 ip（由于这些数据只在游戏对外服中），携带上到 action 业务处理中得到，我们就可以把这些数据设置到 HeadMetadata.attachmentData 属性中，在游戏对外服转发请求到网关之前设置就好的。

当设置好需要携带到游戏逻辑服的数据时，我们可以通过 FlowContext.getAttachment () 方法来获取我们在游戏对外服设置的数据

具体查看 https://gitee.com/iohao/iogame/issues/I5J96X

新增：严格登录，路由访问权限的控制
严格登录指的是，如果玩家没有登录，是不能访问其他业务方法的（即 action）。
var accessAuthenticationHook = ExternalGlobalConfig.accessAuthenticationHook;
// 表示登录才能访问业务方法
accessAuthenticationHook.setVerifyIdentity(true);
// 添加不需要登录（身份验证）也能访问的业务方法 (action)
accessAuthenticationHook.addIgnoreAuthenticationCmd(1, 1);

/** 通常这段代码放到游戏对外服中，因为是在游戏对外服做的权限控制。 */

当 setVerifyIdentity = true 时，是不能访问任何业务方法的，包括开发者编写的登录业务方法。但我们可以放开权限，使得玩家可以访问我们的业务方法。上面的伪代码中，放开了路由 1-1 的访问权限，这样不需要登录也可以访问游戏逻辑服的业务方法了。

默认配置下，setVerifyIdentity = false ，就是不登录就可以访问所有的业务方法。

具体查看 https://gitee.com/iohao/iogame/issues/I5J7HG

#### 2022-07-19 - v17.1.3

 (#I5H8NV、#I5H8H9)

#I5H8H9
增强：相同路由可以用在 action 与广播上；当 action 返回值为 void 时，可以复用路由来作为广播的响应路由。

#I5H8NV
-- 3类通讯方式相关 - 广播，新增 --
广播推送添加新成员，新增顺序特性成员。 BroadcastOrderContext
BroadcastOrderContext 可以确保消息是严格顺序的

```java
... ... 省略部分代码
// 得到严格顺序的广播上下文
var broadcastOrderContext = BrokerClientHelper.getBroadcastOrderContext();

// 使用示例
for (int i = 0; i < 10; i++) {
    BarHelloPb helloPb = new BarHelloPb();
    helloPb.amount = i;

    broadcastOrderContext.broadcastOrder(cmdInfo, helloPb);
}
```

如果没有特殊业务需求，建议使用 BroadcastContext；
因为实际的业务中，关于战斗中的广播都是会设置一个类似帧率的参数，只要不是太密集基本都没问题，
所以我们可以巧妙的利用帧率间隔来达到顺序广播的目的；

BroadcastContext 与 BroadcastOrderContext 的使用方式上基本是一致的；



#### 2022-07-11 - v17.1.2

 (#I5G0FC、#I5GB1D)

3类通讯方式，逻辑服间的相互通信相关
InvokeModuleContext，新增无参请求方法，单个逻辑服与单个逻辑服通信请求 invokeModuleMessage

变更 BarMessage 成员变量类型（#I5G0FC）
BarMessage.dataClass 字段，由 Class<?> 类型改为 String类型
具体的原因可以查看 issu https://gitee.com/iohao/iogame/issues/I5G0FC

当建立多个项目时，在游戏逻辑服处理完业务却无法响应数据到请求端；但可以接收到来自请求端的请求数据； 由于 dataClass 变量是 Class<?> 类型的，如果游戏对外服、游戏网关服没有对应的 class 会造成解码失败，而引发 ClassNotFoundException

将 BarMessage.dataClass 改为 String 类型，这样即使没有在 游戏对外服、游戏网关、游戏逻辑服中引入 PB 业务类，也不会引发这些问题！

将 dataClass 改为 String 类型，这些改动不会对开发者造成影响！

新增综合示例 （#I5GB1D）
综合示例文档 https://www.yuque.com/iohao/game/ruaqza

综合示例内容包含
1. 多服多进程的方式部署
2. 多服单进程的方式部署（类似单体应用的方式部署与开发）
3. springboot 集成
4. JSR380验证
5. 断言 + 异常机制 = 清晰简洁的代码
6. 请求、无响应
7. 请求、响应
8. 广播指定玩家
9. 广播全服玩家
10. 单个逻辑服与单个逻辑服通信请求 - 有返回值（可跨进程）
11. 单个逻辑服与单个逻辑服通信请求 - 无返回值（可跨进程）
12. 单个逻辑服与同类型多个逻辑服通信请求（可跨进程）
13. 业务参数自动装箱、拆箱基础类型（解决协议碎片问题）
14. 游戏文档生成
15. 业务协议文件 .proto 的生成

JSR380相关
移除 JSR303 相关，使用符合 JSR380 标准的校验。这里推荐使用 hibernate-validator 用户需引入validation-api的实现，如：hibernate-validator。注意：hibernate-validator 还依赖了javax.el，需自行引入。
具体使用可以查看 https://www.yuque.com/iohao/game/ghng6g

增加，当触发 JSR380 验证时，会给请求端一些对应的错误信息

#### 2022-07-06 - v17.1.1

 (上传到中央仓库)

(#I5EE8E、#I5DFRM)

ioGame上传到中央仓库；

ioGame 版本规则 x.y.z
    x 表示当前使用的 JDK 版本
    y 表示 ioGame API变更版本 （基本上不会变动、常规下是变动 x 才会变动 API）
    z 表示 ioGame 新特性、新功能、新模块、bugfix 相关

    ioGame 的 x 会跟着最新的 JDK LTS 版本来走的，目的是确保 ioGame 的API 不会发生很大的变化。
    为了保持新活力与接受新鲜事物， ioGame 基本会用上最新的 JDK LTS;
    x 一般延后 1~4 个季度，给开发者一个缓冲。即下一个 JDK LTS 出来后，那么 ioGame 的 x 会在 1~4个季度后跟上。
    也就是说，下一个 x 将会是 21;

扩展库移到
https://gitee.com/iohao/ext-iogame

2022-06-30
(#I5EVQQ)
当开启登录验证时，客户端没登录而请求业务方法时，返回对应的错误码到请求端；

将网络游戏服务器框架的示例放到单独的 git 仓库；前期放在一起是为了方便运行演示，好给开发者进行一个快速的体验；
https://gitee.com/iohao/example-iogame

移除
BrokerClientContext.invokeModuleMessage
--> 请用 InvokeModuleContext.invokeModuleMessage(RequestMessage)

BrokerClientContext.invokeModuleCollectMessage
--> 请用 InvokeModuleContext.invokeModuleCollectMessage(RequestMessage)

参考 https://www.yuque.com/iohao/game/nelwuz#UwwUI


2022-06-27
v1.4.0 (#I5C57I、#I5DTZN)

新增业务参数自动装箱、拆箱基础类型
    int : IntPb
    List<Integer> : IntListPb

    long : LongPb
    List<Long> : LongListPb

文档 https://www.yuque.com/iohao/game/ieimzn

新增 ClientProcessorHooks
    业务框架处理请求时，开发者可以自定义业务线程编排，
    使得框架可以具备集成其他的并发框架的能力，可以无锁实现并发写的需求；
    即逻辑服之间通信时，可以同时写，且不需要开发者显示的加锁。
    建议配合 Disruptor 来使用，框架中提供了一个 Disruptor 的封装，领域事件 https://www.yuque.com/iohao/game/gmfy1k
文档 https://www.yuque.com/iohao/game/eixd6x

IdleProcessSetting
    方法名变更 idleHandler --> idlePipeline，
    因为与成员变量 ChannelHandler idleHandler 重名了，
    重名理论上是方法重载的情况，因为方法参数不同；
    但 lombok 似乎直接给覆盖了，导致成员变量 ChannelHandler idleHandler 不能设置

2022-06-08
v1.3.0 (#I5B8V4)
将模块之间的访问独立一个接口
新增单个逻辑服与单个逻辑服通信请求（可跨进程）无返回值
    如： 模块A 访问 模块B 的某个方法，但是不需要任何返回值
文档 https://www.yuque.com/iohao/game/anguu6#cZfdx

新增 InvokeModuleContext 接口，使得在3类通讯方式上的语义与使用上更明确


2022-06-04
监控相关的
    逻辑服数据上报
    监控逻辑服接收来自各逻辑服的

2022-05-30
v1.2.0 (#I599B9、#I59O74)
支持对外服的玩家绑定指定的游戏逻辑服（可以做到动态分配逻辑服资源）
    描述：
        支持对外服的玩家绑定指定的游戏逻辑服id，如果用户绑定了指定的游戏逻辑服id，之后与该游戏逻辑服的请求都由这个绑定的游戏逻辑服来处理
    场景举例：
        1. 什么意思呢？这里用匹配与象棋的场景举例。
        2. 假设我们部署了 5 台象棋逻辑服，在玩家开始游戏之前。我们可以在匹配服中进行匹配，当匹配逻辑服把A、B两个玩家匹配到一起了。
        3. 此时我们可以通过 访问【同类型】的多个逻辑服方法，当得到象棋房间数最少的象棋逻辑服后（这里假设是房间数最少的象棋逻辑服是《象棋逻辑服-2》），把《象棋逻辑服-2》的逻辑服id 绑定到 A、B 两个玩家身上。
        4. 之后与象棋相关的操作请求都会由《象棋逻辑服-2》这个游戏逻辑服来处理，比如：开始游戏、下棋、吃棋、和棋等。
        5. 也可以简单点把这理解成，类似 LOL、王者荣耀的匹配机制。在匹配服匹配到玩家后，把匹配结果中的所有玩家分配到一个房间（节点）里面。
        6. 这是一种动态分配资源最少的节点（逻辑服）的用法之一。
        7. 这个版本先做成只能绑定一个逻辑服的，因为暂时没有想到多个的场景。

        这是一种动态分配资源最少的节点（逻辑服）的用法之一。
        这个版本先做成只能绑定一个逻辑服的，因为暂时没有想到多个的场景。

新增示例
    示例目录 （#I599B9）
        https://www.yuque.com/iohao/game/lxqbnb
        钩子相关
        心跳钩子在项目中的使用
        用户上线、下线钩子在项目中的使用
    示例目录 （#I59O74）
        https://www.yuque.com/iohao/game/idl1wm
        用户动态绑定逻辑服节点

DebugInOut （业务框架插件机制）
    新增设置最小触发打印时间
    之前的是任何请求都打印，现在可以设置一个最小触发打印时间了，
    比如给 DebugInout 设置了 50 ms（构造重载），只有请求超过这个时间的请求才进行打印。

    ioGame ActionMethodInOut 是业务框架的插件机制。
    是很有用的，比如开发者想记录执行时间比较长的 action，可以通过该机制来做。
    通过这个接口，你可以做很多事情，当然这要看你的想象力有多丰富了
    https://www.yuque.com/iohao/game/pf3sx0

2022-05-23
v1.2.0
模块之间的访问，访问【同类型】的多个逻辑服 (#I58LNI)
    如： 模块A 访问 模块B 的某个方法，因为只有模块B持有这些数据，这里的模块指的是逻辑服。
    假设启动了多个模块B，分别是：模块B-1、模块B-2、模块B-3、模块B-4 等。框架支持访问【同类型】的多个逻辑服，并把多个相同逻辑服结果收集到一起
场景举例：
    【象棋逻辑服】有 3 台，分别是：《象棋逻辑服-1》、《象棋逻辑服-2》、《象棋逻辑服-3》，这些逻辑服可以在不同有进程中。
    我们可以在大厅逻辑服中向同类型的多个游戏逻辑服通信请求，意思是大厅发起一个向这 3 台象棋逻辑服的请求，框架会收集这 3 个结果集（假设结果是：当前服务器房间数）。
    当大厅得到这个结果集，可以统计房间的总数，又或者说根据这些信息做一些其他的业务逻辑；当然实际中不会这么做；这里只是举个例子。    实际当中可以发挥大伙的想象力。

示例文档
    https://www.yuque.com/iohao/game/rf9rb9

业务框架
    action 的返回值支持 null
    debugInout 新增逻辑服id、逻辑服类型的打印信息

SimpleHelper、SimpleRunOne、ClusterSimpleHelper、ClusterSimpleRunOne
的逻辑服参数改为由 BrokerClientStartup --> AbstractBrokerClientStartup

明确这是一个 BrokerClient 启动器
BrokerClientService --> BrokerClientApplication

2022-05-20
v1.0
异步化、事件驱动的架构设计；网关集群无中心节点、负载均衡
每个逻辑服都可以独立进程部署

新增 Broker 概念与 BrokerClient 概念
    Broker 相当于之前的游戏网关，BrokerClient相当于之前的逻辑服
    BrokerClient 负责与 Broker 建立连接

Broker 集群，集群使用 gossip 协议
简化游戏逻辑服的创建

业务框架新增
    DataCodec 开发者可以自定义业务参数的编解码

新增广播（推送日志）与相关示例

包名变更
此次的架构更新很大，相关的文档也进行了更新
支持逻辑服（BrokerClient）与游戏网关（Broker）的数量扩展，并能很好的进行负载均衡。

在结构上进行了明确
    1 业务框架目录 common
    2 网络通信框架目录 net-bolt
    3 内置模块目录
    4 游戏实战目录
    5 示例目录

game-collect --> example-game-collect
    明确这个目录是一个示例目录

后续计划
    日志传输、Metrics采集、染色日志、链路追踪。

Issues (#I57QAZ、#I510AK)

2022-05-08
新增 分布式锁-基于Redisson的简单实现 (#I53XW3)
参考： https://www.yuque.com/iohao/game/wz7af5

2022-04-13
新增 u3d、cocos 连接示例 和 tcp socket 的连接示例

u3d 连接示例 https://www.yuque.com/iohao/game/syv5mm
cocos 连接示例 https://www.yuque.com/iohao/game/ua4afq
tcp socket 示例 https://www.yuque.com/iohao/game/ywe7uc

游戏示例目录 game-collect 新增一些文档说明和单独启动逻辑服的方法

删除 game-test 示例目录

2022-04-06
为添加 alibroker 网络通信框架做准备 (#I510AK)
全异步化架构设计
https://alibroker.info/

独立 bolt 网络通信框架到单独目录 net-bolt。

2022-03-31
添加游戏部件抽象模块
    用于游戏实践的开发，进一步减少开发实践过程中的工作量
    只提供抽象骨架, 具体的逻辑实现由子游戏自定义

    提供抽象流程的有：
        游戏规则接口 RoomRuleInfoCustom
        创建玩家接口 RoomPlayerCreateCustom
        房间创建接口 RoomCreateCustom
        进入房间接口 RoomEnterCustom
        游戏开始接口 RoomGameStartCustom
    
    提供抽象类的有：
        抽象房间 AbstractRoom
        抽象玩家 AbstractPlayer
        业务操作接口 OperationHandler
    
    玩法操作的处理对象, 享元工厂 OperationFlyweightFactory

2022-03-27
新增 spring 集成 (#I4Z2HS)

移除 widget-common 模块

业务框架优化
    优化部分 list 结构改为 array
    DefaultActionFactoryBean 新增创建 action 混合特性

业务框架文档相关
    自动生成系统错误码到 doc_game.txt 文件中，不需要在手动配置

新增示例
    example/example-for-spring
    spring集成 相关 DemoSpringBootApplication.java

    example/example-interaction
    多逻辑服相互交互的 DemoInteractionApplication.java

FlowContext 新增方法
    getCmdInfo 路由信息
    invokeModuleMessageData （请求其他子服务器（其他逻辑服）的数据）
    broadcast (广播)

changeName
    ClientStartupConfig --> ClientStartup
    ServerStartupConfig --> ServerStartup
    ActionControllerFactoryBean --> ActionFactoryBean
    InOutInfo --> InOutManager
    AbstractExternalClientStartupConfig --> AbstractExternalClientStartup

2022-03-21
新增 UserSession (与 channel 是 1:1 的关系，可取到对应的 userId、channel 等信息。 )

FlowContext 新增动态属性
    FlowOptionDynamic、FlowOption

登录相关
    用户登录相关移 除虚拟userId
    新增 UserIdSettingKit，简化开发者登录业务

优化业务框架
    统一业务入口
    优化 FlowContext

删除一些遗留代码 game-logic-all 相关


2022-03-14
新增心跳相关设置 IdleProcessSetting
    心跳事件回调 IdleCallback
    心跳 handler IdleHandler
    (#I4XSCD)


新增ExternalJoinEnum:
    UDP (udp socket 预留扩展)
    EXT_SOCKET (特殊的预留扩展)

新增用户钩子接口 UserHook 上线时、下线时会触发
    利用好该接口，可以把用户当前在线状态通知到逻辑服，比如使用 redis PubSub 之类的。
    (#I4XSCH)

简化对外服务器 - 构建器 ExternalServerBuilder

2022-03-11
动态属性示例

2022-03-10
Copyright

2022-03-08
业务框架新增 InOutInfo 管理插件相关
    把插件的执行放入 InOutInfo 中。
    当只有一个插件时，不走 foreach

2022-03-07
新增基于 FXGL游戏引擎的游戏示例(坦克射击)
    TankApp.java
新增基于FXGL的JAVA游戏引擎，示例 文档

网络游戏框架 change name, ioGame


2022-03-01
新增简单的启动器 SimpleRunOne
简化对外服 ExternalServer 的内部逻辑服启动
文档更新
移除 example 的示例，只保留少量


2022-02-25
文档更新:快速从零编写服务器完整示例
DebugInOut
    打印微调
    打印用户自定义异常的msg

新增 快速从零编写服务器完整示例代码
    HelloReq
    DemoAction
    DemoLogicServer

    DemoApplication
    DemoWebsocketClient

BarSkeletonBuilderParamConfig 新增构建 BarSkeletonBuilder 方法

2022-02-23
文档更新


2022-02-21
游戏实践 game-one pom 添加 maven-assembly-plugin 打包 jar, 方便测试
业务框架 bugfix , 业务文档相关 在打包后没有java源码引发的 null
游戏框架对外服 默认连接协议改为 WEBSOCKET

2022-02-11
编写游戏框架文档
    通信协议
    Action的业务参数获取
    快速入门示例
    用户连接登录编写
    对外服的协议说明
    Action
    路由 - 术语
    异常机制
    业务框架的构建器
    开启JSR303+验证规范

2022-02-07
编写业务框架文档 https://www.yuque.com/iohao/game
    异常机制
    业务框架的构建器
    游戏开发需要具备的知识
    业务文档的生成
    业务PB的生成

    FlowContext
    网关服的编写
    逻辑服的编写
    对外服的编写

新增对外服逻辑服的抽象类 AbstractExternalClientStartupConfig

2022-02-03
新增 游戏（错误码）文档的生成
新增 游戏（异常信息）文档的生成

游戏实践新增大厅服
    与游戏逻辑服关联不强的业务逻辑或比较通用的业务就写在大厅服中
    比如登录... 等.

2022-02-02
删除 DocActionBroadcast 统一使用 DocActionSends 来生成推送文档

新增 BarSkeletonBuilderParamConfig 构建参数的配置

新增注解 DocActionSends 消息推送文档的生成
    配合 DocActionSend 生成多条推送文档

2022-02-01
新增抽象推送 AbstractFlowContextSend
新增推送文档注解
    用于补充推送相关的文档，因为推送是不是用户主动发起的请求，
    所以需要单独标记，以便业务框架生成推送文档。
    文档在生成还未实现

新增动态属性 AttrOptionDynamic 接口
    配合 AttrOption 可以更明确动态属性的类型

FlowContext 的动态属性由 AttrDynamic 改为 AttrOptionDynamic
    可以更明确动态属性的类型

移除业务框架参数解析器的 userId 解析，全部由 FlowContext 接管，
因为 FlowContext 是流程上下文。

2022-01-30
移除广播小部件 模块 (感觉设计过于凌乱)
独立广播操作，将广播内嵌到（逻辑服、网关、对外服）中
新增客户端模拟
修复广播时的 bug （在逻辑服传输数据到网关时，response.data 对象如果没有实现 Serializable 会异常）


2022-01-29
新增游戏文档 广播相关(简单的业务方法版)

2022-01-25
新增轻量小部件 light-jprotobuf
    简化 jprotobuf 的编写方式

    不在需要 @Protobuf(description = "xxx") 来生成注释了
    现在 jprotobuf 的类中，注释即文档
    
    可将多个 jprotobuf 类 合并生成为一个原生的 .proto 文件， 不使用 jprotobuf 提供的生成插件，
    因为 jprotobuf 生成的 .proto 文件太乱（会有重复的文件类），在阅读时也不方便

2022-01-24
坦克 pb 更新

2022-01-23
业务框架支持文档生成, java代码即文档
业务框架日志可定位代码行数
    debug插件
    启动时
删除部分编解码代码

2022-01-22
新增路由错误码： 一般是客户端请求了不存在的路由引起的
网关增加 路由错误码 逻辑，如果客户端请求了不存在的路由，直接响应错误
新增 websocket  编解码
增加 websocket 数据压缩扩展
对外服传输协议改为纯 PB

2022-01-21
网关新增路由检测:
    对外服请求逻辑服时
    如果没有找到对应的逻辑服来处理请求，不在往逻辑服发送
    并立即返回错误码给对外服

2022-01-20
对外服的 socket 和 websocket 共用一个业务处理类 ExternalBizHandler (负责把游戏端的请求 转发给网关)
对外服的 socket 和 websocket 接收来自网关的响应也逻辑也保持一至

2022-01-19
登录业务 新增 ChangeUserIdKit 工具：
    变更对外服的 userId, userId与channel 关联
    用在用户登录时，从程序员的业务数据库中获得 userId

新增用户登录, 登录流程：
    真实客户端发送请求 --> 对外服 --> 网关服 --> 逻辑服（
        逻辑服通知对外服变更：逻辑服 --> 网关服 --> 对外服 --> 网关服 --> 逻辑服
    ） --> 网关服 --> 对外服

业务框架:
BarMessage 增加 rpcCommandType 字段:
    特性如下：
        在 bolt 中， 调用方使用
        （com.alipay.remoting.rpc.RpcServer.oneway
        或 com.alipay.remoting.rpc.RpcClient.oneway）的 oneway 方法

        则 AsyncContext.sendResponse 无法回传响应
        原因可阅读 com.alipay.remoting.rpc.protocol.RpcRequestProcessor.sendResponseIfNecessary 源码。
    
        业务框架保持与 bolt 的风格一至使用 RpcCommandType
        不同的是业务框架会用 RpcCommandType 区别使用什么方式来发送响应。
    
        如果 rpcCommandType != RpcCommandType.REQUEST_ONEWAY ,
        就使用 com.alipay.remoting.AsyncContext.sendResponse 来发送响应
        具体发送逻辑可读 DefaultActionAfter 源码

2022-01-18
优化业务框架: 新增 action void 特性， 定义为 void 的业务方法，不在给调用端响应（除非有业务异常码）
优化业务框架: DebugInOut 日志可以支持 JSR 303、JSR 349、JSR 380 验证规范 的日志
优化业务框架: Flow 流程，在开启业务参数验证规范功能时，业务参数如果验证不通过,则直接响应带有错误码的消息给调用端

2022-01-17
业务框架支持 JSR 303、JSR 349、JSR 380 验证规范
业务框架新增 flow 上下文 (FlowContext), 生命周期存在于当前执行流程

2022-01-16
对外服 新增接收并处理 来自网关的广播消息
广播小部件 新增广播上下文

2022-01-15
整合 protobuf、mapstruct、lombok 的使用
新增坦克游戏相关 pb
规范子游戏的 cmd 编写方式

2022-01-14
业务框架加强规范异常处理
提供异常全局统一处理规范
领域事件新增默认异常处理

2022-01-13
对外服务器 支持 websocket

2022-01-12
业务框架支持 proto
实战(网关、对外服、逻辑服)，简化配置

2022-01-11
编写对外服务器
对外服务器连接到网关

2022-01-07
轻量小部件多环境切换

2022-01-02
boot 加载项 BootConfig
动态属性

2021-12-26
轻量小部件-领域事件
1. 领域驱动设计，基于LMAX架构。
2. 单一职责原则，可以给系统的可扩展、高伸缩、低耦合达到极致。
3. 异步高并发，线程安全的并且基于lmax架构。可并发执行，性能超高，执行1000W次事件只需要1.1秒左右(这个得看你的电脑配置)。
4. 使用事件消费的方式编写代码，使得业务在复杂也不会使得代码混乱，维护代码成本更低。
5. 插件形式提供事件领域，做到了可插拔，就像玩乐高积木般有趣。

2021-12-21
业务框架更新
change ActionMethodInOut method
新增 InOutContext 上下文, 方便扩展 ActionMethodInOut
新增 ActionMethodInOut 实现类DebugInOut 用于开发时，打印一些参数
如:
┏━━━━━ Debug [BeeAction.java] ━━━ [.(BeeAction.java:1).hello]
┣ 参数 : beeApple : BeeApple(id=101, content= jackson )
┣ 返回值: : hello:
┣ 方法名: hello
┣ 时间 : 1 ms (业务方法总耗时)
┗━━━━━ Debug [BeeAction.java] ━━━

2021-12-20
完善客户端示例与启动流程
整合 sofa-bolt

2021-12-15
初始化项目
编写业务框架
