package com.zzh.youchatbackend.module.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.youchatbackend.common.entity.enums.GroupStatsEnum;
import com.zzh.youchatbackend.common.entity.enums.PrivacyLevelEnum;
import com.zzh.youchatbackend.common.entity.po.Group;
import com.zzh.youchatbackend.common.entity.query.GroupQuery;
import com.zzh.youchatbackend.common.exception.BusinessException;
import com.zzh.youchatbackend.common.mapper.GroupMapper;
import com.zzh.youchatbackend.common.token.JwtTokenUtils;
import com.zzh.youchatbackend.module.chat.entity.enums.ContactStatusEnum;
import com.zzh.youchatbackend.module.chat.entity.enums.UserContactEnum;
import com.zzh.youchatbackend.module.chat.entity.po.Contact;
import com.zzh.youchatbackend.module.chat.entity.vo.GroupVO;
import com.zzh.youchatbackend.module.chat.mapper.ContactMapper;
import com.zzh.youchatbackend.module.chat.service.GroupService;
import com.zzh.youchatbackend.module.utils.StringUtils;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Validated
public class GroupServiceImpl implements GroupService {
    GroupMapper groupMapper;
    Environment environment;
    StringUtils stringUtils;
    ContactMapper contactMapper;
    JwtTokenUtils jwtTokenUtils;

    @Autowired
    public GroupServiceImpl(GroupMapper groupMapper, Environment environment, StringUtils stringUtils, ContactMapper contactMapper, JwtTokenUtils jwtTokenUtils) {
        this.groupMapper = groupMapper;
        this.environment = environment;
        this.stringUtils = stringUtils;
        this.contactMapper = contactMapper;
        this.jwtTokenUtils = jwtTokenUtils;
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

    private void updateGroupAvatar(String groupId, MultipartFile avatarFile, MultipartFile avatarCover) throws BusinessException {
        String avatarFolderPath = environment.getProperty("project.folder") + environment.getProperty("file.avatar.path");
        File avatarFolder = new File(avatarFolderPath);
        if (!avatarFolder.exists()) {
            boolean folderMakeRes = avatarFolder.mkdirs();
            if (!folderMakeRes) {
                throw new BusinessException("Failed to create folder.");
            }
        }
        String avatarFilePath = avatarFolderPath + groupId + environment.getProperty("file.avatar.fileExtension");
        String avatarCoverPath = avatarFolderPath + groupId + environment.getProperty("file.avatar.coverExtension");
        try {
            avatarFile.transferTo(new File(avatarFilePath));
            avatarCover.transferTo(new File(avatarCoverPath));
        } catch (IOException e) {
            throw new BusinessException("Failed to copy avatar file.");
        }
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
    public Group createGroup(GroupVO groupVO) {
        GroupQuery groupQuery = GroupQuery.builder()
                .ownerUid(groupVO.getOwnerUid())
                .build();

        List<Group> userGroupList = groupMapper.selectList(buildQueryWrapper(groupQuery));
        if (userGroupList.size() > Integer.parseInt(environment.getProperty("group.maxGroupPerUser"))) {
            throw new BusinessException("Group number of user reached limit.");
        }

        Group group = Group.builder()
                .groupName(groupVO.getGroupName())
                .ownerUid(groupVO.getOwnerUid())
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

        // 添加头像
        if (groupVO.getAvatarCover() != null && groupVO.getAvatarFile() != null) {
            updateGroupAvatar(group.getGid(), groupVO.getAvatarFile(), groupVO.getAvatarCover());
        }

        // TODO 创建会话
        // TODO 发送欢迎消息

        return group;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Group updateGroup(GroupVO groupVO, String token) {
        // 验证 TOKEN 合法性，获取 Token uid
        JwtClaims claims = jwtTokenUtils.verifyToken(token);
        String tokenUid;
        try {
            tokenUid = claims.getSubject();
        } catch (MalformedClaimException e) {
            throw new BusinessException("Token invalid.");
        }

        // 验证 uid 和 ownerUid 是否相同
        String groupOwnerUid = groupMapper.selectById(groupVO.getGid()).getOwnerUid();
        if (!tokenUid.equals(groupOwnerUid)) {
            throw new BusinessException("You are not the owner of this group.");
        }
        // 更新 group 数据库
        groupMapper.updateById(groupVO);

        // 更新 group 头像
        if (groupVO.getAvatarCover() != null && groupVO.getAvatarFile() != null) {
            updateGroupAvatar(groupVO.getGid(), groupVO.getAvatarFile(), groupVO.getAvatarCover());
        }

        // TODO 如果更新昵称则发送 WS 消息
        return groupVO;
    }
}
