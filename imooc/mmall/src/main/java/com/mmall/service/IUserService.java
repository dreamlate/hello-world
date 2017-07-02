package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * Created by MengHan on 2017/7/2.
 */
public interface IUserService {
    ServerResponse<User> login(String username, String password);
}
