package eb;

import entity.ProductEntity;
import entity.TypeValueReply;
import entity.UserEntity;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.ProductRepositories;
import repositories.impl.ProductRepositoriesImpl;
import services.ProductServices;
import services.impl.ProductServicesImpl;
import utils.AddressConstants;
import utils.Constants;
import utils.ReplyMessageEB;
import utils.ReplyMessageSingle;

public class ProductEventBusServices {
    private static final Logger logger = LoggerFactory.getLogger(ProductEventBusServices.class);

    private final ProductServices productServices;
    private final ProductRepositories productRepositories;
    private final ReplyMessageEB replyMessageEB;

    public ProductEventBusServices(Vertx vertx) {
        this.productRepositories = new ProductRepositoriesImpl(vertx);
        this.productServices = new ProductServicesImpl(vertx);
        this.replyMessageEB = new ReplyMessageEB();
    }

    <T> void findProductById(Message<Object> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
                AddressConstants.ADDRESS_EB_GET_PRODUCT_BY_ID,
                message.body());
        ReplyMessageSingle.replyMessage(message, productRepositories.findById(message.body().toString()));
    }

    <T> void getProducts(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_GET_PRODUCT,
                message.body());
        ReplyMessageSingle.replyMessage(message, productRepositories.getAll());
    }

    <T> void insertProduct(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_INSERT_PRODUCT,
                message.body());
        JsonObject json = JsonObject.mapFrom(message.body());
        ProductEntity productEntity = json.mapTo(ProductEntity.class);
        ReplyMessageSingle.replyMessage(message, productRepositories.insert(productEntity));
    }

    <T> void updateProduct(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_UPDATE_PRODUCT,
                message.body());
        JsonObject json = JsonObject.mapFrom(message.body());
        JsonObject jsonUpdate = JsonObject.mapFrom(json.getValue(Constants.JSON_UPDATE));
        ProductEntity productEntity = jsonUpdate.mapTo(ProductEntity.class);
        ReplyMessageSingle.replyMessage(message,
                productRepositories.update(json.getValue(Constants._ID).toString(), productEntity));
    }

    <T> void deleteProduct(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_DELETE_PRODUCT,
                message.body());
        ReplyMessageSingle.replyMessage(message, productRepositories.delete(message.body().toString()));
    }

    public <T> void getProductDetailById(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
                AddressConstants.ADDRESS_EB_GET_PRODUCT_DETAIL_BY_ID, message.body());
        ReplyMessageSingle.replyMessage(message, productServices.getProductDetailByID(message.body().toString()));
    }

    public <T> void getAllProductDetail(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
                AddressConstants.ADDRESS_EB_GET_ALL_PRODUCT_DETAIL, message.body());
        JsonObject jsonMessage = new JsonObject();
        productServices.getAllProductDetail().subscribe(res -> {
            JsonArray jsonValue = new JsonArray(res);
            jsonMessage.put(Constants.STATUS, Constants.PASS).put(Constants.VALUE, jsonValue);
            logger.info("Message reply:{}", jsonMessage);
            message.reply(jsonMessage);
        }, throwable -> {
            jsonMessage.put(Constants.STATUS, Constants.FAIL).put(Constants.MESSAGE, throwable);
            logger.info("Message reply:{}", jsonMessage);
            message.reply(jsonMessage);
        });
    }

    public <T> void checkAndUpdateProduct(Message<T> message) {
        JsonObject json = JsonObject.mapFrom(message.body());
        String id = json.getValue(Constants._ID).toString();
        int quantity = (int) json.getValue(Constants.QUANTITY);
        JsonObject jsonUpdate = JsonObject.mapFrom(json.getValue(Constants.JSON_UPDATE));
        ProductEntity productEntity = jsonUpdate.mapTo(ProductEntity.class);
        ReplyMessageSingle.replyMessage(message, productRepositories.checkAndUpdateProduct(id, quantity, productEntity));
    }

}
