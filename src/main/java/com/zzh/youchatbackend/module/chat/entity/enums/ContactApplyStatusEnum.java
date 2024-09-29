package com.zzh.youchatbackend.module.chat.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum ContactApplyStatusEnum {
    /**
     * 未操作
     */
    NOT_OPERATED(0),

    /**
     * 已同意
     */
    ACCEPTED(1),

    /**
     * 已拒绝
     */
    REJECTED(2),

    /**
     * 已拒绝并拉黑
     */
    REJECTED_AND_BLACKLISTED(3);

    @EnumValue
    private final int code;
    ContactApplyStatusEnum(int code) {
        this.code = code;
    }
}
