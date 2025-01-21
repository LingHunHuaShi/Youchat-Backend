package com.zzh.youchatbackend.module.chat.controller;

import com.zzh.youchatbackend.common.annotation.AuthFilter;
import com.zzh.youchatbackend.common.entity.po.Group;
import com.zzh.youchatbackend.common.entity.query.GroupQuery;
import com.zzh.youchatbackend.common.entity.vo.ResponseVO;
import com.zzh.youchatbackend.module.chat.entity.vo.GroupVO;
import com.zzh.youchatbackend.module.chat.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/groups")
@Validated
public class GroupController {
    GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AuthFilter
    public ResponseEntity<ResponseVO<Group>> createGroup(@ModelAttribute GroupVO group) {
        Group newGroup = groupService.createGroup(group);
        return new ResponseEntity<>(ResponseVO.success(newGroup), HttpStatus.OK);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AuthFilter
    public ResponseEntity<ResponseVO<Group>> updateGroup(@RequestHeader("Authorization") String authHeader, @ModelAttribute GroupVO group) {
        String token = authHeader.substring(7);
        Group updatedGroup = groupService.updateGroup(group, token);
        return new ResponseEntity<>(ResponseVO.success(updatedGroup), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{groupId}")
    @AuthFilter
    public ResponseEntity<ResponseVO> deleteGroup(@RequestHeader("Authorization") String authHeader, @PathVariable("groupId") String groupId) {
        String token = authHeader.substring(7);
        groupService.deleteGroup(groupId, token);
        return new ResponseEntity<>(ResponseVO.success("success"), HttpStatus.OK);
    }

    @GetMapping(value = "/{groupId}")
    @AuthFilter
    public ResponseEntity<ResponseVO<Group>> getGroupById(@RequestHeader("Authorization") String authHeader, @PathVariable("groupId") String groupId) {
        String token = authHeader.substring(7);
        GroupQuery groupQuery = GroupQuery.builder()
                .gid(groupId)
                .build();
        Group group = groupService.getGroupList(groupQuery, token).get(0);
        return new ResponseEntity<>(ResponseVO.success(group), HttpStatus.OK);
    }

    @GetMapping(value = "/{groupId}/cover")
    @AuthFilter
    public ResponseEntity<ResponseVO<byte[]>> getGroupCover(@RequestHeader("Authorization") String authHeader, @PathVariable("groupId") String groupId) {
        String token = authHeader.substring(7);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        return new ResponseEntity<>(ResponseVO.success(groupService.getGroupCover(groupId, token, false)), headers, HttpStatus.OK);
    }

    @GetMapping(value = "/{groupId}/avatar")
    @AuthFilter
    public ResponseEntity<ResponseVO<byte[]>> getGroupAvatar(@RequestHeader("Authorization") String authHeader, @PathVariable("groupId") String groupId) {
        String token = authHeader.substring(7);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        return new ResponseEntity<>(ResponseVO.success(groupService.getGroupCover(groupId, token, true)), headers, HttpStatus.OK);
    }
}
