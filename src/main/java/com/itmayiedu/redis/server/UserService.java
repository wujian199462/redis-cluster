package com.itmayiedu.redis.server;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.itmayiedu.redis.entity.User;
import com.itmayiedu.redis.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserService {

    @Autowired
    private EhCacheUtils ehCacheUtils;
    @Autowired
    private RedisServer redisServer;
    @Autowired
    private UserMapper userMapper;
    private String cacheName = "userCache";

    public User getUser(Long id){
        //1.先查询一级缓存 key是以当前的类名+方法名称+id+参数值FD
        String key = this.getClass().getName()+"-"+Thread.currentThread().getStackTrace()[1].getMethodName()
                +"-id:"+id;
        //1.1查询一级缓存数据有对应的值存在，如果有值直接返回
        User ehuser = (User) ehCacheUtils.get(cacheName,key);
        if(ehuser != null){
            System.out.println("key:"+key+",直接从一级缓存获取数据：user:"+ehuser.toString());
            return ehuser;
        }
        //1.1查询一级缓存数据没有有对应的值存在，直接查询redis redis中如何存放对象呢？json格式
        //2.查询二级缓存
        String userJson = redisServer.getString(key);
        //如果redis缓存中有这个对应的值，修改一级缓存
        if(!StringUtils.isEmpty(userJson)){
            JSONObject jsonObject= new JSONObject();
            User resultUser = jsonObject.parseObject(userJson, User.class);
            //存放一级缓存
            ehCacheUtils.put(cacheName,key,resultUser);
            return resultUser;
        }
        //3.查询db
        User user = userMapper.getUser(id);
        if(user == null){
            return null;
        }
        //存放二级缓存redis
        redisServer.setString(key,new JSONObject().toJSONString(user));
        //存放一级缓存
        ehCacheUtils.put(cacheName,key,user);
        //一级缓存的有效时间减去二级缓存执行代码时间
        return user;
    }
}
