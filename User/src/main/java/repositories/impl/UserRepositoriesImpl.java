package repositories.impl;

import entity.UserEntity;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.util.List;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.UserRepositories;
import utils.Constants;
import utils.MongoDBClient;

public class UserRepositoriesImpl implements UserRepositories {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoriesImpl.class);
  private static MongoClient mongoClient;

  public UserRepositoriesImpl(Vertx vertx) {
    MongoDBClient mongoDBClient = new MongoDBClient();
    mongoClient = mongoDBClient.client(vertx);
  }

  @Override
  public Future<List<UserEntity>> getUsers() {
    Future<List<UserEntity>> future = Future.future();

    mongoClient.find(Constants.COLLECTION_USER, new JsonObject(), res -> {
      List<JsonObject> data = res.result();
      List<UserEntity> entity;
      if (res.succeeded()) {
        entity = data.stream().map(item ->
            item.mapTo(UserEntity.class)
        ).toList();
        LOGGER.info("getUsers:{}", entity);
        future.complete(entity);
      } else {
        LOGGER.error("getUsers fail");
        future.fail(res.cause());
      }
    });
    return future;
  }

  @Override
  public Future<UserEntity> findUserById(String id) {
    Future<UserEntity> future = Future.future();

    mongoClient.findOne(Constants.COLLECTION_USER, new JsonObject().put(Constants._ID, id), null,
        res -> {
          if (res.succeeded()) {
            UserEntity userEntity = res.result().mapTo(UserEntity.class);
            LOGGER.info("findUserById:{}", userEntity);
            future.complete(userEntity);
          } else {
            LOGGER.error("findUserById fail");
            future.fail(res.cause());
          }
        });
    return null;
  }

  @Override
  public Future<String> insertUser(UserEntity userEntity) {
    Future<String> future = Future.future();
    userEntity.setId(ObjectId.get().toHexString());
    JsonObject query = JsonObject.mapFrom(userEntity);

    mongoClient.insert(Constants.COLLECTION_USER, query, event -> {
      if (event.succeeded()) {
        future.complete(Constants.MESSAGE_INSERT_SUCCESS);
        LOGGER.info("insertUser:{}", query);
      } else {
        future.fail(event.cause());
        LOGGER.info(Constants.MESSAGE_INSERT_FAIL + " query:{}", query);
      }
    });

    return future;
  }

  @Override
  public Future<String> updateUser(String id, UserEntity userEntity) {
    Future<String> future = Future.future();
    JsonObject jsonUpdate = JsonObject.mapFrom(userEntity);
    JsonObject query = new JsonObject().put(Constants._ID,id);
    mongoClient.updateCollection(Constants.COLLECTION_USER, query, jsonUpdate, event -> {
      if (event.succeeded()) {
        future.complete(Constants.MESSAGE_UPDATE_SUCCESS);
        LOGGER.info("updateUser:{}", query);
      } else {
        future.fail(event.cause());
        LOGGER.info(Constants.MESSAGE_UPDATE_FAIL + " updateUser:{}", query);
      }
    });

    return future;
  }

  @Override
  public Future<String> deleteUser(String id) {
    Future<String> future = Future.future();
    mongoClient.findOneAndDelete(Constants.COLLECTION_USER,new JsonObject().put(Constants._ID,id),event -> {
      if (event.succeeded()) {
        future.complete(Constants.MESSAGE_DELETE_SUCCESS);
        LOGGER.info("deleteUser:{}", id);
      } else {
        future.fail(event.cause());
        LOGGER.info(Constants.MESSAGE_DELETE_FAIL + " deleteUser:{}", id);
      }
    });
    return future;
  }
}
