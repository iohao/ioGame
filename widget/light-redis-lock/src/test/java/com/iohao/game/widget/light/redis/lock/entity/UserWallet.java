package com.iohao.game.widget.light.redis.lock.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户钱包
 *
 * @author shen
 * @date 2022-03-28
 * @Slogan 慢慢变好，是给自己最好的礼物
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserWallet implements Serializable {
    String userId;
    String name;
    BigDecimal balance;
}
