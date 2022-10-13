package eb;

import io.reactivex.rxjava3.core.Completable;
import io.vertx.core.Vertx;
import utils.AddressConstants;

public class ProductRegisterEBAddress {
    private final Vertx vertx;
    private final ProductEventBusServices productEventBusServices;
    public ProductRegisterEBAddress(Vertx vertx){
        this.vertx = vertx;
        productEventBusServices = new ProductEventBusServices(vertx);
        startComsumer().subscribe();
    }
    public Completable startComsumer(){
        return Completable.create(emitter -> {
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_GET_PRODUCT_BY_ID, message -> productEventBusServices.findProductById(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_GET_PRODUCT, message -> productEventBusServices.getProducts(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_INSERT_PRODUCT, message -> productEventBusServices.insertProduct(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_UPDATE_PRODUCT, message -> productEventBusServices.updateProduct(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_CHECK_AND_UPDATE_PRODUCT, message -> productEventBusServices.checkAndUpdateProduct(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_DELETE_PRODUCT, message -> productEventBusServices.deleteProduct(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_GET_PRODUCT_DETAIL_BY_ID, message -> productEventBusServices.getProductDetailById(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_GET_ALL_PRODUCT_DETAIL, message -> productEventBusServices.getAllProductDetail(message));
        });
    }
}
