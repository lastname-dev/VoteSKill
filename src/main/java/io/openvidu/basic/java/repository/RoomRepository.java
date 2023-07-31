package io.openvidu.basic.java.repository;


import io.openvidu.basic.java.Room;
import io.openvidu.java.client.Session;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoomRepository extends CrudRepository<Room, String> {
    public void deleteByName(String name);
    public Room findByName(String name);
}
