package services;

import DTO.OrderInfoDTO;
import DTO.ProductOrderDTO;
import DTO.ProductOrderDetailDTO;
import entity.OrderDetailEntity;
import entity.OrderEntity;
import entity.ProductEntity;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.Future;
import java.util.List;

public interface OrderProductSevices {
  Single<String> insertOrderDetail(OrderDetailEntity orderDetailEntity);
  Single<String> insertOrder(OrderEntity orderEntity);
//  Future<String> orderProduct(OrderInfoDTO orderInfoDTO);
  Future<OrderEntity> findOrderById(String id);
  Future<OrderDetailEntity> findOrderDetailById(String id);
  Future<List<OrderDetailEntity>> findOrderDetailByOrderId(String id);
  Future<ProductOrderDetailDTO> getOrderProductDetail(String id);
  Single<String> orderSingle(OrderInfoDTO orderInfoDTO);
  Single<ProductEntity> deleteOrder(String id);
}
