package eb;

import entity.ProviderEntity;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.ProviderRepositories;
import repositories.impl.ProviderRepositoriesImpl;
import utils.AddressConstants;
import utils.Constants;
import utils.ReplyMessageEB;
import utils.ReplyMessageSingle;

public class ProviderEventBusServices {
    private static final Logger logger = LoggerFactory.getLogger(ProviderEventBusServices.class);
    private final ProviderRepositories providerRepositories;
    private final Vertx vertx;

    public ProviderEventBusServices(Vertx vertx) {
        this.vertx = vertx;
        this.providerRepositories = new ProviderRepositoriesImpl(vertx);
    }

    <T> void findProviderById(Message<Object> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
                AddressConstants.ADDRESS_EB_GET_PROVIDER_BY_ID,
                message.body());
        ReplyMessageSingle.replyMessage(message, providerRepositories.findById(message.body().toString()));
    }

    <T> void getProviders(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_GET_PROVIDER,
                message.body());
        ReplyMessageSingle.replyMessage(message, providerRepositories.getAll());
    }

    <T> void insertProvider(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_INSERT_PROVIDER,
                message.body());
        JsonObject json = JsonObject.mapFrom(message.body());
        ProviderEntity providerEntity = json.mapTo(ProviderEntity.class);
        ReplyMessageSingle.replyMessage(message, providerRepositories.insert(providerEntity));
    }

    <T> void updateProvider(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_UPDATE_PROVIDER,
                message.body());
        JsonObject json = JsonObject.mapFrom(message.body());
        JsonObject jsonUpdate = JsonObject.mapFrom(json.getValue(Constants.JSON_UPDATE));
        ProviderEntity providerEntity = jsonUpdate.mapTo(ProviderEntity.class);
        ReplyMessageSingle.replyMessage(message,
                providerRepositories.update(json.getValue(Constants._ID).toString(), providerEntity));
    }

    <T> void deleteProvider(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_DELETE_PROVIDER,
                message.body());
        ReplyMessageSingle.replyMessage(message, providerRepositories.delete(message.body().toString()));
    }
}
