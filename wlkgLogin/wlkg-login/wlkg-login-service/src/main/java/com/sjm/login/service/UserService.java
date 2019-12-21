package com.sjm.login.service;

import com.sjm.login.pojo.User;

public interface UserService {
    Boolean checkUserData(String data, Integer type);

    User loginUser(User user, Integer type);
}
