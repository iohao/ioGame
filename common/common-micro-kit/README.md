```java
open module com.iohao.game.common.micro.kit {
    requires transitive lombok;
    requires transitive org.slf4j;
    requires transitive jctools.core;

    requires transitive com.iohao.game.common.kit.other.tool;

    exports com.iohao.game.common.consts;
    exports com.iohao.game.common.internal;

    exports com.iohao.game.common.kit;
    exports com.iohao.game.common.kit.attr;
    exports com.iohao.game.common.kit.micro.room;
    exports com.iohao.game.common.kit.weight;
}
```