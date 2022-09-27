package repositories.impl;

import entity.OrderDetailEntity;
import entity.OrderEntity;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.util.List;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.OrderDetailRepositories;
import repositories.OrderRepositories;
import utils.Constants;
import utils.MongoDBClient;

public class OrderDetailRepositoriesImpl implements OrderDetailRepositories {

  private static final Logger LOGGER = LoggerFactory.getLogger(OrderDetailRepositoriesImpl.class);
  private static MongoClient mongoClient;

  public OrderDetailRepositoriesImpl(Vertx vertx) {
    MongoDBClient mongoDBClient = new MongoDBClient();
    mongoClient = mongoDBClient.client(vertx);
  }

  @Override
  public Future<List<OrderDetailEntity>> getOrderDetails() {
    Future<List<OrderDetailEntity>> future = Future.future();

    mongoClient.find(Constants.COLLECTION_USER, new JsonObject(), res -> {
      List<JsonObject> data = res.result();
      List<OrderDetailEntity> entity;
      if (res.succeeded()) {
        entity = data.stream().map(item ->
            item.mapTo(OrderDetailEntity.class)
        ).toList();
        LOGGER.info("getOrderDetails:{}", entity);
        future.complete(entity);
      } else {
        LOGGER.error("getOrderDetails fail");
        future.fail(res.cause());
      }
    });
    return future;
  }

  @Override
  public Future<OrderDetailEntity> findOrderDetailById(String id) {
    Future<OrderDetailEntity> future = Future.future();

    mongoClient.findOne(Constants.COLLECTION_USER, new JsonObject().put(Constants._ID, id), null,
        res -> {
          if (res.succeeded()) {
            OrderDetailEntity userEntity = res.result().mapTo(OrderDetailEntity.class);
            LOGGER.info("findOrderDetailById:{}", userEntity);
            future.complete(userEntity);
          } else {
            LOGGER.error("findOrderDetailById fail");
            future.fail(res.cause());
          }
        });
    return null;
  }

  @Override
  public Future<String> insertOrderDetail(OrderDetailEntity userEntity) {
    Future<String> future = Future.future();
    userEntity.setId(ObjectId.get().toHexString());
    JsonObject query = JsonObject.mapFrom(userEntity);

    mongoClient.insert(Constants.COLLECTION_USER, query, event -> {
      if (event.succeeded()) {
        future.complete(Constants.MESSAGE_INSERT_SUCCESS);
        LOGGER.info("insertOrderDetail:{}", query);
      } else {
        future.fail(event.cause());
        LOGGER.info(Constants.MESSAGE_INSERT_FAIL + " query:{}", query);
      }
    });

    return future;
  }

  @Override
  public Future<String> updateOrderDetail(String id, OrderDetailEntity userEntity) {
    Future<String> future = Future.future();
    JsonObject jsonUpdate = JsonObject.mapFrom(userEntity);
    JsonObject query = new JsonObject().put(Constants._ID, id);
    mongoClient.updateCollection(Constants.COLLECTION_USER, query, jsonUpdate, event -> {
      if (event.succeeded()) {
        future.complete(Constants.MESSAGE_UPDATE_SUCCESS);
        LOGGER.info("updateOrderDetail:{}", query);
      } else {
        future.fail(event.cause());
        LOGGER.info(Constants.MESSAGE_UPDATE_FAIL + " updateOrderDetail:{}", query);
      }
    });

    return future;
  }

  @Override
  public Future<String> deleteOrderDetail(String id) {
    Future<String> future = Future.future();
    mongoClient.findOneAndDelete(Constants.COLLECTION_USER, new JsonObject().put(Constants._ID, id),
        event -> {
          if (event.succeeded()) {
            future.complete(Constants.MESSAGE_DELETE_SUCCESS);
            LOGGER.info("deleteOrderDetail:{}", id);
          } else {
            future.fail(event.cause());
            LOGGER.info(Constants.MESSAGE_DELETE_FAIL + " deleteOrderDetail:{}", id);
          }
        });
    return future;
  }
}
