package com.zaqbest.base.auth.domain;

import lombok.Data;

@Data
public class User {
    private Long id;

    private String loginName;

    private String password;

    private String mobilePhone;
}
