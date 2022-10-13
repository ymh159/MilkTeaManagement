package repositories.eb;

import entity.CustomerEntity;
import entity.TypeValueReply;
import entity.CustomerEntity;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.CustomerRepositories;
import repositories.impl.CustomerRepositoriesImpl;
import utils.AddressConstants;
import utils.Constants;
import utils.ReplyMessageEB;
import utils.ReplyMessageSingle;

public class CustomerEventBusServices {
    private static final Logger logger = LoggerFactory.getLogger(CustomerEventBusServices.class);

    private final CustomerRepositories customerRepositories;
    private final ReplyMessageEB replyMessageEB;

    public CustomerEventBusServices(Vertx vertx) {
        this.customerRepositories = new CustomerRepositoriesImpl(vertx);
        this.replyMessageEB = new ReplyMessageEB();
    }
    <T> void findCustomerById(Message<Object> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
                AddressConstants.ADDRESS_EB_GET_CUSTOMER_BY_ID,
                message.body());
        ReplyMessageSingle.replyMessage(message, customerRepositories.findById(message.body().toString()));
    }

    <T> void getCustomers(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_GET_CUSTOMER,
                message.body());
        ReplyMessageSingle.replyMessage(message, customerRepositories.getAll());
    }

    <T> void insertCustomer(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_INSERT_CUSTOMER,
                message.body());
        JsonObject json = JsonObject.mapFrom(message.body());
        CustomerEntity customerEntity = json.mapTo(CustomerEntity.class);
        ReplyMessageSingle.replyMessage(message, customerRepositories.insert(customerEntity));
    }

    <T> void updateCustomer(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_UPDATE_CUSTOMER,
                message.body());
        JsonObject json = JsonObject.mapFrom(message.body());
        JsonObject jsonUpdate = JsonObject.mapFrom(json.getValue(Constants.JSON_UPDATE));
        CustomerEntity customerEntity = jsonUpdate.mapTo(CustomerEntity.class);
        ReplyMessageSingle.replyMessage(message,
                customerRepositories.update(json.getValue(Constants._ID).toString(), customerEntity));
    }

    <T> void deleteCustomer(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_DELETE_CUSTOMER,
                message.body());
        ReplyMessageSingle.replyMessage(message, customerRepositories.delete(message.body().toString()));
    }
    
}
