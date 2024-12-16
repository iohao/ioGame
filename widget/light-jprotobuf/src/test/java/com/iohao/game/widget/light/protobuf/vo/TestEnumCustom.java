package com.iohao.game.widget.light.protobuf.vo;

import com.baidu.bjf.remoting.protobuf.EnumReadable;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.game.widget.light.protobuf.ProtoFileMerge;
import lombok.AccessLevel;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * 测试生成一个TestEnum
 */
@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
@ToString
@ProtoFileMerge(fileName = "testEnum.proto", filePackage = "com.iohao.game.widget.light.protobuf.vo")
public enum TestEnumCustom implements EnumReadable {

    AS(0),
    /**
     * AC
     */
    AC(1001),
    /**
     * BC
     */
    BC(1002),
    ;

    private int value;

    TestEnumCustom(int value){
        this.value = value;
    }


    @Override
    public int value() {
        return value;
    }
}
