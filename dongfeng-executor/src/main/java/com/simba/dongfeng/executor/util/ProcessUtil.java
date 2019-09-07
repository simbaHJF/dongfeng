package com.simba.dongfeng.executor.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * DATE:   2019/9/7 16:01
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class ProcessUtil {

    private static Logger logger = LoggerFactory.getLogger(ProcessUtil.class);
    /**
     * 打印进程输出
     *
     * @param process 进程
     */
    public static void readProcessOutput(final Process process) {
        // 将进程的正常输出在 System.out 中打印，进程的错误输出在 System.err 中打印
        read(process.getInputStream(), System.out);
        read(process.getErrorStream(), System.err);
    }

    // 读取输入流
    private static void read(InputStream inputStream, PrintStream out) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                /*out.println(line);*/
                logger.info(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
