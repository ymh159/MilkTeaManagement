import entity.TypeValueReply;
import entity.UserEntity;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.UserRepositories;
import repositories.impl.UserRepositoriesImpl;
import utils.Constants;
import utils.AddressConstants;
import utils.ReplyMessageEB;

public class UserVerticle extends AbstractVerticle {

  private static final Logger logger = LoggerFactory.getLogger(UserVerticle.class);

  @Override
  public void start() {
    UserRepositories userRepositories = new UserRepositoriesImpl(vertx);
    EventBus eb = vertx.eventBus();
    ReplyMessageEB replyMessageEB = new ReplyMessageEB();

    // get user by id
    eb.consumer(AddressConstants.ADDRESS_EB_GET_USER_BY_ID, message -> {
      logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
          AddressConstants.ADDRESS_EB_GET_USER_BY_ID,
          message.body());
      userRepositories.findUserById(message.body().toString()).setHandler(res -> {
        replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT);
      });
    });

    // get all user
    eb.consumer(AddressConstants.ADDRESS_EB_GET_USER, message -> {
      logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_GET_USER,
          message.body());
      userRepositories.getUsers().setHandler(res -> {
        replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_ARRAY);
      });
    });

    // insert user
    eb.consumer(AddressConstants.ADDRESS_EB_INSERT_USER, message -> {
      logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_INSERT_USER,
          message.body());
      JsonObject json = JsonObject.mapFrom(message.body());
      UserEntity userEntity = json.mapTo(UserEntity.class);
      userRepositories.insertUser(userEntity).setHandler(res -> {
        replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT,
            Constants.MESSAGE_INSERT_SUCCESS);
      });
    });

    // update user
    eb.consumer(AddressConstants.ADDRESS_EB_UPDATE_USER, message -> {
      logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_UPDATE_USER,
          message.body());
      JsonObject json = JsonObject.mapFrom(message.body());
      JsonObject jsonUpdate = JsonObject.mapFrom(json.getValue(Constants.JSON_UPDATE));
      UserEntity userEntity = jsonUpdate.mapTo(UserEntity.class);
      userRepositories.updateUser(json.getValue(Constants._ID).toString(), userEntity)
          .setHandler(res -> {
            replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT,
                Constants.MESSAGE_UPDATE_SUCCESS);
          });
    });

    // delete user
    eb.consumer(AddressConstants.ADDRESS_EB_DELETE_USER, message -> {
      logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_DELETE_USER,
          message.body());
      userRepositories.deleteUser(message.body().toString()).setHandler(res -> {
        replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT,
            Constants.MESSAGE_DELETE_SUCCESS);
      });
    });
  }
}
