package repositories.impl;

import com.mongodb.session.ClientSession;
import entity.ProductEntity;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.util.List;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.ProductRepositories;
import utils.Constants;
import utils.MongoDBClient;

public class ProductRepositoriesImpl implements ProductRepositories {

  private static final Logger logger = LoggerFactory.getLogger(ProductRepositoriesImpl.class);
  private static MongoClient mongoClient;

  public ProductRepositoriesImpl(Vertx vertx) {
    mongoClient = MongoDBClient.client(vertx);
  }

  @Override
  public Future<List<ProductEntity>> getProducts() {
    Future<List<ProductEntity>> future = Future.future();

    mongoClient.find(Constants.COLLECTION_PRODUCT, new JsonObject(), res -> {
      List<JsonObject> data = res.result();
      List<ProductEntity> entity;
      if (res.succeeded()) {
        entity = data.stream().map(item ->
            item.mapTo(ProductEntity.class)
        ).toList();
        logger.info("getProducts:{}", entity);
        future.complete(entity);
      } else {
        logger.error("getProducts fail");
        future.fail(res.cause());
      }
    });
    return future;
  }

  @Override
  public Future<ProductEntity> findProductById(String id) {
    Future<ProductEntity> future = Future.future();

    mongoClient.findOne(Constants.COLLECTION_PRODUCT, new JsonObject().put(Constants._ID, id), null,
        res -> {
          if (res.succeeded()) {
            if(res.result()==null){
              future.fail(new Exception("Id_product not found"));
            }else {
              ProductEntity productEntity = res.result().mapTo(ProductEntity.class);
              logger.info("findProductById:{}", productEntity);
              future.complete(productEntity);
            }
          } else {
            logger.error("findProductById fail");
            future.fail(res.cause());
          }
        });
    return future;
  }

  @Override
  public Future<Void> insertProduct(ProductEntity productEntity) {
    Future<Void> future = Future.future();
    productEntity.setId(ObjectId.get().toHexString());
    JsonObject query = JsonObject.mapFrom(productEntity);
    mongoClient.insert(Constants.COLLECTION_PRODUCT, query, event -> {
      if (event.succeeded()) {
        future.complete();
        logger.info("insertProduct:{}", query);
      } else {
        future.fail(event.cause());
        logger.info(Constants.MESSAGE_INSERT_FAIL + " query:{}", query);
      }
    });

    return future;
  }

  @Override
  public Future<ProductEntity> updateProduct(String id, ProductEntity productEntity) {
    Future<ProductEntity> future = Future.future();
    JsonObject jsonUpdate = JsonObject.mapFrom(productEntity);
    JsonObject query = new JsonObject().put(Constants._ID,id);
    jsonUpdate.getMap().values().removeIf(Objects::isNull);
    JsonObject jsonQueryUpdate = new JsonObject().put(Constants.DOCUMENT_SET,jsonUpdate);

    mongoClient.findOneAndUpdate(Constants.COLLECTION_PRODUCT, query, jsonQueryUpdate, event -> {
      if (event.succeeded()) {
        ProductEntity entity = JsonObject.mapFrom(event.result()).mapTo(ProductEntity.class);
        future.complete(entity);
        logger.info("updateProduct:{}", query);
      } else {
        future.fail(event.cause());
        logger.info(Constants.MESSAGE_UPDATE_FAIL + " updateProduct:{}", query);
      }
    });

    return future;
  }

  @Override
  public Future<Void> deleteProduct(String id) {
    Future<Void> future = Future.future();
    mongoClient.findOneAndDelete(Constants.COLLECTION_PRODUCT,new JsonObject().put(Constants._ID,id),event -> {
      if (event.succeeded()) {
        future.complete();
        logger.info("deleteProduct:{}", id);
      } else {
        future.fail(event.cause());
        logger.info(Constants.MESSAGE_DELETE_FAIL + " deleteProduct:{}", id);
      }
    });
    return future;
  }
}
