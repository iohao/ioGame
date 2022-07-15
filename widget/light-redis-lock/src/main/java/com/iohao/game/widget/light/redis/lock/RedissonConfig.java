package com.iohao.game.widget.light.redis.lock;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.io.IOException;

/**
 * redisson的基本配置
 *
 * @author shen
 * @date 2022-03-28
 * @Slogan 慢慢变好，是给自己最好的礼物
 */
public class RedissonConfig {

    public static RedissonClient me() {
        return Holder.me;
    }

    public static class Holder {
        static RedissonClient me = getRedissonClientFromConfig();
    }

    public static RedissonClient getRedissonClientFromConfig() {
        Config config = null;
        try {
            config = Config.fromYAML(RedissonConfig.class.getClassLoader().getResource("redisson-config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

//        if (Objects.isNull(config)) {
//            config.useSingleServer()
//                    .setAddress("redis://localhost:6379");
//        }

        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
