package com.iohao.game.widget.light.protobuf.vo;

import com.baidu.bjf.remoting.protobuf.EnumReadable;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.game.widget.light.protobuf.ProtoFileMerge;
import lombok.AccessLevel;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
@ToString
@ProtoFileMerge(fileName = "testEnum.proto", filePackage = "com.iohao.game.widget.light.protobuf.vo")
public enum TestCustomEnumValue implements EnumReadable {
    /**
     * Start zero
     */
    Test_zero(0),               //EnumReadable must start with 0
    /**
     * AAAA
     */
    A(100),
    /**
     * BBBB
     */
    B(500),
    ;

    private int value;

    TestCustomEnumValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public int value() {
        return this.getValue();
    }
}
