package com.zzh.youchatbackend.module.chat.entity.vo;

import com.zzh.youchatbackend.common.entity.po.Group;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class GroupVO extends Group {
    // 头像原图，用于请求原图时显示
    private MultipartFile avatarFile;
    // 头像缩略图，用于正常显示
    private MultipartFile avatarCover;

    public GroupVO() {
        super();
    }

    public GroupVO(Group group, MultipartFile avatarFile, MultipartFile avatarCover) {
        setGid(group.getGid());
        setGroupName(group.getGroupName());
        setGroupNotice(group.getGroupNotice());
        setOwnerUid(group.getOwnerUid());
        setPrivacyLevel(group.getPrivacyLevel());
        setGroupStats(group.getGroupStats());
        setCreateTime(group.getCreateTime());
        setUpdateTime(group.getUpdateTime());

        setAvatarCover(avatarCover);
        setAvatarFile(avatarFile);
    }
}
