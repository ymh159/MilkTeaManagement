package repositories.impl;

import entity.ProductCategoryEntity;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.util.List;
import java.util.Objects;
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
    mongoClient = MongoDBClient.client(vertx);
  }

  @Override
  public Future<List<ProductCategoryEntity>> getProductCategorys() {
    Future<List<ProductCategoryEntity>> future = Future.future();

    mongoClient.find(Constants.COLLECTION_PRODUCT_CATEGORY, new JsonObject(), res -> {
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

    mongoClient.findOne(Constants.COLLECTION_PRODUCT_CATEGORY, new JsonObject().put(Constants._ID, id), null,
        res -> {
          if (res.succeeded()) {
            ProductCategoryEntity productCategoryEntity = res.result().mapTo(ProductCategoryEntity.class);
            LOGGER.info("findProductCategoryById:{}", productCategoryEntity);
            future.complete(productCategoryEntity);
          } else {
            LOGGER.error("findProductCategoryById fail");
            future.fail(res.cause());
          }
        });
    return future;
  }

  @Override
  public Future<Void> insertProductCategory(ProductCategoryEntity productCategoryEntity) {
    Future<Void> future = Future.future();
    productCategoryEntity.setId(ObjectId.get().toHexString());
    JsonObject query = JsonObject.mapFrom(productCategoryEntity);

    mongoClient.insert(Constants.COLLECTION_PRODUCT_CATEGORY, query, event -> {
      if (event.succeeded()) {
        future.complete();
        LOGGER.info("insertProductCategory:{}", query);
      } else {
        future.fail(event.cause());
        LOGGER.info(Constants.MESSAGE_INSERT_FAIL + " query:{}", query);
      }
    });

    return future;
  }

  @Override
  public Future<Void> updateProductCategory(String id, ProductCategoryEntity productCategoryEntity) {
    Future<Void> future = Future.future();
    JsonObject jsonUpdate = JsonObject.mapFrom(productCategoryEntity);
    JsonObject query = new JsonObject().put(Constants._ID,id);
    jsonUpdate.getMap().values().removeIf(Objects::isNull);
    JsonObject jsonQueryUpdate = new JsonObject().put(Constants.DOCUMENT_SET,jsonUpdate);

    mongoClient.updateCollection(Constants.COLLECTION_PRODUCT_CATEGORY, query, jsonQueryUpdate, event -> {
      if (event.succeeded()) {
        future.complete();
        LOGGER.info("updateProductCategory:{}", query);
      } else {
        future.fail(event.cause());
        LOGGER.info(Constants.MESSAGE_UPDATE_FAIL + " updateProductCategory:{}", query);
      }
    });

    return future;
  }

  @Override
  public Future<Void> deleteProductCategory(String id) {
    Future<Void> future = Future.future();
    mongoClient.findOneAndDelete(Constants.COLLECTION_PRODUCT_CATEGORY,new JsonObject().put(Constants._ID,id),event -> {
      if (event.succeeded()) {
        future.complete();
        LOGGER.info("deleteProductCategory:{}", id);
      } else {
        future.fail(event.cause());
        LOGGER.info(Constants.MESSAGE_DELETE_FAIL + " deleteProductCategory:{}", id);
      }
    });
    return future;
  }
}
