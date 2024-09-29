package com.zzh.youchatbackend.common.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum PrivacyLevelEnum {
    /**
     * 用户：无需同意即可被加为好友
     * 群组：无需同意即可加入
     */
    LEVEL_0(0),

    /**
     * 用户：需要同意才可被加为好友
     * 群组：需要群组或管理员同意才能加入
     */
    LEVEL_1(1),

    /**
     * 用户：无法被加为好友
     * 群组：无法通过申请加入
     */
    LEVEL_2(2);

    @EnumValue
    private final int code;

    PrivacyLevelEnum(int code) {
        this.code = code;
    }
}
