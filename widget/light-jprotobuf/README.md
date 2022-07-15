jprotobuf to proto one file

将 jprotobuf 类归类为一个 proto 文件



```text
#!/bin/sh

protoc --java_out=. one.proto
protoc --java_out=. common.proto

```


see:

https://www.yuque.com/iohao/game/vpe2t6