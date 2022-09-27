package repositories.impl;

import entity.ProductCategoryEntity;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.util.List;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.ProductCategoryRepositories;
import utils.Constants;
import utils.MongoDBClient;

public class ProductCategoryRepositoriesImpl implements ProductCategoryRepositories {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProductCategoryRepositoriesImpl.class);
  private static MongoClient mongoClient;

  public ProductCategoryRepositoriesImpl(Vertx vertx) {
    MongoDBClient mongoDBClient = new MongoDBClient();
    mongoClient = mongoDBClient.client(vertx);
  }

  @Override
  public Future<List<ProductCategoryEntity>> getProductCategorys() {
    Future<List<ProductCategoryEntity>> future = Future.future();

    mongoClient.find(Constants.COLLECTION_USER, new JsonObject(), res -> {
      List<JsonObject> data = res.result();
      List<ProductCategoryEntity> entity;
      if (res.succeeded()) {
        entity = data.stream().map(item ->
            item.mapTo(ProductCategoryEntity.class)
        ).toList();
        LOGGER.info("getProductCategorys:{}", entity);
        future.complete(entity);
      } else {
        LOGGER.error("getProductCategorys fail");
        future.fail(res.cause());
      }
    });
    return future;
  }

  @Override
  public Future<ProductCategoryEntity> findProductCategoryById(String id) {
    Future<ProductCategoryEntity> future = Future.future();

    mongoClient.findOne(Constants.COLLECTION_USER, new JsonObject().put(Constants._ID, id), null,
        res -> {
          if (res.succeeded()) {
            ProductCategoryEntity userEntity = res.result().mapTo(ProductCategoryEntity.class);
            LOGGER.info("findProductCategoryById:{}", userEntity);
            future.complete(userEntity);
          } else {
            LOGGER.error("findProductCategoryById fail");
            future.fail(res.cause());
          }
        });
    return null;
  }

  @Override
  public Future<String> insertProductCategory(ProductCategoryEntity userEntity) {
    Future<String> future = Future.future();
    userEntity.setId(ObjectId.get().toHexString());
    JsonObject query = JsonObject.mapFrom(userEntity);

    mongoClient.insert(Constants.COLLECTION_USER, query, event -> {
      if (event.succeeded()) {
        future.complete(Constants.MESSAGE_INSERT_SUCCESS);
        LOGGER.info("insertProductCategory:{}", query);
      } else {
        future.fail(event.cause());
        LOGGER.info(Constants.MESSAGE_INSERT_FAIL + " query:{}", query);
      }
    });

    return future;
  }

  @Override
  public Future<String> updateProductCategory(String id, ProductCategoryEntity userEntity) {
    Future<String> future = Future.future();
    JsonObject jsonUpdate = JsonObject.mapFrom(userEntity);
    JsonObject query = new JsonObject().put(Constants._ID,id);
    mongoClient.updateCollection(Constants.COLLECTION_USER, query, jsonUpdate, event -> {
      if (event.succeeded()) {
        future.complete(Constants.MESSAGE_UPDATE_SUCCESS);
        LOGGER.info("updateProductCategory:{}", query);
      } else {
        future.fail(event.cause());
        LOGGER.info(Constants.MESSAGE_UPDATE_FAIL + " updateProductCategory:{}", query);
      }
    });

    return future;
  }

  @Override
  public Future<String> deleteProductCategory(String id) {
    Future<String> future = Future.future();
    mongoClient.findOneAndDelete(Constants.COLLECTION_USER,new JsonObject().put(Constants._ID,id),event -> {
      if (event.succeeded()) {
        future.complete(Constants.MESSAGE_DELETE_SUCCESS);
        LOGGER.info("deleteProductCategory:{}", id);
      } else {
        future.fail(event.cause());
        LOGGER.info(Constants.MESSAGE_DELETE_FAIL + " deleteProductCategory:{}", id);
      }
    });
    return future;
  }
}
