package io.openvidu.basic.java.utill;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class Utill {

  private RedisTemplate redisTemplate;
  @Autowired
  public Utill(RedisTemplate redisTemplate){
    this.redisTemplate=redisTemplate;
  }
  public <T> T getData(String key,Class<T> classType) throws Exception{
    String jsonResult = (String) redisTemplate.opsForHash().get(key,)
  }
}
