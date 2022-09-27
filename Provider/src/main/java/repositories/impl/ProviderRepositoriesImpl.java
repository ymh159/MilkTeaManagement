package repositories.impl;

import entity.ProviderEntity;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.util.List;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.ProviderRepositories;
import utils.Constants;
import utils.MongoDBClient;

public class ProviderRepositoriesImpl implements ProviderRepositories {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProviderRepositoriesImpl.class);
  private static MongoClient mongoClient;

  public ProviderRepositoriesImpl(Vertx vertx) {
    MongoDBClient mongoDBClient = new MongoDBClient();
    mongoClient = mongoDBClient.client(vertx);
  }

  @Override
  public Future<List<ProviderEntity>> getProviders() {
    Future<List<ProviderEntity>> future = Future.future();

    mongoClient.find(Constants.COLLECTION_USER, new JsonObject(), res -> {
      List<JsonObject> data = res.result();
      List<ProviderEntity> entity;
      if (res.succeeded()) {
        entity = data.stream().map(item ->
            item.mapTo(ProviderEntity.class)
        ).toList();
        LOGGER.info("getProviders:{}", entity);
        future.complete(entity);
      } else {
        LOGGER.error("getProviders fail");
        future.fail(res.cause());
      }
    });
    return future;
  }

  @Override
  public Future<ProviderEntity> findProviderById(String id) {
    Future<ProviderEntity> future = Future.future();

    mongoClient.findOne(Constants.COLLECTION_USER, new JsonObject().put(Constants._ID, id), null,
        res -> {
          if (res.succeeded()) {
            ProviderEntity userEntity = res.result().mapTo(ProviderEntity.class);
            LOGGER.info("findProviderById:{}", userEntity);
            future.complete(userEntity);
          } else {
            LOGGER.error("findProviderById fail");
            future.fail(res.cause());
          }
        });
    return null;
  }

  @Override
  public Future<String> insertProvider(ProviderEntity userEntity) {
    Future<String> future = Future.future();
    userEntity.setId(ObjectId.get().toHexString());
    JsonObject query = JsonObject.mapFrom(userEntity);

    mongoClient.insert(Constants.COLLECTION_USER, query, event -> {
      if (event.succeeded()) {
        future.complete(Constants.MESSAGE_INSERT_SUCCESS);
        LOGGER.info("insertProvider:{}", query);
      } else {
        future.fail(event.cause());
        LOGGER.info(Constants.MESSAGE_INSERT_FAIL + " query:{}", query);
      }
    });

    return future;
  }

  @Override
  public Future<String> updateProvider(String id, ProviderEntity userEntity) {
    Future<String> future = Future.future();
    JsonObject jsonUpdate = JsonObject.mapFrom(userEntity);
    JsonObject query = new JsonObject().put(Constants._ID,id);
    mongoClient.updateCollection(Constants.COLLECTION_USER, query, jsonUpdate, event -> {
      if (event.succeeded()) {
        future.complete(Constants.MESSAGE_UPDATE_SUCCESS);
        LOGGER.info("updateProvider:{}", query);
      } else {
        future.fail(event.cause());
        LOGGER.info(Constants.MESSAGE_UPDATE_FAIL + " updateProvider:{}", query);
      }
    });

    return future;
  }

  @Override
  public Future<String> deleteProvider(String id) {
    Future<String> future = Future.future();
    mongoClient.findOneAndDelete(Constants.COLLECTION_USER,new JsonObject().put(Constants._ID,id),event -> {
      if (event.succeeded()) {
        future.complete(Constants.MESSAGE_DELETE_SUCCESS);
        LOGGER.info("deleteProvider:{}", id);
      } else {
        future.fail(event.cause());
        LOGGER.info(Constants.MESSAGE_DELETE_FAIL + " deleteProvider:{}", id);
      }
    });
    return future;
  }
}
