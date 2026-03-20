<h2 align="center" style="text-align:center;">
  ioGame
</h2>
<p align="center">
  <strong>Lock-free, async, event-driven architecture — supports clustering & distribution out of the box, no middleware required</strong>
  <br>
  <strong>Build decentralized, auto-scaling, multi-process distributed game servers with ease</strong>
  <br>
  <strong>Tiny footprint, blazing-fast startup, low memory usage, zero config files, elegant route-level access control</strong>
  <br>
  <strong>Simultaneous support for WebSocket, UDP, TCP and more — with built-in full-link distributed tracing</strong>
  <br>
  <strong>One codebase, multiple protocols — seamlessly switch between Protobuf, JSON, and beyond</strong>
  <br>
  <strong>Near-native performance — 11.52 million business operations per second in a single thread</strong>
  <br>
  <strong>Code-as-documentation, JSR380 validation, assertion + exception patterns — minimal maintenance overhead</strong>
  <br>
  <strong>Smart same-process affinity with IDE-friendly code navigation & jump-to-source</strong>
  <br>
  <strong>Deploy your way — components run independently or fused together</strong>
  <br>
  <strong>Write once, generate client SDKs — interactive code generation for any frontend</strong>
  <br>
  <strong>Cross-process, cross-machine communication between logic servers</strong>
  <br>
  <strong>Dynamic player-to-server binding</strong>
  <br>
  <strong>Plays nicely with any framework</strong>
  <br>
  <strong>Feels natural for web MVC developers</strong>
  <br>
  <strong>No hard dependency on Spring</strong>
  <br>
  <strong>Zero learning curve</strong>
  <br>
  <strong>Pure JavaSE</strong>
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

## Vision
Make game server development effortless. We're here to change the industry — lowering the barrier to entry and making game development tools truly accessible to everyone.


## Open, Free & Developer-Friendly License
- There is no commercial edition — never has been, never will be. Every feature is open source.
- We're committed to at least **ten years** of active maintenance, starting from 2022-03-01.
- ioGame is a lightweight networking framework built for **online games, IoT, internal systems**, and any scenario that needs persistent connections. The source code is fully open, documentation is free, and usage costs nothing (subject to license terms).

<hr/>

**Why AGPL 3.0?**

ioGame is released under the [AGPL 3.0](https://www.gnu.org/licenses/agpl-3.0.txt) license. Projects built with ioGame under this license are free of charge.

We chose AGPL 3.0 because it's **fairer to developers**. It ensures that project ownership is shared — meaning that even if a developer leaves a company, they retain legitimate rights to the project they helped build.

Contrast this with permissive licenses like Apache 2.0 or MIT, where developers who leave a company lose all control over the project and get nothing in return. We've all seen it: developers pour their hearts into a project through countless late nights and weekends, only to be let go right before — or just after — launch, watching their hard work become someone else's asset.

Under AGPL 3.0, shared ownership means developers are genuinely motivated to invest in and polish their work.



## Startup Showcase

ioGame is impressively lean:
- **Memory**: Minimal footprint.
- **Startup**: Applications typically boot in under **1 second**.
- **Package size**: ~**15 MB** as a jar.

![](https://iohao.github.io/game/assets/images/start-cc4b7973e832e31c6c5bf50af83aabeb.png)



## What is ioGame?

Looking for a game server framework that's **high-performance, stable, easy to use, with built-in load balancing, clean architecture (no class explosion), cross-process communication, decentralized clustering, auto-scaling, and stateful multi-process distribution**? Meet ioGame — a Java networking framework designed exactly for this.



ioGame is a lightweight networking framework built for **online games, IoT, internal systems**, and any scenario that needs persistent connections.



**Key Features at a Glance:**

> 1. Truly lightweight — lock-free, async, event-driven from the ground up.
> 2. Small package, low memory, fast startup.
> 3. Pure JavaSE — integrates effortlessly with Spring, Vert.x, Quarkus, Solon, and others.
> 4. **Zero learning curve.** If you know basic Java or web MVC, you already know how to use ioGame. No game dev experience required.
> 5. Architecturally eliminates the **N×N scaling problem** that plagues traditional frameworks.
> 6. **No third-party dependencies** for clustering & distribution — just a JVM is all you need.
> 7. Three-part architecture: External Server + Broker (Gateway) + Game Logic Server — each can run **independently or fused together**, adapting to **any game type**.
> 8. Fully **dynamic scaling** — add or remove External Servers, Logic Servers, and Brokers on the fly.
> 9. **Multi-server, single-process** mode for development — debug distributed systems as easily as a monolith.
> 10. Logic Servers can run standalone, **enabling true modularization**.
> 11. Built-in **full-link distributed tracing**.
> 12. Rich communication primitives — logic servers can talk across machines seamlessly.
> 13. MVC-style coding with non-intrusive Java Beans — effectively **prevents class explosion**.
> 14. Built-in **per-player thread safety** — concurrency handled for you.
> 15. **One codebase** supports TCP, WebSocket, UDP simultaneously — no code changes needed. Extensible to KCP, QUIC, and beyond.
> 16. **One codebase** for switching data protocols — Protobuf, JSON, and more. Extensible.
> 17. **Hot-swap protocols** — add or remove protocols without restarting the gateway or External Server. No player disconnections.
> 18. Auto-boxing/unboxing of primitives in actions — solves the [protocol fragment](https://iohao.github.io/game/docs/manual/protocol_fragment) problem.
> 19. Pluggable, extensible **plugin system**.
> 20. Deploy as **single-process** or **multi-process across machines** — switch freely without code changes.
> 21. Logic Servers never expose ports — **immune to port-scanning attacks** by design.
> 22. Built-in **stress testing & simulation module** with real network conditions, continuous interaction, and automation support.
> 23. Sync, async, and async-callback methods for inter-service communication.
> 24. **Distributed event bus** (like MQ / Redis pub-sub — works across machines and processes).
> 25. Elegant **route-level access control**.
> 26. Intelligent **same-process affinity**.
> 27. JSR380 validation + assertions + exceptions = **less boilerplate, fewer bugs**.
> 28. **Write once, generate everywhere** — produce unified interactive SDKs for Godot, UE, Unity, Cocos Creator, Laya, React, Vue, Angular, and more. Massive productivity boost.



Packaging, memory, and startup are all best-in-class: jar size ~**15 MB**, startup typically under **1 second**, low memory footprint.



**Ecosystem integration** is straightforward — [Spring integration](https://iohao.github.io/game/docs/manual/integration_spring) takes just 4 lines of code. Beyond Spring, ioGame plays well with Vert.x, Quarkus, Solon, and any other framework, letting you tap into their ecosystems.



**Zero learning curve.** If you know basic Java or web MVC patterns, you're ready. No game development background needed.



**Clean coding style.** ioGame provides MVC-like conventions with non-intrusive Java Beans, effectively **preventing class explosion**. Sync, async, and callback methods are available for inter-service calls — resulting in elegant code with full-link tracing baked in.



**Write once, connect everywhere.** ioGame generates client interaction code automatically, dramatically cutting client-side workload. Write your Java code once and generate unified SDKs for [Godot](https://godotengine.org/), [UE](https://www.unrealengine.com/), [Unity](https://unity.com/), [Cocos Creator](https://www.cocos.com/), [Laya](https://layaair.layabox.com/#/), [React](https://react.dev/), [Vue](https://vuejs.org/), [Angular](https://angular.dev/), and more. Supports [code generation](https://iohao.github.io/game/docs/examples/code_generate) in C#, TypeScript, GDScript, and C++ — fully extensible.



**No N×N headaches.** Traditional architectures rely on Redis, MQ, ZooKeeper, and other middleware to scale — hardly "lightweight." ioGame solves the [N×N problem](https://iohao.github.io/game/docs/overall/legacy_system) architecturally, without external dependencies.



**Truly lightweight.** No third-party middleware or database is needed for clustering and distribution — just a JVM. This simplifies usage and slashes deployment and maintenance costs. A single dependency gives you the entire framework — no Nginx, Redis, MQ, MySQL, ZooKeeper, or Protobuf compiler to install.



**Flexible architecture.** ioGame's [three-part design](https://iohao.github.io/game/docs/overall/deploy_flexible) — External Server, Broker (Gateway), Game Logic Server — can run independently or merged together, adapting to **any game type** simply by adjusting deployment. These changes are trivial and never break existing code.



**Dynamic scaling.** External Servers, Logic Servers, and Brokers all support live addition and removal. Scale up or down as player counts change. The architecture also enables **zero-downtime updates**: spin up new servers (A-3, A-4) with your latest features, then gracefully retire the old ones (A-1, A-2) — players never notice.



**Decentralized clustering.** The Broker (Gateway) uses a [masterless, self-organizing cluster design](https://iohao.github.io/game/docs/examples/server/example_broker_cluster) — all nodes are equal and autonomous with no single point of failure. The cluster **auto-manages and elastically scales**, maintaining load balance and consistency as nodes join or leave.



**Distributed by design.** Logic servers are organized into distinct layers — [External Servers](https://iohao.github.io/game/docs/overall/external_intro), [Game Logic Servers](https://iohao.github.io/game/docs/overall/logic_intro) — each with clear responsibilities and interfaces. This improves readability, maintainability, and enables effortless **horizontal scaling**.



**Developer-friendly distributed development.** Distributed apps usually mean juggling multiple processes, making debugging painful. Most frameworks can't solve this — **ioGame can.** Multi-server single-process mode lets you develop and debug distributed systems as if they were monoliths.



**Modular ecosystem.** [Game Logic Servers can run standalone](https://iohao.github.io/game/docs/manual_high/your_ecology) — just plug into the Broker to provide services. Build reusable, **componentized logic servers** — Guild, Friends, Login, Lottery, Announcements, Leaderboards, and more. Benefits include:

1. No redundant development.
2. Low coupling between modules.
3. True single-responsibility design — each feature becomes its own **logic server**.
4. Scale any module independently without code changes.
5. Build up your own **ecosystem arsenal** of reusable components for competitive advantage.
6. **Reduced code leak risk.** Monolithic projects put everything in one directory — one leak exposes everything. With modular servers, each developer only accesses their own module.
7. Admins deploy the gateway and External Server on the internal network; developers code and test their own modules locally. Additional perks:
    - Client connections survive logic server restarts.
    - Developers don't need to run each other's modules.
    - Auto-generated docs handle inter-module integration.



**Full-link distributed tracing.** Every request gets a [unique trace ID](https://iohao.github.io/game/docs/manual/trace) recorded across logs — filter by ID to instantly find what you need. ioGame's tracing works **across machines and processes**: from request entry to completion, every logic server touched is precisely recorded.



**Rich communication models.** While most frameworks only offer push/broadcast, ioGame provides a complete set of [communication patterns](https://iohao.github.io/game/docs/manual/communication_model) — all supporting cross-process, cross-machine communication with full-link tracing:

- **Client-facing models:**
    - [request/response](https://iohao.github.io/game/docs/communication/request_response)
    - request/void (fire-and-forget)
    - request/broadcast
    - [broadcast](http://localhost:3000/docs/communication/broadcast) (server push)
- **Internal (server-to-server) models:**
    - [request/response](https://iohao.github.io/game/docs/communication/request_response)
    - request/void
    - [request/multiple_response](https://iohao.github.io/game/docs/communication/request_multiple_response) — fan-out to multiple logic servers of the same type
    - [EventBus](https://iohao.github.io/game/docs/communication/event_bus) — distributed event bus
    - [ExternalRegion](https://iohao.github.io/game/docs/communication/external_biz_region) — access External Servers



Since ioGame 21, **virtual threads** are used for blocking inter-service communication, preventing business thread starvation and significantly boosting throughput.



**Thread safety made easy.** The framework guarantees [per-player thread safety](https://iohao.github.io/game/docs/overall/thread_executor) — even across re-logins, the same thread handles that player's business. For multi-player scenarios (e.g., same room), [Domain Events](https://iohao.github.io/game/docs/extension_module/domain_event) provide a clean solution. ioGame's unique thread executor design makes writing **lock-free concurrent code** straightforward.



**Lock-free concurrency.** ioGame's elegant thread executor design lets developers write high-concurrency code without locks — naturally and safely.



**Protocol-agnostic connections.** Use **one codebase** to support TCP, WebSocket, and UDP simultaneously — no modifications needed. Connection types are extensible: when KCP or QUIC support lands, just switch — your business code stays untouched.



**Flexible data protocols.** [Switch between Protobuf, JSON, and more](https://iohao.github.io/game/docs/manual/data_protocol) with a single line of code. No business method changes required.



**Hot-swap protocols.** Add or remove protocols **without restarting** the External Server or Broker — no player disconnections, no fleet-wide restarts.



**Protocol fragment solution.** Actions auto-box and unbox primitive types, solving the [protocol fragment](https://iohao.github.io/game/docs/manual/protocol_fragment) problem while making business code cleaner and boosting developer productivity.



**[Same-process affinity](https://iohao.github.io/game/docs/manual_high/same_process).** Within a single process, Netty instances communicate via memory — no network overhead, blazing-fast data transfer. The framework intelligently routes requests to same-process logic servers first, falling back to other processes/machines only when needed.



**Great developer experience.** ioGame ships with [JSR380 validation](https://iohao.github.io/game/docs/core/jsr380), [assertions + exception handling](https://iohao.github.io/game/docs/manual/assert_game_code), [code navigation](https://iohao.github.io/game/docs/core_plugin/action_debug), auto-boxing for primitives, and more — all designed to keep your business code clean and concise.



**Extensible [plugin system](https://iohao.github.io/game/docs/manual/plugin_intro).** Built-in plugins include DebugInOut, action call statistics, thread monitoring, time-bucketed call analytics, and more. Combine monitoring plugins to catch **performance issues during development** — find and fix problems before they reach production.



**Flexible deployment.** Run as a **single process** during development, deploy as **multi-process across machines** in production — switch freely without changing a line of code.



**Secure by design.** Logic Servers [never expose ports](https://iohao.github.io/game/docs/overall/legacy_system#Usage-Management) — **port-scanning attacks are impossible.** No need to manage per-service ports or cloud firewall rules. This entire category of ops headaches simply **disappears**.



**Realistic testing.** The [stress test & simulation module](https://iohao.github.io/game/docs/extension_module/simulation_client) goes beyond unit tests. It simulates real network conditions with continuous, interactive server communication and full automation support. Great for complex scenario testing and load validation.



**Cost-effective at every stage.** ioGame reduces costs across learning, development, testing, deployment, scaling, and beyond. With equal resources, ioGame gives your team a competitive edge — and protects you from building value that only benefits others. See the full [cost analysis](https://iohao.github.io/game/services/cost_analysis).



**Well-organized projects.** ioGame's thoughtful **route design** and elegant [access control](https://iohao.github.io/game/docs/external/access_authentication) naturally produce clean, maintainable codebases. Combined with [code organization conventions](https://iohao.github.io/game/docs/manual_high/code_organization), handoffs and long-term maintenance become much smoother. The deeper you go, the more you'll appreciate this.



**Modern Java, modern performance.** ioGame requires **JDK 21+**, giving you access to **Generational ZGC** with **sub-millisecond** pause times and modern syntax. GC pauses become invisible — no stuttering, no crashes — like having a JVM tuning expert on your team.



**In short**, ioGame is purpose-built for online game development. It lets you create high-performance, low-latency, easily scalable game servers while saving time and resources. The framework handles the complex, repetitive infrastructure so you can focus on what matters — your game. It provides **clear structural organization** for modules and development workflows, reducing long-term maintenance costs.



We believe you now have a solid overview of ioGame. There are many more features to discover as you dive deeper. Thank you for reading — we look forward to seeing what you build!

---

## Write Once, Generate Everywhere — A Massive Productivity Boost

ioGame is built around the principle of **code-as-documentation** and **methods-as-interfaces**.



**Write once** means writing your Java business code a single time. **Generate everywhere** means automatically producing client interaction code for any frontend project.



Write your Java code once and generate unified interaction interfaces for [Godot](https://godotengine.org/), [UE](https://www.unrealengine.com/), [Unity](https://unity.com/), [CocosCreator](https://www.cocos.com/), [Laya](https://layaair.layabox.com/#/), [React](https://react.dev/), [Vue](https://vuejs.org/), [Angular](https://angular.dev/), and more.



ioGame generates **action, broadcast, and error code** interfaces for any frontend project. Write your business logic once — it works with all these game engines and modern frontend frameworks simultaneously.



**Why generated client code matters:**

1. **Eliminates boilerplate.** Client developers no longer write mountains of template code.
2. **Crystal-clear semantics.** Generated interfaces explicitly define parameter types and whether to expect a response — no guesswork.
3. **Type-safe parameters.** Precise interface definitions mean type-safe method signatures, fewer security risks, and **fewer integration bugs**.
4. **Code is the documentation.** Generated code includes docs and usage examples — **zero learning cost**, even for newcomers.
5. **Focus on business logic.** Client developers can ignore server communication plumbing and **spend their time on what matters**.
6. **Smooth integration.** Using generated code feels **as natural as calling a local method** — minimal cognitive overhead for both teams.
7. **Interface-oriented, not protocol-oriented.** A modern approach that replaces the traditional protocol-centric integration workflow.
8. **Always in sync.** When your Java code changes, documentation and interfaces update automatically — **no separate docs to maintain**.



## Architecture Overview

> Lock-free, async, event-driven architecture. Truly lightweight — build a clustered, distributed game server with zero middleware.
>
> Decentralized cluster nodes, automated clustering, built-in load balancing, distributed deployment, dynamic machine scaling.

| Component                                                    | Scaling      | Responsibility                   |
| ------------------------------------------------------------ | ------------ | -------------------------------- |
| **ExternalServer** — [External Server](https://iohao.github.io/game/docs/overall/external_intro) | Distributed  | Player connections & interaction |
| **GameLogicServer** — [Game Logic Server](https://iohao.github.io/game/docs/overall/logic_intro) | Distributed  | Business logic processing        |
| **BrokerCluster** — [Broker (Gateway)](https://iohao.github.io/game/docs/overall/broker_intro) | Clustered    | Request scheduling & forwarding  |

![ioGame](https://iohao.github.io/game/assets/images/ioGame-2cd7572c6c81afd341b7d2e9d703bf65.svg)

For details, see the [Architecture Guide](https://iohao.github.io/game/docs/overall/architecture_intro).

------

> The Broker (Gateway) runs as a **cluster** — typically stateless, focused on scheduling and forwarding.
>
> External Servers and Game Logic Servers use a **distributed** model, supporting multiple instances of the same type. When player counts grow, simply spin up more Logic Servers.
>
> **Example:** Two type-A Logic Servers (A-1, A-2) share requests via the gateway's random load-balancing strategy.
>
> Both External Servers and Logic Servers support dynamic addition and removal. **Zero-downtime updates** are built in: launch A-3 and A-4 with new features, then gracefully retire A-1 and A-2 — players won't notice a thing.
>
> The framework also supports [dynamic player-to-server binding](https://iohao.github.io/game/docs/manual/binding_logic_server) — once bound, all subsequent requests from that player route to the same Logic Server.
>
> **Beyond gaming:** ioGame works great for IoT too. Replace "players" with "devices" in the diagram — the architecture handles hundreds of millions of connections. IoT companies have been using ioGame successfully since 2022.



**External Server**

The External Server manages persistent player connections. Say one server supports up to 5,000 connections — when you hit 7,000, just add another External Server to distribute the load.

Scaling External Servers provides natural load balancing and traffic control under high concurrency. Thanks to trivial scaling, supporting **millions** of concurrent players is entirely achievable.

Even with multiple External Servers, developers don't need to track which server a player is connected to — broadcasts and pushes reach every player automatically. From the player's perspective, there's only one server. From the developer's perspective, the same is true.

Wondering about max connections per External Server? That's a Netty question — because under the hood, that's exactly what it is. If you know Netty, extending the External Server will feel second nature.



## Quick Start

Here's a simplified view of how a game engine interacts with the game server:

![Business Interaction Diagram](https://iohao.github.io/game/assets/images/introduction_quick-6b29dfc678257db43afb10939356edb6.jpeg)

> The game frontend and server communicate **bidirectionally**, exchanging business data encoded/decoded via `.proto` files. Protocol Buffers (PB) is currently the best choice for game data serialization, though JSON, XML, or custom formats are also supported since everything is transmitted as binary.
>
> **Game frontends** can be [Godot](https://godotengine.org/), [Unity](https://unity.cn/), [UE](https://www.unrealengine.com/zh-CN/), [Cocos Creator](https://www.cocos.com/), [Laya](https://layaair.layabox.com/#/), [FXGL](https://github.com/AlmasB/FXGL), or any other engine. The engine handles rendering; data exchange happens over TCP, UDP, etc.



**Data Protocol**

Define two simple data protocols for client-server communication. These are jprotobuf objects — a simplified wrapper around Google Protobuf with equivalent performance.

Think of them as DTOs — carriers for business data:

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

Here's your server-side business logic. This code simultaneously supports TCP, WebSocket, and UDP — no changes needed:

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



Each method (like `here`) is an [Action](https://iohao.github.io/game/docs/manual/action) — a unit of business logic.

Method parameters receive data from the frontend. The return value is automatically sent back to the client. You don't need to understand framework internals.

Notice how this looks just like ordinary Java? That's intentional — and it **prevents class explosion**. If your job is writing game logic, your ioGame learning journey can stop right here.



**Game programming really is this simple.**



**Q: Am I ready to start building a game server?**

> Yes. Yes you are.



**Console Output Example**

When an action is called, the console logs:

```text
┏━━━━━ Debug. [(DemoAction.java:5).here] ━━━━━ [cmd:1-0 65536] ━━━━━ [xxxLogicServer - id:[76526c134cc88232379167be83e4ddfc]
┣ userId: 1
┣ Params: message : LoginVerifyMessage(jwt=hello)
┣ Response: UserMessage(name=Michael Jackson, hello)
┣ Time: 1 ms (total business method execution time)
┗━━━━━ [ioGameVersion] ━━━━━ [Thread:User-8-2] ━━━━━━━ [traceId:956230991452569600] ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```

**What each field means:**

- **Debug** `[(DemoAction.java:5).here]` — The action that ran, with line number. Click `DemoAction.java:5` in your IDE to jump straight to the code.
- **userId** — The requesting player's ID.
- **Params** — Input from the frontend.
- **Response** — The return value, automatically pushed to the client.
- **Time** — Execution duration — use this to spot and optimize slow logic.
- **cmd** — The [route](https://iohao.github.io/game/docs/manual/cmd) (unique address) for this action.
- **ioGameVersion** — Current framework version.
- **Thread** — The thread that executed this action.
- **traceId** — Unique per-request ID for distributed tracing.
- **Logic Server** — Which Logic Server handled the request.

This visibility eliminates the most common time sinks in game development:

- "Did the client actually send the data?" *(Parameter issues)*
- "Did the server actually respond?" *(Response issues)*
- "Why is the client not getting responses?" *(Timing issues)*

Code navigation lets developers jump to any business method instantly — invaluable in team settings for understanding and modifying execution flow.



## Who is ioGame For?

1. Web developers curious about game server development.
2. Developers new to the game industry.
3. Anyone interested in game development — no prior experience needed.
4. Learners who want to see design patterns applied in practice.
5. Developers open to modern approaches.
6. Anyone ready to leave legacy codebases behind.



At least one year of hands-on programming experience is recommended.
