package com.yulaiz.https.qcloud.cdn.utils;

import org.aeonbits.owner.Config;

import java.util.List;

/**
 * Created by YuLai on 2018/3/6.
 */
@Config.Sources({
        "file:${user.dir}/config.properties"
//        ,"classpath:config.properties"
})
public interface ServerConfig extends Config {

    @Key("qcloud.hosts")
    List<String> getHosts();

    @Key("qcloud.secret.id")
    String getSecretId();

    @Key("qcloud.secret.key")
    String getSecretKey();

    @Key("file.public.cert")
    String getPublicCert();

    @Key("file.private.cert")
    String getPrivateCert();

    @Key("openssl.windows")
    @DefaultValue("openssl.exe")
    String getOpensslWindows();

    @Key("openssl.linux")
    @DefaultValue("openssl")
    String getOpensslLinux();
}
