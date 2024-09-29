package com.zzh.youchatbackend.common.redis;

public class RedisConstants {
    // KEY NAME

    /**
     * redis key for captcha code and captcha image uuid
     */
    public static final String CAPTCHA_MAP_KEY = "youchat:captcha";

    /**
     * redis key for user heartbeat
     */
    public static final String USER_HEARTBEAT_KEY = "youchat:user:heartbeat";

    /**
     * redis key for user token blacklist
     */
    public static final String USER_TOKEN_BLACK_KEY = "youchat:user:token_blacklist";
}
