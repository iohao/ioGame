package com.iohao.game.action.skeleton.toy;

import com.iohao.game.action.skeleton.IoGameVersion;
import org.junit.Test;

/**
 * @author 渔民小镇
 * @date 2023-01-30
 */
public class ToyTableTest {
    @Test
    public void test() {

        ToyTable table = new ToyTable();
        ToyTableRegion ioGameRegion = table.getRegion("ioGame");
        ioGameRegion.putLine("pid", "73033");
        ioGameRegion.putLine("version", IoGameVersion.VERSION);
        ioGameRegion.putLine("document", "http://game.iohao.com");

        ToyTableRegion memoryRegion = table.getRegion("Memory");
        memoryRegion.putLine("used", "xx.xxMB");
        memoryRegion.putLine("freeMemory", "xxx.xxMB");
        memoryRegion.putLine("totalMemory", "xxx.xMB");

        table.render();
    }

}