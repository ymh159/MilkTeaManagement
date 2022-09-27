package repositories;

import entity.UserEntity;
import io.vertx.core.Future;
import java.util.List;

public interface UserRepositories {

  Future<List<UserEntity>> getUsers();

  Future<UserEntity> findUserById(String id);

  Future<String> insertUser(UserEntity userEntity);

  Future<String> updateUser(String id, UserEntity userEntity);

  Future<String> deleteUser(String id);
}
