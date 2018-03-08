package com.yulaiz.https.qcloud.cdn.utils;

import com.yulaiz.https.qcloud.cdn.exception.ExeResultException;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by YuLai on 2018/3/6.
 */
public class OpenSslUtil {

    private static String OS = System.getProperty("os.name").toLowerCase();

    private static Logger log = Logger.getLogger(OpenSslUtil.class);

    public static boolean isLinux() {
        return OS.indexOf("linux") >= 0;
    }

    public static boolean isWindows() {
        return OS.indexOf("windows") >= 0;
    }

    public static void convertPKCS8ToPKCS1(String filePath_PKCS8, String filePath_PKCS1) throws ExeResultException {
        String command = null;
        if (isWindows()) {
            command = Config.getInstance().getOpensslWindows() + " rsa -in " + filePath_PKCS8 + " -out " + filePath_PKCS1;
        } else if (isLinux()) {
            command = Config.getInstance().getOpensslLinux() + " rsa -in " + filePath_PKCS8 + " -out " + filePath_PKCS1;
        } else {
            throw new ExeResultException("不支持的操作系统");
        }
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec(command);
            int code = process.waitFor();
            if (code == 1) {
                String errorMessage = getErrorMessage(process);
                log.debug(errorMessage);
                throw new ExeResultException(errorMessage);
            }
        } catch (Exception e) {
//            e.printStackTrace();
            String errorMessage = getErrorMessage(process);
            log.debug(errorMessage);
            throw new ExeResultException(errorMessage);
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    // 得到控制台输出的错误信息
    private static String getErrorMessage(Process process) {
        String errMeaage = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            errMeaage = sb.toString();
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return errMeaage;
    }
}
