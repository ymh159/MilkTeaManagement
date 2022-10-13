package repositories.eb;

import io.reactivex.rxjava3.core.Completable;
import io.vertx.core.Vertx;
import utils.AddressConstants;

public class CustomerRegisterEBAddress {
    private final Vertx vertx;
    private final CustomerEventBusServices customerEventBusServices;
    public CustomerRegisterEBAddress(Vertx vertx){
        this.vertx = vertx;
        this.customerEventBusServices = new CustomerEventBusServices(vertx);
        startComsumer().subscribe();
    }

    public Completable startComsumer() {
        return Completable.create(emitter -> {
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_GET_CUSTOMER_BY_ID, message -> customerEventBusServices.findCustomerById(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_GET_CUSTOMER, message -> customerEventBusServices.getCustomers(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_INSERT_CUSTOMER, message -> customerEventBusServices.insertCustomer(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_UPDATE_CUSTOMER, message -> customerEventBusServices.updateCustomer(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_DELETE_CUSTOMER, message -> customerEventBusServices.deleteCustomer(message));
        });
    }
}
