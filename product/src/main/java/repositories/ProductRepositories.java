package repositories;

import entity.ProductEntity;
import io.vertx.core.Future;
import java.util.List;

public interface ProductRepositories {

  Future<List<ProductEntity>> getProducts();

  Future<ProductEntity> findProductById(String id);

  Future<Void> insertProduct(ProductEntity productEntity);

  Future<Void> updateProduct(String id, ProductEntity productEntity);

  Future<Void> deleteProduct(String id);
}
