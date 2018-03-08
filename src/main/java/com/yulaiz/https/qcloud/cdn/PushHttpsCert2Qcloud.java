package com.yulaiz.https.qcloud.cdn;

import com.alibaba.fastjson.JSON;
import com.yulaiz.https.qcloud.cdn.exception.ExeResultException;
import com.yulaiz.https.qcloud.cdn.response.PushHttpsRes;
import com.yulaiz.https.qcloud.cdn.utils.Config;
import com.yulaiz.https.qcloud.cdn.utils.HttpClientUtil;
import com.yulaiz.https.qcloud.cdn.utils.Sign;
import org.apache.log4j.Logger;

import java.util.Random;
import java.util.TreeMap;

/**
 * Created by YuLai on 2018/3/5.
 *
 * @see <a href="https://cloud.tencent.com/document/product/228/12965">HTTPS 配置</a>
 */
public class PushHttpsCert2Qcloud {
    private static final String PROTOCOL = "https://";
    private static final String URI = "cdn.api.qcloud.com/v2/index.php";
    private static final String POST = "POST";
    private static final String ACTION = "SetHttpsInfo";
    private static Logger log = Logger.getLogger(PushHttpsCert2Qcloud.class);

    public static void pushCert(String host, String publicCertBase64Content, String privateRsaKeyBase64Content) throws ExeResultException {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("Action", ACTION);
        params.put("Timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        params.put("Nonce", String.valueOf(new Random().nextInt(Integer.MAX_VALUE)));
        params.put("SecretId", Config.getInstance().getSecretId());

        params.put("host", host);
        params.put("httpsType", "2");
        params.put("cert", publicCertBase64Content);
        params.put("privateKey", privateRsaKeyBase64Content);
        try {
            String plainText = Sign.makeSignPlainText(params, POST, URI);
            params.put("Signature", Sign.sign(plainText, Config.getInstance().getSecretKey()));
        } catch (Exception e) {
//            e.printStackTrace();
            log.debug(e.getMessage(), e);
            throw new ExeResultException("[" + host + "]签名失败");
        }
        String response = null;
        try {
            response = HttpClientUtil.doPost(PROTOCOL + URI, params, null);
        } catch (Exception e) {
//            e.printStackTrace();
            log.debug(e.getMessage(), e);
            throw new ExeResultException("[" + host + "]请求腾讯云失败");
        }
        PushHttpsRes res = null;
        try {
            res = JSON.parseObject(response, PushHttpsRes.class);
        } catch (Exception e) {
//            e.printStackTrace();
            log.debug(e.getMessage(), e);
            log.debug("[" + host + "]腾讯云返回内容为:" + response);
            throw new ExeResultException("[" + host + "]腾讯云返回解析失败");
        }
        if (res.getCode() != 0) {
            throw new ExeResultException("[" + host + "]腾讯云执行失败:" + res.getMessage());
        }
    }
}
