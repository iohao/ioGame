package com.iohao.game.action.skeleton.protocol;

import com.iohao.game.action.skeleton.protocol.wrapper.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author 渔民小镇
 * @date 2024-06-09
 */
public class ResponseMessageTest {

    @Test
    public void getValue() {
        ResponseMessage responseMessage = new ResponseMessage();

        Student student = new Student();
        student.name = "1";

        // ------ object ------
        responseMessage.setData(student);
        Assert.assertEquals(responseMessage.getValue(Student.class).name, "1");

        // ------ object list ------
        responseMessage.setData(ByteValueList.ofList(List.of(student)));

        List<Student> listValue = responseMessage.listValue(Student.class);
        Assert.assertEquals(listValue.size(), 1);
        Assert.assertEquals(listValue.getFirst().name, student.name);

        // ------ int ------
        responseMessage.setData(IntValue.of(1));
        Assert.assertEquals(responseMessage.getInt(), 1);

        // ------ int list ------
        responseMessage.setData(IntValueList.of(List.of(1, 2)));

        List<Integer> listInt = responseMessage.listInt();
        Assert.assertEquals(listInt.size(), 2);
        Assert.assertEquals(listInt.getFirst().intValue(), 1);
        Assert.assertEquals(listInt.get(1).intValue(), 2);

        // ------ long ------
        responseMessage.setData(LongValue.of(1));
        Assert.assertEquals(responseMessage.getLong(), 1);

        // ------ long list ------
        responseMessage.setData(LongValueList.of(List.of(1L, 2L)));

        List<Long> listLong = responseMessage.listLong();
        Assert.assertEquals(listLong.size(), 2);
        Assert.assertEquals(listLong.getFirst().longValue(), 1);
        Assert.assertEquals(listLong.get(1).longValue(), 2);

        // ------ string ------
        responseMessage.setData(StringValue.of("1"));
        Assert.assertEquals(responseMessage.getString(), "1");

        // ------ string list ------
        responseMessage.setData(StringValueList.of(List.of("1L", "2L")));

        List<String> listString = responseMessage.listString();
        Assert.assertEquals(listString.size(), 2);
        Assert.assertEquals(listString.getFirst(), "1L");
        Assert.assertEquals(listString.get(1), "2L");

        // ------ boolean ------
        responseMessage.setData(BoolValue.of(true));
        Assert.assertTrue(responseMessage.getBoolean());

        // ------ boolean list ------
        responseMessage.setData(BoolValueList.of(List.of(true, false)));

        List<Boolean> listBoolean = responseMessage.listBoolean();
        Assert.assertEquals(listBoolean.size(), 2);
        Assert.assertTrue(listBoolean.getFirst());
        Assert.assertFalse(listBoolean.get(1));
    }
}