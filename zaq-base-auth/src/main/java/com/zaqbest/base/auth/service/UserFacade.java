package com.zaqbest.base.auth.service;

import com.zaqbest.base.auth.domain.User;

public interface UserFacade {
    User selectById(Long id);
}
