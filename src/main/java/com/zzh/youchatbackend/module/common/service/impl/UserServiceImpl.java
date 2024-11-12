package com.zzh.youchatbackend.module.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.youchatbackend.common.entity.po.User;
import com.zzh.youchatbackend.common.entity.query.UserQuery;
import com.zzh.youchatbackend.common.mapper.UserMapper;
import com.zzh.youchatbackend.module.common.entity.vo.UserVO;
import com.zzh.youchatbackend.module.common.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    public static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    private LambdaQueryWrapper<User> buildLambdaQueryWrapper(UserQuery userQuery) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (userQuery.getUid() != null) {
            queryWrapper.eq(User::getUid, userQuery.getUid());
        }
        if (userQuery.getUidFuzzy() != null) {
            queryWrapper.like(User::getUid, userQuery.getUidFuzzy());
        }
        if (userQuery.getEmail() != null) {
            queryWrapper.eq(User::getEmail, userQuery.getEmail());
        }
        if (userQuery.getEmailFuzzy() != null) {
            queryWrapper.like(User::getEmail, userQuery.getEmailFuzzy());
        }
        if (userQuery.getNickName() != null) {
            queryWrapper.eq(User::getNickName, userQuery.getNickName());
        }
        if (userQuery.getNickNameFuzzy() != null) {
            queryWrapper.like(User::getNickName, userQuery.getNickNameFuzzy());
        }
        if (userQuery.getGender() != null) {
            queryWrapper.eq(User::getGender, userQuery.getGender());
        }
        if (userQuery.getStatus() != null) {
            queryWrapper.eq(User::getStatus, userQuery.getStatus());
        }
        if (userQuery.getPrivacyLevel() != null) {
            queryWrapper.eq(User::getPrivacyLevel, userQuery.getPrivacyLevel());
        }
        if (userQuery.getCreateDate() != null) {
            queryWrapper.apply("DATE(create_time) = {0}", userQuery.getCreateDate());
        }
        if (userQuery.getLastLoginDate() != null) {
            queryWrapper.apply("DATE(last_login_time) = {0}", userQuery.getLastLoginDate());
        }
        return queryWrapper;
    }

    @Override
    public Page<User> getPagedUserList(UserQuery userQuery, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<User> queryWrapper = buildLambdaQueryWrapper(userQuery);

        return userMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
    }

    @Override
    public List<User> getUserList(UserQuery userQuery) {
        LambdaQueryWrapper<User> queryWrapper = buildLambdaQueryWrapper(userQuery);

        return userMapper.selectList(queryWrapper);
    }

    @Override
    public User getUser(UserQuery userQuery) {
        LambdaQueryWrapper<User> queryWrapper = buildLambdaQueryWrapper(userQuery);

        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public UserVO getUserVOByUid(String uid) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUid, uid);
        User user = userMapper.selectOne(queryWrapper);
        return UserVO.of(user);
    }
}
