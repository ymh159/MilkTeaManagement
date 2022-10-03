package repositories;

import entity.OrderEntity;
import io.vertx.core.Future;
import java.util.List;

public interface OrderRepositories {

  Future<List<OrderEntity>> getOrders();

  Future<OrderEntity> findOrderById(String id);

  Future<String> insertOrder(OrderEntity orderEntity);

  Future<Void> updateOrder(String id, OrderEntity orderEntity);

  Future<Void> deleteOrder(String id);
}
