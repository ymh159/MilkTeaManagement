package eb;

import entity.ProductCategoryEntity;
import entity.TypeValueReply;
import entity.UserEntity;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.ProductCategoryRepositories;
import repositories.impl.ProductCategoryRepositoriesImpl;
import utils.AddressConstants;
import utils.Constants;
import utils.ReplyMessageEB;
import utils.ReplyMessageSingle;

public class ProductCategoryEBServices {
    private static final Logger logger = LoggerFactory.getLogger(ProductCategoryEBServices.class);

    private final ProductCategoryRepositories productCategoryRepositories;
    private final ReplyMessageEB replyMessageEB;

    public ProductCategoryEBServices(Vertx vertx) {
        this.productCategoryRepositories = new ProductCategoryRepositoriesImpl(vertx);
        this.replyMessageEB = new ReplyMessageEB();
    }

    <T> void findProductCategoryById(Message<Object> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
                AddressConstants.ADDRESS_EB_GET_PRODUCT_CATEGORY_BY_ID,
                message.body());
        ReplyMessageSingle.replyMessage(message, productCategoryRepositories.findById(message.body().toString()));
    }

    <T> void getAllProductCategory(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_GET_PRODUCT_CATEGORY,
                message.body());
        ReplyMessageSingle.replyMessage(message, productCategoryRepositories.getAll());
    }

    <T> void insertProductCategory(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_INSERT_PRODUCT_CATEGORY,
                message.body());
        JsonObject json = JsonObject.mapFrom(message.body());
        ProductCategoryEntity productCategoryEntity = json.mapTo(ProductCategoryEntity.class);
        ReplyMessageSingle.replyMessage(message, productCategoryRepositories.insert(productCategoryEntity));
    }

    <T> void updateProductCategory(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_UPDATE_PRODUCT_CATEGORY,
                message.body());
        JsonObject json = JsonObject.mapFrom(message.body());
        JsonObject jsonUpdate = JsonObject.mapFrom(json.getValue(Constants.JSON_UPDATE));
        ProductCategoryEntity productCategoryEntity = jsonUpdate.mapTo(ProductCategoryEntity.class);
        ReplyMessageSingle.replyMessage(message,
                productCategoryRepositories.update(json.getValue(Constants._ID).toString(), productCategoryEntity));
    }

    <T> void deleteProductCategory(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_DELETE_PRODUCT_CATEGORY,
                message.body());
        ReplyMessageSingle.replyMessage(message, productCategoryRepositories.delete(message.body().toString()));
    }
}
