package repositories.impl;

import com.mongodb.MongoCursorNotFoundException;
import entity.UserEntity;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.util.List;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.UserRepositories;
import utils.Constants;
import utils.MongoDBClient;

public class UserRepositoriesImpl implements UserRepositories {

  private static final Logger logger = LoggerFactory.getLogger(UserRepositoriesImpl.class);
  private static MongoClient mongoClient;

  public UserRepositoriesImpl(Vertx vertx) {
    mongoClient = MongoDBClient.client(vertx);
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
        logger.info("getUsers:{}", entity);
        future.complete(entity);
      } else {
        logger.error("getUsers fail");
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
            if (res.result() != null) {
              UserEntity userEntity = res.result().mapTo(UserEntity.class);
              logger.info("findUserById:{}", userEntity);
              future.complete(userEntity);
            } else {
              logger.info("Find not found user_id:{}", id);
              future.fail(new NullPointerException("Find not found user_id:" + id));
            }
          } else {
            logger.info("findUserById fail");
            future.fail(res.cause());
          }
        });
    return future;
  }

  @Override
  public Future<Void> insertUser(UserEntity userEntity) {
    Future<Void> future = Future.future();
    userEntity.setId(ObjectId.get().toHexString());
    JsonObject query = JsonObject.mapFrom(userEntity);

    mongoClient.insert(Constants.COLLECTION_USER, query, event -> {
      if (event.succeeded()) {
        future.complete();
        logger.info("insertUser:{}", query);
      } else {
        future.fail(event.cause());
        logger.info(Constants.MESSAGE_INSERT_FAIL + " query:{}", query);
      }
    });

    return future;
  }

  @Override
  public Future<Void> updateUser(String id, UserEntity userEntity) {
    Future<Void> future = Future.future();
    JsonObject jsonUpdate = JsonObject.mapFrom(userEntity);
    JsonObject query = new JsonObject().put(Constants._ID, id);
    jsonUpdate.getMap().values().removeIf(Objects::isNull);
    JsonObject jsonQueryUpdate = new JsonObject().put(Constants.DOCUMENT_SET, jsonUpdate);
    mongoClient.updateCollection(Constants.COLLECTION_USER, query, jsonQueryUpdate, event -> {
      if (event.succeeded()) {
        if (event.result().getDocMatched() > 0) {
          future.complete();
          logger.info("updateUser:{}", query);
        } else {
          future.fail(new IllegalArgumentException("Update user fail. Find user not found"));
        }
      } else {
        future.fail(event.cause());
        logger.info(Constants.MESSAGE_UPDATE_FAIL + " updateUser:{}", query);
      }
    });

    return future;
  }

  @Override
  public Future<Void> deleteUser(String id) {
    Future<Void> future = Future.future();
    mongoClient.findOneAndDelete(Constants.COLLECTION_USER, new JsonObject().put(Constants._ID, id),
        event -> {
          if (event.succeeded()) {
            future.complete();
            logger.info("deleteUser:{}", id);
          } else {
            future.fail(event.cause());
            logger.info(Constants.MESSAGE_DELETE_FAIL + " deleteUser:{}", id);
          }
        });
    return future;
  }
}
