package com.zzh.youchatbackend.module.common.entity.vo;

import com.zzh.youchatbackend.common.entity.enums.GenderEnum;
import com.zzh.youchatbackend.common.entity.enums.PrivacyLevelEnum;
import com.zzh.youchatbackend.common.entity.enums.UserStatusEnum;
import com.zzh.youchatbackend.common.entity.po.User;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Builder
public class UserVO {
    private String uid;
    private String nickName;
    private String email;
    private PrivacyLevelEnum privacyLevel;
    private GenderEnum gender;
    private String introduction;
    private UserStatusEnum status;
    private String areaName;
    private String areaCode;

    private LocalDateTime lastLoginTime;
    private Long lastLogoutTime;

    private MultipartFile avatarFile;
    private MultipartFile avatarCover;

    public static UserVO of(User user) {
        return UserVO.builder()
                .uid(user.getUid())
                .nickName(user.getNickName())
                .email(user.getEmail())
                .privacyLevel(user.getPrivacyLevel())
                .gender(user.getGender())
                .introduction(user.getIntroduction())
                .status(user.getStatus())
                .areaName(user.getAreaName())
                .areaCode(user.getAreaCode())
                .lastLoginTime(user.getLastLoginTime())
                .lastLogoutTime(user.getLastLogoutTime())
                .build();
    }
}
