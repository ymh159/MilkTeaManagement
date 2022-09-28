package repositories;

import entity.UserEntity;
import io.vertx.core.Future;
import java.util.List;

public interface UserRepositories {

  Future<List<UserEntity>> getUsers();

  Future<UserEntity> findUserById(String id);

  Future<Void> insertUser(UserEntity userEntity);

  Future<Void> updateUser(String id, UserEntity userEntity);

  Future<Void> deleteUser(String id);
}
