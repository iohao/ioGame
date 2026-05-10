package com.iohao.game.widget.light.profile;

import java.util.Map;

/**
 * @Author: caochaojie
 * @Date: 2026-01-2215:29
 */
public class ProfileStaticBinder extends AbstractStaticBinder {

    private final Map<String, Object> configMap;

    public ProfileStaticBinder(Map<String, Object> configMap) {
        this.configMap = configMap;
    }

    @Override
    protected boolean isValidDataSource() {
        return configMap != null && !configMap.isEmpty();
    }

    @Override
    protected Map<String, Object> getDataMap() {
        return configMap;
    }

    @Override
    protected String getSourceName() {
        return "Config";
    }

}