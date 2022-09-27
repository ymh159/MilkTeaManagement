package repositories;

import entity.OrderDetailEntity;
import io.vertx.core.Future;
import java.util.List;

public interface OrderDetailRepositories {

  Future<List<OrderDetailEntity>> getOrderDetails();

  Future<OrderDetailEntity> findOrderDetailById(String id);

  Future<String> insertOrderDetail(OrderDetailEntity userEntity);

  Future<String> updateOrderDetail(String id, OrderDetailEntity userEntity);

  Future<String> deleteOrderDetail(String id);
}
