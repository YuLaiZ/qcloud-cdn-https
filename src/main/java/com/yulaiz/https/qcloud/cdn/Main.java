package com.yulaiz.https.qcloud.cdn;

import com.yulaiz.https.qcloud.cdn.exception.ExeResultException;
import com.yulaiz.https.qcloud.cdn.utils.Base64;
import com.yulaiz.https.qcloud.cdn.utils.Config;
import com.yulaiz.https.qcloud.cdn.utils.OpenSslUtil;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

/**
 * Created by YuLai on 2018/3/7.
 */
public class Main {
    public static final String FILE_ENCODING = "utf8";
    private static Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            properties.load(Main.class.getClassLoader().getResourceAsStream("log4j.properties"));
        } catch (IOException e) {
            System.out.println("log4j.properties加载出错,请检查jar包完整性");
        }
        PropertyConfigurator.configure(properties);

        String publicCertContent = null;
        try {
            publicCertContent = FileUtils.readFileToString(FileUtils.getFile(Config.getInstance().getPublicCert()), FILE_ENCODING);
        } catch (IOException e) {
//            e.printStackTrace();
            log.debug(e.getMessage(), e);
            System.out.println("公钥文件读取失败,可在logs/debug.log中查看详细日志");
            return;
        }
        try {
            FileUtils.readFileToString(FileUtils.getFile(Config.getInstance().getPrivateCert()), FILE_ENCODING);
        } catch (IOException e) {
//            e.printStackTrace();
            log.debug(e.getMessage(), e);
            System.out.println("私钥文件读取失败,可在logs/debug.log中查看详细日志");
            return;
        }
        String privateRsaKeyPath = Config.getInstance().getPrivateCert() + ".tmp";
        File privateRsaFile = FileUtils.getFile(privateRsaKeyPath);
        if (privateRsaFile.exists()) {
            privateRsaFile.delete();
        }
        try {
            OpenSslUtil.convertPKCS8ToPKCS1(Config.getInstance().getPrivateCert(), privateRsaKeyPath);
        } catch (ExeResultException e) {
            log.debug(e.getMessage(), e);
            System.out.println(e.getMessage());
        }
        String privateRsaKeyContent = null;
        try {
            privateRsaKeyContent = FileUtils.readFileToString(privateRsaFile, FILE_ENCODING);
        } catch (IOException e) {
//            e.printStackTrace();
            log.debug(e.getMessage(), e);
            System.out.println("RSA私钥文件读取失败,可在logs/debug.log中查看详细日志");
            return;
        }
        String publicCertBase64Content = null;
        try {
            publicCertBase64Content = Base64.encode(publicCertContent.getBytes(FILE_ENCODING));
        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
            log.debug(e.getMessage(), e);
            System.out.println("公钥Base64失败,可在logs/debug.log中查看详细日志");
            return;
        }
        String privateRsaKeyBase64Content = null;
        try {
            privateRsaKeyBase64Content = Base64.encode(privateRsaKeyContent.getBytes(FILE_ENCODING));
        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
            log.debug(e.getMessage(), e);
            System.out.println("私钥Base64失败,可在logs/debug.log中查看详细日志");
            return;
        }

        List<String> hosts = Config.getInstance().getHosts();
        for (String host : hosts) {
            try {
                PushHttpsCert2Qcloud.pushCert(host, publicCertBase64Content, privateRsaKeyBase64Content);
            } catch (ExeResultException e) {
                System.out.println("[" + host + "]腾讯云上传失败,可在logs/debug.log中查看详细日志");
            }
        }
    }
}
