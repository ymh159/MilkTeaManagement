package eb;

import io.reactivex.rxjava3.core.Completable;
import io.vertx.core.Vertx;
import utils.AddressConstants;

public class ProviderRegisterEBAddress {
    private final Vertx vertx;
    private ProviderEventBusServices providerEventBusServices;

    public ProviderRegisterEBAddress(Vertx vertx) {
        this.vertx = vertx;
    }

    public Completable startConsumer() {
        return Completable.create(emitter -> {
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_GET_PROVIDER_BY_ID, message -> providerEventBusServices.findProviderById(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_GET_PROVIDER, message -> providerEventBusServices.getProviders(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_INSERT_PROVIDER, message -> providerEventBusServices.insertProvider(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_UPDATE_PROVIDER, message -> providerEventBusServices.updateProvider(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_DELETE_PROVIDER, message -> providerEventBusServices.deleteProvider(message));
        });
    }
}
