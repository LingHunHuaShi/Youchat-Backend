package com.zzh.youchatbackend.module.chat.controller;

import com.zzh.youchatbackend.common.annotation.AuthFilter;
import com.zzh.youchatbackend.common.entity.po.Group;
import com.zzh.youchatbackend.common.entity.vo.ResponseVO;
import com.zzh.youchatbackend.module.chat.entity.vo.GroupVO;
import com.zzh.youchatbackend.module.chat.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group")
@Validated
public class GroupController {
    GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping(value = "/createGroup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AuthFilter
    public ResponseEntity<ResponseVO<Group>> createGroup(@ModelAttribute GroupVO group) {
//        Group newGroup = groupService.createGroup(group.getGroupName(), group.getOwnerUid(), group.getAvatarFile(), group.getAvatarCover());
        Group newGroup = groupService.createGroup(group);
        return new ResponseEntity<>(ResponseVO.success(newGroup), HttpStatus.OK);
    }

    @PostMapping(value = "/updateGroup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AuthFilter
    public ResponseEntity<ResponseVO<Group>> updateGroup(@RequestHeader("Authorization") String authHeader, @ModelAttribute GroupVO group) {
        String token = authHeader.substring(7);
        Group updatedGroup = groupService.updateGroup(group, token);
        return new ResponseEntity<>(ResponseVO.success(updatedGroup), HttpStatus.OK);
    }
}
