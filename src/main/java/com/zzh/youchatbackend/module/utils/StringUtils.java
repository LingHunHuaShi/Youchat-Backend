package com.zzh.youchatbackend.module.utils;

import com.zzh.youchatbackend.module.chat.entity.enums.UserContactEnum;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

@Component
public class StringUtils {

    Environment env;

    @Autowired
    public StringUtils(Environment env) {
        this.env = env;
    }


    /**
     * spawn a random uid
     * @return uid
     */
    public String getRandomUid() {
        return UserContactEnum.USER.getPrefix() + RandomStringUtils.random(Integer.parseInt(env.getProperty("user.uidLength")) - 1, true, true);
    }

    /**
     * spawn a random gid
     */
    public String getRandomGid() {
        return UserContactEnum.GROUP.getPrefix() + RandomStringUtils.random(Integer.parseInt(env.getProperty("user.uidLength")) - 1, true, true);
    }


    /**
     * MD5 encode string
     * @param origin
     * @return encoded string
     */
    public String MD5Encode(String origin) {
        return origin == null ? "" : DigestUtils.md5DigestAsHex(origin.getBytes());
    }


}
