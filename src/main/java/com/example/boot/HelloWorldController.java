package com.example.boot;

import com.example.boot.model.User;
import com.example.boot.service.UserService;
import com.example.boot.utils.FileUtils;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.apache.naming.SelectorContext.prefix;

@RestController
public class HelloWorldController {

    private final UserService userService;

    @Autowired
    private Resource resource;

    @RequestMapping("/getResource")
    public Resource getResource(){
        Resource bean = new Resource();
        BeanUtils.copyProperties(resource,bean);
        return bean;
    }

    @Autowired
    public HelloWorldController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/hello")
    public String say(@RequestParam("fileName") MultipartFile file) throws IOException {
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件后缀
        String prefix=fileName.substring(fileName.lastIndexOf("."));
        final File excelFile = File.createTempFile("test", prefix);
        // MultipartFile to File
        file.transferTo(excelFile);
        FileUtils.copyFile(excelFile);
        return "Hello World!";
    }

    @RequestMapping("/getUserList")
    public List<User> getUserList(){
        return userService.getUserList();
    }
}
