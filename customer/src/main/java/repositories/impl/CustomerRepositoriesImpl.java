package repositories.impl;

import entity.CustomerEntity;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.util.List;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.CustomerRepositories;
import utils.Constants;
import utils.MongoDBClient;

public class CustomerRepositoriesImpl implements CustomerRepositories {

  private static final Logger logger = LoggerFactory.getLogger(CustomerRepositoriesImpl.class);
  private static MongoClient mongoClient;

  public CustomerRepositoriesImpl(Vertx vertx){
    mongoClient = MongoDBClient.client(vertx);
  }

  @Override
  public Future<List<CustomerEntity>> getCustomers() {
    Future<List<CustomerEntity>> future = Future.future();

    mongoClient.find(Constants.COLLECTION_CUSTOMER, new JsonObject(), res -> {
      List<JsonObject> data = res.result();
      List<CustomerEntity> entity;
      if (res.succeeded()) {
        entity = data.stream().map(item ->
            item.mapTo(CustomerEntity.class)
        ).toList();
        logger.info("getCustomers:{}", entity);
        future.complete(entity);
      } else {
        logger.error("getCustomers fail");
        future.fail(res.cause());
      }
    });
    return future;
  }

  @Override
  public Future<CustomerEntity> findCustomerById(String id) {
    Future<CustomerEntity> future = Future.future();

    mongoClient.findOne(Constants.COLLECTION_CUSTOMER, new JsonObject().put(Constants._ID, id), null,
        res -> {
          if (res.succeeded()) {
            CustomerEntity customerEntity = res.result().mapTo(CustomerEntity.class);
            logger.info("findCustomerById:{}", customerEntity);
            future.complete(customerEntity);
          } else {
            logger.error("findCustomerById fail");
            future.fail(res.cause());
          }
        });
    return future;
  }

  @Override
  public Future<Void> insertCustomer(CustomerEntity customerEntity) {
    Future<Void> future = Future.future();
    customerEntity.setId(ObjectId.get().toHexString());
    JsonObject query = JsonObject.mapFrom(customerEntity);

    mongoClient.insert(Constants.COLLECTION_CUSTOMER, query, event -> {
      if (event.succeeded()) {
        future.complete();
        logger.info("insertCustomer:{}", query);
      } else {
        future.fail(event.cause());
        logger.info(Constants.MESSAGE_INSERT_FAIL + " query:{}", query);
      }
    });

    return future;
  }

  @Override
  public Future<CustomerEntity> updateCustomer(String id, CustomerEntity customerEntity) {
    Future<CustomerEntity> future = Future.future();
    JsonObject jsonUpdate = JsonObject.mapFrom(customerEntity);
    JsonObject query = new JsonObject().put(Constants._ID,id);
    jsonUpdate.getMap().values().removeIf(Objects::isNull);
    JsonObject jsonQueryUpdate = new JsonObject().put(Constants.DOCUMENT_SET,jsonUpdate);

    mongoClient.findOneAndUpdate(Constants.COLLECTION_CUSTOMER, query, jsonQueryUpdate, event -> {
      if (event.succeeded()) {
        future.complete(event.result().mapTo(CustomerEntity.class));
        logger.info("updateCustomer:{}", query);
      } else {
        future.fail(event.cause());
        logger.info(Constants.MESSAGE_UPDATE_FAIL + " updateCustomer:{}", query);
      }
    });

    return future;
  }

  @Override
  public Future<Void> deleteCustomer(String id) {
    Future<Void> future = Future.future();
    mongoClient.findOneAndDelete(Constants.COLLECTION_CUSTOMER,new JsonObject().put(Constants._ID,id),event -> {
      if (event.succeeded()) {
        future.complete();
        logger.info("deleteCustomer:{}", id);
      } else {
        future.fail(event.cause());
        logger.info(Constants.MESSAGE_DELETE_FAIL + " deleteCustomer:{}", id);
      }
    });
    return future;
  }
}
