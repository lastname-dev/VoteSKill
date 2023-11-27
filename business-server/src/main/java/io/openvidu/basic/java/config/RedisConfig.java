package io.openvidu.basic.java.config;

import io.openvidu.basic.java.room.domain.Room;
import io.openvidu.basic.java.game.domain.GameInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class RedisConfig {

  @Value("${spring.redis.host}")
  private String host;

  @Value("${spring.redis.port}")
  private int port;

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    return new LettuceConnectionFactory(host, port);
  }

//  @Bean
//  public RedisTemplate<?, ?> redisTemplate() {
//    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//    redisTemplate.setConnectionFactory(redisConnectionFactory());
//    redisTemplate.setKeySerializer(new StringRedisSerializer());
//    redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//    redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
//    redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
//    return redisTemplate;
//  }
//  @Bean
//  public RedisTemplate<?, ?> redisTemplateTest() {
//
//    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//    redisTemplate.setConnectionFactory(redisConnectionFactory());
//    redisTemplate.setKeySerializer(new StringRedisSerializer());
//    redisTemplate.setValueSerializer(new StringRedisSerializer());
//    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//    redisTemplate.setHashValueSerializer(new StringRedisSerializer());
//
//    return redisTemplate;
//  }



  @Bean
  public StringRedisTemplate stringRedisTemplate(){
    StringRedisTemplate stringRedisTemplate=new StringRedisTemplate();
    stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
    stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
    stringRedisTemplate.setConnectionFactory(redisConnectionFactory());
    return stringRedisTemplate;
  }
  @Bean
  public RedisTemplate<?, ?> redisTemplate() {

    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory());
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());

    redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(GameInfo.class));
    redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer(GameInfo.class));
//    redisTemplate.setDefaultSerializer(new Jackson2JsonRedisSerializer(GameInfo.class));
    return redisTemplate;
  }
  @Bean
  public RedisTemplate<?, ?> roomRedisTemplate() {

    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory());
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());

    redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(Room.class));
    redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer(Room.class));
//    redisTemplate.setDefaultSerializer(new Jackson2JsonRedisSerializer(GameInfo.class));
    return redisTemplate;
  }


}
