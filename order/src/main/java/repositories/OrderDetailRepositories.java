package repositories;

import entity.OrderDetailEntity;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.Future;
import java.util.List;

public interface OrderDetailRepositories {

  Future<List<OrderDetailEntity>> getOrderDetails();

  Future<OrderDetailEntity> findOrderDetailById(String id);

  Single<String> insertOrderDetail(OrderDetailEntity orderDetail);

  Future<Void> updateOrderDetail(String id, OrderDetailEntity orderDetail);

  Single<OrderDetailEntity> deleteOrderDetail(String id);
  Future<List<OrderDetailEntity>> findOrderDetailByOrderId(String order_id);
}
