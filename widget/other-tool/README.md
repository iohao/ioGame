### 第三方工具

此模块是 copy 自 https://gitee.com/dromara/hutool

由于只使用了部分功能，就不引入 hutool 的包了，目的是缩小包体。

LICENSE
https://gitee.com/dromara/hutool/blob/v5-master/LICENSE



这个模块中文件内的 author 仅表示添加人。

这个模块中只添加了 hutool 中小部分的 File 和 StrUtil.format



```java
open module com.iohao.game.common.kit.other.tool {
    requires transitive lombok;

    exports com.iohao.game.common.kit.hutool;
}
```