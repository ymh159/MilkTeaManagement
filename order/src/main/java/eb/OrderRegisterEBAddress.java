package eb;

import io.reactivex.rxjava3.core.Completable;
import io.vertx.core.Vertx;
import utils.AddressConstants;

public class OrderRegisterEBAddress {
    private final Vertx vertx;
    private final OrderEventBusServices orderEventBusServices;
    public OrderRegisterEBAddress(Vertx vertx){
        this.vertx = vertx;
        this.orderEventBusServices = new OrderEventBusServices(vertx);
        startComsumer().subscribe();
    }
    public Completable startComsumer(){
        return Completable.create(emitter -> {
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_GET_ORDER_PRODUCT_DETAIL, message ->orderEventBusServices.getOrderProductDetail(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_ORDER_PRODUCT, message ->orderEventBusServices.orderProductServices(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_GET_ORDER_BY_ID, message ->orderEventBusServices.getOrderById(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_GET_ORDER, message ->orderEventBusServices.getOrders(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_INSERT_ORDER, message ->orderEventBusServices.insertOrder(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_UPDATE_ORDER, message ->orderEventBusServices.updateOrder(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_DELETE_ORDER, message ->orderEventBusServices.deleteOrder(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_GET_ORDER_DETAIL_BY_ID, message ->orderEventBusServices.getOrderDetailById(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_GET_ORDER_DETAIL_BY_ORDER_ID, message ->orderEventBusServices.getOrderDetailByOrderId(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_GET_ORDER_DETAIL, message ->orderEventBusServices.getAllOrderDetail(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_INSERT_ORDER_DETAIL, message ->orderEventBusServices.insertOrderDetail(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_UPDATE_ORDER_DETAIL, message ->orderEventBusServices.updateOrderDetail(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_DELETE_ORDER_DETAIL, message ->orderEventBusServices.deleteOrderDetail(message));
            emitter.onComplete();
        });
    }
}
