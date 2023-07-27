package io.openvidu.basic.java.config;

import io.openvidu.basic.java.Room;
import io.openvidu.basic.java.repository.RoomRepository;
import io.openvidu.java.client.Session;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class SessionConfig {

  @Bean
  public RoomRepository roomRepository(){
    return new RoomRepository() {

      @Override
      public <S extends Room> S save(S entity) {
        return null;
      }

      @Override
      public <S extends Room> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
      }

      @Override
      public Optional<Room> findById(String s) {
        return Optional.empty();
      }

      @Override
      public boolean existsById(String s) {
        return false;
      }

      @Override
      public Iterable<Room> findAll() {
        return null;
      }

      @Override
      public Iterable<Room> findAllById(Iterable<String> strings) {
        return null;
      }

      @Override
      public long count() {
        return 0;
      }

      @Override
      public void deleteById(String s) {

      }

      @Override
      public void delete(Room entity) {

      }

      @Override
      public void deleteAllById(Iterable<? extends String> strings) {

      }

      @Override
      public void deleteAll(Iterable<? extends Room> entities) {

      }

      @Override
      public void deleteAll() {

      }
    };
  }
}
