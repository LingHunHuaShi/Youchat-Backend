package com.zzh.youchatbackend.common.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zzh.youchatbackend.common.entity.enums.UniqueUidStatusEnum;
import lombok.Data;

@Data
@TableName(value = "unique_uid")
public class UniqueUid {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String uid;
    private String email;
    private UniqueUidStatusEnum status;
}
