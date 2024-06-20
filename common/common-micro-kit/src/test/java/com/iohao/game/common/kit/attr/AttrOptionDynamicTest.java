package com.iohao.game.common.kit.attr;

import lombok.Getter;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author 渔民小镇
 * @date 2024-06-05
 */
public class AttrOptionDynamicTest {

    final MyAttrOptions myAttrOptions = new MyAttrOptions();

    AttrOption<AttrCat> catAttrOption = AttrOption.valueOf("AttrCat");

    @Test
    public void ifNull() {
        Assert.assertNull(myAttrOptions.option(catAttrOption));

        // 如果 catAttrOption 属性为 null，创建 AttrCat 对象，并赋值到属性中
        myAttrOptions.ifNull(catAttrOption, AttrCat::new);
        Assert.assertNotNull(myAttrOptions.option(catAttrOption));

        myAttrOptions.option(catAttrOption, null);
        Assert.assertNull(myAttrOptions.option(catAttrOption));

        AttrCat attrCat = new AttrCat();
        attrCat.name = "a";
        myAttrOptions.option(catAttrOption, attrCat);
        myAttrOptions.ifNull(catAttrOption, AttrCat::new);
        Assert.assertEquals(myAttrOptions.option(catAttrOption).name, attrCat.name);
    }

    private static class AttrCat {
        String name;
    }

    @Getter
    private static class MyAttrOptions implements AttrOptionDynamic {
        final AttrOptions options = new AttrOptions();
    }
}