package com.zzh.youchatbackend.module.common.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.youchatbackend.common.annotation.AuthFilter;
import com.zzh.youchatbackend.common.entity.po.User;
import com.zzh.youchatbackend.common.entity.query.UserQuery;
import com.zzh.youchatbackend.common.entity.vo.ResponseVO;
import com.zzh.youchatbackend.module.common.entity.vo.UserVO;
import com.zzh.youchatbackend.module.common.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
@Validated
public class UserController {
    public static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @AuthFilter(checkAdmin = true)
    public ResponseEntity<ResponseVO<Page<User>>> queryUser(@RequestBody UserQuery userQuery,
                                                            @RequestParam(defaultValue = "0") Integer pageNum,
                                                            @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<User> pagedUserList = userService.getPagedUserList(userQuery, pageNum, pageSize);
        return new ResponseEntity<>(ResponseVO.success(pagedUserList), HttpStatus.OK);
    }

    @GetMapping("/{uid}")
    @AuthFilter
    public ResponseEntity<ResponseVO<UserVO>> queryUserVOByUid(@PathVariable("uid") String uid) {
        UserVO userVO = userService.getUserVOByUid(uid);
        return new ResponseEntity<>(ResponseVO.success(userVO), HttpStatus.OK);
    }
}
