package com.zzh.youchatbackend.module.auth.entity.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UserTokenVO implements Serializable {
    public static final Long serialVersionUID = 1L;

    private String token;
    private String email;
    private String nickname;
    private Boolean isAdmin;
}
