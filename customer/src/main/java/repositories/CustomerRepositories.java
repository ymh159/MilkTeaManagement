package repositories;

import entity.CustomerEntity;
import io.vertx.core.Future;
import java.util.List;

public interface CustomerRepositories {

  Future<List<CustomerEntity>> getCustomers();

  Future<CustomerEntity> findCustomerById(String id);

  Future<Void> insertCustomer(CustomerEntity customerEntity);

  Future<Void> updateCustomer(String id, CustomerEntity customerEntity);

  Future<Void> deleteCustomer(String id);
}
