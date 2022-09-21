package com.iohao.game.common.validation;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.io.resource.ResourceUtil;
import com.iohao.game.common.kit.ClassScanner;
import com.iohao.game.common.validation.support.JakartaValidator;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@UtilityClass
public class Validation {

    final String fileName = "META-INF/ioGame/com.iohao.game.common.validation.Validator";
    final String defaultValidator = "com.iohao.game.common.validation.support.JakartaValidator";

    private Validator validator;

    public Validator getValidator() throws Exception {
        if (validator != null) {
            return validator;
        }
        String className = null;
        try {
            className = ResourceUtil.readStr(fileName, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.info("读取" + fileName + "失败");
        }
        if (StringUtils.isBlank(className)) {
            className = defaultValidator;
        }
        className = StringUtils.trim(className);
        List<String> segments = Arrays.stream(className.split("\\.")).toList();

        String packageName =segments.stream().limit(segments.size()-1).collect(Collectors.joining("/"));
        String finalClassName = className;
        ClassScanner classScanner = new ClassScanner(packageName, clazz -> clazz.getName().equals(finalClassName));
        List<Class<?>> classList = classScanner.listScan();
        if (classList == null || classList.isEmpty()) {
            throw new Exception("缺少类" + className);
        }
        Class<?> clazz = classList.get(0);
        validator = (Validator) clazz.getConstructor().newInstance();
        return validator;
    }
}
