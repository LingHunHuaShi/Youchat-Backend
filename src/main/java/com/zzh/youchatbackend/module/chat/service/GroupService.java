package com.zzh.youchatbackend.module.chat.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.youchatbackend.common.entity.po.Group;
import com.zzh.youchatbackend.common.entity.query.GroupQuery;
import com.zzh.youchatbackend.module.chat.entity.vo.GroupVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GroupService {
    List<Group> getGroupList(GroupQuery groupQuery);
    Page<Group> getPagedGroupList(GroupQuery groupQuery, Integer pageNum, Integer pageSize);
    Group createGroup(GroupVO groupVO);
    Group updateGroup(GroupVO groupVO, String token);
}
