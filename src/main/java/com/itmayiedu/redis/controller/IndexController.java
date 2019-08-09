package com.itmayiedu.redis.controller;

import com.itmayiedu.redis.entity.User;
import com.itmayiedu.redis.server.RedisServer;
import com.itmayiedu.redis.server.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @Autowired
    private RedisServer redisServer;
    @Autowired
    private UserService userService;
    @RequestMapping("/setString")
    public String setString(String key,String object){
        redisServer.set(key,object,60l);
        return "success";
    }
    @RequestMapping("getString")
    public String getString(String key){
        return redisServer.getString(key);
    }

    @RequestMapping("/getUserId")
    public User getUserId(Long id){
        return userService.getUser(id);
    }
}
