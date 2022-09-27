package repositories.impl;

import entity.OrderEntity;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.util.List;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.OrderRepositories;
import utils.Constants;
import utils.MongoDBClient;

public class OrderRepositoriesImpl implements OrderRepositories {

  private static final Logger LOGGER = LoggerFactory.getLogger(OrderRepositoriesImpl.class);
  private static MongoClient mongoClient;

  public OrderRepositoriesImpl(Vertx vertx) {
    MongoDBClient mongoDBClient = new MongoDBClient();
    mongoClient = mongoDBClient.client(vertx);
  }

  @Override
  public Future<List<OrderEntity>> getOrders() {
    Future<List<OrderEntity>> future = Future.future();

    mongoClient.find(Constants.COLLECTION_USER, new JsonObject(), res -> {
      List<JsonObject> data = res.result();
      List<OrderEntity> entity;
      if (res.succeeded()) {
        entity = data.stream().map(item ->
            item.mapTo(OrderEntity.class)
        ).toList();
        LOGGER.info("getOrders:{}", entity);
        future.complete(entity);
      } else {
        LOGGER.error("getOrders fail");
        future.fail(res.cause());
      }
    });
    return future;
  }

  @Override
  public Future<OrderEntity> findOrderById(String id) {
    Future<OrderEntity> future = Future.future();

    mongoClient.findOne(Constants.COLLECTION_USER, new JsonObject().put(Constants._ID, id), null,
        res -> {
          if (res.succeeded()) {
            OrderEntity userEntity = res.result().mapTo(OrderEntity.class);
            LOGGER.info("findOrderById:{}", userEntity);
            future.complete(userEntity);
          } else {
            LOGGER.error("findOrderById fail");
            future.fail(res.cause());
          }
        });
    return null;
  }

  @Override
  public Future<String> insertOrder(OrderEntity userEntity) {
    Future<String> future = Future.future();
    userEntity.setId(ObjectId.get().toHexString());
    JsonObject query = JsonObject.mapFrom(userEntity);

    mongoClient.insert(Constants.COLLECTION_USER, query, event -> {
      if (event.succeeded()) {
        future.complete(Constants.MESSAGE_INSERT_SUCCESS);
        LOGGER.info("insertOrder:{}", query);
      } else {
        future.fail(event.cause());
        LOGGER.info(Constants.MESSAGE_INSERT_FAIL + " query:{}", query);
      }
    });

    return future;
  }

  @Override
  public Future<String> updateOrder(String id, OrderEntity userEntity) {
    Future<String> future = Future.future();
    JsonObject jsonUpdate = JsonObject.mapFrom(userEntity);
    JsonObject query = new JsonObject().put(Constants._ID,id);
    mongoClient.updateCollection(Constants.COLLECTION_USER, query, jsonUpdate, event -> {
      if (event.succeeded()) {
        future.complete(Constants.MESSAGE_UPDATE_SUCCESS);
        LOGGER.info("updateOrder:{}", query);
      } else {
        future.fail(event.cause());
        LOGGER.info(Constants.MESSAGE_UPDATE_FAIL + " updateOrder:{}", query);
      }
    });

    return future;
  }

  @Override
  public Future<String> deleteOrder(String id) {
    Future<String> future = Future.future();
    mongoClient.findOneAndDelete(Constants.COLLECTION_USER,new JsonObject().put(Constants._ID,id),event -> {
      if (event.succeeded()) {
        future.complete(Constants.MESSAGE_DELETE_SUCCESS);
        LOGGER.info("deleteOrder:{}", id);
      } else {
        future.fail(event.cause());
        LOGGER.info(Constants.MESSAGE_DELETE_FAIL + " deleteOrder:{}", id);
      }
    });
    return future;
  }
}
