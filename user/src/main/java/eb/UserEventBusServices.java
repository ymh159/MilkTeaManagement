package eb;

import entity.UserEntity;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.UserRepositories;
import repositories.impl.UserRepositoriesImpl;
import utils.AddressConstants;
import utils.Constants;
import utils.ReplyMessageSingle;

public class UserEventBusServices {
    private static final Logger logger = LoggerFactory.getLogger(UserEventBusServices.class);
    private final UserRepositories userRepositories;

    public UserEventBusServices(Vertx vertx) {
        this.userRepositories = new UserRepositoriesImpl(vertx);
    }

    <T> void findUserById(Message<Object> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
                AddressConstants.ADDRESS_EB_GET_USER_BY_ID,
                message.body());
        ReplyMessageSingle.replyMessage(message, userRepositories.findById(message.body().toString()));
    }

    <T> void getUsers(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_GET_USER,
                message.body());
        ReplyMessageSingle.replyMessage(message, userRepositories.getAll());
    }

    <T> void insertUser(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_INSERT_USER,
                message.body());
        JsonObject json = JsonObject.mapFrom(message.body());
        UserEntity userEntity = json.mapTo(UserEntity.class);
        ReplyMessageSingle.replyMessage(message, userRepositories.insert(userEntity));
    }

    <T> void updateUser(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_UPDATE_USER,
                message.body());
        JsonObject json = JsonObject.mapFrom(message.body());
        JsonObject jsonUpdate = JsonObject.mapFrom(json.getValue(Constants.JSON_UPDATE));
        UserEntity userEntity = jsonUpdate.mapTo(UserEntity.class);
        ReplyMessageSingle.replyMessage(message,
                userRepositories.update(json.getValue(Constants._ID).toString(), userEntity));
    }

    <T> void deleteUser(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_DELETE_USER,
                message.body());
        ReplyMessageSingle.replyMessage(message, userRepositories.delete(message.body().toString()));
    }
}

