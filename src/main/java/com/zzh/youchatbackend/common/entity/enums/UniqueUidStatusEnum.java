package com.zzh.youchatbackend.common.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum UniqueUidStatusEnum {
    NOT_IN_USE(0),
    IN_USE(1);

    @EnumValue
    private final int code;

    UniqueUidStatusEnum(int code) {
        this.code = code;
    }
}
