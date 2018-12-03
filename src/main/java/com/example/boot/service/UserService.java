package com.example.boot.service;

import com.example.boot.mapper.UserMapper;
import com.example.boot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;


    public List<User> getUserList(){
        return userMapper.getUserList();
    }
}
