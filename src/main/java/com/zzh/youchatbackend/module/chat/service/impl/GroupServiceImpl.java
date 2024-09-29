package com.zzh.youchatbackend.module.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.youchatbackend.common.entity.enums.GroupStatsEnum;
import com.zzh.youchatbackend.common.entity.enums.PrivacyLevelEnum;
import com.zzh.youchatbackend.common.entity.po.Group;
import com.zzh.youchatbackend.common.entity.query.GroupQuery;
import com.zzh.youchatbackend.common.exception.BusinessException;
import com.zzh.youchatbackend.common.mapper.GroupMapper;
import com.zzh.youchatbackend.module.chat.entity.enums.ContactStatusEnum;
import com.zzh.youchatbackend.module.chat.entity.enums.UserContactEnum;
import com.zzh.youchatbackend.module.chat.entity.po.Contact;
import com.zzh.youchatbackend.module.chat.mapper.ContactMapper;
import com.zzh.youchatbackend.module.chat.service.GroupService;
import com.zzh.youchatbackend.module.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Validated
public class GroupServiceImpl implements GroupService {
    GroupMapper groupMapper;
    Environment environment;
    StringUtils stringUtils;
    ContactMapper contactMapper;

    @Autowired
    public GroupServiceImpl(GroupMapper groupMapper, Environment environment, StringUtils stringUtils, ContactMapper contactMapper) {
        this.groupMapper = groupMapper;
        this.environment = environment;
        this.stringUtils = stringUtils;
        this.contactMapper = contactMapper;
    }

    private LambdaQueryWrapper<Group> buildQueryWrapper(GroupQuery groupQuery) {
        LambdaQueryWrapper<Group> queryWrapper = new LambdaQueryWrapper<>();
        if (groupQuery == null) {
            return queryWrapper;
        }
        if (groupQuery.getGroupName() != null) {
            queryWrapper.eq(Group::getGroupName, groupQuery.getGroupName());
        }
        if (groupQuery.getGid() != null) {
            queryWrapper.eq(Group::getGid, groupQuery.getGid());
        }
        if (groupQuery.getOwnerUid() != null) {
            queryWrapper.eq(Group::getOwnerUid, groupQuery.getOwnerUid());
        }
        return queryWrapper;
    }

    @Override
    public List<Group> getGroupList(GroupQuery groupQuery) {
        LambdaQueryWrapper<Group> queryWrapper = buildQueryWrapper(groupQuery);
        List<Group> groupList = groupMapper.selectList(queryWrapper);
        return groupList;
    }

    @Override
    public Page<Group> getPagedGroupList(GroupQuery groupQuery, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Group> queryWrapper = buildQueryWrapper(groupQuery);
        Page<Group> page = groupMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Group createGroup(String groupName, String ownerId, MultipartFile avatarFile, MultipartFile avatarCover) {
        GroupQuery groupQuery = GroupQuery.builder()
                .ownerUid(ownerId)
                .build();

        List<Group> userGroupList = groupMapper.selectList(buildQueryWrapper(groupQuery));
        if (userGroupList.size() > Integer.parseInt(environment.getProperty("group.maxGroupPerUser"))) {
            throw new BusinessException("Group number of user reached limit.");
        }

        Group group = Group.builder()
                .groupName(groupName)
                .ownerUid(ownerId)
                .gid(stringUtils.getRandomGid())
                .privacyLevel(PrivacyLevelEnum.LEVEL_1)
                .groupStats(GroupStatsEnum.NORMAL)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .groupNotice(environment.getProperty("group.defaultNotice"))
                .build();

        // 添加新增的群组到表中
        groupMapper.insert(group);

        //将该群组增加到创建者的通讯录
        Contact contact = Contact.builder()
                .uid(group.getOwnerUid())
                .contactId(group.getGid())
                .contactType(UserContactEnum.GROUP)
                .status(ContactStatusEnum.NORMAL)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        contactMapper.insert(contact);

        return group;
    }
}
