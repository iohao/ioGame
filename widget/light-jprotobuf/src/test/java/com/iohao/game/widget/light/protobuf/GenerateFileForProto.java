package com.iohao.game.widget.light.protobuf;


import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

/**
 * 自动生成proto文件
 */
@Slf4j
public class GenerateFileForProto {

    public static void main(String[] args) {

        /*
         * .proto 文件生成
         * 相关文档 https://www.yuque.com/iohao/game/vpe2t6
         *
         * 运行该类，会在当前项目 target/proto 目录下生成 .proto 文件
         */

        // 需要扫描的包名
        String protoPackagePath = GenerateFileForProto.class.getPackageName();

        String curDir = System.getProperty("user.dir");
        char[] curDirCharArray = curDir.toCharArray();
        if (StringUtils.equals(String.valueOf(curDirCharArray[curDirCharArray.length - 1]), File.separator)) {
            curDir = curDir.substring(0, curDir.length() - 1);
        }
        String[] protoSourcePathArray = new String[]{
            curDir
            , "widget"
            , "light-jprotobuf"
            , "src"
            , "test"
            , "java", "com", "iohao", "game", "widget", "light", "protobuf"
        };

        // 源码目录
        String protoSourcePath = StringUtils.join(protoSourcePathArray, File.separator);

        String[] generateFolderArray = new String[]{
            System.getProperty("user.dir")
            , "widget"
            , "light-jprotobuf"
            , "target"
            , "proto"
        };

        // 生成 .proto 文件存放的目录
        String generateFolder = StringUtils.join(generateFolderArray, File.separator);

        ProtoGenerateFile protoGenerateFile = ProtoGenerateFile.builder()
            // 源码目录
            .protoSourcePath(protoSourcePath)
            // 需要扫描的包名
            .protoPackagePath(protoPackagePath)
            // 生成 .proto 文件存放的目录
            .generateFolder(generateFolder)
            .build();

        // 生成 .proto 文件
        protoGenerateFile.generate();
    }
}