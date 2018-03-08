package com.yulaiz.https.qcloud.cdn.utils;

import org.aeonbits.owner.ConfigFactory;

/**
 * Created by YuLai on 2018/3/7.
 */
public class Config {
    private static ServerConfig cfg = ConfigFactory.create(ServerConfig.class);

    private Config() {
        throw new IllegalStateException("Utility class");
    }

    public static ServerConfig getInstance() {
        return cfg;
    }
}
