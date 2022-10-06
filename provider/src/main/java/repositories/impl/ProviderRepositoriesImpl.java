package repositories.impl;

import entity.ProviderEntity;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.util.List;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.ProviderRepositories;
import utils.Constants;
import utils.MongoDBClient;

public class ProviderRepositoriesImpl implements ProviderRepositories {

  private static final Logger logger = LoggerFactory.getLogger(ProviderRepositoriesImpl.class);
  private static MongoClient mongoClient;

  public ProviderRepositoriesImpl(Vertx vertx) {
    mongoClient = MongoDBClient.client(vertx);
  }

  @Override
  public Future<List<ProviderEntity>> getProviders() {
    Future<List<ProviderEntity>> future = Future.future();

    mongoClient.find(Constants.COLLECTION_PROVIDER, new JsonObject(), res -> {
      List<JsonObject> data = res.result();
      List<ProviderEntity> entity;
      if (res.succeeded()) {
        entity = data.stream().map(item ->
            item.mapTo(ProviderEntity.class)
        ).toList();
        logger.info("getProviders:{}", entity);
        future.complete(entity);
      } else {
        logger.error("getProviders fail");
        future.fail(res.cause());
      }
    });
    return future;
  }

  @Override
  public Future<ProviderEntity> findProviderById(String id) {
    Future<ProviderEntity> future = Future.future();

    mongoClient.findOne(Constants.COLLECTION_PROVIDER, new JsonObject().put(Constants._ID, id),
        null,
        res -> {
          if (res.succeeded()) {
            ProviderEntity providerEntity = res.result().mapTo(ProviderEntity.class);
            logger.info("findProviderById:{}", providerEntity);
            future.complete(providerEntity);
          } else {
            logger.error("findProviderById fail");
            future.fail(res.cause());
          }
        });
    return future;
  }

  @Override
  public Future<Void> insertProvider(ProviderEntity providerEntity) {
    Future<Void> future = Future.future();
    providerEntity.setId(ObjectId.get().toHexString());
    JsonObject query = JsonObject.mapFrom(providerEntity);

    mongoClient.insert(Constants.COLLECTION_PROVIDER, query, event -> {
      if (event.succeeded()) {
        future.complete();
        logger.info("insertProvider:{}", query);
      } else {
        future.fail(event.cause());
        logger.info(Constants.MESSAGE_INSERT_FAIL + " query:{}", query);
      }
    });

    return future;
  }

  @Override
  public Future<Void> updateProvider(String id, ProviderEntity providerEntity) {
    Future<Void> future = Future.future();
    JsonObject jsonUpdate = JsonObject.mapFrom(providerEntity);
    JsonObject query = new JsonObject().put(Constants._ID, id);
    jsonUpdate.getMap().values().removeIf(Objects::isNull);
    JsonObject jsonQueryUpdate = new JsonObject().put(Constants.DOCUMENT_SET, jsonUpdate);

    mongoClient.updateCollection(Constants.COLLECTION_PROVIDER, query, jsonQueryUpdate, event -> {
      if (event.succeeded()) {
        future.complete();
        logger.info("updateProvider:{}", query);
      } else {
        future.fail(event.cause());
        logger.info(Constants.MESSAGE_UPDATE_FAIL + " updateProvider:{}", query);
      }
    });

    return future;
  }

  @Override
  public Future<Void> deleteProvider(String id) {
    Future<Void> future = Future.future();
    mongoClient.findOneAndDelete(Constants.COLLECTION_PROVIDER,
        new JsonObject().put(Constants._ID, id), event -> {
          if (event.succeeded()) {
            future.complete();
            logger.info("deleteProvider:{}", id);
          } else {
            future.fail(event.cause());
            logger.info(Constants.MESSAGE_DELETE_FAIL + " deleteProvider:{}", id);
          }
        });
    return future;
  }
}
