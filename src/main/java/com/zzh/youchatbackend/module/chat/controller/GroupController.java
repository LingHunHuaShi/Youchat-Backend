package com.zzh.youchatbackend.module.chat.controller;

import com.zzh.youchatbackend.common.annotation.AuthFilter;
import com.zzh.youchatbackend.common.entity.po.Group;
import com.zzh.youchatbackend.common.entity.vo.ResponseVO;
import com.zzh.youchatbackend.module.chat.entity.vo.GroupVO;
import com.zzh.youchatbackend.module.chat.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/group")
@Validated
public class GroupController {
    GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping("/createGroup")
    @AuthFilter(checkAdmin = false)
    public ResponseEntity<ResponseVO<Group>> createGroup(@RequestBody GroupVO group) {
        Group newGroup = groupService.createGroup(group.getGroupName(), group.getOwnerUid(), group.getAvatarFile(), group.getAvatarCover());
        return new ResponseEntity<>(ResponseVO.success(newGroup), HttpStatus.OK);
    }
}
