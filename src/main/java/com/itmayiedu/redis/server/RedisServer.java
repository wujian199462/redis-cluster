package com.itmayiedu.redis.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

//springboot 整合redis
@Component
public class RedisServer {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void set(String key,Object object,Long time){

        //让该方法能够支持多种数据类型存放
        if(object instanceof String){ //如果是String类型的数据
            setString(key,object);
        }
        //如果存放Set类型
        if(object instanceof Set){
            setSet(key,object);
        }
        if(object instanceof List){
            setList(key,object);
        }
        //设置有效期
        if(time!=null){
            stringRedisTemplate.expire(key,time, TimeUnit.SECONDS);
        }
    }


   /* public void setString(String key,Object object){
        //开启事务权限
        stringRedisTemplate.setEnableTransactionSupport(true);
        try{
            //开启事务 begin
            stringRedisTemplate.multi();
            String value = (String) object;
            //存放String 类型
            stringRedisTemplate.opsForValue().set(key,value);
            //提交事务
            stringRedisTemplate.exec();
        }catch (Exception e){
            //回滚
            stringRedisTemplate.discard();
        }

    }*/
    public void setString(String key,Object object){
        String value = (String) object;
        //存放String 类型
        stringRedisTemplate.opsForValue().set(key,value);
    }

    public String getString(String key){
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void setSet(String key,Object object){
        Set<String> valueSet = (Set<String>) object;
        for(String string : valueSet){
            stringRedisTemplate.opsForSet().add(key,string);
        }
    }
    private void setList(String key, Object object) {
        List<String> valueList = (List<String>) object;
        stringRedisTemplate.opsForList().leftPushAll(key,valueList);
    }

}
