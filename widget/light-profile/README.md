## light-profile 多环境切换

无需修改代码实现（开发、测试、部署）配置间的切换

## profile 的概念
支持概要文件的概念，它允许您在不同的环境中轻松地组织配置(由概要文件定义)。看看下面的配置，来自我们的一个真实项目:

```text
resources
└── conf
    ├── common
    │   ├── db.props
    │   └── other.props
    ├── local
    │   └── db.props
    └── production
        ├── db.props
        └── other.props
```

假设在local服务器上，你用JVM选项-Diohao.profile=local 启动应用程序，将按照以下顺序加载配置:
1. 读取/resources/conf/common目录下的所有.props文件
2. 读取/resources/conf/local目录下所有的.props文件

这样，使用local配置文件中定义的配置项来覆盖公共配置文件中定义的相同配置项。未被覆盖的公共项仍然有效。

## see

https://www.yuque.com/iohao/game/ekx6ve
