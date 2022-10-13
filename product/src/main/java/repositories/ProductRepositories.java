package repositories;

import entity.ProductEntity;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.Future;
import java.util.List;

public interface ProductRepositories<T> {
  Single<List<T>> getAll();
  Single<T> findById(String id);
  Single<String> insert(T entity);
  Single<T> update(String id, T entity);
  Completable delete(String id);
  Single<ProductEntity> checkAndUpdateProduct(String id,int quantity, ProductEntity productEntity);
}
