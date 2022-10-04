import entity.ProductCategoryEntity;
import entity.TypeValueReply;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.ProductCategoryRepositories;
import repositories.impl.ProductCategoryRepositoriesImpl;
import utils.Constants;
import utils.ConstantsAddress;
import utils.ReplyMessageEB;

public class ProductCategoryVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProductCategoryVerticle.class);

  @Override
  public void start() throws Exception {
    ProductCategoryRepositories productCategoryRepositories = new ProductCategoryRepositoriesImpl(
        vertx);
    EventBus eb = vertx.eventBus();
    ReplyMessageEB replyMessageEB = new ReplyMessageEB();

    // get productCategory by id
    eb.consumer(ConstantsAddress.ADDRESS_EB_GET_PRODUCT_CATEGORY_BY_ID, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
          ConstantsAddress.ADDRESS_EB_GET_PRODUCT_CATEGORY_BY_ID,
          message.body());
      productCategoryRepositories.findProductCategoryById(message.body().toString())
          .setHandler(res -> {
            replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT);
          });
    });

    // get all productCategory
    eb.consumer(ConstantsAddress.ADDRESS_EB_GET_PRODUCT_CATEGORY, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
          ConstantsAddress.ADDRESS_EB_GET_PRODUCT_CATEGORY,
          message.body());
      productCategoryRepositories.getProductCategorys().setHandler(res -> {
        replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_ARRAY);
      });
    });

    // insert productCategory
    eb.consumer(ConstantsAddress.ADDRESS_EB_INSERT_PRODUCT_CATEGORY, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
          ConstantsAddress.ADDRESS_EB_INSERT_PRODUCT_CATEGORY,
          message.body());
      JsonObject json = JsonObject.mapFrom(message.body());
      ProductCategoryEntity productCategoryEntity = json.mapTo(ProductCategoryEntity.class);
      productCategoryRepositories.insertProductCategory(productCategoryEntity).setHandler(res -> {
        replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT,
            Constants.MESSAGE_INSERT_SUCCESS);
      });
    });

    // update productCategory
    eb.consumer(ConstantsAddress.ADDRESS_EB_UPDATE_PRODUCT_CATEGORY, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
          ConstantsAddress.ADDRESS_EB_UPDATE_PRODUCT_CATEGORY,
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
    eb.consumer(ConstantsAddress.ADDRESS_EB_DELETE_PRODUCT_CATEGORY, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
          ConstantsAddress.ADDRESS_EB_DELETE_PRODUCT_CATEGORY,
          message.body());
      productCategoryRepositories.deleteProductCategory(message.body().toString())
          .setHandler(res -> {
            replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT,
                Constants.MESSAGE_DELETE_SUCCESS);
          });
    });
  }
}