<h2 align="center" style="text-align:center;">
  ioGame
</h2>
<p align="center">
  <strong>无锁异步化、事件驱动的架构设计；轻量级，无需依赖任何第三方中间件或数据库就能支持集群、分布式</strong>
  <br>
  <strong>通过 ioGame 可以很容易的搭建出一个集群无中心节点、集群自动化、多进程的分布式游戏服务器</strong>
  <br>
  <strong>包体小、启动快、内存占用少、更加的节约、无需配置文件、提供了优雅的路由访问权限控制</strong>
  <br>
  <strong>可同时支持多种连接方式：WS、UDP、TCP...等；框架已支持全链路调用日志跟踪特性</strong>
  <br>
  <strong>让开发者用一套业务代码，能轻松切换和扩展不同的数据协议：Protobuf、JSON</strong>
  <br>
  <strong>近原生的性能；业务框架在单线程中平均每秒可以执行 1152 万次业务逻辑</strong>
  <br>
  <strong>代码即联调文档、JSR380验证、断言 + 异常机制 = 更少的维护成本</strong>
  <br>
  <strong>框架具备智能的同进程亲和性；开发中，业务代码可定位与跳转</strong>
  <br>
  <strong>架构部署灵活性与多样性：既可相互独立，又可相互融合</strong>
  <br>
  <strong>一次编写到处对接，能为客户端生成可交互的代码</strong>
  <br>
  <strong>逻辑服之间可相互跨进程、跨机器进行通信</strong>
  <br>
  <strong>支持玩家对游戏逻辑服进行动态绑定</strong>
  <br>
  <strong>能与任何其他框架做融合共存</strong>
  <br>
  <strong>对 webMVC 开发者友好</strong>
  <br>
  <strong>无 spring 强依赖</strong>
  <br>
  <strong>零学习成本</strong>
  <br>
  <strong>javaSE</strong>
</p>
<p align="center">
	<a href="http://game.iohao.com">https://iohao.github.io/game</a>
</p>
<p align="center">
	<a target="_blank" href="https://www.oracle.com/java/technologies/downloads/#java21">
		<img src="https://img.shields.io/badge/JDK-21-success.svg" alt="JDK 21" />
	</a>
	<br>
	<a target="_blank" href="https://www.gnu.org/licenses/agpl-3.0.txt">
		<img src="https://img.shields.io/:license-AGPL3.0-blue.svg" alt="AGPL3.0" />
	</a>
	<br />
	<a target="_blank" href='https://gitee.com/iohao/ioGame'>
		<img src='https://gitee.com/iohao/ioGame/badge/star.svg' alt='gitee star'/>
	</a>
	<a target="_blank" href='https://github.com/iohao/ioGame'>
		<img src="https://img.shields.io/github/stars/iohao/ioGame.svg?logo=github" alt="github star"/>
	</a>
  <br />
	<a target="_blank" href='https://app.codacy.com/gh/iohao/ioGame/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade'>
		<img src="https://app.codacy.com/project/badge/Grade/4981fff112754686baad7442be998b17" alt="code quality"/>
</a>
</p>
<hr />

Documentation: https://iohao.github.io/game/docs/intro

## 愿景
让网络游戏服务器的编程变得轻松简单。 改变行业现状，降低使用难度，让游戏开发领域的生产资料公有制！


## 开放、自由、友好的开源协议
- 过去、现在、将来都不会有商业版本，所有功能全部开源。
- 承诺项目的维护周期是十年起步， 2022-03-01 起，至少十年维护期。
- ioGame 是一个轻量级的网络编程框架，适用于**网络游戏服务器、物联网、内部系统**及各种需要长连接的场景。 源码完全开放、最新文档阅读完全开放、提供高质量的使用文档，使用完全自由、免费（遵守开源协议）。

<hr/>

**友好的开源协议**

ioGame 使用 [AGPL3.0](https://www.gnu.org/licenses/agpl-3.0.txt) 开源协议，开发者在使用时需要遵守该协议。 在该协议下开发的项目是免费的，没有任何成本。

同时，该协议对开发者更加友好，它确保了项目所有权在资本家和开发者之间共享。 这意味着，即使开发者带着项目离开公司或自行运营，也依然是合法的。

与 Apache 2.0、MIT 等其他协议不同，那些协议下，一旦开发者离开公司，便会彻底失去对项目的控制权，难以获得任何实质性回报。 尤其是在开发阶段，程序员辛勤付出、996 加班，但当项目即将上线或刚上线时，却可能面临被裁的风险。 最终，他们只能眼睁睁看着自己倾注心血的成果被资本家掌控，而自己却一无所有。

因此，采用 AGPL 3.0 协议的项目，由于开发者能共享所有权，更能激励程序员全身心投入，用心打磨项目。



## 启动展示

ioGame 在内存占用、启动速度、打包等方面也是优秀的。
- 内存方面：内存占用小。
- 启动速度方面：应用通常会在 **0.x 秒**内完成启动。
- 打包方面：打 jar 包后大约 **15MB** 。


![](https://iohao.github.io/game/assets/images/start-cc4b7973e832e31c6c5bf50af83aabeb.png)


---

**框架的代码质量、安全质量**

[ioGame - Qodana Cloud](https://qodana.cloud/organizations/3k6Pm/teams/zxRGm) 

<a target="_blank" href='https://app.codacy.com/gh/iohao/ioGame/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade'>
<img src="https://app.codacy.com/project/badge/Grade/4981fff112754686baad7442be998b17" alt="code quality"/>
</a>

>  Qodana 是 JetBrains 推出的代码质量检测工具。它将 JetBrains IDE 具有的智能代码检查带入了项目 CI/CD 管道中。它可以识别代码中的错误，安全漏洞，重复项和缺陷并提出修复建议。使开发人员轻松地改善代码结构，使代码符合众多准则和标准，解决潜在的性能问题。

![](https://iohao.github.io/game/assets/images/qodana-c7e2e8cbd24ab5426443ff33833fab05.png)



## ioGame 使用趋势数据

关注 ioGame 的游戏服务器开发者持续增多，2022-09 ~ 2025-06 各月的统计数据；


这里的统计信息是关于开发者关注 ioGame 框架相关的，从统计数据中可以看出，由于 ioGame 上手简单，功能强大等优点，得到了众多开发者的关注。如果你想知道 ioGame 有没有人在使用，可以先到这里看下统计数据、开发者的评价与讨论。



https://www.yuque.com/iohao/game/gpxk93#TwVa8



这里展示了每月的统计数据，统计数据来源于语雀后台，这些数据都是**真实的、客观存在的、活的**。



因为成本的原因，某宝某多还没有出现能提供这种服务的商家，所以这样的统计数据也更具真实性。通过统计数据，我们可以看到每天会有很多开发者在访问 ioGame 的在线文档，并且这些统计数据不是来源于口嗨的，也不是主观创造的。



所以，还在犹豫要不要使用 ioGame 的开发者们，更应该讨论的是“为什么这些开发者会选择使用 ioGame”，而不是 ioGame 有没有人在使用的问题。



[点击我，到语雀后台查看 ioGame 的数据](https://www.yuque.com/iohao)

![](https://raw.githubusercontent.com/iohao/ioGameResource/main/images/stat.png)



**注意：从 2025-06 开始将文档迁移至 https://iohao.github.io/game ，后续将不会提供语雀第三方的访问统计数据。**

---



## 整体预览导图

![image](https://user-images.githubusercontent.com/26356013/236759330-9a87bb74-db09-4fa2-b002-65770f86b892.png)



## 介绍

你是否想要开发一个**高性能、稳定、易用、自带负载均衡、避免类爆炸设计、可跨进程跨机器通信、集群无中心节点、集群自动化、有状态多进程的分布式的**网络编程服务器呢？ 如果是的话，这里向你推荐一个由 java 语言编写的网络编程框架 ioGame。



ioGame 是一个轻量级的网络编程框架，适用于**网络游戏服务器、物联网、内部系统**及各种需要长连接的场景。



**ioGame 有以下特点：**

> 1. 真轻量级、无锁异步化、事件驱动的架构设计。
> 2. 包体小、内存占用少、启动速度快。
> 3. ioGame 是纯 javaSE 的，使得 ioGame 能与其他框架方便的进行集成、融合，如 Spring ...等。
> 4. 在学习成本方面，ioGame 的学习成本非常低，可以说是**零学习成本**，即使没有游戏编程经验，也能轻松上手。开发者只需掌握普通的 java 方法或 webMVC 相关知识，就能用框架开发业务。
> 5. ioGame 在架构上解决了传统框架所产生的 **N\*N 问题**。
> 6. 在轻量级方面，ioGame **不依赖任何第三方**中间件或数据库**就能支持集群、分布式**，只需要 java 环境就可以运行。
> 7. 在架构灵活性方面，ioGame 的架构由三部分组成：1.游戏对外服、2.Broker（游戏网关）、3.游戏逻辑服。三者既可相互独立，又可相互融合，这意味着使用 ioGame 可以**适应任何类型的游戏**。
> 8. 架构是可以动态扩缩的，游戏对外服、游戏逻辑服、Broker（游戏网关）都**支持动态增加和减少**。
> 9. 在分布式开发体验方面，ioGame 支持多服单进程的启动方式，这使得开发者在开发和调试分布式系统时更加简单。
> 10. 在生态规划方面，游戏逻辑服是支持独立运行，**从而实现功能模块化的可能性**。
> 11. 具备全链路调用日志跟踪特性。
> 12. 在通讯方式方面，提供多种通讯方式，且逻辑服之间可以相互跨机器通信。
> 13. 在编码风格上，提供了类 MVC 的编码风格（无入侵的 Java Bean ），这种设计方式很好的**避免了类爆炸**。
> 14. 在线程安全方面，框架为开发者解决了单个玩家的**并发问题**。
> 15. 在连接方式方面，允许开发者**使用一套业务代码**，同时支持 TCP、WebSocket、UDP 等多种连接方式，无需进行任何修改，并且可扩展。
> 16. 在数据协议方面，ioGame 让开发者**用一套业务代码**，就能轻松切换不同的数据协议，如 Protobuf、JSON 等，并且可扩展。
> 17. 在增减协议方面，ioGame 可以让你在**新增或减少协议**时，**无需重启**游戏对外服与 Broker（游戏网关）。这样既能避免玩家断线，又能避免因新增、减少协议而重启所有机器的痛点。
> 18. action 支持自动装箱、拆箱基础类型，用于解决[协议碎片](https://iohao.github.io/game/docs/manual/protocol_fragment)的问题。
> 19. 业务框架提供了插件机制，插件是可插拨、可扩展的。
> 20. 在部署方面，ioGame 支持**多服单进程**的方式部署，也支持**多服多进程**多机器的方式部署，在部署方式上可以随意的切换而不需要更改代码。
> 21. 在安全方面，所有的游戏逻辑服不需要开放端口，**天然地避免了扫描攻击**。
> 22. 在模拟客户端测试方面，ioGame 提供了压测&模拟客户端请求模块。该模块**可以模拟真实的网络环境**，并且在模拟测试的过程中与服务器的交互是可持续的、可互动的，同时也是支持自动化的。
> 23. 框架为开发者提供了同步、异步、异步回调的方法，用于逻辑服之间的相互访问。
> 24. 分布式事件总线支持（类似 MQ、Redis 发布订阅机制，可跨多个机器通信、可跨多个进程通信）。
> 25. 提供优雅的路由访问权限控制。
> 26. 具备智能的同进程亲和性。
> 27. JSR380 验证、断言 + 异常机制 = 更少的维护成本。
> 28. 一次编写到处对接，提升巨大的生产力，能为各客户端生成可交互的代码。你只需要编写一次 java 代码，就能为 Godot、UE、Unity、CocosCreator、Laya、React、Vue、Angular ...等项目生成统一的交互接口。



ioGame 在打包、内存占用、启动速度等方面也是优秀的。 打 jar 包后大约 **15MB**，应用通常会在 **0.x 秒**内完成启动，内存占用小。



在生态融合方面，ioGame 可以很方便的[与 Spring 集成](https://iohao.github.io/game/docs/manual/integration_spring)（4 行代码）。 除了 Spring 外，还能与任何其他的框架做**融合**，如 Vert.x、Quarkus、Solon ...等，从而使用其他框架的相关生态。



在学习成本方面，ioGame 的学习成本非常低，可以说是**零学习成本**，即使没有游戏编程经验也能轻松上手。 开发者只需掌握普通的 java 方法或 webMVC 相关知识，就能使用框架开发业务。



在编码风格上，ioGame 为开发者提供了类 MVC 的编码风格（无入侵的 Java Bean ），这种设计方式很好的**避免了类爆炸**。 同时，框架为开发者提供了同步、异步、异步回调的方法，用于逻辑服之间的相互访问。 这使得开发者所编写的代码会非常的优雅，并且具备全链路调用日志跟踪。



与客户端对接方面，ioGame 具备**一次编写到处对接**的能力，为客户端提供了代码生成的辅助功能，能够帮助客户端开发者减少巨大的工作量。 这将意味着，你只需要编写一次 java 代码，就能为 Godot、UE、Unity、CocosCreator、Laya、React、Vue、Angular ...等项目生成统一的交互接口。 ioGame 提供了多种语言的 SDK 支持及相关语言的[代码生成](https://iohao.github.io/game/docs/examples/code_generate)，分别是 C#、TypeScript、GDScript、C++，并支持扩展。



ioGame 在架构上解决了传统框架所产生的 **N\*N 问题**（[与传统架构对比](https://iohao.github.io/game/docs/overall/legacy_system)）。 传统架构在扩展机器时，需要借助很多第三方中间件，如：Redis、MQ、ZooKeeper ...等，才能满足整体架构的运作。 通常，只要引入了需要安装的中间件才能做到扩展的，那么你的架构或者说框架，基本上与轻量级无缘了。



在轻量级方面，ioGame **不依赖任何第三方**中间件或数据库**就能支持集群、分布式**，只需要 java 环境就可以运行。 这意味着在使用上简单了，在部署上也为企业减少了部署成本、维护难度。使用 ioGame 时，只需一个依赖即可获得整个框架， 而无需安装其他服务，如： Nginx、Redis、MQ、Mysql、ZooKeeper、Protobuf 协议编译工具 ...等。



在[架构灵活性](https://iohao.github.io/game/docs/overall/deploy_flexible)方面，ioGame 的架构由三部分组成：1.游戏对外服、2.Broker（游戏网关）、3.游戏逻辑服。 三者既可相互独立，又可相互融合。 这意味着使用 ioGame 可以**适应任何类型的游戏**，因为只需通过调整部署方式，就可以满足不同类型的游戏需求。 在 ioGame 中进行这些调整工作非常简单，而且不会对现有代码产生不良影响。



架构是可以动态扩缩的，游戏对外服、游戏逻辑服、Broker（游戏网关）都**支持动态增加和减少**。 无论未来玩家数量增加或减少，我们都能够轻松应对。 同时，架构是**支持玩家无感知更新**的，这得益于分布式设计。 举例来说，如果 A 类型的游戏逻辑服需要增加一些新功能，我们可以启动 A-3、A-4 等已经支持了新功能的服务器， 然后逐步将之前的 A-1 和 A-2 下线，从而实现了无感知的更新。



在集群方面，ioGame 的 Broker （游戏网关）采用无中心节点、[自动化的集群设计](https://iohao.github.io/game/docs/examples/server/example_broker_cluster)，所有节点平等且自治，不存在单点故障。 集群能够**自动管理和弹性扩缩**，节点加入或退出时，能够自动保证负载均衡和数据一致性，不影响服务可用性。



在分布式方面，ioGame 的逻辑服使用了分布式设计思想，将服务器分为[游戏对外服](https://iohao.github.io/game/docs/overall/external_intro)、[游戏逻辑服](https://iohao.github.io/game/docs/overall/logic_intro)等不同层次， 并且每一层都有明确的职责和接口。这样可以提高代码可读性和可维护性，并且方便进行**水平扩展**。



在分布式开发体验方面，通常在开发分布式应用时是需要启动多个进程的。 这会让调试与排查问题变得非常困难，从而降低开发者的效率、增加工作量等，这也是很多框架都**解决不了的问题**，但 ioGame 做到了！ ioGame 支持多服单进程的启动方式，这使得开发者在开发和调试分布式系统时更加简单。



在[生态规划](https://iohao.github.io/game/docs/manual_high/your_ecology)方面，我们的游戏逻辑服是支持独立运行的，只需接入 Broker（游戏网关）上， 就可以为玩家和其他游戏逻辑服提供功能上的扩展与增强。 我们可以将一些**游戏逻辑服组件化**，并制作成相对通用的组件，**从而实现功能模块化的可能性**。这么做有几个优点

1. 避免一些重复开发的工作量。
2. 减少各功能模块的耦合。
3. 更符合单一职责的设计，将相对通用的功能扩展成一个个的**功能逻辑服**。如，公会逻辑服、好友逻辑服、登录逻辑服、抽奖逻辑服、公告逻辑服、排行榜逻辑服...等。
4. 由于模块功能是独立，那么将来可以对任意的功能逻辑服进行扩容，且不需要改动任何代码。
5. 这些组件化后的功能逻辑服就好比一件件武器，积累得足够多时就形成了自己的生态武器库，可以更好的帮助公司与同行竞争。
6. **代码泄漏机率更小**。传统的游戏项目通常采用单机结构，把所有的代码放在一个目录中。这样做有很大的风险，因为如果代码泄漏了，就会泄漏整个项目的内容。当功能模块化后，可以让不同的开发人员只负责自己的游戏逻辑服模块，从而避免代码泄漏的风险和影响。
7. 团队管理员只需要在内网服务器上部署一个游戏网关和游戏对外服，而开发人员就可以在本机上编码和测试自己的游戏逻辑服模块。这样还有以下好处
    - 游戏客户端不会因为游戏逻辑服的变更或重启而断开连接。
    - 开发人员不需要启动其他人的游戏逻辑服模块。
    - 开发人员可以通过 ioGame 自动生成的文档来进行模块间的对接。



ioGame 具备[全链路调用日志跟踪](https://iohao.github.io/game/docs/manual/trace)特性，这在分布式下非常的实用。 该特性为每个请求分配一个唯一标识，并记录在日志中，通过唯一标识可以快速的在日志中过滤出指定请求的信息。 ioGame 提供的全链路调用日志跟踪特性更是强大，**支持跨机器、跨进程**。 简单的说，从玩家的请求进来到结束，无论该请求经过了多少个游戏逻辑服，都能精准记录。



在通讯方式方面，大部分框架只能支持推送（广播）这一类型的通讯方式。 ioGame 则提供了多种[通讯模型](https://iohao.github.io/game/docs/manual/communication_model)， 通过对各种通讯方式的组合使用，可以简单完成以往难以完成的工作， 并且这些通讯方式都支持跨进程、跨机器通信，且具备全链路调用日志跟踪。

- 在客户端的角度，提供了如下的通讯模型
    - [request/response](https://iohao.github.io/game/docs/communication/request_response)，请求/响应
    - request/void，请求/无响应
    - request/broadcast，请求/广播响应
    - [broadcast](http://localhost:3000/docs/communication/broadcast)，广播
- 内部通讯主要用于服务器内部之间的通信，跨服、跨进程通信。提供了如下的通讯模型
    - [request/response](https://iohao.github.io/game/docs/communication/request_response)，请求/响应
    - request/void，请求/无响应
    - [request/multiple_response](https://iohao.github.io/game/docs/communication/request_multiple_response)，同时请求同类型多个游戏逻辑服
    - [EventBus](https://iohao.github.io/game/docs/communication/event_bus)，分布式事件总线
    - [ExternalRegion](https://iohao.github.io/game/docs/communication/external_biz_region)，访问游戏对外服



从 ioGame21 开始，框架添加了虚拟线程的相关支持。 各逻辑服之间通信阻塞部分使用虚拟线程，这样可以很好的避免阻塞业务线程，并大幅提高了框架的吞吐量。



在线程安全方面，框架为开发者解决了单个玩家的**并发问题**。 即使玩家重新登录后，也会使用相同的线程来消费业务，并推荐使用[领域事件](https://iohao.github.io/game/docs/extension_module/domain_event)来解决同一房间或业务内多个玩家的并发问题。 [框架在线程的扩展性](https://iohao.github.io/game/docs/overall/thread_executor)上提供了友好的支持，开发者可以很容易的编写出无锁并发代码，这得益于 ioGame 独有的线程执行器设计与扩展。 换句话说，你不会因为并发问题烦恼。



在无锁并发方面，ioGame 提供了优雅、独特的线程执行器设计。通过该特性，开发者能轻易的编写出无锁高并发的代码。



在连接方式方面，ioGame 允许开发者**使用一套业务代码**，**同时支持**多种连接方式，无需进行任何修改。 ioGame 已经支持了 TCP、WebSocket 和 UDP 连接方式，并且也支持在这几种连接方式之间进行灵活切换。 连接方式是可扩展的，并且扩展操作也很简单，这意味着之后如果支持了 KCP、QUIC， 无论你当前项目使用的是 TCP、WebSocket、UDP，都可以切换成 KCP、QUIC。 即使切换到 KCP、QUIC 的连接方式，现有的业务代码也无需改变。



在通信协议方面，ioGame 让开发者**用一套业务代码**，就能轻松[切换和扩展不同的数据协议](https://iohao.github.io/game/docs/manual/data_protocol)， 如 Protobuf、JSON 等。只需一行代码，就可以从 Protobuf 切换到 JSON，无需改变业务方法。



在增减协议方面，ioGame 可以让你在**新增或减少协议**时，**无需重启**游戏对外服与 Broker（游戏网关）。 这样既能避免玩家断线，又能避免因新增、减少协议而重启所有机器的痛点。



在协议碎片方面，action 支持自动装箱、拆箱基础类型特性，用于解决[协议碎片](https://iohao.github.io/game/docs/manual/protocol_fragment)的问题。 同时该特性除了能使你的业务代码更加清晰以外，还能大幅提高开发者在该环节的生产力。



在[同进程亲和性](https://iohao.github.io/game/docs/manual_high/same_process)方面，在同一进程内， 不同 Netty 实例之间的通信，是通过内存进行传输的，不需要经过网络传输，数据传输速度极快。 同进程亲和性指的是，优先访问同进程内的游戏逻辑服，当同进程内没有能处理请求的游戏逻辑服时， 才会去其他进程或机器中查找能处理请求的游戏逻辑服。 简单点说，框架对于请求的处理很智能，会优先将请求给同进程内的逻辑服消费。



在开发体验方面，ioGame 非常注重开发者的开发体验。 框架提供了 [JSR380 验证](https://iohao.github.io/game/docs/core/jsr380)、[断言 + 异常机制](https://iohao.github.io/game/docs/manual/assert_game_code)、[业务代码定位](https://iohao.github.io/game/docs/core_plugin/action_debug)， action 支持自动装箱、拆箱基础类型，用于解决协议碎片的问题 ...等。 诸多丰富的功能，使得开发者的业务代码更加的清晰、简洁。



业务框架提供了[插件](https://iohao.github.io/game/docs/manual/plugin_intro)机制，插件是可插拨、可扩展的。 框架内置提供了 DebugInOut、action 调用统计、业务线程监控插件、各时间段调用统计插件...等插件。 不同的插件提供了不同的关注点，比如我们可以使用调用、监控等插件相互配合，可以让我们在开发阶段就知道**是否存在性能问题**。 合理利用好各个插件，可以让我们在开发阶段就能知道问题所在，提前发现问题，提前预防问题。



在部署方面，ioGame 支持**多服单进程**的方式部署，也支持**多服多进程**多机器的方式部署，在部署方式上可以随意的切换而不需要更改代码。 日常中我们可以按照单体思维开发，到了生产可以选择性的使用多进程的方式部署。



在安全方面，所有的游戏逻辑服[不需要开放端口，天然地避免了扫描攻击](https://iohao.github.io/game/docs/overall/legacy_system#Usage-Management)。 由于不需要为每个逻辑服分配独立的端口，那么我们在使用诸如云服务器之类的服务时，就不需要担心端口开放权限的问题了。 别小看这一个环节，通常这些小细节最浪费开发者的时间。 由于我们不需要管理这些 IP:Port，**这部分的工作量就自然地消失了**。



在模拟客户端测试方面，ioGame 提供了[压测&模拟客户端请求](https://iohao.github.io/game/docs/extension_module/simulation_client)模块。 此模块是用于模拟客户端，简化模拟工作量，只需要编写对应请求与回调。 除了可以模拟简单的请求外，通常还可以做一些复杂的请求编排，并支持复杂业务的压测。 **与单元测试不同的是，该模块可以模拟真实的网络环境，并且在模拟测试的过程中与服务器的交互是可持续的、可互动的，同时也是支持自动化的**。



使用 ioGame，可以显著的帮助企业减少巨额成本。 文档中，**成本**关键字提到了很多次，各个阶段均有关联，包括了学习、研发、测试、部署、扩展、投入 ...等各阶段。 在同等资源的竞争下，使用 ioGame 能为公司节省更多的资源，从而提高了自身的生存率。 更重要的是避免了为其他公司做嫁衣的可能性，具体可阅读[成本分析案例](https://iohao.github.io/game/services/cost_analysis)。



开发者基于 ioGame 编写的项目模块，通常是条理清晰的，得益于框架对**路由的合理设计**，同时也为路由提供了优雅的[访问权限控制](https://iohao.github.io/game/docs/external/access_authentication)。 当我们整理好这些模块后，对于其他开发者接管项目或后续的维护中，会是一个不错的帮助（[代码组织与约定](https://iohao.github.io/game/docs/manual_high/code_organization)）。 或许现阶段你感受不到这块的威力，随着你深入地使用实践就能体会到这么设计的诸多好处与优势。



开发者基于 ioGame 编写的项目，通常是语法简洁的、高性能的、低延迟的。 框架最低要求使用 **JDK21**，这样即可以让项目享受到**分代 ZGC** 带来的改进，还能享受语法上的简洁。 分代 ZGC 远低于其**亚毫秒级**暂停时间的目标，可以在不影响游戏速度的情况下，清理掉多余的内存。 这样就不会出现卡顿或者崩溃的问题了，相当于在项目中变相的引入了一位 JVM 调优大师。



综上所述，ioGame 是一个非常适合网络游戏开发的框架。可以让你轻松地创建高性能、低延迟、易扩展的游戏服务器，并且节省时间和资源。 如果你想要快速地开发出令人惊艳的网络游戏，请不要犹豫，立即选择 ioGame 吧！ 框架屏蔽了很多复杂且重复性的工作，并可为项目中的功能模块结构、开发流程等进行**清晰的组织定义**，减少了后续的项目维护成本。



框架在开发、部署、压测&模拟测试 ...等，各个阶段都提供了很好的支持。 相信你已经对 ioGame 有了一个初步的了解，虽然还有很多丰富的功能与特性没有介绍到，但你可以通过后续的实践过程中来深入了解。 感谢你的阅读，并期待你使用 ioGame 来打造自己的游戏服务器。

---

## 一次编写到处对接，提升巨大的生产力

ioGame 是非常注重开发体验的，代码注释即文档、方法即交互接口的原则。



ioGame 具备一次编写到处对接的能力，从而让你们团队提升巨大的生产力。 **一次编写**指的是编写一次 java 业务代码，而**到处对接**则是指为不同的前端项目生成与服务器交互的代码。



你只需要编写一次 java 代码，就能为 [Godot](https://godotengine.org/)、 [UE](https://www.unrealengine.com/)、 [Unity](https://unity.com/)、 [CocosCreator](https://www.cocos.com/)、 [Laya](https://layaair.layabox.com/#/)、 [React](https://react.dev/)、 [Vue](https://vuejs.org/)、 [Angular](https://angular.dev/) ...等项目生成统一的交互接口



ioGame 能为各种前端项目生成 **action、广播、错误码** 相关接口代码。 这将意味着，你只需要编写一次业务代码，就可以同时与这些游戏引擎或现代化的前端框架交互。



前端代码生成的几个优势

1. 帮助客户端开发者减少巨大的工作量，**不需要编写大量的模板代码**。
2. **语义明确，清晰**。生成的交互代码即能明确所需要的参数类型，又能明确服务器是否会有返回值。这些会在生成接口时就提前明确好。
3. 由于我们可以做到明确交互接口，进而可以明确参数类型。这使得**接口方法参数类型安全、明确**，从而有效避免安全隐患，从而**减少联调时的低级错误**。
4. 减少服务器与客户端双方对接时的沟通成本，代码即文档。生成的联调代码中有文档与使用示例，方法上的示例会教你如何使用，即使是新手也能做到**零学习成本**。
5. 帮助客户端开发者屏蔽与服务器交互部分，**将更多的精力放在真正的业务上**。
6. 为双方联调减少心智负担。联调代码使用简单，**与本地方法调用一般丝滑**。
7. 抛弃传统面向协议对接的方式，转而**使用面向接口方法的对接方式**。
8. 当我们的 java 代码编写完成后，我们的文档及交互接口可做到同步更新，**不需要额外花时间去维护对接文档及其内容**。



## 架构简图

> 无锁异步化、事件驱动的架构设计；真轻量级，无需依赖任何第三方中间件或数据库就能搭建出一个集群、分布式的网络游戏服务器。
>
> 集群无中心节点、集群自动化、自带负载均衡、分布式支持、可动态增减机器。

| 名称                                                         | 扩展方式 | 职责             |
| ------------------------------------------------------------ | -------- | ---------------- |
| **ExternalServer**，[游戏对外服](https://iohao.github.io/game/docs/overall/external_intro) | 分布式   | 与玩家连接、交互 |
| **GameLogicServer**，[游戏逻辑服](https://iohao.github.io/game/docs/overall/logic_intro) | 分布式   | 处理具体业务逻辑 |
| **BrokerCluster**，[Broker（游戏网关）](https://iohao.github.io/game/docs/overall/broker_intro) | 集群     | 调度和转发任务   |

![ioGame](https://iohao.github.io/game/assets/images/ioGame-2cd7572c6c81afd341b7d2e9d703bf65.svg)

更详细的介绍请阅读[架构介绍](https://iohao.github.io/game/docs/overall/architecture_intro)。

------

> 从图中可以看出，游戏网关支持以集群方式启动多个实例。这个设计选择了集群的方式，因为游戏网关通常是无状态的，主要用于调度和转发任务
>
> 而游戏对外服、游戏逻辑服使用分布式设计，支持启动多个相同类型的服务。这意味着，当玩家数量增加时，我们可以轻松增加相应类型的游戏逻辑服以处理更多请求。
>
> 以游戏逻辑服为例，假设我们启动了两个 A 类型的游戏逻辑服，分别为 A-1 和 A-2。当玩家向 A 类型的游戏逻辑服发起多次请求时，游戏网关会使用默认的随机负载策略将请求分配给 A-1 和 A-2 来处理。
>
> 现在我们明白，游戏对外服和游戏逻辑服都支持动态增加和减少。无论未来玩家数量增加或减少，我们都能够轻松应对。架构是**支持玩家无感知更新**的，这得益于分布式设计。举例来说，如果 A 类型的游戏逻辑服需要增加一些新功能，我们可以启动 A-3、A-4 等已经支持了新功能的服务器，然后逐步将之前的 A-1 和 A-2 下线，从而实现了无感知的更新。
>
> 此外，框架还支持玩家[动态绑定游戏逻辑服](https://iohao.github.io/game/docs/manual/binding_logic_server)。玩家与游戏逻辑服绑定后，之后的请求都由该游戏逻辑服来处理。
>
> 除了游戏之外，ioGame 也适用于物联网相关项目。只需将图中的玩家视为具体的设备，即使存在数亿个设备，ioGame 的架构也可以轻松支持。从 2022 年开始，已经有一些物联网公司开始采用这一解决方案，并得到了很好的体验。



**游戏对外服**

游戏对外服主要负责与用户（玩家）的长连接，先来个假设，假如我们的一台硬件支持我们建立用户连接的上限是 5000 人， 当用户量达到 7000 人时，我们可以多加一个对外服务器来进行分流减压。

通过增加游戏对外服的数量，可以有效地进行连接的负载均衡和流量控制，使得系统能够更好地承受高并发的压力。 由于游戏对外服扩展的简单性，意味着支持同时在线玩家可以轻松的达到百万、千万甚至更多。

即使我们启动了多个游戏对外服，开发者也不需要关心这些玩家连接到了哪个游戏对外服的问题， 这些玩家总是能接收到广播（推送）消息的，因为框架已经把这些事情给做了。 在玩家的角度我们只有“一个”服务器，同样的，在开发者的角度我们只有“一个”游戏对外服。

通常，有些开发者想知道游戏对外服最大支持多少玩家连接。 关于这个问题，只需要搜索 Netty 相关知识即可，因为游戏对外服本质上是 Netty。

同样的，如果开发者已经熟悉了 Netty 相关知识，那么在游戏对外服的扩展上也会变得非常的容易。



## 快速入门

下面是游戏引擎与游戏服务器的业务交互简图。

![业务交互简图](https://iohao.github.io/game/assets/images/introduction_quick-6b29dfc678257db43afb10939356edb6.jpeg)

> 抽象的说，游戏前端与游戏服务器的的交互由上图组成。 游戏前端与游戏服务器可以自由地**双向交互**，即发送和接收业务数据。 业务数据由 .proto 文件作为载体，在前端和后端之间进行编码和解码。 .proto 文件是对业务数据的描述载体，定义了数据类型和消息类型，以及它们的属性和规则。
>
> 通过这种方式，游戏前端和游戏服务端可以建立连接，并开始相互传递业务数据，处理各自的业务。 以上是对游戏前端与游戏服务器之间交互方式的介绍。 接下来，我们将编写一个简单的游戏业务处理示例，并定制一个适合我们需求的业务数据协议。
>
> **协议文件**是对业务数据的描述载体，用于游戏前端与游戏服务器的数据交互。 Protocol Buffers 是 Google 开发的一种数据描述语言，也简称 PB。 协议文件描述还可以是 json、xml 或者任意自定义的，因为最后传输时会转换为二进制，但游戏开发中 PB 是目前的最佳选择。
>
> **游戏前端**的展现可以是 [Godot](https://godotengine.org/)、 [Unity](https://unity.cn/)、 [UE](https://www.unrealengine.com/zh-CN/)、 [Cocos Creator](https://www.cocos.com/)、 [Laya](https://layaair.layabox.com/#/)、 [FXGL](https://github.com/AlmasB/FXGL) 或者其他的游戏引擎。 这些游戏引擎只是展现游戏画面的一种形式，数据交互则由通信来完成（TCP、UDP ...等）。



**数据协议**

现在，我们定义两个数据协议，用于客户端与服务器的数据交互。 这是一个 jprotobuf 的 pb 对象，jprotobuf 是对 google protobuf 的简化使用，性能同等。



可以把这理解成 DTO 业务数据载体等，其主要目的是用于业务数据的传输。

```java
@ProtobufClass
public class LoginVerifyMessage {
    public String jwt;
}

@ProtobufClass
public class UserMessage {
    public String name;
}
```



**Action**

游戏服务器的编程，游戏服务器接收业务数据后，对业务数据进行处理。 下面这段代码可以同时支持 TCP、WebSocket、UDP 通信方式。

```java
@Slf4j
@ActionController(1)
public class DemoAction {
    @ActionMethod(0)
    public UserMessage here(LoginVerifyMessage message) {
        var userMessage = new UserMessage();
        userMessage.name = "Michael Jackson, " + message.jwt;
        return userMessage;
    }
}
```



一个方法（here）在业务框架中表示一个 [Action](https://iohao.github.io/game/docs/manual/action)（业务动作）。

方法声明的参数是用于接收前端传入的业务数据，在方法 return 时，数据就可以被游戏前端接收到。 程序员可以不需要关心业务框架的内部细节。

从上面的示例可以看出，这和普通的 java 类并无区别，同时这种设计方式**避免了类爆炸**。 如果只负责编写游戏业务，那么对于业务框架的学习可以到此为止了。



**游戏编程就是如此简单！**



**问：我可以开始游戏服务器的编程了吗？**

> 是的，你已经可以开始游戏服务器的编程了。



**访问示例（控制台）**

当访问 action 业务方法时，控制台将会打印的日志输出如下

```text
┏━━━━━ Debug. [(DemoAction.java:5).here] ━━━━━ [cmd:1-0 65536] ━━━━━ [xxx逻辑服 - id:[76526c134cc88232379167be83e4ddfc]
┣ userId: 1
┣ 参数: message : LoginVerifyMessage(jwt=hello)
┣ 响应: UserMessage(name=Michael Jackson, hello)
┣ 时间: 1 ms (业务方法总耗时)
┗━━━━━ [ioGameVersion] ━━━━━ [线程:User-8-2] ━━━━━━━ [traceId:956230991452569600] ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```

**控制台打印说明**

- **Debug**. [(DemoAction.java:5).here] : 表示执行业务的是 DemoAction 类下的 here 方法，5 表示业务方法所在的代码行数。在工具中点击控制台的 DemoAction.java:5 这条信息，就可以跳转到对应的代码中（快速导航到对应的代码），这是一个开发良好体验的开始！
- **userId** : 当前发起请求的 用户 id。
- **参数** : 通常是游戏前端传入的值。
- **响应** : 通常是业务方法返回的值 ，业务框架会把这个返回值推送到游戏前端。
- **时间** : 执行业务方法总耗时，我们可根据业务方法总耗时的时长来优化业务。
- **路由信息** : [cmd - subCmd][路由](https://iohao.github.io/game/docs/manual/cmd)是唯一的访问地址。
- **ioGameVersion** : 表示当前所使用的 ioGame 版本。
- **线程** : 当前执行 action 所使用的线程。
- **traceId** : 全链路调用日志跟踪 id，每个请求唯一。（该特性在分布式下非常实用）
- **逻辑服** : 当前游戏逻辑服的 id

有了以上信息，游戏开发者可以很快的定位问题。 如果没有可视化的信息，开发中会浪费很多时间在前后端的沟通上。问题包括：

- 是否传参问题 （游戏前端说传了）
- 是否响应问题（游戏后端说返回了）
- 业务执行时长问题 （游戏前端说没收到响应， 游戏后端说早就响应了）

其中代码导航可以让开发者快速的跳转到业务类对应代码中， 在多人合作的项目中可以快速的知道业务经过了哪些方法的执行，使得我们可以快速的进行阅读或修改。



## 适合人群

1. 长期从事 web 内部系统开发人员， 想了解游戏的。
2. 刚从事游戏开发的。
3. 未从事过游戏开发，但却对其感兴趣的。
4. 对设计模式在实践中的应用有兴趣的学习者。
5. 可以接受新鲜事物的。
6. 想放弃祖传代码的。



推荐实际编程经验一年以上的人员。

