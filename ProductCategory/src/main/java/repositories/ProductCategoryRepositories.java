package repositories;

import entity.ProductCategoryEntity;
import io.vertx.core.Future;
import java.util.List;

public interface ProductCategoryRepositories {

  Future<List<ProductCategoryEntity>> getProductCategorys();

  Future<ProductCategoryEntity> findProductCategoryById(String id);

  Future<String> insertProductCategory(ProductCategoryEntity userEntity);

  Future<String> updateProductCategory(String id, ProductCategoryEntity userEntity);

  Future<String> deleteProductCategory(String id);
}
