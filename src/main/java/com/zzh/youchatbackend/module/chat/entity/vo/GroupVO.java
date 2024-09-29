package com.zzh.youchatbackend.module.chat.entity.vo;

import com.zzh.youchatbackend.common.entity.po.Group;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

//@Builder
@Getter
public class GroupVO extends Group {
    private MultipartFile avatarFile;
    private MultipartFile avatarCover;
}
