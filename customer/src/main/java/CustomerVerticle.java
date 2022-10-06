import entity.CustomerEntity;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.CustomerRepositories;
import repositories.impl.CustomerRepositoriesImpl;
import utils.Constants;
import utils.AddressConstants;
import utils.ReplyMessageEB;
import entity.TypeValueReply;

public class CustomerVerticle extends AbstractVerticle {

  private static final Logger logger = LoggerFactory.getLogger(CustomerVerticle.class);

  @Override
  public void start() {
    CustomerRepositories customerRepositories = new CustomerRepositoriesImpl(vertx);
    EventBus eb = vertx.eventBus();
    ReplyMessageEB replyMessageEB =new ReplyMessageEB();

    // get customer by id
    eb.consumer(AddressConstants.ADDRESS_EB_GET_CUSTOMER_BY_ID, message -> {

      customerRepositories.findCustomerById(message.body().toString()).setHandler(res -> {

        replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT);
      });
    });

    // get all customer
    eb.consumer(AddressConstants.ADDRESS_EB_GET_CUSTOMER, message -> {
      logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_GET_CUSTOMER,
          message.body());
      customerRepositories.getCustomers().setHandler(res -> {
        replyMessageEB.replyMessage(message,res,TypeValueReply.JSON_ARRAY);
      });
    });

    // insert customer
    eb.consumer(AddressConstants.ADDRESS_EB_INSERT_CUSTOMER, message -> {
      logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_INSERT_CUSTOMER,
          message.body());
      JsonObject json = JsonObject.mapFrom(message.body());
      CustomerEntity customerEntity = json.mapTo(CustomerEntity.class);
      customerRepositories.insertCustomer(customerEntity).setHandler(res -> {
        replyMessageEB.replyMessage(message,res,TypeValueReply.JSON_OBJECT, Constants.MESSAGE_INSERT_SUCCESS);
      });
    });

    // update customer
    eb.consumer(AddressConstants.ADDRESS_EB_UPDATE_CUSTOMER, message -> {
      logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_UPDATE_CUSTOMER,
          message.body());
      JsonObject json = JsonObject.mapFrom(message.body());
      JsonObject jsonUpdate = JsonObject.mapFrom(json.getValue(Constants.JSON_UPDATE));
      CustomerEntity customerEntity = jsonUpdate.mapTo(CustomerEntity.class);
      customerRepositories.updateCustomer(json.getValue(Constants._ID).toString(), customerEntity)
          .setHandler(res -> {
            replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT,Constants.MESSAGE_UPDATE_SUCCESS);
          });
    });

    // delete customer
    eb.consumer(AddressConstants.ADDRESS_EB_DELETE_CUSTOMER, message -> {
      logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_DELETE_CUSTOMER,
          message.body());
      customerRepositories.deleteCustomer(message.body().toString()).setHandler(res -> {
        replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT,Constants.MESSAGE_DELETE_SUCCESS);
      });
    });
  }
}
