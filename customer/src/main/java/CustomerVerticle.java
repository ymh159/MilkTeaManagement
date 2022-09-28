import entity.CustomerEntity;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.CustomerRepositories;
import repositories.impl.CustomerRepositoriesImpl;
import utils.Constants;
import utils.ConstantsAddress;

public class CustomerVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerVerticle.class);

  @Override
  public void start() throws Exception {
    CustomerRepositories customerRepositories = new CustomerRepositoriesImpl(vertx);
    EventBus eb = vertx.eventBus();

    // get customer by id
    eb.consumer(ConstantsAddress.ADDRESS_EB_GET_CUSTOMER_BY_ID, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_GET_CUSTOMER_BY_ID,
          message.body());
      customerRepositories.findCustomerById(message.body().toString()).setHandler(res -> {
        if (res.succeeded()) {
          CustomerEntity customerEntity = res.result();
          JsonObject jsonObject = JsonObject.mapFrom(customerEntity);
          message.reply(jsonObject);
        } else {
          message.reply(Constants.MESSAGE_GET_FAIL);
        }
      });
    });

    // get all customer
    eb.consumer(ConstantsAddress.ADDRESS_EB_GET_CUSTOMER, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_GET_CUSTOMER,
          message.body());
      customerRepositories.getCustomers().setHandler(res -> {
        if (res.succeeded()) {
          List<CustomerEntity> customerEntityList = res.result();
          JsonArray jsonArray = new JsonArray(customerEntityList);
          message.reply(jsonArray);
        } else {
          message.reply(Constants.MESSAGE_GET_FAIL);
        }
      });
    });

    // insert customer
    eb.consumer(ConstantsAddress.ADDRESS_EB_INSERT_CUSTOMER, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_INSERT_CUSTOMER,
          message.body());
      JsonObject json = JsonObject.mapFrom(message.body());
      CustomerEntity customerEntity = json.mapTo(CustomerEntity.class);
      customerRepositories.insertCustomer(customerEntity).setHandler(res -> {
        if (res.succeeded()) {
          message.reply(Constants.MESSAGE_INSERT_SUCCESS);
        } else {
          message.reply(Constants.MESSAGE_INSERT_FAIL);
        }
      });
    });

    // update customer
    eb.consumer(ConstantsAddress.ADDRESS_EB_UPDATE_CUSTOMER, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_UPDATE_CUSTOMER,
          message.body());
      JsonObject json = JsonObject.mapFrom(message.body());
      JsonObject jsonUpdate = JsonObject.mapFrom(json.getValue(Constants.JSON_UPDATE));
      CustomerEntity customerEntity = jsonUpdate.mapTo(CustomerEntity.class);
      customerRepositories.updateCustomer(json.getValue(Constants._ID).toString(), customerEntity)
          .setHandler(res -> {
            if (res.succeeded()) {
              message.reply(Constants.MESSAGE_UPDATE_SUCCESS);
            } else {
              message.reply(Constants.MESSAGE_UPDATE_FAIL);
            }
          });
    });

    // delete customer
    eb.consumer(ConstantsAddress.ADDRESS_EB_DELETE_CUSTOMER, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_DELETE_CUSTOMER,
          message.body());
      customerRepositories.deleteCustomer(message.body().toString()).setHandler(res -> {
        if (res.succeeded()) {
          message.reply(Constants.MESSAGE_DELETE_SUCCESS);
        } else {
          message.reply(Constants.MESSAGE_DELETE_FAIL);
        }
      });
    });
  }
}
