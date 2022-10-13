package services;

import DTO.ProductDetailDTO;
import entity.ProductEntity;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.Future;
import java.util.List;

public interface ProductServices {
  Single<List<ProductDetailDTO>> getAllProductDetail();
  Single<ProductDetailDTO> getProductDetailByID(String id);
  Single<List<ProductEntity>> getAllProduct();
}
