package repositories.impl;

import entity.UserEntity;
import io.vertx.core.Vertx;
import repositories.CRUDRepositories;
import repositories.UserRepositories;
import utils.Constants;

public class UserRepositoriesImpl extends CRUDRepositories implements UserRepositories {
  public UserRepositoriesImpl(Vertx vertx) {
    super(vertx);
    setCollectionName(Constants.COLLECTION_USER);
    setClassType(UserEntity.class);
  }

}
