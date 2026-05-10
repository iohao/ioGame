package com.iohao.game.widget.light.profile;

import java.util.HashMap;
import java.util.Map;

public class EnvStaticBinder extends AbstractStaticBinder {

    private final Map<String, String> configFileData;

    // 构造函数：接受配置文件数据
    public EnvStaticBinder(Map<String, String> configFileData) {
        this.configFileData = configFileData != null ? configFileData : new HashMap<>();
    }

    @Override
    protected boolean isValidDataSource() {
        // 检查配置文件或环境变量是否有数据
        return !configFileData.isEmpty() || !System.getenv().isEmpty();
    }

    @Override
    protected Map<String, Object> getDataMap() {
        // 合并配置文件和环境变量数据，配置文件优先
        Map<String, Object> mergedData = new HashMap<>(configFileData);
        System.getenv().forEach((key, value) -> {
            mergedData.putIfAbsent(key, value); // 环境变量作为备选
        });
        return mergedData;
    }

    @Override
    protected String getSourceName() {
        return "Env"; // 日志标识仍为 Env，但内部已融合配置文件数据
    }
}
