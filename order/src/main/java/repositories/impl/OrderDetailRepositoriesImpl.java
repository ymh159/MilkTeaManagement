package repositories.impl;

import entity.OrderDetailEntity;
import entity.ProductEntity;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.OrderDetailRepositories;
import utils.Constants;
import utils.MongoDBClient;

public class OrderDetailRepositoriesImpl implements OrderDetailRepositories {

  private static final Logger logger = LoggerFactory.getLogger(OrderDetailRepositoriesImpl.class);
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
        logger.info("getOrderDetails:{}", entity);
        future.complete(entity);
      } else {
        logger.error("getOrderDetails fail");
        future.fail(res.cause());
      }
    });
    return future;
  }

  @Override
  public Future<OrderDetailEntity> findOrderDetailById(String id) {
    Future<OrderDetailEntity> future = Future.future();

    mongoClient.findOne(Constants.COLLECTION_ORDER_DETAIL, new JsonObject().put(Constants._ID, id),
        null,
        res -> {
          if (res.succeeded()) {
            OrderDetailEntity orderDetailEntity = res.result().mapTo(OrderDetailEntity.class);
            logger.info("findOrderDetailById:{}", orderDetailEntity);
            future.complete(orderDetailEntity);
          } else {
            logger.error("findOrderDetailById fail");
            future.fail(res.cause());
          }
        });
    return future;
  }

  @Override
  public Single<String> insertOrderDetail(OrderDetailEntity orderDetailEntity) {
    return Single.create(emitter -> {
      JsonObject query = JsonObject.mapFrom(orderDetailEntity);
      query.getMap().values().removeIf(Objects::isNull);
      mongoClient.insert(Constants.COLLECTION_ORDER_DETAIL, query, res -> {
        if (res.succeeded()) {
          logger.info("insertOrderDetail:{}", query);
          emitter.onSuccess(res.result());
        } else {
          logger.info(Constants.MESSAGE_INSERT_FAIL + " query:{}", query);
          emitter.onError(res.cause());
        }
      });
    });
  }

  @Override
  public Future<Void> updateOrderDetail(String id, OrderDetailEntity orderDetailEntity) {
    Future<Void> future = Future.future();
    JsonObject jsonUpdate = JsonObject.mapFrom(orderDetailEntity);
    JsonObject query = new JsonObject().put(Constants._ID, id);
    jsonUpdate.getMap().values().removeIf(Objects::isNull);
    JsonObject jsonQueryUpdate = new JsonObject().put(Constants.DOCUMENT_SET, jsonUpdate);

    mongoClient.updateCollection(Constants.COLLECTION_ORDER_DETAIL, query, jsonQueryUpdate,
        event -> {
          if (event.succeeded()) {
            future.complete();
            logger.info("updateOrderDetail:{}", query);
          } else {
            future.fail(event.cause());
            logger.info(Constants.MESSAGE_UPDATE_FAIL + " updateOrderDetail:{}", query);
          }
        });

    return future;
  }

  @Override
  public Single<OrderDetailEntity> deleteOrderDetail(String id) {

    return Single.create(emitter -> {
      mongoClient.findOneAndDelete(Constants.COLLECTION_ORDER_DETAIL,
          new JsonObject().put(Constants._ID, id),
          event -> {
            if (event.succeeded()) {
              logger.info("deleteOrderDetail:{}", id);
              emitter.onSuccess(event.result().mapTo(OrderDetailEntity.class));
            } else {
              logger.info(Constants.MESSAGE_DELETE_FAIL + " deleteOrderDetail:{}", id);
              emitter.onError(event.cause());
            }
          });
    });
  }

  @Override
  public Future<List<OrderDetailEntity>> findOrderDetailByOrderId(String order_id) {
    Future<List<OrderDetailEntity>> future = Future.future();
    mongoClient.find(Constants.COLLECTION_ORDER_DETAIL,
        new JsonObject().put(Constants.ORDER_ID, order_id), res -> {
          if (res.succeeded()) {
            if (res.result() != null) {
              List<OrderDetailEntity> orderDetailEntityList = new ArrayList<>();
              orderDetailEntityList = res.result().stream()
                  .map(orderDetail -> orderDetail.mapTo(OrderDetailEntity.class))
                  .collect(Collectors.toList());
              future.complete(orderDetailEntityList);
            } else {
              future.fail(new Exception("Find not found"));
            }
          } else {
            future.fail(res.cause());
          }
        });

    return future;
  }
}
