package com.zzh.youchatbackend.common.entity.query;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UniqueUidQuery {
    private String uid;
    private String uidFuzzy;

    private String email;
    private String emailFuzzy;

    private Integer status;
}
