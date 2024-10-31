package com.zzh.youchatbackend.module.auth.controller;

import com.zzh.youchatbackend.common.entity.vo.ResponseVO;
import com.zzh.youchatbackend.module.auth.entity.vo.UserTokenVO;
import com.zzh.youchatbackend.module.auth.entity.vo.LoginVO;
import com.zzh.youchatbackend.module.auth.entity.vo.RegisterVO;
import com.zzh.youchatbackend.module.auth.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseVO<Map<String, String>> getCaptcha() {
        logger.info("Received Request for Captcha");
        return ResponseVO.success(authService.getCaptcha());
    }

    @PostMapping("/register")
    public ResponseVO<String> register(@RequestBody RegisterVO registerVO) {
        logger.info("register user: {}", registerVO);
        return ResponseVO.success(authService.register(registerVO));
    }

    @PostMapping("/login")
    public ResponseVO<UserTokenVO> login(@RequestBody LoginVO loginVO) {
        return ResponseVO.success(authService.login(loginVO));
    }

    @PostMapping("/logout")
    public ResponseVO<String> logout(@RequestParam String uid) {
        return ResponseVO.success(authService.logout(uid));
    }


}
