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
  <strong>让开发者用一套业务代码，能轻松切换和扩展不同的通信协议：Protobuf、JSON</strong>
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
	<a href="http://game.iohao.com">http://game.iohao.com</a>
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

<br/>

过去、现在、将来都不会有商业版本，所有功能全部开源！

只做真的完全式开源，拒绝虚假开源，售卖商业版，不搞短暂维护！

承诺项目的维护周期是十年起步， 2022-03-01 起，至少十年维护期！

提供高质量的使用文档！

<br/>

ioGame 源码完全开放、最新文档阅读完全开放；使用完全自由、免费（遵守开源协议）。

<br/>

ioGame 是一个轻量级的网络编程框架，适用于**网络游戏服务器、物联网、内部系统**及各种需要长连接的场景。

<br/>



**ioGame 架构简图**

![架构简图](https://raw.githubusercontent.com/iohao/ioGameResource/main/images/ioGame.jpg)

<br/>

使用 ioGame，可以显著的帮助企业减少巨额成本。在文档中，"**成本**"关键字提到了很多次，各个阶段均有关联，包括了学习、研发、测试、部署、扩展、投入 ...等各阶段。在同等资源的竞争下，使用 ioGame 可为公司节省更多的资源，从而提高了自身的生存率。更重要的是避免了为其他公司做嫁衣的可能性，具体可阅读[成本分析案例](https://www.yuque.com/iohao/game/gd5l3b0y0h027kcv#aSk5x)。

<br/>

> [为什么采用授权许可申请?](https://www.yuque.com/iohao/game/gd5l3b0y0h027kcv)

<details>
<summary>授权许可申请 -- 点我展开</summary>

```text
ioGame 使用的是 AGPL3.0 开源协议。
由于 ioGame 遵循过去、现在、将来都不会有商业版本，所有功能全部开源的原则，使得我们很难找到盈利点，现在使用授权的方式；

使用 ioGame 是免费的，前提是要认同生产资料公有制，就是把你的产品也完全的开源出来（遵守 AGPL3.0 开源协议）；同时，ioGame 是允许私有的，前提是支付闭源授权费。所以，付费不是必须的，也不是强制性的，选择权在使用者手上。

ioGame 支持申请盈利后支付闭源授权费，收入不超过10万人民币/年或拥有不超过100万人民币的资产，可以申请盈利后支付授权费用。

### 闭源授权说明
1.授权费用：2799 人民币每年。
2.一个产品只对应一个授权。
3.取得授权的产品只可自己运营或使用，如果是提供给客户二次开发或销售类的产品，每份需要取得一个授权。

如果你的产品不想开源，可以向我们申请为期一年的闭源授权，企业用户需要通过企业邮箱申请闭源授权。如果是个人开发者、独立开发者的，可以使用非企业邮箱申请闭源授权。

### 授权类型
个人和独立开发者：收入不超过10万人民币/年或拥有不超过100万人民币的资产，可以申请盈利后支付授权费用。
企业：收入不超过10万人民币/年或拥有不超过100万人民币的资产，可以申请盈利后支付授权费用。

我们最终是期望你的产品盈利后，支付闭源授权费用。我们的目的也很明确，通过授权的方式获得资金，在使用这些资金来加快发展框架的功能及建立完善社区，更好的为大家服务。

以下行业或产品需要请提前沟通：基于 ioGame 的商业化视频、围绕 ioGame 的商业化售卖产品； 围绕 ioGame 的商业化产品指的是售卖相关的商业产品；

存在法律纠纷阶段是不予授权的。

### 申请授权
申请授权邮箱（发到 262610965@qq.com 或 luoyizhu@gmail.com），内容格式如下
公司全名：
统一社会信用代码：
产品描述：
软件著作权编号：（没有可以不用填写）
申请的授权类型：（个人和独立开发者、企业、专业版、定制版）


### 合法授权码
授权码是根据 【公司统一社会信用代码、软著码、发放日期、失效日期】等信息生成的，确保唯一性与合法性；

授权码会通过邮箱发放，用于留存，具备法律效力。
产品在取得授权期内，不需要对外开放产品源代码；如果不在授权期内，你的产品将转为 AGPL3.0 开源协议。

```

</details>

<br>

---

### 启动展示

ioGame 在内存占用、启动速度、打包等方面也是优秀的。

- 内存方面：内存占用小。
- 启动速度方面：应用通常会在 **0.x 秒**内完成启动。
- 打包方面：打 jar 包后大约 **15MB** ，详细请看 [快速从零编写服务器完整示例](https://www.yuque.com/iohao/game/zm6qg2#LF8Qh)。



![](https://user-images.githubusercontent.com/26356013/215700352-1dbab29d-672d-4d68-bf50-2441e49bd87d.png)

---



### 源码、示例、效率


| github                                           | gitee                                           |
| ------------------------------------------------ | ----------------------------------------------- |
| [ioGame - 源码](https://github.com/iohao/ioGame) | [ioGame - 源码](https://gitee.com/iohao/ioGame) |

---



**Sdk、客户端示例集合**

> Sdk + [代码生成](https://www.yuque.com/iohao/game/irth38)的示例。
>
> 
>
> ioGame 具备一次编写到处对接的能力，你只需要编写一次 java 代码，就能为 Unity、Godot、CocosCreator、Laya、Vue、Angular 等 C#、GDScript、TypeScript 类型的项目生成交互接口，帮助客户端开发者减少巨大的工作量。

| Github                                                       | 描述                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| [ioGameSdkC#ExampleGodot](https://github.com/iohao/ioGameSdkCsharpExampleGodot) | 与 [Godot](https://godotengine.org/) 互通的一个示例。 Godot、Protobuf、Netty、ioGame、C#、Csharp、WebSocket |
| [ioGameSdkC#ExampleUnity](https://github.com/iohao/ioGameSdkCsharpExampleUnity) | 与 [Unity](https://unity.com/) 互通的一个示例。 Unity、Protobuf、Netty、ioGame、C#、Csharp、WebSocket |
| [ioGameSdkTsExampleCocos](https://github.com/iohao/ioGameSdkTsExampleCocos) | 与 [Cocos Creator](https://www.cocos.com/) 互通的一个示例。 CocosCreator、Protobuf、Netty、ioGame、TypeScript、WebSocket |
| [ioGameSdkTsExampleVue](https://github.com/iohao/ioGameSdkTsExampleVue) | 与 [Vue](https://github.com/vuejs/) 互通的一个示例。 Vue、Protobuf、Netty、ioGame、TypeScript、WebSocket |
| [ioGameSdkTsExampleAngular](https://github.com/iohao/ioGameSdkTsExampleAngular) | 与 [Angular](https://github.com/angular/angular) 互通的一个示例。 Angular、Protobuf、Netty、ioGame、TypeScript、WebSocket |
| [ioGameSdkTsExampleHtml](https://github.com/iohao/ioGameSdkTsExampleHtml) | 与 [webpack](https://github.com/webpack/webpack) 互通的一个示例。 （webpack: html + ts）、Protobuf、Netty、ioGame、TypeScript、WebSocket |
| [ioGameSdkGDScriptExampleGodot](https://github.com/iohao/ioGameSdkGDScriptExampleGodot) | 与 [Godot](https://godotengine.org/) 互通的一个示例。 GDScript、Godot、Protobuf、Netty、ioGame、WebSocket |

---



**非 Sdk 的客户端示例集合，由热心市民提供**

| 示例                                                         | 描述                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| [[示例] FXGL 连接示例；Protobuf、java、Netty](https://www.yuque.com/iohao/game/bolt) | FXGL + ioGame 网络游戏中的多人移动演示                       |
| [[示例] Unity 连接示例 - 1；Protobuf、C#、Netty](https://www.yuque.com/iohao/game/syv5mm) | unity3d 连接示例 websocket + protobuf（已经与综合示例调通）  |
| [[示例] Unity 连接示例 - 2；Protobuf、C#、Netty](https://www.yuque.com/iohao/game/kswsfk13ocg069uf) | 提供了 unity 与 ioGame 的【多人】移动同步演示                |
| [[示例] Cocos Creator 连接示例；Protobuf、TypeScript、Netty](https://www.yuque.com/iohao/game/ua4afq) | cocosCreator 连接示例 websocket + protobuf（已经与综合示例调通） |
| [[示例] Godot 连接示例；Protobuf、C#、Netty](https://www.yuque.com/iohao/game/ci9ebb3cztpbhsbm) | 网络通信使用 webSocket                                       |
| [[示例] UE5 连接示例；Protobuf、C++、Netty](https://www.yuque.com/iohao/game/rus213) | UE5 连接示例 websocket + protobuf（已经与综合示例调通）      |
| [[示例] JavaScript 连接示例；json、JavaScript、Netty](https://www.yuque.com/iohao/game/knqxehz2pl1sal5s) | 使用 websocket.js 来连接 ioGame 的一个示例，使用 json 协议来传输交互。 |
| [[示例] TypeScript 连接示例；json、TypeScript、Netty](https://www.yuque.com/iohao/game/wbsnir210c4xtpyp) | 使用 json 协议来传输交互。                                   |

---



**ioGame 服务器示例集合**

此仓库包含 ioGame 文档中所有示例的集合，每个目录都是一个独立的示例项目。

| gitee                                                     | github                                                     |
| --------------------------------------------------------- | ---------------------------------------------------------- |
| [ioGame 示例集合](https://gitee.com/iohao/ioGameExamples) | [ioGame 示例集合](https://github.com/iohao/ioGameExamples) |

| **目录、源码**    | **描述、文档**                                               |
| ----------------- | ------------------------------------------------------------ |
| RoomExample       | [桌游、房间类 (yuque.com)](https://www.yuque.com/iohao/game/vtzbih) 相关示例，游戏客户端使用 FXGL 制作，有大厅、房间。 |
| SimpleExample     | 文档中所有功能点的示例                                       |
| SpringBootExample | [ioGame 综合示例 (yuque.com)](https://www.yuque.com/iohao/game/ruaqza) ，示例中有功能特性的实践、打包部署（docker、k8s）等介绍 |
| ioGameWeb2Game    | [web 转游戏 - 示例理解篇 (yuque.com)](https://www.yuque.com/iohao/game/gpzmc8vadn4vl70z) |
| fxglSimpleGame    | [fxgl-ioGame-移动同步 (yuque.com)](https://www.yuque.com/iohao/game/bolt) |
| SdkExample        | [代码生成(yuque.com)](https://www.yuque.com/iohao/game/irth38)，该项目是与 ioGame SDK（C# Sdk、Ts Sdk） 相关的联调演示，服务器提供了 `action、广播、错误码` 等相关内容用于交互演示。 |



**效率**

| **效率**                                                     |                        |
| ------------------------------------------------------------ | ---------------------- |
| [ioGame 快速理解篇](https://www.yuque.com/iohao/game/le48p1go9gkdqgih) | 快速掌握 ioGame 的概念 |



---

### **最小依赖**

ioGame 已经上传到中央仓库，如果无法下载最新的框架源码，建议开发者的 maven 仓库代理使用原生的或腾讯云的代理，目前不推荐阿里云的代理。[腾讯云代理设置可参考这里](https://www.yuque.com/iohao/game/swt3ls#Amq4K)。



ioGame 最新版本查看 https://www.yuque.com/iohao/game/ab15oe



ioGame 是轻量级的网络编程框架，**不依赖任何第三方**中间件或数据库**就能支持集群、分布式**，只需要 java 环境就可以运行。此时，你只需一个依赖即可获得整个框架，并同时支持开头介绍的全部功能特性。

```xml
<!-- https://mvnrepository.com/artifact/com.iohao.game/run-one-netty -->
<dependency>
    <groupId>com.iohao.game</groupId>
    <artifactId>run-one-netty</artifactId>
    <version>21.27</version>
</dependency>
```

---



### ioGame 使用趋势数据

关注 ioGame 的游戏服务器开发者持续增多，2022-09 ~ 至今各月的统计数据；



这里的统计信息是关于开发者关注 ioGame 框架相关的，从统计数据中可以看出，由于 ioGame 上手简单，功能强大等优点，得到了众多开发者的关注。如果你想知道 ioGame 有没有人在使用，可以先到这里看下统计数据、开发者的评价与讨论。



https://www.yuque.com/iohao/game/gpxk93#TwVa8



这里展示了每月的统计数据，统计数据来源于语雀后台，这些数据都是**真实的、客观存在的、活的**。



> 因为成本的原因，某宝某多还没有出现能提供这种服务的商家，所以这样的统计数据也更具真实性。

通过统计数据，我们可以看到每天会有很多开发者在访问 ioGame 的在线文档，并且这些统计数据不是来源于口嗨的，也不是主观创造的。



所以，还在犹豫要不要使用 ioGame 的开发者们，更应该讨论的是“为什么这些开发者会选择使用 ioGame”，而不是 ioGame 有没有人在使用的问题。



[点击我，到语雀后台查看 ioGame 的数据](https://www.yuque.com/iohao)



![](https://raw.githubusercontent.com/iohao/ioGameResource/main/images/stat.png)



---

**框架的代码质量、安全质量**

[ioGame - Qodana Cloud](https://qodana.cloud/organizations/3k6Pm/teams/zxRGm) 

<a target="_blank" href='https://app.codacy.com/gh/iohao/ioGame/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade'>
<img src="https://app.codacy.com/project/badge/Grade/4981fff112754686baad7442be998b17" alt="code quality"/>
</a>

>  Qodana 是 JetBrains 推出的代码质量检测工具。它将 JetBrains IDE 具有的智能代码检查带入了项目 CI/CD 管道中。它可以识别代码中的错误，安全漏洞，重复项和缺陷并提出修复建议。使开发人员轻松地改善代码结构，使代码符合众多准则和标准，解决潜在的性能问题。

![](https://raw.githubusercontent.com/iohao/ioGameResource/main/images/Qodana.png)




ioGame 已经接入 OSCS 墨菲安全扫描，框架的安全质量近乎100%，游戏开发者们可放心使用。更详细的可以到 [ioGame 框架的安全质量、代码质量、性能](https://www.yuque.com/iohao/game/azpsro) 来了解。

![ioGame-OSCS](https://foruda.gitee.com/images/1663829958058696562/0e9c7251_5475.png)



---


## 愿景

**让网络游戏服务器的编程变得轻松简单！**  



**改变行业现状，降低使用难度，让游戏开发领域的生产资料公有制。**



## 整体预览导图

![image](https://user-images.githubusercontent.com/26356013/236759330-9a87bb74-db09-4fa2-b002-65770f86b892.png)



## 了解 ioGame 前的准备工作

在了解 ioGame 前，这里有几个问题需要你帮忙解决，或者说你目前使用的现有框架是否能解决下面提出的问题。

下面这些问题是 ioGame 已经解决了的，并且的将来还能解决更多的问题。

如果你现在使用的框架能解决下列问题，可以不需要在花时间来学习 ioGame 了。

<details>
<summary>问题 -- 点我展开</summary>

**学习成本、风险相关的问题**

Q：你的使用的框架是祖传的吗？有完整的使用文档吗？如果将你当前使用的框架给到其他团队，在没有你的帮助下，该团队能顺利完成开发吗？



Q：你使用的框架是公司内部的吗？去到了新公司是否还需要重新学一套新公司的框架？或将现有公司的框架带到下个公司去使用，会给新公司带来风险吗？



Q：打算新招一个编写业务逻辑的人员，可以做到零学习成本吗？



Q：每次通信协议的变更（如 pb 内容的变更），可以做到不需要重新编译协议就能使用吗？



Q：提供游戏文档生成的辅助功能吗，支持联调文档的生成吗，支持代码即文档吗？



Q：你使用的框架会像 ioGame 这样使用语雀的每月统计，公布真实的访问数据吗？

> 因为成本的原因，某宝某多还没有出现能提供这种服务的商家，所以这样的统计数据也更具真实性。



Q：路由可以控制访问权限吗？如某些方法非登录玩家也能访问，有些方法只能登录后访问，有些方法即使是登录的玩家也无法访问（用于内部调用的敏感方法）。



**架构相关的提问**

Q：支持集群、分布式吗？是轻量级的框架吗？可以做到无需依赖任何第三方中间件或数据库就能支持集群、分布式这种真正的轻量级吗？



Q：当现有机器不能支撑现有玩家的处理时，支持机器动态增加吗？支持机器动态减少吗？动态增减会有风险吗？



Q：当有相同类型的多个逻辑服时，支持负载均衡吗？



**分布式开发体验与部署相关提问**

支持多服多进程的启动方式吗？

支持多服单进程的启动方式吗？

启动方式上支持随意的切换而不需要更改代码吗？



支持多服单进程的方式部署吗？

支持多服多进程多机器的方式部署吗？

部署方式上支持随意的切换而不需要更改代码吗？



**业务框架相关的问题**

Q：是线程安全的吗，玩家断线后，重新登录也是同个线程吗？



Q：玩家可以动态绑定指定的游戏逻辑服吗？之后的每个请求都由该游戏逻辑服来处理吗？



Q：框架支持灵活的线程扩展与设置吗？



Q：能与 spring 融合吗？除了 spring 外，还能与任何其他的框架做融合，从而使用其他框架的相关生态吗？



Q：能解决[协议碎片](https://www.yuque.com/iohao/game/ieimzn)吗？



Q：action 支持类 MVC 的编码方式吗？

> action 表示一个业务动作；是接收请求，处理请求，响应数据的统称；
>
> 是路由与方法映射的一种编程模型，这种编程模型可以避免类爆炸。它提供一种清晰、灵活的方式来组织应用程序代码，将请求、响应和业务逻辑分离开来，并实现松耦合。这使得应用程序更易于维护和测试，并支持代码重用。

```java
// 你敢相信这样就能处理长连接通信请求吗
// 这是 ioGame 编写业务逻辑时的代码，如果是新招的业务编写人员，几乎是零学习成本的。
@ActionController(1)
public class DemoAction {
    @ActionMethod(0)
    public HelloReq here(HelloReq helloReq) {
        HelloReq newHelloReq = new HelloReq();
        newHelloReq.name = helloReq.name + ", I'm here ";
        return newHelloReq;
    }
}
```



Q：action 请求参数除了对象外，能够支持基础类型和 List 吗？参数支持 JSR380 验证吗？



Q：action 返回值支持基础类型、List、对象的返回值吗？还是说只能支持传统的推送方式来响应数据？



Q：action 支持断言和异常机制吗？



Q：action 支持插件扩展吗？如请求信息的打印？



Q：action 支持业务代码定位与跳转吗？推送广播数据时，支持业务代码定位与跳转吗？



Q：支持类似 FlowContext 的特性吗？FlowContext 可扩展吗、可定制吗、可扩展元附加信息吗？（FlowContext 是一种对当前请求处理的上下文）



Q：能用一套业务代码，无需任何改动，同时支持多种通信协议：WebSocket、TCP 吗？



Q：能用一套业务代码，能轻松切换和扩展不同的通信协议：Protobuf、JSON 吗？



**通讯方式相关的提问**

支持跨进程、跨机器的单个游戏逻辑服间的相互通讯吗？

支持跨进程、跨机器与同类型多个游戏逻辑服相互通讯吗？

支持跨进程、跨机器的【游戏逻辑服】与多个【游戏对外服】通讯吗？

支持跨进程、跨机器的脉冲通讯吗？



</details>

<br>

## ioGame 简介

你是否想要开发一个**高性能、稳定、易用、自带负载均衡、避免类爆炸设计、可跨进程跨机器通信、集群无中心节点、集群自动化、有状态多进程的分布式的**网络编程服务器呢？如果是的话，这里向你推荐一个由 java 语言编写的网络编程框架 ioGame。下面将会从多个方面来对框架做一些简单的介绍。



ioGame 是一个轻量级的网络编程框架，适用于**网络游戏服务器、物联网、内部系统**及各种需要长连接的场景；



> ioGame 有以下特点：
>
> 1. 真轻量级、无锁异步化、事件驱动的架构设计。
> 2. 包体小、内存占用少、启动速度快。
> 3. ioGame 是纯 javaSE 的，使得 ioGame 能与其他框架方便的进行集成、融合，如 Spring ...等。
> 4. 在学习成本方面，ioGame 的学习成本非常低，可以说是**零学习成本**，即使没有游戏编程经验，也能轻松上手。开发者只需掌握普通的 java 方法或 webMVC 相关知识，就能用框架开发业务。
> 5. ioGame 在架构上解决了传统框架所产生的 **N\*N 问题**。
> 6. 在轻量级方面，ioGame **不依赖任何第三方**中间件或数据库**就能支持集群、分布式**，只需要 java 环境就可以运行。
> 7. 在架构灵活性方面，ioGame 的架构由三部分组成：1.游戏对外服、2.Broker（游戏网关）、3.游戏逻辑服；三者既可相互独立，又可相互融合，这意味着使用 ioGame 可以**适应任何类型的游戏**。
> 8. 架构是可以动态扩缩的，游戏对外服、游戏逻辑服、Broker（游戏网关）都**支持动态增加和减少**。
> 9. 在分布式开发体验方面，ioGame 支持多服单进程的启动方式，这使得开发者在开发和调试分布式系统时更加简单。
> 10. 在生态规划方面，游戏逻辑服是支持独立运行，**从而实现功能模块化的可能性**。
> 11. 具备全链路调用日志跟踪特性。
> 12. 在通讯方式方面，提供多种通讯方式，且逻辑服之间可以相互跨机器通信。
> 13. 在编码风格上，提供了类 MVC 的编码风格（无入侵的 Java Bean ），这种设计方式很好的**避免了类爆炸**。
> 14. 在线程安全方面，框架为开发者解决了单个玩家的**并发问题**。
> 15. 在连接方式方面，允许开发者**使用一套业务代码**，同时支持 TCP、WebSocket、UDP 等多种连接方式，无需进行任何修改，并且可扩展。
> 16. 在通信协议方面，ioGame 让开发者**用一套业务代码**，就能轻松切换不同的通信协议，如 Protobuf、JSON 等，并且可扩展。
> 17. 在增减协议方面，ioGame 可以让你在**新增或减少协议**时，**无需重启**游戏对外服与 Broker（游戏网关）；这样既能避免玩家断线，又能避免因新增、减少协议而重启所有机器的痛点。
> 18. action 支持自动装箱、拆箱基础类型，用于解决[协议碎片](https://www.yuque.com/iohao/game/ieimzn)的问题。
> 19. 业务框架提供了插件机制，插件是可插拨、可扩展的。
> 20. 在部署方面，ioGame 支持**多服单进程**的方式部署，也支持**多服多进程**多机器的方式部署；在部署方式上可以随意的切换而不需要更改代码。
> 21. 在安全方面，所有的游戏逻辑服不需要开放端口，**天然地避免了扫描攻击**。
> 22. 在模拟客户端测试方面，ioGame 提供了压测&模拟客户端请求模块。该模块**可以模拟真实的网络环境**，并且在模拟测试的过程中与服务器的交互是可持续的、可互动的，同时也是支持自动化的。
> 23. 框架为开发者提供了同步、异步、异步回调的方法，用于逻辑服之间的相互访问。
> 24. 分布式事件总线支持（类似 MQ、Redis 发布订阅机制；可跨多个机器通信、可跨多个进程通信）。
> 25. 提供优雅的路由访问权限控制。
> 26. 具备智能的同进程亲和性。
> 27. JSR380 验证、断言 + 异常机制 = 更少的维护成本。
> 28. 一次编写到处对接，提升巨大的生产力，能为各客户端生成可交互的代码；你只需要编写一次 java 代码，就能为 Unity、Godot、CocosCreator、Laya、Vue、Angular 等 C#、GDScript、TypeScript 类型的项目生成交互接口。
>



ioGame 是一个专为网络编程设计的轻量级框架，它可以帮助你快速地搭建和运行自己的网络服务器。ioGame 适用于**网络游戏服务器、物联网、内部系统**及各种需要长连接的场景。如各种类型和规模的网络游戏，无论是 H5、手游还是 PC 游戏，无论是简单的聊天室，还是复杂的**全球同服**、回合制游戏、策略游戏、放置休闲游戏、即时战斗、MMORPG 等，ioGame 都可以满足你的需求。



ioGame 在打包、内存占用、启动速度等方面也是优秀的。打 jar 包后大约 **15MB**，应用通常会在 **0.x 秒**内完成启动，内存占用小。详细请看 [快速从零编写服务器完整示例](https://www.yuque.com/iohao/game/zm6qg2#LF8Qh)。



在生态融合方面，ioGame 可以很方便的[与 spring 集成](https://www.yuque.com/iohao/game/evkgnz)（5 行代码）；除了 spring 外，还能与任何其他的框架做**融合**，如：solon ...等，从而使用其他框架的相关生态。



在学习成本方面，ioGame 的学习成本非常低，可以说是**零学习成本**，即使没有游戏编程经验，也能轻松上手。开发者只需掌握普通的 java 方法或 webMVC 相关知识，就能用框架开发业务。框架不要求开发者改变编码习惯，而是自身适应开发者的需求。



与客户端对接方面，能帮助客户端开发者**减少巨大的工作量**。ioGame 具备**一次编写到处对接**的能力，从而做到了你们团队提升巨大的生产力可能性。ioGame 提供了多种语言的 SDK 支持及相关语言的[代码生成](https://www.yuque.com/iohao/game/irth38)，分别是 C#、TypeScript、GDScript。这将意味着，你只需要编写一次 java 业务代码，就能为 Godot、Unity、Laya、CocosCreator、Vue 等前端项目生成交互接口，并同时与这些游戏引擎或现代化的前端框架交互。



ioGame 在架构上解决了传统框架所产生的 **N\*N 问题**（[与传统架构对比](https://www.yuque.com/iohao/game/cklv8p)）。传统架构在扩展机器时，需要借助很多第三方中间件，如：Redis、MQ、ZooKeeper ...等，才能满足整体架构的运作。通常，只要引入了需要安装的中间件才能做到扩展的，那么你的架构或者说框架，基本上与轻量级无缘了。



在轻量级方面，ioGame **不依赖任何第三方**中间件或数据库**就能支持集群、分布式**，只需要 java 环境就可以运行。这意味着在使用上简单了，在部署上也为企业减少了部署成本、维护难度。使用 ioGame 时，只需一个依赖即可获得整个框架，而无需安装其他服务，如： Nginx、Redis、MQ、Mysql、ZooKeeper、Protobuf 协议编译工具 ...等。



在[架构灵活性](https://www.yuque.com/iohao/game/zqgdv3g9if8w37vr)方面，ioGame 的架构由三部分组成：1.游戏对外服、2.Broker（游戏网关）、3.游戏逻辑服；三者既可相互独立，又可相互融合。这意味着使用 ioGame 可以**适应任何类型的游戏**，因为只需通过调整部署方式，就可以满足不同类型的游戏需求。在 ioGame 中进行这些调整工作非常简单，而且不会对现有代码产生不良影响。



架构是可以动态扩缩的，游戏对外服、游戏逻辑服、Broker（游戏网关）都**支持动态增加和减少**。无论未来玩家数量增加或减少，我们都能够轻松应对。同时，架构是**支持玩家无感知更新**的，这得益于分布式设计。举例来说，如果 A 类型的游戏逻辑服需要增加一些新功能，我们可以启动 A-3、A-4 等已经支持了新功能的服务器，然后逐步将之前的 A-1 和 A-2 下线，从而实现了无感知的更新。



在集群方面，ioGame 的 Broker （游戏网关）采用无中心节点、[自动化的集群设计](https://www.yuque.com/iohao/game/qmo56c)，所有节点平等且自治，不存在单点故障。集群能够**自动管理和弹性扩缩**，节点加入或退出时，能够自动保证负载均衡和数据一致性，不影响服务可用性。



在分布式方面，ioGame 的逻辑服使用了分布式设计思想，将服务器分为[游戏对外服](https://www.yuque.com/iohao/game/wotnhl)、[游戏逻辑服](https://www.yuque.com/iohao/game/ensevx)等不同层次，并且每一层都有明确的职责和接口。这样可以提高代码可读性和可维护性，并且方便进行**水平扩展**。



在分布式开发体验方面，通常在开发分布式应用时是需要启动多个进程的。这会让调试与排查问题变得非常困难，从而降低开发者的效率、增加工作量等，这也是很多框架都**解决不了的问题**，但 ioGame 做到了！ioGame 支持多服单进程的启动方式，这使得开发者在开发和调试分布式系统时更加简单。



在[生态规划](https://www.yuque.com/iohao/game/ddbovlbhb3g6bno2)方面，我们的游戏逻辑服是支持独立运行的，只需接入 Broker（游戏网关）上，就可以为玩家和其他游戏逻辑服提供功能上的扩展与增强。我们可以将一些**游戏逻辑服组件化**，并制作成相对通用的组件，**从而实现功能模块化的可能性**。这么做有几个优点：
1. 避免一些重复开发的工作量。
2. 减少各功能模块的耦合。
3. 更符合单一职责的设计，将相对通用的功能扩展成一个个的**功能逻辑服**。如，公会逻辑服、好友逻辑服、登录逻辑服、抽奖逻辑服、公告逻辑服、排行榜逻辑服...等。
4. 由于模块功能是独立，那么将来可以对任意的功能逻辑服进行扩容，且不需要改动任何代码。
5. 这些组件化后的功能逻辑服就好比一件件武器，积累得足够多时就形成了自己的生态武器库，可以更好的帮助公司与同行竞争。
6. 代码泄漏机率更小。传统的游戏项目通常采用单机结构，把所有的代码放在一个目录中。这样做有很大的风险，因为如果代码泄漏了，就会泄漏整个项目的内容。当功能模块化后，可以让不同的开发人员只负责自己的游戏逻辑服模块，从而避免代码泄漏的风险和影响。
7. 团队管理员只需要在内网服务器上部署一个游戏网关和游戏对外服，而开发人员就可以在本机上编码和测试自己的游戏逻辑服模块。这样还有以下好处：
   - 游戏客户端不会因为游戏逻辑服的变更或重启而断开连接。
   - 开发人员不需要启动其他人的游戏逻辑服模块。
   - 开发人员可以通过 ioGame 自动生成的文档来进行模块间的对接。




ioGame 具备[全链路调用日志跟踪](https://www.yuque.com/iohao/game/zurusq)特性，这在分布式下非常的实用。该特性为每个请求分配一个唯一标识，并记录在日志中，通过唯一标识可以快速的在日志中过滤出指定请求的信息。ioGame 提供的全链路调用日志跟踪特性更是强大，**支持跨机器、跨进程**。简单的说，从玩家的请求进来到结束，无论该请求经过了多少个游戏逻辑服，都能精准记录。



在通讯方式方面，大部分框架只能支持推送（广播）这一类型的通讯方式；ioGame 则提供了多种类型的通讯方式，通过对各种通讯方式的组合使用，可以简单完成以往难以完成的工作，并且这些通讯方式都支持跨进程、跨机器通信，且具备全链路调用日志跟踪。这些通讯方式分别是
1. [请求响应](https://www.yuque.com/iohao/game/krzxcmgoispw0gl8)（单次请求处理）
2. [广播](https://www.yuque.com/iohao/game/qv4qfo)（推送）
3. [单个逻辑服间的相互通讯](https://www.yuque.com/iohao/game/anguu6)（可跨机器通信、可跨进程通信）
4. [与同类型多个逻辑服相互通讯](https://www.yuque.com/iohao/game/rf9rb9)（可跨多个机器通信、可跨多个进程通信）
5. [脉冲通讯](https://www.yuque.com/iohao/game/zgaldoxz6zgg0tgn)（可跨多个机器通信、可跨多个进程通信）
6. [分布式事件总线](https://www.yuque.com/iohao/game/gmxz33)（类似 MQ、Redis 发布订阅机制；可跨多个机器通信、可跨多个进程通信）



在编码风格上，ioGame 为开发者提供了类 MVC 的编码风格（无入侵的 Java Bean ），这种设计方式很好的**避免了类爆炸**。同时，框架为开发者提供了同步、异步、异步回调的方法，用于逻辑服之间的相互访问；这使得开发者所编写的代码会非常的优雅，并且具备全链路调用日志跟踪。



从 ioGame21 开始，框架添加了虚拟线程的相关支持。各逻辑服之间通信阻塞部分使用虚拟线程，这样可以很好的避免阻塞业务线程，并大幅提高了框架的吞吐量。



在线程安全方面，框架为开发者解决了单个玩家的**并发问题**；即使玩家重新登录后，也会使用相同的线程来消费业务，并推荐使用[领域事件](https://www.yuque.com/iohao/game/gmfy1k)来解决同一房间或业务内多个玩家的并发问题。[框架在线程的扩展性](https://www.yuque.com/iohao/game/eixd6x)上提供了友好的支持，开发者可以很容易的编写出无锁并发代码，这得益于 ioGame 独有的线程执行器设计与扩展。换句话说，你不会因为并发问题烦恼。



在无锁并发方面，ioGame 提供了优雅、独特的线程执行器设计。通过该特性，开发者能轻易的编写出无锁高并发的代码。



在连接方式方面，ioGame 允许开发者**使用一套业务代码**，**同时支持**多种连接方式，无需进行任何修改。ioGame 已经支持了 TCP、WebSocket 和 UDP 连接方式，并且也支持在这几种连接方式之间进行灵活切换。连接方式是可扩展的，并且扩展操作也很简单，这意味着之后如果支持了 KCP，无论你当前项目使用的是  TCP、WebSocket 还是 UDP，都可以切换成 KCP；注意了，即使切换到 KCP 的连接方式，现有的业务代码也无需改变。



在通信协议方面，ioGame 让开发者**用一套业务代码**，就能轻松[切换和扩展不同的通信协议](https://www.yuque.com/iohao/game/uq2zrltrc7to27bt)，如 Protobuf、JSON 等。只需一行代码，就可以从 Protobuf 切换到 JSON，无需改变业务方法。



在增减协议方面，ioGame 可以让你在**新增或减少协议**时，**无需重启**游戏对外服与 Broker（游戏网关）；这样既能避免玩家断线，又能避免因新增、减少协议而重启所有机器的痛点。



在协议碎片方面，action 支持自动装箱、拆箱基础类型特性，用于解决[协议碎片](https://www.yuque.com/iohao/game/ieimzn)的问题。同时该特性除了能使你的业务代码更加清晰以外，还能大幅提高开发者在该环节的生产力。



在[同进程亲和性](https://www.yuque.com/iohao/game/unp26u)方面，在同一进程内，不同 Netty 实例之间的通信，是通过内存进行传输的，不需要经过网络传输，数据传输速度极快。同进程亲和性指的是，优先访问同进程内的游戏逻辑服，当同进程内没有能处理请求的游戏逻辑服时，才会去其他进程或机器中查找能处理请求的游戏逻辑服；简单点说，框架对于请求的处理很智能，会优先将请求给同进程内的逻辑服消费。



在开发体验方面，ioGame 非常注重开发者的开发体验；框架提供了 [JSR380 验证](https://www.yuque.com/iohao/game/ghng6g)、[断言 + 异常机制](https://www.yuque.com/iohao/game/avlo99)、[业务代码定位](https://www.yuque.com/iohao/game/pf3sx0)，action 支持自动装箱、拆箱基础类型，用于解决[协议碎片](https://www.yuque.com/iohao/game/ieimzn)的问题 ...等。诸多丰富的功能，使得开发者的业务代码更加的清晰、简洁；



业务框架提供了[插件](https://www.yuque.com/iohao/game/bsgvzglvlr5tenao)机制，插件是可插拨、可扩展的。框架内置提供了 [DebugInOut](https://www.yuque.com/iohao/game/pf3sx0)、[action 调用统计](https://www.yuque.com/iohao/game/znapzm1dqgehdyw8)、[业务线程监控插件](https://www.yuque.com/iohao/game/zoqabk4gez3bckis)、[各时间段调用统计插件](https://www.yuque.com/iohao/game/umzk2d6lovo4n9gz)...等插件；不同的插件提供了不同的关注点，比如我们可以使用调用、监控等插件相互配合，可以让我们在开发阶段就知道**是否存在性能问题**。合理利用好各个插件，可以让我们在开发阶段就能知道问题所在，提前发现问题，提前预防问题。



在部署方面，ioGame 支持**多服单进程**的方式部署，也支持**多服多进程**多机器的方式部署；在部署方式上可以随意的切换而不需要更改代码。日常中我们可以按照单体思维开发，到了生产可以选择使用多进程的方式部署。



在安全方面，所有的游戏逻辑服[不需要开放端口，天然地避免了扫描攻击](https://www.yuque.com/iohao/game/cklv8p#NQ6Oc)。由于不需要为每个逻辑服分配独立的端口，那么我们在使用诸如云服务器之类的服务时，就不需要担心端口开放权限的问题了。别小看这一个环节，通常这些小细节最浪费开发者的时间。由于我们不需要管理这些 IP:Port，**这部分的工作量就自然地消失了**。



在模拟客户端测试方面，ioGame 提供了[压测&模拟客户端请求](https://www.yuque.com/iohao/game/tc83ud)模块。此模块是用于模拟客户端，简化模拟工作量，只需要编写对应请求与回调。除了可以模拟简单的请求外，通常还可以做一些复杂的请求编排，并支持复杂业务的压测。**与单元测试不同的是，该模块可以模拟真实的网络环境，并且在模拟测试的过程中与服务器的交互是可持续的、可互动的，同时也是支持自动化的**。



开发者基于 ioGame 编写的项目模块，通常是条理清晰的，得益于框架对**路由的合理设计**，同时也为路由提供了优雅的[访问权限控制](https://www.yuque.com/iohao/game/nap5y8p5fevhv99y)。当我们整理好这些模块后，对于其他开发者接管项目或后续的维护中，会是一个不错的帮助（[模块的整理与建议](https://www.yuque.com/iohao/game/ruaqza/#OBwXQ)）。或许现阶段你感受不到这块的威力，随着你深入地使用实践就能体会到这么设计的诸多好处与优势。



开发者基于 ioGame 编写的项目，通常是语法简洁的、高性能的、低延迟的。框架最低要求使用 **JDK21**，这样即可以让项目享受到**分代 ZGC** 带来的改进，还能享受语法上的简洁。分代 ZGC 远低于其**亚毫秒级**暂停时间的目标，可以在不影响游戏速度的情况下，清理掉多余的内存；这样就不会出现卡顿或者崩溃的问题了，相当于在项目中变相的引入了一位 JVM 调优大师。



综上所述，ioGame 是一个非常适合网络游戏开发的框架。可以让你轻松地创建高性能、低延迟、易扩展的游戏服务器，并且节省时间和资源。如果你想要快速地开发出令人惊艳的网络游戏，请不要犹豫，立即选择 ioGame 吧！框架屏蔽了很多复杂且重复性的工作，并可为项目中的功能模块结构、开发流程等进行**清晰的组织定义**，减少了后续的项目维护成本。



框架在开发、部署、压测&模拟测试 ...等，各个阶段都提供了很好的支持。相信你已经对 ioGame 有了一个初步的了解，虽然还有很多丰富的功能与特性没有介绍到，但你可以通过后续的实践过程中来深入了解。感谢你的阅读，并期待你使用 ioGame 来打造自己的游戏服务器。

---



## 一次编写到处对接，提升巨大的生产力



ioGame 具备一次编写到处对接的能力，从而做到了你们团队提升巨大的生产力可能性。



**一次编写**指的是编写一次 java 业务代码，而**到处对接**则是指为不同的前端项目生成与服务器交互的代码。



1. 与前端对接联调方面，ioGame 提供了[前端代码生成](https://www.yuque.com/iohao/game/irth38)的辅助功能，你只需要编写一次 java 代码，就能为 Unity、Godot、CocosCreator、Laya、Vue、Angular 等 C#、TypeScript 类型的项目生成交互接口。
2. ioGame 是非常注重开发体验的，代码注释即文档、方法即交互接口的原则。
3. ioGame 能为各种前端项目生成 `action、广播、错误码` 相关接口代码。这将意味着，你只需要编写一次业务代码，就可以同时与这些游戏引擎或现代化的前端框架交互。



前端代码生成的几个优势

1. 帮助客户端开发者减少巨大的工作量，**不需要编写大量的模板代码**。
2. **语义明确，清晰**。生成的交互代码即能明确所需要的参数类型，又能明确服务器是否会有返回值。这些会在生成接口时就提前明确好。
3. 由于我们可以做到明确交互接口，进而可以明确参数类型。这使得**接口方法参数类型安全、明确**，从而有效避免安全隐患，从而**减少联调时的低级错误**。
4. 减少服务器与客户端双方对接时的沟通成本，代码即文档。生成的联调代码中有文档与使用示例，方法上的示例会教你如何使用，即使是新手也能做到**零学习成本**。
5. 帮助客户端开发者屏蔽与服务器交互部分，**将更多的精力放在真正的业务上**。
6. 为双方联调减少心智负担。联调代码使用简单，**与本地方法调用一般丝滑**。
7. 抛弃传统面向协议对接的方式，转而**使用面向接口方法的对接方式**。
8. 当我们的 java 代码编写完成后，我们的文档及交互接口可做到同步更新，**不需要额外花时间去维护对接文档及其内容**。



## ioGame 的组成

ioGame 由 [网络通信框架] 和 [业务框架] 组成

- 网络通信框架：职责是各服务器之间的网络通信
- 业务框架：职责是业务逻辑的处理方式和编写方式



**网络通信框架**
[SOFABolt](https://www.sofastack.tech/projects/sofa-bolt/overview/) 是蚂蚁金融服务集团开发的一套基于 Netty 实现的网络通信框架。

- 为了让 Java 程序员能将更多的精力放在基于网络通信的业务逻辑实现上，而不是过多的纠结于网络底层 NIO 的实现以及处理难以调试的网络问题，Netty 应运而生。
- 为了让中间件开发者能将更多的精力放在产品功能特性实现上，而不是重复地一遍遍制造通信框架的轮子，SOFABolt 应运而生。

Bolt 名字取自迪士尼动画-闪电狗，是一个基于 Netty 最佳实践的轻量、易用、高性能、易扩展的通信框架。



**业务框架**
如果说  sofa-bolt 是为了让 Java 程序员能将更多的精力放在基于网络通信的业务逻辑实现上，而业务框架正是解决业务逻辑如何方便实现这一问题上。业务框架是游戏框架的一部分，职责是简化程序员的业务逻辑实现，业务框架使程序员能够快速的开始编写游戏业务。



业务框架对于每个 action （即业务的处理方法） 都是通过 asm 与 Singleton、Flyweight 、Command 等设计模式结合，对 action 的获取上通过 array 来得到，是一种近原生的方式。



单线程中，业务框架平均每秒可以执行 1152 万次业务逻辑。



<details>
<summary>业务框架性能报告JMH--点我展开</summary>

![ioGameJmeter](https://github.com/game-town/ioGame/assets/26356013/96bb2ea9-f459-402d-8957-ff47e251285d)

----------------------------------------
上面是在单线程中的测试数据，业务框架平均每秒执行 1152 万次。

</details>

------

## 架构简图

![ioGame](https://github.com/game-town/ioGame/assets/26356013/70c6a157-ca12-4a46-9b35-c67f329d067e)


<p align="center" style="color:red">
通过 ioGame 你可以很容易的搭建出一个集群无中心节点、集群自动化、分布式的网络游戏服务器！
</p>

> 从图中可以看出，游戏网关支持以集群方式启动多个实例。这个设计选择了集群的方式，因为游戏网关通常是无状态的，主要用于调度和转发任务
>
> 
>
> 而游戏对外服、游戏逻辑服使用分布式设计，支持启动多个相同类型的服务。这意味着，当玩家数量增加时，我们可以轻松增加相应类型的游戏逻辑服以处理更多请求。
>
> 
>
> 以游戏逻辑服为例，假设我们启动了两个 A 类型的游戏逻辑服，分别为 A-1 和 A-2。当玩家向 A 类型的游戏逻辑服发起多次请求时，游戏网关会使用默认的随机负载策略将请求分配给 A-1 和 A-2 来处理。
>
> 
>
> 现在我们明白，游戏对外服和游戏逻辑服都支持动态增加和减少。无论未来玩家数量增加或减少，我们都能够轻松应对。架构是**支持玩家无感知更新**的，这得益于分布式设计。举例来说，如果 A 类型的游戏逻辑服需要增加一些新功能，我们可以启动 A-3、A-4 等已经支持了新功能的服务器，然后逐步将之前的 A-1 和 A-2 下线，从而实现了无感知的更新。
>
> 
>
> 此外，框架还支持玩家[动态绑定游戏逻辑服](https://www.yuque.com/iohao/game/idl1wm)；玩家与游戏逻辑服绑定后，之后的请求都由该游戏逻辑服来处理。
>
> 
>
> 除了游戏之外，ioGame 也适用于物联网相关项目。只需将图中的玩家视为具体的设备，即使存在数亿个设备，ioGame 的架构也可以轻松支持。从 2022 年开始，已经有一些物联网公司开始采用这一解决方案，并得到了很好的体验。
>
> 
>
> ioGame 适用于**网络游戏服务器、物联网、内部系统**及各种需要长连接的场景；



| 名称               | 扩展方式 | 职责             |
| ------------------ | -------- | ---------------- |
| 游戏对外服         | 分布式   | 与玩家连接、交互 |
| 游戏逻辑服         | 分布式   | 处理具体业务逻辑 |
| Broker（游戏网关） | 集群     | 调度和转发任务； |



无锁异步化与事件驱动的架构设计、集群无中心节点、自带负载均衡、分布式支持、可动态增减机器、避免类爆炸的设计；



图中的每个游戏对外服、每个游戏逻辑服、每个 broker （游戏网关）都可以在单独的进程中部署，逻辑服之间可以跨进程通信（游戏对外服也是逻辑服的一种）。



**游戏网关集群**

broker （游戏网关）支持**集群**的方式部署，集群的使用是简单的，**集群无中心节点、集群自动化、自带负载均衡**。ioGame 本身就包含服务注册，你不需要外接一个服务注册中心，如 Eureka，ZooKeeper 等（变相的节约服务器成本）。



通过 broker （游戏网关） 的介入，之前非常复杂的负载均衡设计，如服务注册、健康度检查（后续版本提供）、到服务端的连接维护等这些问题，在 ioGame 中都不需要了，结构也简单了很多。实际上单台 broker （游戏网关） 性能已经能够满足了，因为游戏网关只做了转发。



**逻辑服**

逻辑服通常说的是游戏对外服和游戏逻辑服。逻辑服可以有很多个，逻辑服扩展数量的理论上限是 netty 的连接上限。



**游戏对外服**

对外服保持与用户（玩家）的长连接。先来个假设，假如我们的一台硬件支持我们建立用户连接的上限是 5000 人，当用户量达到 7000 人时，我们可以多加一个对外服务器来进行分流减压。由于游戏对外服扩展的简单性，意味着支持同时在线玩家可以轻松的达到百万、千万甚至更多。



即使我们启动了多个游戏对外服，开发者也不需要关心这些玩家连接到了哪个游戏对外服的问题，这些玩家总是能接收到广播（推送）消息的，因为框架已经把这些事情给做了；在玩家的角度我们只有“一个”服务器，同样的，在开发者的角度我们只有“一个”游戏对外服；



通常，有些开发者想知道游戏对外服最大支持多少玩家连接。关于这个问题，只需要搜索 Netty 相关知识即可，因为[游戏对外服](https://www.yuque.com/iohao/game/wotnhl)本质上是 Netty。



同样的，如果开发者已经熟悉了 Netty 相关知识，那么在游戏对外服的扩展上也会变得非常的容易。



**在结构组合上（部署多样性）**

在部署上，支持多服单进程的方式部署（类似单体应用、在分布式开发时，调试更加方便）、也支持多服多进程多机器的方式部署。



架构由三部分组成：1.游戏对外服、2.Broker（游戏网关）、3.游戏逻辑服；**三者既可相互独立，又可相互融合**，如：

- 游戏对外服、Broker（游戏网关）、游戏逻辑服这三部分，在一个进程中；【单体应用；在开发分布式时，调试更加方便】
- 游戏对外服、Broker（游戏网关）、游戏逻辑服这三部分，在多个进程中；【分布式】
- 游戏对外服、Broker（游戏网关）这两部分在一个进程中；而游戏逻辑服在多个进程中；【类似之前游戏的传统架构】
- 甚至可以不需要游戏对外服，只使用Broker（游戏网关）和游戏逻辑服这两部分，用于其他系统业务；



因为 ioGame 遵循面向对象的设计原则（单一职责原则、开闭原则、里式替换原则、依赖倒置原则、接口隔离原则、迪米特法则）等，所以使得架构的职责分明，可以灵活的进行组合；



游戏对外服是架构的三部分之一，默认的游戏对外服是基于 netty 实现的。如果有需要，将来我们还可以使用基于 mina、smart-socket 等通信框架编写，额外提供一个游戏对外服的实现；即使是使用 mina、smart-socket 提供的游戏对外服，也并不会影响现有的游戏逻辑服业务逻辑，因为游戏对外服满足单一职责原则，只维护用户（玩家）长连接相关的。



开发人员几乎都遇见过这么一种情况；在项目初期阶段，通常是以单体项目的方式进行开发，随着需求不断的增加与迭代，会演变成一个臃肿的项目；此时在对一个整体进行拆分是困难的，成本是极高的。甚至是不可完成的，最后导致完全的重新重构；



ioGame 提供了在结构组合上的部署多样性，通过组合的方式，在项目初期就可以避免这些拆分问题。在开发阶段中，我们可以使用单体应用开发思维，降低了开发成本。通过单体应用的开发方式，在开发分布式项目时，调试更加的方便；这既能兼顾分布式开发、项目模块的拆分，又能降低团队的开发成本；



**架构其他疑问**



**问题一**：如果一个请求经过 ioGame 再返回到客户端，对比直接用 Netty 搭个简单服务器，延迟会增加很多吗？纳秒级别还是毫秒级别？



答：是内存级。将三者部署在一起后，三者是通过内存进行传输的，不需要经过网络传输，数据传输速度极快。



原因分析，为了更好理解，现在把三者用字母代替，A.游戏对外服、B.Broker（游戏网关）、C.游戏逻辑服

- ABC ：三者在一个进程中，他们之间使用内存通信；
- AB + C ：【游戏对外服和游戏网关】在一个进程中，他们之间使用内存通信；
- A + BC ：【游戏网关和游戏逻辑服】在一个进程中，他们之间使用内存通信；



此外，ioGame 还支持同进程亲和性特性；如果我们启动了同类型的多个游戏逻辑服。【游戏网关】会优先选择与同进程的那个游戏逻辑服进行通信，使用内存通信；在同进程中没有找到时，会在同类型的多个游戏逻辑服中随机选出一个通信。



简单的一句话概括就是：同进程亲和性是指，优先访问同进程内的游戏逻辑服，当同进程内没有能处理请求的游戏逻辑服时，才会去其他进程或机器中查找能处理请求的游戏逻辑服；



所以，想要什么样的通信效果，取决于你所选择的启动方式（部署方式）。



**问题二**：如何更好的理解 ioGame 架构由三部分组成：1.游戏对外服、2.Broker（游戏网关）、3.游戏逻辑服；三者既可相互独立，又可相互融合？



这就好比编码时常用的三层 controller、service、dao

- 将这三部分写在一个类也是可以的，【controller、service、dao】
- 或者 【controller、service】+ dao
- 又或者 controller +【service、dao】



不要将三者看成一台台生硬的机器，而是三份独立的代码。三者是可以随意组合的，这样可以满足开发者当前项目的不同需求。



[ioGame 架构多样性](https://www.yuque.com/iohao/game/zqgdv3g9if8w37vr)

[多服单进程、多服多进程的启动方式](https://www.yuque.com/iohao/btmfld/fkrsgu4i648fuim3)



**架构优点**

架构有很高程度的抽象，让设计者更加关注于业务，而无需考虑底层的实现、通信参数等问题。



逻辑服的位置透明性；同时，由于模块化、抽象化，使得整个架构各服务器之间耦合度很低，逻辑服注册即可用，大大增加了可伸缩性、可维护性，动态扩展变得简单而高效。由于逻辑服是注册到 Broker（游戏网关） 上的，所以逻辑服可以动态的增加、删除、改变；由于逻辑服之间耦合度较小，调试和测试的工作也是可控的；



架构比较清晰的就是，游戏对外服负责维护客户端的接入（用户、玩家的连接），游戏逻辑服专心负责业务逻辑，他们之间的调度由 Broker（游戏网关）来负责；因为架构拆分的合理，所以特别方便用 k8s 来自由伸缩部署这三种服，哪个服水位高就扩容哪个，水位过去了又可以缩容。



参考：[构架简图中：对外服、游戏网关、游戏逻辑服各自的职责](https://www.yuque.com/iohao/game/dqf0he)



## 通讯方式

ioGame 提供了[多种类型的通讯方式](https://www.yuque.com/iohao/game/nelwuz)，通过对各种通讯方式的组合使用，可以简单完成以往难以完成的工作，并且这些通讯方式都支持跨进程、跨机器通信的。



传统框架想要实现类似的通讯功能，只能借助大量的第三方中间件，而 ioGame 则无需任何中间件就能实现这些通讯功能。这意味着在使用上简单了，在部署上也为企业减少了部署成本、维护难度。



这些通讯方式都是通过扩展实现的，重点是支持可扩展。



一般传统的框架只提供了接收请求，当请求处理完后使用推送的方式将数据响应给请求端。但在使用 ioGame 时，不要被过去的传统框架束缚住，可以做任何大胆的设计，因为通讯方式足够丰富；

![img](https://user-images.githubusercontent.com/26356013/289906560-56efd0ed-ef60-4c77-8eef-36793d01662c.png)


总的来说，ioGame 支持的通讯方式分为两大类，分别是路由类和主题类；下面分别对这些通讯方式做一些应用场景举例介绍。



框架对这些通讯方式提供了代码调用点的日志，简单点说就是框架可以让开发者知道，是在哪一行代码中触发的业务逻辑。



我们可以想象一下，假如框架没有提供代码调用点的日志会是什么样的；比如，游戏前端发送一个业务请求到游戏服务器中，但是处理这个请求的业务方法，会触发多个响应（通常是推送、广播）给游戏前端。一但时间久了，开发者是很难知道分别响应了哪些业务数据给游戏前端，特别是一些二手项目；所以这将是一个灾难性的问题，因为这会耗费大量的时间来寻找这些相关的业务代码。



<details>
<summary>多类型的通讯方式的详细内容--点我展开</summary>

**1.单次请求处理**

[1.1 请求、无响应](https://www.yuque.com/iohao/game/krzxcmgoispw0gl8)

action 处理逻辑时，使用是就是这种通讯方式。将方法声明为 void，无需给请求端任何响应；本质上是不给请求端推送任何数据；

当请求端发起请求后，游戏逻辑服不会发送任何响应给请求端。可以用在在网络通讯中，存在着不需要接收方回执确认的调用模型，如数据采集的场景: 打点采集、日志传输、metrics上报等。

在写 [action](https://www.yuque.com/iohao/game/sqcevl) 时，将方法返回值声名为 **void** 就表示处理 **请求、无响应**的。



[1.2 请求、响应](https://www.yuque.com/iohao/game/krzxcmgoispw0gl8)

action 处理逻辑时，使用是就是这种通讯方式。将数据 return 给请求端；本质上是将数据推送到请求端；

请求、响应是在游戏开发中常见的通讯模式，也就是通讯的一方发出请求，而远程通讯的对方做出响应，也就是常说的请求/响应模式。

比如：装备的升级、人物的升级、玩家的移动、抽奖、游戏前端到某一个场景时需要从游戏服务端获取一些对应的场景配置等；

在写 [action](https://www.yuque.com/iohao/game/sqcevl) 时，方法有返回值的就表示处理 **请求、响应**的，框架会将这个返回值给到请求端。



**2.推送**

[2.1 指定单个或多个用户广播](https://www.yuque.com/iohao/game/qv4qfo)

服务器主动的将数据给【单个或多个玩家】

向一个或多个指定的用户（玩家）主动发送一些数据。比如：

- 给指定的在线玩家发送一些奖励。
- 给在同一个房间内的玩家广播一些数据，如某一个玩家射击子弹，把这子弹的数据广播给房间内的其他玩家。如几个玩家在同一个房间内打牌，某个玩家出牌后，把这张牌的数据广播给房间内的其他玩家。



[2.2 全服广播](https://www.yuque.com/iohao/game/qv4qfo)

服务器主动的将数据给【全服在线的玩家】

给全服的所有在线玩家广播消息，如广播公告、即将停服维护等。

详细示例可参考：[广播示例](https://www.yuque.com/iohao/game/qv4qfo)



**3.单个逻辑服间的相互通讯**

[3.1【游戏逻辑服】与单个【游戏逻辑服】通信；](https://www.yuque.com/iohao/game/anguu6)**- 有返回值（可跨进程）**

1 : 1 的请求，也就是【**单次请求处理**】的通讯方式；支持进程内、跨进程、跨机器；开发者无需关心，一切都是自动的。



比如：我们有两个游戏逻辑服，分别是：a.天气预报逻辑服、b.战斗逻辑服。



现在我们设想一个回合制游戏的战斗场景，需要配合天气，根据天气来增强或者减弱某个英雄的能力。



那么在战斗开始前，战斗逻辑服只需要向游戏网关发起一个获取当前天气的请求，就可以得到当前的天气信息了，在根据当前的天气数据来增强或减弱该英雄的能力。



又比如：a.大厅逻辑服、b.奖励发放逻辑服。大厅记录着一些数据（房间总数），奖励发放逻辑服根据当前的房间数量，来生成不同奖品，随机发放给在线用户。



详细示例可参考：[逻辑服与逻辑服之间的交互示例](https://www.yuque.com/iohao/game/anguu6)



[3.2【游戏逻辑服】与单个【游戏逻辑服】通讯；](https://www.yuque.com/iohao/game/anguu6) **- 无返回值（可跨进程）**

1 : 1 的请求，也就是【**单次请求处理**】的通讯方式；支持进程内、跨进程、跨机器；开发者无需关心，一切都是自动的。



比如：我们有两个游戏逻辑服，分别是：a.匹配逻辑服、b.房间逻辑服。



业务场景如下，多个玩家在开始游戏前需要匹配。这里假设有两个玩家，当匹配完成后，给这两个玩家返回所匹配到的房间信息。



具体实现如下，两个玩家分别向匹配逻辑服发送匹配请求，匹配逻辑服收到玩家的请求后进行逻辑处理，并成功的把这两个玩家匹配到一起，此时我们把两个匹配到一起的玩家先称为匹配结果。



匹配逻辑服只负责匹配相关的算法逻辑，所以在匹配逻辑服中，我们可以把匹配结果给到房间逻辑服，因为与匹配相关的工作已经完成了。



在匹配逻辑服中，我们可以向房间逻辑服发起一个（单个逻辑服与单个逻辑服通信请求 - 无返回值）的请求，当房间逻辑服拿到匹配结果，根据匹配结果来创建房间。房间创建完成后把结果用推送（广播）给这两名玩家。



为什么要用无返回值的通信请求呢，因为匹配逻辑服并不关心房间的创建。



详细说明可参考：[逻辑服与逻辑服之间的交互-无返回值](https://www.yuque.com/iohao/game/anguu6#cZfdx)



**4.与同类型多个逻辑服相互通讯**

[4.1【游戏逻辑服】与同类型多个【游戏逻辑服】通信；](https://www.yuque.com/iohao/game/rf9rb9)**-** **可跨进程**

1 : N 的请求，本质上是【**单次请求处理**】；支持进程内、跨进程、跨机器；开发者无需关心，一切都是自动的。



从多个游戏逻辑服中得到结果；



如： 【象棋逻辑服】有 3 台，分别是：《象棋逻辑服-1》、《象棋逻辑服-2》、《象棋逻辑服-3》，这些逻辑服可以在**不同的进程中**。



我们可以在大厅逻辑服中向【同类型】的多个游戏逻辑服请求，意思是大厅发起一个向这 3 台象棋逻辑服的请求，框架会收集这 3 个结果集（假设结果是：当前服务器房间数）。



当大厅得到这个结果集，可以统计房间的总数，又或者说根据这些信息做一些其他的业务逻辑；这里只是举个例子。实际当中可以发挥大伙的想象力。



详细示例可参考：[请求同类型多个逻辑服通信结果](https://www.yuque.com/iohao/game/rf9rb9)

其中配合[动态绑定逻辑服节点；可以实现LOL、王者荣耀匹配后动态分配房间](https://www.yuque.com/iohao/game/idl1wm)



[4.2【游戏逻辑服】与多个【游戏对外服】通信；](https://www.yuque.com/iohao/game/ivxsw5)**-** **可跨进程**



从多个游戏对外服中得到结果；



可以向游戏对外服拿一些玩家数据，或者是其他的一些操作。框架在游戏对外服中提供了 ExternalBizRegion 接口，可以使得开发者在游戏对外服中的扩展变得很简单。



框架利用这一通讯特性与 ExternalBizRegion 扩展，在不到 15 行的有效代码中，就实现了，如：查询用户（玩家）是否在线、强制用户（玩家）下线....等功能，从而实现了登录功能的增强：重复登录、顶号这些业务。



具体扩展与使用可以参考 [获取游戏对外服的数据与扩展](https://www.yuque.com/iohao/game/ivxsw5) 文档



**5.脉冲通讯**

**5.1 脉冲通讯**

脉冲通讯与发布订阅类似，但是它除了具备发布订阅的无需反馈的方式，还增加了接收消息响应的动作，这是它与发布订阅的重要区别。

需要注意的是，脉冲通讯只是一种通讯方式，不能完全取代发布订阅，而是适用于一些特殊的业务场景。虽然在理论上，这些特殊的业务场景可以使用发布订阅来完成，但这会让代码变得复杂。



[脉冲通讯方式 - 文档](https://www.yuque.com/iohao/game/zgaldoxz6zgg0tgn)

<br>



**6.分布式事件总线**

**6.1 分布式事件总线**

分布式事件总线是 ioGame 提供的通讯方式之一。该通讯方式与 Guava EventBus、Redis 发布订阅、MQ ... 等产品类似。



如果使用 Redis、MQ ...等中间件，需要开发者额外的安装这些中间件，并支付所占用机器的费用；使用 Guava EventBus 则只能在当前进程中通信，无法实现跨进程。



而 ioGame 提供的分布式事件总线，拥有上述两者的优点。此外，还可以有效的帮助企业节省云上 Redis、 MQ 这部分的支出。



事件发布后，除了当前进程所有的订阅者能接收到，远程的订阅者也能接收到（支持跨机器、跨进程、跨不同类型的多个逻辑服）。可以代替 redis pub sub 、 MQ ，并且具备全链路调用日志跟踪，这点是中间件产品做不到的。



 **ioGame 分布式事件总线，特点**

- 使用方式与 Guava EventBus 类似
- 具备**全链路调用日志跟踪**。（这点是中间件产品做不到的）
- 支持跨多个机器、多个进程通信
- 支持与多种不同类型的多个逻辑服通信
- 纯 javaSE，不依赖其他服务，耦合性低。（不需要安装任何中间件）
- 事件源和事件监听器之间通过事件进行通信，从而实现了模块之间的解耦
- 当没有任何远程订阅者时，**将不会触发网络请求**。（这点是中间件产品做不到的）



[分布式事件总线 - 文档](https://www.yuque.com/iohao/game/gmxz33)

<br>

**最后，发挥你的想象力，把这些类通讯方式用活，可以满足很多业务。**

</details>


## 快速入门

业务交互

![img](https://raw.githubusercontent.com/iohao/ioGameResource/main/images/interaction.jpg)

> 抽象的说，游戏前端与游戏服务器的的交互由上图组成。游戏前端与游戏服务器可以自由地双向交互，即发送和接收业务数据。业务数据由 .proto 文件作为载体，在前端和后端之间进行编码和解码。.proto 文件是对业务数据的描述载体，定义了数据类型和消息类型，以及它们的属性和规则。
>
> 
>
> 通过这种方式，游戏前端和游戏服务端可以建立连接，并开始相互传递业务数据，处理各自的业务。以上是对游戏前端与游戏服务器之间交互方式的介绍。接下来，我们将编写一个简单的游戏业务处理示例，并定制一个适合我们需求的业务数据协议。
>
> 
>
> **协议文件**
>
> 协议文件是对业务数据的描述载体，用于游戏前端与游戏服务器的数据交互。Protocol Buffers 是 Google 公司开发的一种数据描述语言，也简称 PB。协议文件描述还可以是 json、xml或者任意自定义的，因为最后传输时会转换为二进制，但游戏开发中 PB 是目前的最佳。
>
> 
>
> **游戏前端**
>
> 游戏前端的展现可以是 [Unity](https://unity.cn/)、 [UE](https://www.unrealengine.com/zh-CN/)、 [Cocos Creator](https://www.cocos.com/)、[FXGL](https://github.com/AlmasB/FXGL)、[Godot](https://godotengine.org/) 或者其他的游戏引擎。这些游戏引擎只是展现游戏画面的一种形式，数据交互则由通信来完成（TCP、UDP 等）。

<br>

**快速入门代码示例**

> 这里主要介绍游戏服务器相关的，下面这个示例介绍了服务器编程可以变得如此简单。

<br>

**协议文件定义**

首先我们自定义一个协议文件，这个协议文件作为我们的业务载体描述。这个协议是纯 java 代码编写的，使用的是 jprotobuf，jprotobuf 是对 google protobuf 的简化使用，性能同等。



可以把这理解成 DTO、POJO 业务数据载体等，其主要目的是用于业务数据的传输；

```java
/** 请求 */
@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
public class HelloReq {
    String name;
}
```

<br>

**Action**

游戏服务器的编程，游戏服务器接收业务数据后，对业务数据进行处理；下面这段代码可以同时支持 TCP、WebSocket、UDP 通信方式。



示例代码中展示了玩家的请求与响应处理，还展示了**跨服**（跨进程、跨机器）的请求处理的示例。

```java
@ActionController(1)
public class DemoAction {
    @ActionMethod(0)
    public HelloReq here(HelloReq helloReq) {
        // 业务数据
        var newHelloReq = new HelloReq();
        newHelloReq.name = helloReq.name + ", I'm here ";
        return newHelloReq;
    }

    // 注意，这个方法只是为了演示而写的；（ioGame21 开始支持）
    // 效果与上面的方法一样，只不过是用广播（推送）的方式将数据返回给请求方
    @ActionMethod(0)
    public void here(HelloReq helloReq, FlowContext flowContext) {
        // 业务数据
        var newHelloReq = new HelloReq();
        newHelloReq.name = helloReq.name + ", I'm here ";

        flowContext.broadcastMe(newHelloReq);
    }

    // 跨服调用示例，下面分别展示了同步与异步回调的写法
    void testShowInvokeModule(FlowContext flowContext) {
        /*
         * 框架为跨服请求提供了同步、异步、异步回调的编码风格 api。（ioGame21 开始支持）
         */
        var cmdInfo = CmdInfo.of(1,0);
        var yourData = ... 你的请求参数
        
        // 跨服请求（异步回调 - 无阻塞）-- 路由、请求参数、回调。
        flowContext.invokeModuleMessageAsync(cmdInfo, yourData, responseMessage -> {
            var helloReq = responseMessage.getData(HelloReq.class);
             // --- 此异步回调，具备全链路调用日志跟踪 ---
            log.info("异步回调 : {}", helloReq);
        });


        // 跨服请求（同步 - 阻塞）-- 路由、请求参数。
        ResponseMessage responseMessage = flowContext.invokeModuleMessage(cmdInfo, yourData);
        var helloReq = responseMessage.getData(HelloReq.class);
        log.info("同步调用 : {}", helloReq);
    }
}
```

一个方法（here）在业务框架中表示一个 [Action](https://www.yuque.com/iohao/game/sqcevl)（一个业务动作）。



方法声明的参数是用于接收前端传入的业务数据，在方法 return 时，数据就可以被游戏前端接收到。程序员可以不需要关心业务框架的内部细节。



从上面的示例可以看出，这和普通的 java 类并无区别，同时这种设计方式**避免了类爆炸**。如果只负责编写游戏业务，那么对于业务框架的学习可以到此为止了。



游戏编程就是如此简单！



**问：我可以开始游戏服务器的编程了吗？**

是的，你已经可以开始游戏服务器的编程了。



**访问示例（控制台）**

当我们访问 here 方法时（通常由游戏前端来请求），控制台将会打印

```basic
┏━━━━━ Debug. [(DemoAction.java:4).hello] ━━━━━ [cmd:1-0 65536] ━━━━━ [逻辑服 [xxx逻辑服] - id:[76526c134cc88232379167be83e4ddfc]]
┣ userId: 1
┣ 参数: active : HelloReq(id=101, name=塔姆)
┣ 响应: HelloReq(name=塔姆, I'm here )
┣ 时间: 1 ms (业务方法总耗时)
┗━━━━━ [ioGameVersion] ━ [线程:User-8-2] ━ [连接方式:WebSocket] ━ [traceId:956230991452569600] ━
```


**控制台打印说明**

> Debug. [(DemoAction.java:4).here]：  
> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;表示执行业务的是 DemoAction 类下的 here 方法，4 表示业务方法所在的代码行数。  
> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在工具中点击控制台的 DemoAction.java:4 这条信息，就可以跳转到对应的代码中（快速导航到对应的代码）。
>
> 
>
> userId :  当前发起请求的 用户 id。
>
> 参数 :  通常是游戏前端传入的值。
>
> 响应 :  通常是业务方法返回的值 ，业务框架会把这个返回值推送到游戏前端。
>
> 时间 :  执行业务方法总耗时，我们可根据业务方法总耗时的时长来优化业务。
>
> 路由信息 :  [路由](https://www.yuque.com/iohao/game/soxp4u) 是唯一的访问地址。
>
> 
>
> ioGameVersion：表示当前所使用的 ioGame 版本。
>
> 线程：当前执行 action 所使用的线程。
>
> traceId：全链路调用日志跟踪 id，每个请求唯一。
>
> 逻辑服：当前游戏逻辑服与其 id
>
> 连接方式：当前玩家所使用的连接方式，TCP、WebSocket、UDP



有了以上信息，游戏开发者可以很快的定位问题。如果没有可视化的信息，开发中会浪费很多时间在前后端的沟通上。问题包括：

- 是否传参问题 （游戏前端说传了）
- 是否响应问题（游戏后端说返回了）
- 业务执行时长问题 （游戏前端说没收到响应， 游戏后端说早就响应了）



其中代码导航可以让开发者快速的跳转到业务类对应代码中，在多人合作的项目中，可以快速的知道业务经过了哪些方法的执行，使得我们可以快速的进行阅读或修改；

<br>

## 适合人群？

1. 长期从事 web 内部系统开发人员， 想了解游戏的
2. 刚从事游戏开发的
3. 未从事过游戏开发，但却对其感兴趣的
4. 对设计模式在实践中的应用和 sofa-bolt 有兴趣的学习者
5. 可以接受新鲜事物的
6. 想放弃祖传代码的



推荐实际编程经验一年以上的人员


<br>

## 更多示例代码（广播、跨服）

在上面的简单示例中，我们展示了请求/响应、跨服调用时的同步与异步回调的写法。这里，将展示更多使用通讯相关的方法，分别是：

1. **广播（推送）**
   1. 全服广播
   2. 指定单个用户广播
   3. 指定多个用户广播

2. **跨服调用**（单个游戏逻辑服之间的交互）
   1. 同步的调用方式
   2. 异步回调的调用方式

3. **跨服调用**（请求同类型多个游戏逻辑服通信结果）
   1. 同步的调用方式
   2. 异步回调的调用方式




除了上面这三种的通讯方式外，还有更多通讯方式，可阅读相关文档：[请先读我-通讯相关](https://www.yuque.com/iohao/game/nelwuz)。

```java
@ActionController(1)
public class TestAction {
    ... ...省略部分代码
    // ======== 广播相关 ========
    // https://www.yuque.com/iohao/game/qv4qfo
    // 特点：可向任意玩家主动发送消息
    public void broadcast() {
        // ======== 广播相关 --- 全服、指定用户（单个、多个） ========
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

        // ======== 广播相关 --- 给自己广播 ========
        // 给自己发送消息 - 路由、业务数据
        flowContext.broadcastMe(cmdInfo, yourData);

        // 给自己发送消息 - 业务数据
        // 路由则使用当前 action 的路由。
        flowContext.broadcastMe(yourData);
    }

    // ======== 单个游戏逻辑服之间的交互 - 跨服调用 ========
    // https://www.yuque.com/iohao/game/anguu6
    // 特点：可接收响应
    void invokeModuleMessage() {
        // ======== 单个游戏逻辑服之间的交互 --- 同步 ========
        // 路由
        ResponseMessage responseMessage = flowContext.invokeModuleMessage(cmdInfo);
        RoomNumMsg roomNumMsg = responseMessage.getData(RoomNumMsg.class);
        log.info("同步调用 : {}", roomNumMsg.roomCount);

        // 路由、请求参数
        ResponseMessage responseMessage2 = flowContext.invokeModuleMessage(cmdInfo, yourData);
        RoomNumMsg roomNumMsg2 = responseMessage2.getData(RoomNumMsg.class);
        log.info("同步调用 : {}", roomNumMsg2.roomCount);

        // ======== 单个游戏逻辑服之间的交互 --- 异步回调 ========
        // --- 此回调写法，具备全链路调用日志跟踪 ---
        // 路由、回调
        flowContext.invokeModuleMessageAsync(cmdInfo, responseMessage -> {
            RoomNumMsg roomNumMsg = responseMessage.getData(RoomNumMsg.class);
            log.info("异步回调 : {}", roomNumMsg.roomCount);
        });

        // 路由、请求参数、回调
        flowContext.invokeModuleMessageAsync(cmdInfo, yourData, responseMessage -> {
            RoomNumMsg roomNumMsg = responseMessage.getData(RoomNumMsg.class);
            log.info("异步回调 : {}", roomNumMsg.roomCount);
        });
    }

    // ======== 单个游戏逻辑服之间的交互 - 跨服调用 ========
    // https://www.yuque.com/iohao/game/anguu6
    // 特点：异步，不需要接收响应
    void invokeModuleVoidMessage() {
        // 适合不需要接收响应的业务，默认异步
        
        // 路由
        flowContext.invokeModuleVoidMessage(cmdInfo);
        // 路由、请求参数
        flowContext.invokeModuleVoidMessage(cmdInfo, yourData);
    }

    // ======== 请求同类型多个逻辑服通信结果 - 跨同类型多个游戏逻辑服调用 ========
    // https://www.yuque.com/iohao/game/rf9rb9
    // 特点：可同时接收多个游戏逻辑服的响应
    void invokeModuleCollectMessage() {
        // ======== 请求同类型多个逻辑服通信结果 --- 同步 ========
        // 路由
        ResponseCollectMessage response = flowContext.invokeModuleCollectMessage(cmdInfo);

        // 打印其他游戏逻辑服所响应的数据
        for (ResponseCollectItemMessage message : response.getMessageList()) {
            RoomNumMsg roomNumMsg = message.getData(RoomNumMsg.class);
            log.info("同步调用 : {}", roomNumMsg.roomCount);
        }

        // 路由、请求参数
        ResponseCollectMessage response2 = flowContext.invokeModuleCollectMessage(cmdInfo, yourData);
        log.info("同步调用 : {}", response2.getMessageList());

        // ======== 请求同类型多个逻辑服通信结果 --- 异步回调 ========
        // --- 此回调写法，具备全链路调用日志跟踪 ---

        // 路由、回调
        flowContext.invokeModuleCollectMessageAsync(cmdInfo, responseCollectMessage -> {
            List<ResponseCollectItemMessage> messageList = responseCollectMessage.getMessageList();

            for (ResponseCollectItemMessage message : messageList) {
                RoomNumMsg roomNumMsg = message.getData(RoomNumMsg.class);
                log.info("异步回调 : {}", roomNumMsg.roomCount);
            }
        });

        // 路由、请求参数、回调
        flowContext.invokeModuleCollectMessageAsync(cmdInfo, yourData, responseCollectMessage -> {
            log.info("异步回调 : {}", responseCollectMessage.getMessageList());
        });
    }
}

@ToString
@ProtobufClass
public class RoomNumMsg {
    public int roomCount;
}
```

<br>

## 框架内置功能

**内置多种可选模块，可按需选择，以方便应用开发：**

<details>
<summary>游戏服务器框架内置功能详细--点我展开</summary>

1. [领域事件](https://www.yuque.com/iohao/game/gmfy1k) （轻量级单机最快MQ -- disruptor；可为你的系统实现类似 Guava-EventBus、Spring 事件驱动模型 ApplicationEvent、业务解耦、规避并发、不阻塞主线程... 等，各种浪操作。）
2. [多环境切换](https://www.yuque.com/iohao/game/ekx6ve) （不同运行环境下的配置支持）
3. [light-jprotobuf ](https://www.yuque.com/iohao/game/vpe2t6) （补足 jprotobuf 不能让多个对象在单个 .proto 源文件中生成的需求，并简化jprotobuf对源文件的注释）
4. [压测&模拟客户端请求](https://www.yuque.com/iohao/game/tc83ud)
5. [room 桌游、房间类](https://www.yuque.com/iohao/game/vtzbih)，该模块是桌游类、房间类游戏的解决方案。比较适合桌游类、房间类的游戏基础搭建，基于该模型可以做一些如，炉石传说、三国杀、斗地主、麻将 ...等类似的桌游。或者说只要是房间类的游戏，该模型都适用。比如，CS、泡泡堂、飞行棋、坦克大战 ...等。
6. [代码生成](https://www.yuque.com/iohao/game/tufktv)（可为不同的客户端生成代码，如 Unity、Godot、CocosCreator、Vue）

**内置的其他功能：**

1. [心跳相关](https://www.yuque.com/iohao/game/uueq3i)
2. [用户上线、离线相关的钩子方法](https://www.yuque.com/iohao/game/hv5qqh)
3. [UserSessions](https://www.yuque.com/iohao/game/wg6lk7) （对所有用户UserSession的管理，统计在线用户等）
4. [UserSession](https://www.yuque.com/iohao/game/wg6lk7) (与 channel 是 1:1 的关系，可取到对应的 userId、channel 等信息。)
5. [登录相关](https://www.yuque.com/iohao/game/tywkqv)（提供重复登录、顶号等相关增强功能）
6. [业务参数自动装箱、拆箱基础类型](https://www.yuque.com/iohao/game/ieimzn) （解决协议碎片）
7. [内置 Kit](https://www.yuque.com/iohao/game/inkbrpnodgs1lvrt) （动态属性、属性监听、超时处理任务、定时器模拟、任务调度、延时任务、轻量可控的延时任务 ...等）

</details>



<br>

## 快速从零编写服务器完整示例

如果觉得 ioGame 适合你，可以看一下 [快速从零编写服务器完整示例](https://www.yuque.com/iohao/game/zm6qg2) 。在这个示例中，你可以用很少的代码实现一个完整的、可运行的、高性能的、稳定的服务器。

<br>

## 参考

什么是 [Action](https://www.yuque.com/iohao/game/sqcevl) 

[快速从零编写服务器完整示例](https://www.yuque.com/iohao/game/zm6qg2)

[广播（推送）相关示例与文档](https://www.yuque.com/iohao/game/qv4qfo)

[逻辑服与逻辑服之间的交互示例](https://www.yuque.com/iohao/game/anguu6)

[构架简图中：对外服、游戏网关、游戏逻辑服各自的职责](https://www.yuque.com/iohao/game/dqf0he)



## 安装与使用 ioGame

参考 https://www.yuque.com/iohao/game/wsgmba



## 感谢 JetBrains 对开源项目支持

<a href="https://jb.gg/OpenSourceSupport">
  <img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jetbrains.png" alt="JetBrains logo." height="100" width="314">
</a>
