package com.zzh.youchatbackend.common.entity.query;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupQuery {
    private String gid;
    private String groupName;
    private String ownerUid;
}
