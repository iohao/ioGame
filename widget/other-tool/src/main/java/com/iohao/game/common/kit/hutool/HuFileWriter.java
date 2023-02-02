/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iohao.game.common.kit.hutool;


import java.io.*;
import java.nio.charset.Charset;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
 class HuFileWriter implements Serializable {

    protected File file;
    protected Charset charset;

    public static HuFileWriter create(File file, Charset charset) {
        return new HuFileWriter(file, charset);
    }

    public HuFileWriter(File file, Charset charset) {
        this.file = file;
        this.charset = charset;
        checkFile();
    }

    private void checkFile() throws HuIoRuntimeException {
        HuAssert.notNull(file, "File to write content is null !");
        if (this.file.exists() && !file.isFile()) {
            throw new HuIoRuntimeException("File [{}] is not a file !", this.file.getAbsoluteFile());
        }
    }

    public File write(String content) throws HuIoRuntimeException {
        return write(content, false);
    }

    public File write(String content, boolean isAppend) throws HuIoRuntimeException {
        BufferedWriter writer = null;
        try {
            writer = getWriter(isAppend);
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            throw new HuIoRuntimeException(e);
        } finally {
            HuIoUtil.close(writer);
        }
        return file;
    }

    public BufferedWriter getWriter(boolean isAppend) throws HuIoRuntimeException {
        try {
            return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(HuFileUtil.touch(file), isAppend), charset));
        } catch (Exception e) {
            throw new HuIoRuntimeException(e);
        }
    }

}
