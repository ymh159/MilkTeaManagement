package repositories;

import entity.OrderEntity;
import io.vertx.core.Future;
import java.util.List;

public interface OrderRepositories {

  Future<List<OrderEntity>> getOrders();

  Future<OrderEntity> findOrderById(String id);

  Future<String> insertOrder(OrderEntity userEntity);

  Future<String> updateOrder(String id, OrderEntity userEntity);

  Future<String> deleteOrder(String id);
}
