package com.yulaiz.https.qcloud.cdn.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

/**
 * Created by YuLai on 2018/3/5.
 *
 * @see <a href="https://github.com/QCloudCDN/CDN_API_DEMO/blob/master/Qcloud_CDN_API/java/cdn_openapi_demo/src/Utilities/Sign.java">Sign.java</a>
 */
public class Sign {

    // 编码方式
    private static final String CONTENT_CHARSET = "UTF-8";
    // HMAC算法
    private static final String HMAC_ALGORITHM = "HmacSHA1";

    /**
     * @param signStr 被加密串
     * @param secret  加密密钥
     * @return
     * @brief 签名
     * @author gavinyao@tencent.com
     * @date 2014-08-13 21:07:27
     */
    public static String sign(String signStr, String secret)
            throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String sig = null;
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(CONTENT_CHARSET), mac.getAlgorithm());
        mac.init(secretKey);
        byte[] hash = mac.doFinal(signStr.getBytes(CONTENT_CHARSET));
        // base64
        //sig = new String(new BASE64Encoder().encode(hash).getBytes());
        //sig = new String(Base64.encodeBase64(hash));
        sig = new String(Base64.encode(hash));
        return sig;
    }

    public static String makeSignPlainText(TreeMap<String, String> requestParams, String requestMethod, String request) {
        String retStr = "";
        retStr += requestMethod;
        retStr += request;
        retStr += buildParamStr(requestParams, requestMethod);
        return retStr;
    }

    protected static String buildParamStr(TreeMap<String, String> requestParams, String requestMethod) {
        String retStr = "";
        for (String key : requestParams.keySet()) {
            //排除上传文件的参数
            if (requestMethod == "POST" && requestParams.get(key).substring(0, 1).equals("@")) {
                continue;
            }
            if (retStr.length() == 0) {
                retStr += '?';
            } else {
                retStr += '&';
            }
            retStr += key.replace("_", ".") + '=' + requestParams.get(key);
        }
        return retStr;
    }
}
