## 业务框架

如果说  [sofa-bolt](https://www.sofastack.tech/projects/sofa-bolt/overview/) 是为了让 Java 程序员能将更多的精力放在基于网络通信的业务逻辑实现上。而业务框架正是解决业务逻辑如何方便实现这一问题上。业务框架是游戏框架的一部份，职责是简化程序员的业务逻辑实现，业务框架使程序员能够快速的开始编写游戏业务。



业务框架对于每个 action （即业务的处理类） 都是通过 [asm](https://www.oschina.net/p/reflectasm) 与 Singleton、Flyweight 、Command 等设计模式结合，对 action 的获取上通过 array 来得到，是一种近原生的方式。



业务框架平均每秒可以执行 1152 万次业务逻辑。

