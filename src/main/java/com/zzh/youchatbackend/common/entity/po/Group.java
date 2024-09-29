package com.zzh.youchatbackend.common.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzh.youchatbackend.common.entity.enums.GroupStatsEnum;
import com.zzh.youchatbackend.common.entity.enums.PrivacyLevelEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@TableName("user_group")
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    @TableId(type = IdType.AUTO)
    private String gid;

    @TableField("group_name")
    @NotBlank
    private String groupName;

    @TableField("group_notice")
    private String groupNotice;

    @TableField("owner_uid")
    @NotBlank
    private String ownerUid;

    @TableField("privacy_level")
    private PrivacyLevelEnum privacyLevel;

    @TableField("status")
    private GroupStatsEnum groupStats;

    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
