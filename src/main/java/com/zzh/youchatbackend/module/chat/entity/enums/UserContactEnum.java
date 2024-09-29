package com.zzh.youchatbackend.module.chat.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum UserContactEnum {
    USER(0, "U", "User"),
    GROUP(1, "G", "Group");

    @EnumValue
    private final Integer code;
    private final String prefix;
    private final String name;

    UserContactEnum(Integer code, String prefix, String name) {
        this.code = code;
        this.prefix = prefix;
        this.name = name;
    }

    public static UserContactEnum getByName(String name) {
        if (name == null) {
            return null;
        }
        try {
            return UserContactEnum.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static UserContactEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (UserContactEnum userContactEnum : UserContactEnum.values()) {
            if (userContactEnum.getCode().equals(code)) {
                return userContactEnum;
            }
        }
        return null;
    }

    public static UserContactEnum getByPrefix(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return null;
        }
        prefix = prefix.substring(0, 1).toUpperCase();
        for (UserContactEnum userContactEnum : UserContactEnum.values()) {
            if (userContactEnum.getPrefix().equals(prefix)) {
                return userContactEnum;
            }
        }
        return null;
    }
}
