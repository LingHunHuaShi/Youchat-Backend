package com.zzh.youchatbackend.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzh.youchatbackend.common.entity.po.Group;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GroupMapper extends BaseMapper<Group> {
}
