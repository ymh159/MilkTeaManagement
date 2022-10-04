package repositories.impl;

import entity.OrderDetailEntity;
import entity.OrderEntity;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.util.List;
import java.util.Objects;
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
    mongoClient = MongoDBClient.client(vertx);
  }

  @Override
  public Future<List<OrderDetailEntity>> getOrderDetails() {
    Future<List<OrderDetailEntity>> future = Future.future();

    mongoClient.find(Constants.COLLECTION_ORDER_DETAIL, new JsonObject(), res -> {
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

    mongoClient.findOne(Constants.COLLECTION_ORDER_DETAIL, new JsonObject().put(Constants._ID, id), null,
        res -> {
          if (res.succeeded()) {
            OrderDetailEntity orderDetailEntity = res.result().mapTo(OrderDetailEntity.class);
            LOGGER.info("findOrderDetailById:{}", orderDetailEntity);
            future.complete(orderDetailEntity);
          } else {
            LOGGER.error("findOrderDetailById fail");
            future.fail(res.cause());
          }
        });
    return null;
  }

  @Override
  public Future<Void> insertOrderDetail(OrderDetailEntity orderDetailEntity) {
    Future<Void> future = Future.future();
    orderDetailEntity.setId(ObjectId.get().toHexString());
    JsonObject query = JsonObject.mapFrom(orderDetailEntity);

    mongoClient.insert(Constants.COLLECTION_ORDER_DETAIL, query, event -> {
      if (event.succeeded()) {
        future.complete();
        LOGGER.info("insertOrderDetail:{}", query);
      } else {
        future.fail(event.cause());
        LOGGER.info(Constants.MESSAGE_INSERT_FAIL + " query:{}", query);
      }
    });

    return future;
  }

  @Override
  public Future<Void> updateOrderDetail(String id, OrderDetailEntity orderDetailEntity) {
    Future<Void> future = Future.future();
    JsonObject jsonUpdate = JsonObject.mapFrom(orderDetailEntity);
    JsonObject query = new JsonObject().put(Constants._ID, id);
    jsonUpdate.getMap().values().removeIf(Objects::isNull);
    JsonObject jsonQueryUpdate = new JsonObject().put(Constants.DOCUMENT_SET,jsonUpdate);

    mongoClient.updateCollection(Constants.COLLECTION_ORDER_DETAIL, query, jsonQueryUpdate, event -> {
      if (event.succeeded()) {
        future.complete();
        LOGGER.info("updateOrderDetail:{}", query);
      } else {
        future.fail(event.cause());
        LOGGER.info(Constants.MESSAGE_UPDATE_FAIL + " updateOrderDetail:{}", query);
      }
    });

    return future;
  }

  @Override
  public Future<Void> deleteOrderDetail(String id) {
    Future<Void> future = Future.future();
    mongoClient.findOneAndDelete(Constants.COLLECTION_ORDER_DETAIL, new JsonObject().put(Constants._ID, id),
        event -> {
          if (event.succeeded()) {
            future.complete();
            LOGGER.info("deleteOrderDetail:{}", id);
          } else {
            future.fail(event.cause());
            LOGGER.info(Constants.MESSAGE_DELETE_FAIL + " deleteOrderDetail:{}", id);
          }
        });
    return future;
  }
}
