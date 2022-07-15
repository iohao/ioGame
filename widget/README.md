### 小部件

每个小部件解决业务中的一类问题；

```text
├── light-all
├── light-domain-event 领域事件
├── light-jprotobuf 生成 .proto 源文件
├── light-profile 多环境切换
├── light-redis-lock 分步式锁 (基于Redisson的简单实现)
├── light-redis-lock-spring-boot-starter 分步式锁 (基于Redisson的简单实现)
└── light-timer-task 任务延时器
```


**内置多种可选模块，可按需选择，以方便应用开发：**

- [领域事件](https://www.yuque.com/iohao/game/gmfy1k) （[disruptor](https://www.yuque.com/iohao/game/gmfy1k) 实现类似Spring事件驱动模型 ApplicationEvent）
- [任务延时器](https://www.yuque.com/iohao/game/niflk0) （将来某个时间可对任务进行执行、暂停、取消等操作，并不是类似 Quartz 的任务调度）
- [多环境切换](https://www.yuque.com/iohao/game/ekx6ve) （不同运行环境下的配置支持）
- [light-jprotobuf ](https://www.yuque.com/iohao/game/vpe2t6) （补足 jprotobuf 不能让多个对象在单个 .proto 源文件中生成的需求，并简化jprotobuf对源文件的注释）


























### 小部件的开发过程

尽量以抽象的方式，如接口对外提供服务。

但前期的抽象并不是必须的，这是一个矛盾的过程。

矛盾的过程分为这几个阶段：

1 先以完成功能为前提（即能用）

2 改善来源于自己编码思想的提升与小部件使用方的反馈

3 重构不理想的小部件

使小部件最后的抽象达到现阶段满意的情况，并循环往复矛盾过程；



