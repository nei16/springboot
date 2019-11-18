package com.nei.controller;

import com.nei.common.util.MapUtil;
import com.nei.entity.first.Template;
import com.nei.entity.second.User;
import com.nei.mapper.first.TemplateMapper;
import com.nei.mapper.second.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("demo")
public class DemoController {

//    @ApolloConfig
//    private Config config;

    @Autowired
    private TemplateMapper templateMapper;
    @Autowired
    private UserMapper userMapper;

    /*@GetMapping("getApolloConfig")
    public HashMap<String, String> getConfig(String key) {
        String property = config.getProperty(key, "");
        return MapUtil.newHashMap("property", property);
    }*/

    @GetMapping("template")
    public Template template(Long id) {
        return templateMapper.selectByPrimaryKey(id);
    }

    @GetMapping("user")
    public User user(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @GetMapping("save")
    public HashMap<String, Integer> save() {
        User user = new User();
        user.setName("test");
        int row = userMapper.insertSelective(user);
        return MapUtil.newHashMap("row", row);
    }

    @GetMapping("hello")
    public HashMap<String, String> hello() {
        log.info("hello~");
        return MapUtil.newHashMap("msg", "Hello, Spring Boot!");
    }

    @PostMapping("post")
    public void post(@RequestBody Template template){
        log.info("{}", template);
    }

    @PutMapping("put")
    public void put(@RequestBody Template template){
        log.info("{}", template);
    }

    @DeleteMapping("{id}")
    public void put(@PathVariable Long id){
        log.info("{}", id);
    }

}
