import entity.ProductCategoryEntity;
import entity.TypeValueReply;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.ProductCategoryRepositories;
import repositories.impl.ProductCategoryRepositoriesImpl;
import utils.Constants;
import utils.AddressConstants;
import utils.ReplyMessageEB;

public class ProductCategoryVerticle extends AbstractVerticle {

  private static final Logger logger = LoggerFactory.getLogger(ProductCategoryVerticle.class);

  @Override
  public void start() {
    ProductCategoryRepositories productCategoryRepositories = new ProductCategoryRepositoriesImpl(
        vertx);
    EventBus eb = vertx.eventBus();
    ReplyMessageEB replyMessageEB = new ReplyMessageEB();

    // get productCategory by id
    eb.consumer(AddressConstants.ADDRESS_EB_GET_PRODUCT_CATEGORY_BY_ID, message -> {
      logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
          AddressConstants.ADDRESS_EB_GET_PRODUCT_CATEGORY_BY_ID,
          message.body());
      productCategoryRepositories.findProductCategoryById(message.body().toString())
          .setHandler(res -> {
            replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT);
          });
    });

    // get all productCategory
    eb.consumer(AddressConstants.ADDRESS_EB_GET_PRODUCT_CATEGORY, message -> {
      logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
          AddressConstants.ADDRESS_EB_GET_PRODUCT_CATEGORY,
          message.body());
      productCategoryRepositories.getProductCategorys().setHandler(res -> {
        replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_ARRAY);
      });
    });

    // insert productCategory
    eb.consumer(AddressConstants.ADDRESS_EB_INSERT_PRODUCT_CATEGORY, message -> {
      logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
          AddressConstants.ADDRESS_EB_INSERT_PRODUCT_CATEGORY,
          message.body());
      JsonObject json = JsonObject.mapFrom(message.body());
      ProductCategoryEntity productCategoryEntity = json.mapTo(ProductCategoryEntity.class);
      productCategoryRepositories.insertProductCategory(productCategoryEntity).setHandler(res -> {
        replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT,
            Constants.MESSAGE_INSERT_SUCCESS);
      });
    });

    // update productCategory
    eb.consumer(AddressConstants.ADDRESS_EB_UPDATE_PRODUCT_CATEGORY, message -> {
      logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
          AddressConstants.ADDRESS_EB_UPDATE_PRODUCT_CATEGORY,
          message.body());
      JsonObject json = JsonObject.mapFrom(message.body());
      JsonObject jsonUpdate = JsonObject.mapFrom(json.getValue(Constants.JSON_UPDATE));
      ProductCategoryEntity productCategoryEntity = jsonUpdate.mapTo(ProductCategoryEntity.class);
      productCategoryRepositories.updateProductCategory(json.getValue(Constants._ID).toString(),
              productCategoryEntity)
          .setHandler(res -> {
            replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT,
                Constants.MESSAGE_UPDATE_SUCCESS);
          });
    });

    // delete productCategory
    eb.consumer(AddressConstants.ADDRESS_EB_DELETE_PRODUCT_CATEGORY, message -> {
      logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
          AddressConstants.ADDRESS_EB_DELETE_PRODUCT_CATEGORY,
          message.body());
      productCategoryRepositories.deleteProductCategory(message.body().toString())
          .setHandler(res -> {
            replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT,
                Constants.MESSAGE_DELETE_SUCCESS);
          });
    });
  }
}