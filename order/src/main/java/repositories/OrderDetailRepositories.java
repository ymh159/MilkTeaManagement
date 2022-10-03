package repositories;

import entity.OrderDetailEntity;
import io.vertx.core.Future;
import java.util.List;

public interface OrderDetailRepositories {

  Future<List<OrderDetailEntity>> getOrderDetails();

  Future<OrderDetailEntity> findOrderDetailById(String id);

  Future<Void> insertOrderDetail(OrderDetailEntity orderDetail);

  Future<Void> updateOrderDetail(String id, OrderDetailEntity orderDetail);

  Future<Void> deleteOrderDetail(String id);
  Future<List<OrderDetailEntity>> findOrderDetailByOrderId(String order_id);
}
