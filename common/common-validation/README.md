# 背景：
游戏逻辑服务需要与已有的业务服务(`SpringBoot`)进行通信，因此游戏逻辑服务也使用了`SpringBoot`框架，同时也依赖了`spring-cloud-starter-stream-rabbit`，
问题点在于`spring-cloud-starter-stream-rabbit` 依赖 `javax.validation`,而框架中依赖`jakarta.validation`,实测过程中二者不能兼容，最直接的办法是替换框架中的`jakarta.validation`依赖，
但是该办法比较粗暴，违背原作者的意图，因此单独抽出一个模块`common-validation`；

# 解决方案：
Java、SpringBoot中均有SPI的实现，SPI的思路能比较好的解决该问题，因此其中定义了`Validator`接口，用于抽象`javax`和`jakarta`中的`Validator`；实际项目中按需加载。
同时对框架中的`ValidatorKit`类进行调整

# 使用方法：

## 依赖jakarta.validation的使用
1. 在游戏逻辑服务的`pom.xml`中添加依赖：
``` xml
<!-- https://mvnrepository.com/artifact/org.hibernate.validator/hibernate-validator -->
<dependency>
  <groupId>org.hibernate.validator</groupId>
  <artifactId>hibernate-validator</artifactId>
  <version>7.0.4.Final</version>
</dependency>


<!-- https://mvnrepository.com/artifact/org.glassfish/jakarta.el -->
<!-- EL实现。在Java SE环境中，您必须将实现作为依赖项添加到POM文件中-->
<dependency>
  <groupId>org.glassfish</groupId>
  <artifactId>jakarta.el</artifactId>
  <version>4.0.2</version>
</dependency>

<!-- https://mvnrepository.com/artifact/jakarta.validation/jakarta.validation-api -->
<!-- 验证器Maven依赖项 -->
<dependency>
  <groupId>jakarta.validation</groupId>
  <artifactId>jakarta.validation-api</artifactId>
  <version>3.0.2</version>
</dependency>
```

2. 在 `resources` 目录下新建文件 `META-INF/ioGame/com.iohao.game.common.validation.Validator` 并写入内容：
 ```
 com.iohao.game.common.validation.support.JakartaValidator
 ```

或者使用注解：
``` java
@EnableValidation("com.iohao.game.common.validation.support.JakartaValidator")
@SpringBootApplication
public class JakartaServerApplication {
......
}
```

> 依赖jakarta.validation时，该步骤为非必须操作

3. 对应实体类中的依赖更改为`jakarta.validation` 相关
``` java
import jakarta.validation.constraints.Pattern;
/**
 * 登录请求数据
 */
@Data
@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
public class LoginReq implements Serializable {

    // 用户名
    @Pattern(regexp = "^[a-z|A-Z|0-9]{10,20}$", message = "用户名长度不正确")
    String username;

    // 登录密码
    String password;
}
```

## 依赖javax.validation的使用
1. 在游戏逻辑服务的`pom.xml`中添加依赖：

```xml
<!--javax.validation -->
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>5.3.6.Final</version>
</dependency>
<dependency>
<groupId>javax.validation</groupId>
<artifactId>validation-api</artifactId>
</dependency>
<dependency>
<groupId>javax.el</groupId>
<artifactId>javax.el-api</artifactId>
<version>3.0.0</version>
</dependency>
<dependency>
<groupId>org.glassfish.web</groupId>
<artifactId>javax.el</artifactId>
<version>2.2.4</version>
</dependency>
```

2. 在 `resources` 目录下新建文件 `META-INF/ioGame/com.iohao.game.common.validation.Validator` 并写入内容：
 ```
 com.iohao.game.common.validation.support.JavaxValidator
 ```
或者使用注解：
``` java
@EnableValidation("com.iohao.game.common.validation.support.JavaxValidator")
@SpringBootApplication
public class JavaXServerApplication {
......
}
```
> 依赖javax.validation时，该步骤为必须操作

3. 对应实体类中的依赖更改为`javax.validation` 相关
``` java
import javax.validation.constraints.Pattern;
/**
 * 登录请求数据
 */
@Data
@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
public class LoginReq implements Serializable {

    // 用户名
    @Pattern(regexp = "^[a-z|A-Z|0-9]{10,20}$", message = "用户名长度不正确")
    String username;

    // 登录密码
    String password;
}
```



# pom 设置

如果使用 javax 的验证，需要在 pom 中加入如下代码；

> 使用默认的 jakarta 则不需要在 pom 加入下面这段配置；

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.8.1</version>
    <configuration>
        <compilerVersion>${java.version}</compilerVersion>
        <source>${java.version}</source>
        <target>${java.version}</target>
        <!-- maven 3.6.2及之后加上编译参数，可以让我们在运行期获取方法参数名称。 -->
        <parameters>true</parameters>
        <skip>true</skip>
        <!-- JDK9+ with module-info.java -->
        <annotationProcessorPaths>
            <!-- 实体映射工具 -->
            <path>
                <groupId>org.mapstruct</groupId>
                <artifactId>mapstruct-processor</artifactId>
                <version>${org.mapstruct.version}</version>
            </path>

            <!-- lombok 消除冗长的 Java 代码 -->
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>${lombok.version}</version>
            </path>
            <!-- additional annotation processor required as of Lombok 1.18.16 -->
            <!-- mapStruct 支持 lombok -->
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok-mapstruct-binding</artifactId>
                <version>0.2.0</version>
            </path>
            <path>
                <groupId>com.iohao.game</groupId>
                <artifactId>common-validation</artifactId>
                <version>${ioGame.version}</version>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```

