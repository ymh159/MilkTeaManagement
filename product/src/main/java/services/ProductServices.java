package services;

import DTO.ProductDetailDTO;
import entity.ProductEntity;
import io.vertx.core.Future;
import java.util.List;

public interface ProductServices {
  Future<List<ProductDetailDTO>> getAllProductDetail();
  Future<ProductDetailDTO> getProductDetailByID(String id);
  Future<List<ProductEntity>> getAllProduct();
  Future<ProductEntity> findProductById(String id);
}
