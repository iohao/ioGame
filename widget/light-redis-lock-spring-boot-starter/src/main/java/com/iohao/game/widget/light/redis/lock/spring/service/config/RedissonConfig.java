package com.iohao.game.widget.light.redis.lock.spring.service.config;

import cn.hutool.core.util.StrUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Objects;

/**
 * redisson的基本配置
 *
 * @author shen
 * @date 2022-03-28
 */
@Configuration
public class RedissonConfig {

    @Resource
    private DistibutedLockProperties distibutedLockProperties;

    @Bean(destroyMethod="shutdown")
    @ConditionalOnMissingBean
    public RedissonClient redissonClient() throws Exception {
        Config config = null;
        try {
            String redissonConfigName = distibutedLockProperties.getRedissonConfigName();
            if (StrUtil.isBlankIfStr(redissonConfigName)) {
                throw new Exception("没有可用的redisson配置源");
            }

            config = Config.fromYAML(RedissonConfig.class.getClassLoader().getResource(redissonConfigName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Objects.isNull(config)) {
            throw new Exception("没有可用的redisson配置源");
        }
        return Redisson.create(config);
    }
}
