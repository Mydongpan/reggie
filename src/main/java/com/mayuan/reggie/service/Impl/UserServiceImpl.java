package com.mayuan.reggie.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mayuan.reggie.entity.User;
import com.mayuan.reggie.mapper.UserMapper;
import com.mayuan.reggie.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
