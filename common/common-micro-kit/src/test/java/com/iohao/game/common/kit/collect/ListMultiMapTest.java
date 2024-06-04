package com.iohao.game.common.kit.collect;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Set;

/**
 * @author 渔民小镇
 * @date 2023-12-07
 */
public class ListMultiMapTest {
    ListMultiMap<Integer, String> map = ListMultiMap.of();

    @Test
    public void test() {
        Assert.assertTrue(map.isEmpty());

        map.put(1, "a");
        map.put(1, "a");
        map.put(1, "b");
        Assert.assertEquals(1, map.size());
        Assert.assertEquals(3, map.sizeValue());

        List<String> list2 = map.get(2);
        Assert.assertNull(list2);

        list2 = map.of(2);
        Assert.assertNotNull(list2);
        Assert.assertEquals(2, map.size());

        list2.add("2 - a");
        list2.add("2 - a");
        Assert.assertEquals(5, map.sizeValue());
        Assert.assertTrue(map.containsValue("a"));
        Assert.assertTrue(map.containsValue("b"));

        var collection = map.clearAll(1);
        Assert.assertTrue(collection.isEmpty());
        Assert.assertEquals(2, map.size());

        Set<Integer> keySet = this.map.keySet();
        Assert.assertNotNull(keySet);
        Assert.assertEquals(2, keySet.size());
        Assert.assertTrue(keySet.contains(1));
        Assert.assertTrue(keySet.contains(2));
        Assert.assertFalse(keySet.contains(3));

        this.map.clear();
        Assert.assertTrue(this.map.isEmpty());
    }
}