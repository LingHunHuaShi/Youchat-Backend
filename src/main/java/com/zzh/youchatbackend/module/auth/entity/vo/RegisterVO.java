package com.zzh.youchatbackend.module.auth.entity.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
public class RegisterVO {
    @NotBlank(message = "Email couldn't be empty")
    @Email(message = "Wrong email format")
    private String email;

    @NotBlank(message = "Nickname couldn't be empty")
    private String nickName;

    @NotBlank(message = "Password couldn't be empty")
    @Size(min = 8, max = 20, message = "Length of password should be between 8 and 20 characters")
    private String password;

    @NotBlank(message = "Captcha code must be filled")
    private String captchaCode;

    @NotBlank(message = "Captcha Image UUID should be filled")
    private String captchaImgUuid;
}
