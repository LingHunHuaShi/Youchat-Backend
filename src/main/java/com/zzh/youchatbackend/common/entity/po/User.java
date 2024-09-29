package com.zzh.youchatbackend.common.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzh.youchatbackend.common.entity.enums.GenderEnum;
import com.zzh.youchatbackend.common.entity.enums.PrivacyLevelEnum;
import com.zzh.youchatbackend.common.entity.enums.UserStatusEnum;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Slf4j
@Data
@Builder
@TableName(value = "user")
public class User {
    @TableField("uid")
    private String uid;

    @TableField("email")
    private String email;

    @TableField("nick_name")
    private String nickName;

    @TableField("privacy_level")
    private PrivacyLevelEnum privacyLevel;

    @TableField("gender")
    private GenderEnum gender;

    @TableField("password")
    private String password;

    @TableField("introduction")
    private String introduction;

    @TableField("status")
    private UserStatusEnum status;

    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField("last_login_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginTime;

    @TableField("last_logout_time")
    private Long lastLogoutTime;

}
