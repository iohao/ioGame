package com.iohao.game.bolt.broker.server;

import com.iohao.game.bolt.broker.cluster.BrokerRunModeEnum;
import com.iohao.game.bolt.broker.core.common.BrokerGlobalConfig;

import java.util.concurrent.TimeUnit;

/**
 * 单机网关服
 * <pre>
 *     {@link BrokerRunModeEnum#STANDALONE}
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-16
 */
public class BrokerServerStandaloneTest {
    public static void main(String[] args) throws Exception {
        // Broker Server （游戏网关服） 构建器
        BrokerServerBuilder brokerServerBuilder = BrokerServer.newBuilder()
                // broker （游戏网关）默认端口 10200
                .port(BrokerGlobalConfig.brokerPort)
                ;

        // 构建游戏网关
        BrokerServer brokerServer = brokerServerBuilder.build();

        // 启动 游戏网关
        brokerServer.startup();

        TimeUnit.SECONDS.sleep(1);
    }
}