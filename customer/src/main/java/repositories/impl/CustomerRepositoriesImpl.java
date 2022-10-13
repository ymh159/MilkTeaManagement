package repositories.impl;

import entity.CustomerEntity;
import entity.UserEntity;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.util.List;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.CRUDRepositories;
import repositories.CustomerRepositories;
import utils.Constants;
import utils.MongoDBClient;

public class CustomerRepositoriesImpl extends CRUDRepositories implements CustomerRepositories {

  private static final Logger logger = LoggerFactory.getLogger(CustomerRepositoriesImpl.class);
  private static MongoClient mongoClient;

  public CustomerRepositoriesImpl(Vertx vertx){
    super(vertx);
    setCollectionName(Constants.COLLECTION_CUSTOMER);
    setClassType(UserEntity.class);
  }
}
