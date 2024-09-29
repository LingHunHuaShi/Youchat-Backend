package com.zzh.youchatbackend.common.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum GenderEnum {
    MALE(0),
    FEMALE(1),
    UNKNOWN(2);

    @EnumValue
    private final int code;

    GenderEnum(int code) {
        this.code = code;
    }
}
