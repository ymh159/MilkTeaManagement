package eb;

import io.reactivex.rxjava3.core.Completable;
import io.vertx.core.Vertx;
import utils.AddressConstants;

public class UserRegisterEBAddress {
    private final Vertx vertx;
    private final UserEventBusServices userEventBusServices;


    public UserRegisterEBAddress(Vertx vertx) {
        this.vertx = vertx;
        this.userEventBusServices = new UserEventBusServices(vertx);
        startComsumer().subscribe();
    }

    public Completable startComsumer() {
        return Completable.create(emitter -> {
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_GET_USER_BY_ID, message -> userEventBusServices.findUserById(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_GET_USER, message -> userEventBusServices.getUsers(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_INSERT_USER, message -> userEventBusServices.insertUser(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_UPDATE_USER, message -> userEventBusServices.updateUser(message));
            vertx.eventBus().consumer(AddressConstants.ADDRESS_EB_DELETE_USER, message -> userEventBusServices.deleteUser(message));
        });
    }
}
