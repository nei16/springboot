package com.nei.controller;

import com.nei.entity.first.Template;
import com.nei.entity.second.User;
import com.nei.mapper.first.TemplateMapper;
import com.nei.mapper.second.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("demo")
public class DemoController {

    @Autowired
    private TemplateMapper templateMapper;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("template")
    public Template template(Long id) {
        return templateMapper.selectByPrimaryKey(id);
    }

    @GetMapping("user")
    public User user(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @GetMapping("save")
    public int save() {
        User user = new User();
        user.setName("test");
        return userMapper.insertSelective(user);
    }

    @GetMapping("hello")
    public String hello() {
        log.info("hello~");
        return "Hello, Spring Boot!";
    }

}
