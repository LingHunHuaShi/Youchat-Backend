package com.zzh.youchatbackend.module.auth.service;

import com.zzh.youchatbackend.module.auth.entity.vo.UserTokenVO;
import com.zzh.youchatbackend.module.auth.entity.vo.LoginVO;
import com.zzh.youchatbackend.module.auth.entity.vo.RegisterVO;

import java.util.Map;

public interface AuthService {
    Map<String, String> getCaptcha();
    String register(RegisterVO registerVO);
    UserTokenVO login(LoginVO loginVO);
    String logout(String uid);
}
