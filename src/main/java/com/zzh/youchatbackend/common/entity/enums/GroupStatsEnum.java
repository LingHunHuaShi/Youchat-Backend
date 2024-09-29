package com.zzh.youchatbackend.common.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum GroupStatsEnum {

    /**
     * 正常
     */
    NORMAL(0),

    /**
     * 已被解散
     */
    DISBANDED(1),

    /**
     * 已被封禁
     */
    BANNED(2);

    @EnumValue
    private final int code;
    GroupStatsEnum(int code) {
        this.code = code;
    }
}
