package com.iohao.game.widget.light.protobuf.data;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.game.widget.light.protobuf.ProtoFileMerge;
import lombok.AccessLevel;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * 测试生成一个TestEnum
 */
@ToString
@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
@ProtoFileMerge(fileName = TempProtoFile.fileName, filePackage = TempProtoFile.filePackage)
public enum TestEnum {

    /**
     * AAAA
     */
    A,
    /**
     * BBBB
     */
    B,
    ;
}
