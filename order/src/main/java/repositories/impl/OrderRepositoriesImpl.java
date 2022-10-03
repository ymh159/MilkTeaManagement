package repositories.impl;

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
import repositories.OrderRepositories;
import utils.Constants;
import utils.MongoDBClient;

public class OrderRepositoriesImpl implements OrderRepositories {

  private static final Logger LOGGER = LoggerFactory.getLogger(OrderRepositoriesImpl.class);
  private static MongoClient mongoClient;

  public OrderRepositoriesImpl(Vertx vertx) {
    mongoClient = MongoDBClient.client(vertx);
  }

  @Override
  public Future<List<OrderEntity>> getOrders() {
    Future<List<OrderEntity>> future = Future.future();

    mongoClient.find(Constants.COLLECTION_ORDER, new JsonObject(), res -> {
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

    mongoClient.findOne(Constants.COLLECTION_ORDER, new JsonObject().put(Constants._ID, id), null,
        res -> {
          if (res.succeeded()) {
            OrderEntity orderEntity = res.result().mapTo(OrderEntity.class);
            LOGGER.info("findOrderById:{}", orderEntity);
            future.complete(orderEntity);
          } else {
            LOGGER.error("findOrderById fail");
            future.fail(res.cause());
          }
        });

    return future;
  }

  @Override
  public Future<String> insertOrder(OrderEntity orderEntity) {
    Future<String> future = Future.future();
    orderEntity.setId(ObjectId.get().toHexString());
    JsonObject query = JsonObject.mapFrom(orderEntity);
    //query.put(Constants.DATE_ORDER, new JsonObject().put("$date",query.getValue(Constants.DATE_ORDER)));

    mongoClient.insert(Constants.COLLECTION_ORDER, query, event -> {
      if (event.succeeded()) {
        future.complete(orderEntity.getId());
        LOGGER.info("insertOrder:{}", query);
      } else {
        future.fail(event.cause());
        LOGGER.info(Constants.MESSAGE_INSERT_FAIL + " insertOrder:{}", query);
      }
    });

    return future;
  }

  @Override
  public Future<Void> updateOrder(String id, OrderEntity orderEntity) {
    Future<Void> future = Future.future();
    JsonObject jsonUpdate = JsonObject.mapFrom(orderEntity);
    JsonObject query = new JsonObject().put(Constants._ID,id);
    jsonUpdate.getMap().values().removeIf(Objects::isNull);
    JsonObject jsonQueryUpdate = new JsonObject().put(Constants.DOCUMENT_SET,jsonUpdate);

    mongoClient.updateCollection(Constants.COLLECTION_ORDER, query, jsonQueryUpdate, event -> {
      if (event.succeeded()) {
        future.complete();
        LOGGER.info("updateOrder:{}", query);
      } else {
        future.fail(event.cause());
        LOGGER.info(Constants.MESSAGE_UPDATE_FAIL + " updateOrder:{}", query);
      }
    });

    return future;
  }

  @Override
  public Future<Void> deleteOrder(String id) {
    Future<Void> future = Future.future();
    mongoClient.findOneAndDelete(Constants.COLLECTION_ORDER,new JsonObject().put(Constants._ID,id),event -> {
      if (event.succeeded()) {
        future.complete();
        LOGGER.info("deleteOrder:{}", id);
      } else {
        future.fail(event.cause());
        LOGGER.info(Constants.MESSAGE_DELETE_FAIL + " deleteOrder:{}", id);
      }
    });

    return future;
  }
}
