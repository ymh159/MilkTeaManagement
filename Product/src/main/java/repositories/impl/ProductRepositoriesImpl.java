package repositories.impl;

import entity.ProductEntity;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.util.List;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.ProductRepositories;
import utils.Constants;
import utils.MongoDBClient;

public class ProductRepositoriesImpl implements ProductRepositories {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProductRepositoriesImpl.class);
  private static MongoClient mongoClient;

  public ProductRepositoriesImpl(Vertx vertx) {
    MongoDBClient mongoDBClient = new MongoDBClient();
    mongoClient = mongoDBClient.client(vertx);
  }

  @Override
  public Future<List<ProductEntity>> getProducts() {
    Future<List<ProductEntity>> future = Future.future();

    mongoClient.find(Constants.COLLECTION_USER, new JsonObject(), res -> {
      List<JsonObject> data = res.result();
      List<ProductEntity> entity;
      if (res.succeeded()) {
        entity = data.stream().map(item ->
            item.mapTo(ProductEntity.class)
        ).toList();
        LOGGER.info("getProducts:{}", entity);
        future.complete(entity);
      } else {
        LOGGER.error("getProducts fail");
        future.fail(res.cause());
      }
    });
    return future;
  }

  @Override
  public Future<ProductEntity> findProductById(String id) {
    Future<ProductEntity> future = Future.future();

    mongoClient.findOne(Constants.COLLECTION_USER, new JsonObject().put(Constants._ID, id), null,
        res -> {
          if (res.succeeded()) {
            ProductEntity userEntity = res.result().mapTo(ProductEntity.class);
            LOGGER.info("findProductById:{}", userEntity);
            future.complete(userEntity);
          } else {
            LOGGER.error("findProductById fail");
            future.fail(res.cause());
          }
        });
    return null;
  }

  @Override
  public Future<String> insertProduct(ProductEntity userEntity) {
    Future<String> future = Future.future();
    userEntity.setId(ObjectId.get().toHexString());
    JsonObject query = JsonObject.mapFrom(userEntity);

    mongoClient.insert(Constants.COLLECTION_USER, query, event -> {
      if (event.succeeded()) {
        future.complete(Constants.MESSAGE_INSERT_SUCCESS);
        LOGGER.info("insertProduct:{}", query);
      } else {
        future.fail(event.cause());
        LOGGER.info(Constants.MESSAGE_INSERT_FAIL + " query:{}", query);
      }
    });

    return future;
  }

  @Override
  public Future<String> updateProduct(String id, ProductEntity userEntity) {
    Future<String> future = Future.future();
    JsonObject jsonUpdate = JsonObject.mapFrom(userEntity);
    JsonObject query = new JsonObject().put(Constants._ID,id);
    mongoClient.updateCollection(Constants.COLLECTION_USER, query, jsonUpdate, event -> {
      if (event.succeeded()) {
        future.complete(Constants.MESSAGE_UPDATE_SUCCESS);
        LOGGER.info("updateProduct:{}", query);
      } else {
        future.fail(event.cause());
        LOGGER.info(Constants.MESSAGE_UPDATE_FAIL + " updateProduct:{}", query);
      }
    });

    return future;
  }

  @Override
  public Future<String> deleteProduct(String id) {
    Future<String> future = Future.future();
    mongoClient.findOneAndDelete(Constants.COLLECTION_USER,new JsonObject().put(Constants._ID,id),event -> {
      if (event.succeeded()) {
        future.complete(Constants.MESSAGE_DELETE_SUCCESS);
        LOGGER.info("deleteProduct:{}", id);
      } else {
        future.fail(event.cause());
        LOGGER.info(Constants.MESSAGE_DELETE_FAIL + " deleteProduct:{}", id);
      }
    });
    return future;
  }
}
