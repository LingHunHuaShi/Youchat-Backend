package com.zzh.youchatbackend.common.entity.query;

import com.zzh.youchatbackend.common.entity.enums.GenderEnum;
import com.zzh.youchatbackend.common.entity.enums.PrivacyLevelEnum;
import com.zzh.youchatbackend.common.entity.enums.UserStatusEnum;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
public class UserQuery {
    private String uid;
    private String uidFuzzy;

    private String email;
    private String emailFuzzy;

    private String nickName;
    private String nickNameFuzzy;

    private PrivacyLevelEnum privacyLevel;

    private GenderEnum gender;

    private UserStatusEnum status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastLoginDate;

    private Long lastLogoutTime;
}
