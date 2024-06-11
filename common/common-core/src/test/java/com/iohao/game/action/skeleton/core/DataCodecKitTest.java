package com.iohao.game.action.skeleton.core;

import com.iohao.game.action.skeleton.protocol.Student;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author 渔民小镇
 * @date 2024-06-11
 */
public class DataCodecKitTest {

    @Test
    public void decode() {
        Student student = new Student();
        student.name = "a";

        byte[] encode = DataCodecKit.encode(student);
        Student decode = DataCodecKit.decode(encode, Student.class);
        Assert.assertEquals(student.name, decode.name);
    }
}