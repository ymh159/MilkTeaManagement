package services;

import DTO.OrderInfoDTO;
import DTO.ProductOrderDetailDTO;
import entity.OrderDetailEntity;
import entity.OrderEntity;
import io.vertx.core.Future;
import java.util.List;

public interface OrderProductSevices {
  Future<Void> insertOrderDetail(OrderDetailEntity orderDetailEntity);
  Future<String> insertOrder(OrderEntity orderEntity);
  Future<String> orderProduct(OrderInfoDTO orderInfoDTO);
  Future<OrderEntity> findOrderById(String id);
  Future<OrderDetailEntity> findOrderDetailById(String id);
  Future<List<OrderDetailEntity>> findOrderDetailByOrderId(String id);
  Future<ProductOrderDetailDTO> getOrderProductDetail(String id);
}
