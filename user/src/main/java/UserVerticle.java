import entity.UserEntity;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.UserRepositories;
import repositories.impl.UserRepositoriesImpl;
import utils.Constants;
import utils.ConstantsAddress;

public class UserVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserVerticle.class);

  @Override
  public void start() throws Exception {
    UserRepositories userRepositories = new UserRepositoriesImpl(vertx);
    EventBus eb = vertx.eventBus();

      // get user by id
      eb.consumer(ConstantsAddress.ADDRESS_EB_GET_USER_BY_ID, message -> {
        LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
            ConstantsAddress.ADDRESS_EB_GET_USER_BY_ID,
            message.body());
        userRepositories.findUserById(message.body().toString()).setHandler(res -> {
          if (res.succeeded()) {
            UserEntity userEntity = res.result();
            JsonObject jsonObject = JsonObject.mapFrom(userEntity);
            message.reply(jsonObject);
          } else {
            message.reply(Constants.MESSAGE_GET_FAIL);
          }
        });
      });

      // get all user
      eb.consumer(ConstantsAddress.ADDRESS_EB_GET_USER, message -> {
        LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_GET_USER,
            message.body());
        userRepositories.getUsers().setHandler(res -> {
          if (res.succeeded()) {
            List<UserEntity> userEntityList = res.result();
            JsonArray jsonArray = new JsonArray(userEntityList);
            message.reply(jsonArray);
          } else {
            message.reply(Constants.MESSAGE_GET_FAIL);
          }
        });
      });

      // insert user
      eb.consumer(ConstantsAddress.ADDRESS_EB_INSERT_USER, message -> {
        LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_INSERT_USER,
            message.body());
        JsonObject json = JsonObject.mapFrom(message.body());
        UserEntity userEntity = json.mapTo(UserEntity.class);
        userRepositories.insertUser(userEntity).setHandler(res -> {
          if (res.succeeded()) {
            message.reply(Constants.MESSAGE_INSERT_SUCCESS);
          } else {
            message.reply(Constants.MESSAGE_INSERT_FAIL);
          }
        });
      });

      // update user
      eb.consumer(ConstantsAddress.ADDRESS_EB_UPDATE_USER, message -> {
        LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_UPDATE_USER,
            message.body());
        JsonObject json = JsonObject.mapFrom(message.body());
        JsonObject jsonUpdate = JsonObject.mapFrom(json.getValue(Constants.JSON_UPDATE));
        UserEntity userEntity = jsonUpdate.mapTo(UserEntity.class);
        userRepositories.updateUser(json.getValue(Constants._ID).toString(), userEntity)
            .setHandler(res -> {
              if (res.succeeded()) {
                message.reply(Constants.MESSAGE_UPDATE_SUCCESS);
              } else {
                message.reply(Constants.MESSAGE_UPDATE_FAIL);
              }
            });
      });

      // delete user
      eb.consumer(ConstantsAddress.ADDRESS_EB_DELETE_USER, message -> {
        LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_DELETE_USER,
            message.body());
        userRepositories.deleteUser(message.body().toString()).setHandler(res -> {
          if (res.succeeded()) {
            message.reply(Constants.MESSAGE_DELETE_SUCCESS);
          } else {
            message.reply(Constants.MESSAGE_DELETE_FAIL);
          }
        });
      });
  }
}
