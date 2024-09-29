package com.zzh.youchatbackend.common.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum UserStatusEnum {
    ONLINE(0),
    OFFLINE(1),
    BANNED(2);

    @EnumValue
    private final int code;

    UserStatusEnum(int code) {
        this.code = code;
    }

}
