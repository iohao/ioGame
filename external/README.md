
![image](https://github.com/iohao/ioGame/assets/26356013/72ba5121-204b-43a7-85b3-3db051725f02)

【新版游戏对外服】用于取代【旧的游戏对外服】，如果条件允许，请尽可能做迁移，整体工作量很少。旧的游戏对外服将不在做功能上的新增，如果存在 bug 将会继续修复，维护期会持续到下个大版本前。



**连接方式的支持、切换**

ioGame 已提供了 TCP、WebSocket、UDP 连接方式的支持，并提供了灵活的方式来实现连接方式的切换。可以将 TCP、WebSocket、UDP 连接方式与业务代码进行无缝衔接。开发者可以用一套业务代码，无需任何改动，同时支持多种通信协议。



如果想要切换到不同的连接方式，只需要更改相应的枚举即可，非常简单。在不使用 ioGame 时，将连接方式从 TCP 改为 WebSocket 或 UDP 等，需要进行大量的调整和改动。然而，在 ioGame 中，实现这些转换是非常简单的。此外，不仅可以轻松切换各种连接方式，而且可以同时支持多种连接方式，并使它们在同一应用程序中共存。



连接方式是可扩展的，而且扩展也简单，这意味着之后如果支持了 KCP，那么将已有项目的连接方式，如 TCP、WebSocket、UDP 切换成 KCP 也是简单的。



需要再次强调的是，连接方式的切换对业务代码没有任何影响，无需做出任何改动即可实现连接方式的更改。



**游戏对外服的核心接口**

- ExternalServer：游戏对外服，由 ExternalCore 和 ExternalBrokerClientStartup 组成的一个整体。
- ExternalCore： 帮助开发者屏蔽各通信框架的细节，如 Netty、mina、smart-socket 等通信框，ioGame 默认提供了基于 Netty 的实现。
- MicroBootstrap：真实玩家连接的服务器，服务器的创建由 MicroBootstrap 完成，MicroBootstrap 帮助开发者屏蔽连接方式的细节，如 TCP、WebSocket、UDP、KCP 等。目前已经支持 TCP、WebSocket、UDP 的连接方式，而 KCP 的连接方式也在计划内。
- MicroBootstrapFlow：MicroBootstrapFlow	与真实玩家连接【真实】服务器的启动流程，专为 MicroBootstrap 服务。开发者可通过此接口对服务器做编排，编排分为：构建时、新建连接时两种。



MicroBootstrapFlow 接口的目的是尽可能地细化服务器创建和连接时的每个环节，以方便开发者对游戏对外服进行定制化扩展。通常情况下，开发者只需要关注重写 MicroBootstrapFlow.pipelineCustom 方法，就可以实现很强的扩展了。



参考文档 [新游戏对外服](https://www.yuque.com/iohao/game/ea6geg)

