package com.nei.controller;

import com.nei.common.util.MapUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public HashMap<String, String> hello(){
        return MapUtil.newHashMap("msg", "hello jwt");
    }

    @GetMapping("/admin")
    public HashMap<String, String> admin(){
        return MapUtil.newHashMap("msg", "hello admin");
    }

}
