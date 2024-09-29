package com.zzh.youchatbackend.module.chat.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zzh.youchatbackend.module.chat.entity.enums.ContactStatusEnum;
import com.zzh.youchatbackend.module.chat.entity.enums.UserContactEnum;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Data
@Builder
@Slf4j
@TableName("contact")
public class Contact {
    @TableId(type = IdType.AUTO)
    private String uid;

    @TableField("contact_id")
    private String contactId;

    @TableField("contact_type")
    private UserContactEnum contactType;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("status")
    private ContactStatusEnum status;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
