package repositories;

import entity.ProductEntity;
import io.vertx.core.Future;
import java.util.List;

public interface ProductRepositories {

  Future<List<ProductEntity>> getProducts();

  Future<ProductEntity> findProductById(String id);

  Future<String> insertProduct(ProductEntity userEntity);

  Future<String> updateProduct(String id, ProductEntity userEntity);

  Future<String> deleteProduct(String id);
}
