package com.zzh.youchatbackend.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzh.youchatbackend.common.entity.po.UniqueUid;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UniqueUidMapper extends BaseMapper<UniqueUid> {
}
