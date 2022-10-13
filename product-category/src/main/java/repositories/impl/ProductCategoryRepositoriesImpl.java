package repositories.impl;

import entity.UserEntity;
import io.vertx.core.Vertx;
import io.vertx.ext.mongo.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.CRUDRepositories;
import repositories.ProductCategoryRepositories;
import utils.Constants;

public class ProductCategoryRepositoriesImpl extends CRUDRepositories implements ProductCategoryRepositories {

  public ProductCategoryRepositoriesImpl(Vertx vertx) {
    super(vertx);
    setCollectionName(Constants.COLLECTION_PRODUCT_CATEGORY);
    setClassType(UserEntity.class);
  }


}
