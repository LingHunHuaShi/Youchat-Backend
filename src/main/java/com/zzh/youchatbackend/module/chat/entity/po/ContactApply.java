package com.zzh.youchatbackend.module.chat.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zzh.youchatbackend.module.chat.entity.enums.ContactApplyStatusEnum;
import com.zzh.youchatbackend.module.chat.entity.enums.UserContactEnum;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@TableName("contact_apply")
public class ContactApply {
    @TableId(type = IdType.AUTO)
    private Long apply_id;

    @TableField("applicant_id")
    private String applicant_id;

    @TableField("contact_id")
    private String contact_id;

    @TableField("contact_type")
    private UserContactEnum contact_type;

    @TableField("apply_time")
    private LocalDateTime apply_time;

    @TableField("status")
    private ContactApplyStatusEnum status;

    @TableField("operator_id")
    private String operator_id;

    @TableField("apply_info")
    private String apply_info;
}
