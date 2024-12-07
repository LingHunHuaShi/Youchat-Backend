package com.zzh.youchatbackend.module.auth.controller;

import com.zzh.youchatbackend.common.entity.vo.ResponseVO;
import com.zzh.youchatbackend.module.auth.entity.vo.UserTokenVO;
import com.zzh.youchatbackend.module.auth.entity.vo.LoginVO;
import com.zzh.youchatbackend.module.auth.entity.vo.RegisterVO;
import com.zzh.youchatbackend.module.auth.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    private final AuthService authService;
    public static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/captcha")
    public ResponseEntity<ResponseVO<Map<String, String>>> getCaptcha() {
        logger.info("Received Request for Captcha");
        return new ResponseEntity<>(ResponseVO.success(authService.getCaptcha()), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseVO<String>> register(@RequestBody RegisterVO registerVO) {
        logger.info("register user: {}", registerVO);
        return new ResponseEntity<>(ResponseVO.success(authService.register(registerVO)), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseVO<UserTokenVO>> login(@RequestBody LoginVO loginVO) {
        return new ResponseEntity<>(ResponseVO.success(authService.login(loginVO)), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseVO<String>> logout(@RequestParam String uid) {
        return new ResponseEntity<>(ResponseVO.success(authService.logout(uid)), HttpStatus.OK);
    }


}
