import entity.ProductCategoryEntity;
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

public class ProductCategoryVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProductCategoryVerticle.class);

  @Override
  public void start() throws Exception {
    ProductCategoryRepositories productCategoryRepositories = new ProductCategoryRepositoriesImpl(vertx);
    EventBus eb = vertx.eventBus();

    // get productCategory by id
    eb.consumer(ConstantsAddress.ADDRESS_EB_GET_PRODUCT_CATEGORY_BY_ID, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_GET_PRODUCT_CATEGORY_BY_ID,
          message.body());
      productCategoryRepositories.findProductCategoryById(message.body().toString()).setHandler(res -> {
        if (res.succeeded()) {
          ProductCategoryEntity productCategoryEntity = res.result();
          JsonObject jsonObject = JsonObject.mapFrom(productCategoryEntity);
          message.reply(jsonObject);
        } else {
          message.reply(Constants.MESSAGE_GET_FAIL);
        }
      });
    });

    // get all productCategory
    eb.consumer(ConstantsAddress.ADDRESS_EB_GET_PRODUCT_CATEGORY, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_GET_PRODUCT_CATEGORY,
          message.body());
      productCategoryRepositories.getProductCategorys().setHandler(res -> {
        if (res.succeeded()) {
          List<ProductCategoryEntity> productCategoryEntityList = res.result();
          JsonArray jsonArray = new JsonArray(productCategoryEntityList);
          message.reply(jsonArray);
        } else {
          message.reply(Constants.MESSAGE_GET_FAIL);
        }
      });
    });

    // insert productCategory
    eb.consumer(ConstantsAddress.ADDRESS_EB_INSERT_PRODUCT_CATEGORY, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_INSERT_PRODUCT_CATEGORY,
          message.body());
      JsonObject json = JsonObject.mapFrom(message.body());
      ProductCategoryEntity productCategoryEntity = json.mapTo(ProductCategoryEntity.class);
      productCategoryRepositories.insertProductCategory(productCategoryEntity).setHandler(res -> {
        if (res.succeeded()) {
          message.reply(Constants.MESSAGE_INSERT_SUCCESS);
        } else {
          message.reply(Constants.MESSAGE_INSERT_FAIL);
        }
      });
    });

    // update productCategory
    eb.consumer(ConstantsAddress.ADDRESS_EB_UPDATE_PRODUCT_CATEGORY, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_UPDATE_PRODUCT_CATEGORY,
          message.body());
      JsonObject json = JsonObject.mapFrom(message.body());
      JsonObject jsonUpdate = JsonObject.mapFrom(json.getValue(Constants.JSON_UPDATE));
      ProductCategoryEntity productCategoryEntity = jsonUpdate.mapTo(ProductCategoryEntity.class);
      productCategoryRepositories.updateProductCategory(json.getValue(Constants._ID).toString(), productCategoryEntity)
          .setHandler(res -> {
            if (res.succeeded()) {
              message.reply(Constants.MESSAGE_UPDATE_SUCCESS);
            } else {
              message.reply(Constants.MESSAGE_UPDATE_FAIL);
            }
          });
    });

    // delete productCategory
    eb.consumer(ConstantsAddress.ADDRESS_EB_DELETE_PRODUCT_CATEGORY, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_DELETE_PRODUCT_CATEGORY,
          message.body());
      productCategoryRepositories.deleteProductCategory(message.body().toString()).setHandler(res -> {
        if (res.succeeded()) {
          message.reply(Constants.MESSAGE_DELETE_SUCCESS);
        } else {
          message.reply(Constants.MESSAGE_DELETE_FAIL);
        }
      });
    });
  }
}
