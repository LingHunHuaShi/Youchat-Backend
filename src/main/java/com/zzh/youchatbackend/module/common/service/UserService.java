package com.zzh.youchatbackend.module.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.youchatbackend.common.entity.po.User;
import com.zzh.youchatbackend.common.entity.query.UserQuery;
import com.zzh.youchatbackend.module.common.entity.vo.UserVO;

import java.util.List;

public interface UserService {
    Page<User> getPagedUserList(UserQuery userQuery, Integer pageNum, Integer pageSize);
    List<User> getUserList(UserQuery userQuery);
    User getUser(UserQuery userQuery);
    UserVO getUserVOByUid(String uid);
}
