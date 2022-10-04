import entity.ProviderEntity;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import repositories.ProviderRepositories;
import repositories.impl.ProviderRepositoriesImpl;
import utils.Constants;
import utils.ConstantsAddress;

public class ProviderVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProviderVerticle.class);

  @Override
  public void start() throws Exception {
    ProviderRepositories providerRepositories = new ProviderRepositoriesImpl(vertx);
    EventBus eb = vertx.eventBus();

    // get provider by id
    eb.consumer(ConstantsAddress.ADDRESS_EB_GET_PROVIDER_BY_ID, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_GET_PROVIDER_BY_ID,
          message.body());
      providerRepositories.findProviderById(message.body().toString()).setHandler(res -> {
        if (res.succeeded()) {
          ProviderEntity providerEntity = res.result();
          JsonObject jsonObject = JsonObject.mapFrom(providerEntity);
          message.reply(jsonObject);
        } else {
          message.reply(Constants.MESSAGE_GET_FAIL);
        }
      });
    });

    // get all provider
    eb.consumer(ConstantsAddress.ADDRESS_EB_GET_PROVIDER, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_GET_PROVIDER,
          message.body());
      providerRepositories.getProviders().setHandler(res -> {
        if (res.succeeded()) {
          List<ProviderEntity> providerEntityList = res.result();
          JsonArray jsonArray = new JsonArray(providerEntityList);
          message.reply(jsonArray);
        } else {
          message.reply(Constants.MESSAGE_GET_FAIL);
        }
      });
    });

    // insert provider
    eb.consumer(ConstantsAddress.ADDRESS_EB_INSERT_PROVIDER, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_INSERT_PROVIDER,
          message.body());
      JsonObject json = JsonObject.mapFrom(message.body());
      ProviderEntity providerEntity = json.mapTo(ProviderEntity.class);
      providerRepositories.insertProvider(providerEntity).setHandler(res -> {
        if (res.succeeded()) {
          message.reply(Constants.MESSAGE_INSERT_SUCCESS);
        } else {
          message.reply(Constants.MESSAGE_INSERT_FAIL);
        }
      });
    });

    // update provider
    eb.consumer(ConstantsAddress.ADDRESS_EB_UPDATE_PROVIDER, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_UPDATE_PROVIDER,
          message.body());
      JsonObject json = JsonObject.mapFrom(message.body());
      JsonObject jsonUpdate = JsonObject.mapFrom(json.getValue(Constants.JSON_UPDATE));
      ProviderEntity providerEntity = jsonUpdate.mapTo(ProviderEntity.class);
      providerRepositories.updateProvider(json.getValue(Constants._ID).toString(), providerEntity)
          .setHandler(res -> {
            if (res.succeeded()) {
              message.reply(Constants.MESSAGE_UPDATE_SUCCESS);
            } else {
              message.reply(Constants.MESSAGE_UPDATE_FAIL);
            }
          });
    });

    // delete provider
    eb.consumer(ConstantsAddress.ADDRESS_EB_DELETE_PROVIDER, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_DELETE_PROVIDER,
          message.body());
      providerRepositories.deleteProvider(message.body().toString()).setHandler(res -> {
        if (res.succeeded()) {
          message.reply(Constants.MESSAGE_DELETE_SUCCESS);
        } else {
          message.reply(Constants.MESSAGE_DELETE_FAIL);
        }
      });
    });
  }
}
