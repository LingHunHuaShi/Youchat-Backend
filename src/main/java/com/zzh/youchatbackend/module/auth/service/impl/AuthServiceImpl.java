package com.zzh.youchatbackend.module.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.wf.captcha.GifCaptcha;
import com.zzh.youchatbackend.common.entity.enums.UniqueUidStatusEnum;
import com.zzh.youchatbackend.common.entity.po.UniqueUid;
import com.zzh.youchatbackend.common.entity.enums.GenderEnum;
import com.zzh.youchatbackend.common.entity.enums.PrivacyLevelEnum;
import com.zzh.youchatbackend.common.entity.enums.UserStatusEnum;
import com.zzh.youchatbackend.common.entity.po.User;
import com.zzh.youchatbackend.common.exception.BusinessException;
import com.zzh.youchatbackend.common.mapper.UniqueUidMapper;
import com.zzh.youchatbackend.common.mapper.UserMapper;
import com.zzh.youchatbackend.common.redis.RedisConstants;
import com.zzh.youchatbackend.common.redis.RedisUtils;
import com.zzh.youchatbackend.common.token.JwtTokenUtils;
import com.zzh.youchatbackend.module.auth.entity.vo.UserTokenVO;
import com.zzh.youchatbackend.module.auth.entity.vo.LoginVO;
import com.zzh.youchatbackend.module.auth.entity.vo.RegisterVO;
import com.zzh.youchatbackend.module.auth.service.AuthService;
import com.zzh.youchatbackend.module.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    RedisUtils redisUtils;
    UserMapper userMapper;
    UniqueUidMapper uniqueUidMapper;
    Environment env;
    StringUtils stringUtils;
    JwtTokenUtils jwtTokenUtils;

    public static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    public AuthServiceImpl(RedisUtils redisUtils, UserMapper userMapper, Environment env, UniqueUidMapper uniqueUidMapper, StringUtils stringUtils, JwtTokenUtils jwtTokenUtils) {
        this.redisUtils = redisUtils;
        this.userMapper = userMapper;
        this.env = env;
        this.uniqueUidMapper = uniqueUidMapper;
        this.stringUtils = stringUtils;
        this.jwtTokenUtils = jwtTokenUtils;
    }


    @Override
    public Map<String, String> getCaptcha() {
        GifCaptcha gifCaptcha = new GifCaptcha(130, 50, 6);
        gifCaptcha.setCharType(GifCaptcha.TYPE_ONLY_UPPER);
        String captchaImg = gifCaptcha.toBase64();
        String captchaCode = gifCaptcha.text();
        String captchaImgUuid = UUID.randomUUID().toString();
        Map<String, String> result = new HashMap<>();
        result.put("captchaImgUuid", captchaImgUuid);
        result.put("captchaImg", captchaImg);

        Map<String, Object> cache = new HashMap<>();
        cache.put(captchaImgUuid, captchaCode);
        redisUtils.hmset(RedisConstants.CAPTCHA_MAP_KEY, cache, Integer.parseInt(env.getProperty("auth.captchaExpire")));

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String register(RegisterVO registerVO) {
        String captchaImgUuid = registerVO.getCaptchaImgUuid();
        String captchaCode = registerVO.getCaptchaCode();
        String uid = null;

        if (!captchaCode.equals(redisUtils.hmget(RedisConstants.CAPTCHA_MAP_KEY).get(captchaImgUuid))) {
            throw new BusinessException("Incorrect captcha code");
        }
        logger.info("Captcha used, delete from Redis");
        redisUtils.hdel(RedisConstants.CAPTCHA_MAP_KEY, captchaImgUuid);

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, registerVO.getEmail());
        User user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            throw new BusinessException("User already exist");
        }

        user = User.builder()
                .email(registerVO.getEmail())
                .password(registerVO.getPassword())
                .nickName(registerVO.getNickName())
                .privacyLevel(PrivacyLevelEnum.LEVEL_1)
                .gender(GenderEnum.UNKNOWN)
                .status(UserStatusEnum.OFFLINE)
                .createTime(LocalDateTime.now())
                .build();

        // search unique uid first, if not exist, spawn a random one
        LambdaQueryWrapper<UniqueUid> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(UniqueUid::getEmail, registerVO.getEmail());
        UniqueUid uniqueUid = uniqueUidMapper.selectOne(queryWrapper1);
        if (uniqueUid == null) {
            user.setUid(stringUtils.getRandomUid());
        } else {
            user.setUid(uniqueUid.getUid());
            uniqueUid.setStatus(UniqueUidStatusEnum.IN_USE);
            LambdaUpdateWrapper<UniqueUid> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(UniqueUid::getEmail, registerVO.getEmail());
            uniqueUidMapper.update(uniqueUid, updateWrapper);
            logger.info("Successfully updated unique uid");
        }
        userMapper.insert(user);
        logger.info("Successfully inserted user");
        uid = user.getUid();
        return uid;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserTokenVO login(LoginVO loginVO) {
        String captchaImgUuid = loginVO.getCaptchaImgUuid();
        String captchaCode = loginVO.getCaptchaCode().toUpperCase();

        if (!captchaCode.equals(redisUtils.hmget(RedisConstants.CAPTCHA_MAP_KEY).get(captchaImgUuid))) {
            throw new BusinessException("Incorrect captcha code");
        }

        logger.info("Captcha used, delete from Redis");
        redisUtils.hdel(RedisConstants.CAPTCHA_MAP_KEY, captchaImgUuid);

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, loginVO.getEmail());
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            throw new BusinessException("User does not exist");
        }
        if (!user.getPassword().equals(loginVO.getPassword())) {
            throw new BusinessException("Incorrect password");
        }
        if (user.getStatus() == UserStatusEnum.BANNED) {
            throw new BusinessException("User is Banned");
        }
        // TODO 单设备登录 强制下线
        else if (user.getStatus() == UserStatusEnum.ONLINE) {
            throw new BusinessException("User is Online");
        }

        Boolean isAdmin;
        String adminEmails =  env.getProperty("admin.email");
        isAdmin = adminEmails != null && !adminEmails.isEmpty() && ArrayUtils.contains(adminEmails.split(","), user.getEmail());

        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getEmail, user.getEmail());
        user.setStatus(UserStatusEnum.ONLINE);
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.update(user, updateWrapper);

        String token = jwtTokenUtils.createToken(user.getUid());

        return UserTokenVO.builder()
                .email(user.getEmail())
                .nickname(user.getNickName())
                .isAdmin(isAdmin)
                .token(token)
                .uid(user.getUid())
                .build();
    }

    @Override
    public String logout(String uid) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUid, uid);
        User user = userMapper.selectOne(lambdaQueryWrapper);
        user.setStatus(UserStatusEnum.OFFLINE);
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getUid, user.getUid());
        userMapper.update(user, updateWrapper);
        logger.info("Successfully logged out");

        return "Successfully logged out";
    }
}
