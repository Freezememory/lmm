package com.diary.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginVO {
    private String token;
    private String username;
    private String nickname;
}
