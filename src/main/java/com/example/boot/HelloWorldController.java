package com.example.boot;

import com.example.boot.model.User;
import com.example.boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloWorldController {

    @Autowired
    private UserService userService;

    @RequestMapping("/hello")
    public String say(){
        return "Hello World!";
    }

    @RequestMapping("/getUserList")
    public List<User> getUserList(){
        return userService.getUserList();
    }
}
