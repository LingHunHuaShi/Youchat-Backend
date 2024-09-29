package com.zzh.youchatbackend.module.chat.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum ContactStatusEnum {
    /**
     * 正常
     */
    NORMAL(0),

    /**
     * 非好友私聊
     */
    NOT_FRIEND(1),

    /**
     * 1.已删除的好友
     * 2.已退出的群聊
     */
    DELETED(2),

    /**
     * 1.被好友删除
     * 2.被踢出的群聊
     */
    BE_DELETED(3),

    /**
     * 已拉黑的联系人
     */
    BLACKLISTED(4),

    /**
     * 被联系人拉黑
     */
    BE_BLACKLISTED(5);

    @EnumValue
    private final int code;
    ContactStatusEnum(int code) {
        this.code = code;
    }
}
