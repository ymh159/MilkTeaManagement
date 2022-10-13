package repositories.impl;

import entity.UserEntity;
import io.vertx.core.Vertx;
import repositories.CRUDRepositories;
import repositories.ProviderRepositories;
import utils.Constants;

public class ProviderRepositoriesImpl extends CRUDRepositories implements ProviderRepositories {

  public ProviderRepositoriesImpl(Vertx vertx) {
    super(vertx);
    setCollectionName(Constants.COLLECTION_PROVIDER);
    setClassType(UserEntity.class);
  }
}
