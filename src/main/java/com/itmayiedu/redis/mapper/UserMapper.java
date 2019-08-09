package com.itmayiedu.redis.mapper;

import com.itmayiedu.redis.entity.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

//@CacheConfig配置缓存基本信息 cacheNames缓存名称
//@Cacheable 该方法查询数据库完毕之后，存入到缓存中

@CacheConfig(cacheNames = {"userCache"}) //配置缓存的基本信息
public interface UserMapper {
    @Select("SELECT ID,NAME,AGE from ehcacheTest where id = #{id}")
    @Cacheable
    User getUser(Long id);
}
