package eb;

import io.reactivex.rxjava3.core.Completable;
import io.vertx.core.Vertx;
import utils.AddressConstants;

public class ProductCategoryRegisterEBAddress {
    private final Vertx vertx;
    private final ProductCategoryEBServices productCategoryEBServices;
    public ProductCategoryRegisterEBAddress(Vertx vertx){
        this.vertx = vertx;
        this.productCategoryEBServices = new ProductCategoryEBServices(vertx);
        startConsumer().subscribe();
    }

    public Completable startConsumer(){
        return Completable.create(emitter -> {
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_GET_PRODUCT_CATEGORY_BY_ID, message ->productCategoryEBServices.findProductCategoryById(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_GET_PRODUCT_CATEGORY, message ->productCategoryEBServices.getAllProductCategory(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_INSERT_PRODUCT_CATEGORY, message ->productCategoryEBServices.insertProductCategory(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_UPDATE_PRODUCT_CATEGORY, message ->productCategoryEBServices.updateProductCategory(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_DELETE_PRODUCT_CATEGORY, message ->productCategoryEBServices.deleteProductCategory(message));
        });
    }
}
