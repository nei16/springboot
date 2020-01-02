package com.nei.controller;

import com.nei.common.redis.cache.RedisCache;
import com.nei.entity.second.User;
import com.nei.mapper.second.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by LiBo on 2019-11-27
 */
@RedisCache
@RestController
@RequestMapping("redisCache")
public class RedisCacheController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("selectByPrimaryKey")
    public User selectByPrimaryKey() {
        return userMapper.selectByPrimaryKey(1);
    }

}
