package repositories;

import entity.ProductCategoryEntity;
import io.vertx.core.Future;
import java.util.List;

public interface ProductCategoryRepositories {

  Future<List<ProductCategoryEntity>> getProductCategorys();

  Future<ProductCategoryEntity> findProductCategoryById(String id);

  Future<Void> insertProductCategory(ProductCategoryEntity productCategoryEntity);

  Future<Void> updateProductCategory(String id, ProductCategoryEntity productCategoryEntity);

  Future<Void> deleteProductCategory(String id);
}
