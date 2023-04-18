package com.iohao.game.widget.light.protobuf.vo;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.game.widget.light.protobuf.ProtoFileMerge;
import lombok.AccessLevel;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * 测试枚举
 */
@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
@ToString
@ProtoFileMerge(fileName = "testEnum.proto", filePackage = "com.iohao.game.widget.light.protobuf.vo")
public class TestEnumVo {

    String roomKey;
    /**
     * 测试枚举生成
     */
    TestEnum testEnum;
}