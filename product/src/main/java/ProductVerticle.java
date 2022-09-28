import entity.ProductEntity;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.ProductRepositories;
import repositories.impl.ProductRepositoriesImpl;
import utils.Constants;
import utils.ConstantsAddress;

public class ProductVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProductVerticle.class);

  @Override
  public void start() throws Exception {
    ProductRepositories productRepositories = new ProductRepositoriesImpl(vertx);
    EventBus eb = vertx.eventBus();

    // get product by id
    eb.consumer(ConstantsAddress.ADDRESS_EB_GET_PRODUCT_BY_ID, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_GET_PRODUCT_BY_ID,
          message.body());
      productRepositories.findProductById(message.body().toString()).setHandler(res -> {
        if (res.succeeded()) {
          ProductEntity productEntity = res.result();
          JsonObject jsonObject = JsonObject.mapFrom(productEntity);
          message.reply(jsonObject);
        } else {
          message.reply(Constants.MESSAGE_GET_FAIL);
        }
      });
    });

    // get all product
    eb.consumer(ConstantsAddress.ADDRESS_EB_GET_PRODUCT, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_GET_PRODUCT,
          message.body());
      productRepositories.getProducts().setHandler(res -> {
        if (res.succeeded()) {
          List<ProductEntity> productEntityList = res.result();
          JsonArray jsonArray = new JsonArray(productEntityList);
          message.reply(jsonArray);
        } else {
          message.reply(Constants.MESSAGE_GET_FAIL);
        }
      });
    });

    // insert product
    eb.consumer(ConstantsAddress.ADDRESS_EB_INSERT_PRODUCT, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_INSERT_PRODUCT,
          message.body());
      JsonObject json = JsonObject.mapFrom(message.body());
      ProductEntity productEntity = json.mapTo(ProductEntity.class);
      productRepositories.insertProduct(productEntity).setHandler(res -> {
        if (res.succeeded()) {
          message.reply(Constants.MESSAGE_INSERT_SUCCESS);
        } else {
          message.reply(Constants.MESSAGE_INSERT_FAIL);
        }
      });
    });

    // update product
    eb.consumer(ConstantsAddress.ADDRESS_EB_UPDATE_PRODUCT, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_UPDATE_PRODUCT,
          message.body());
      JsonObject json = JsonObject.mapFrom(message.body());
      JsonObject jsonUpdate = JsonObject.mapFrom(json.getValue(Constants.JSON_UPDATE));
      ProductEntity productEntity = jsonUpdate.mapTo(ProductEntity.class);
      productRepositories.updateProduct(json.getValue(Constants._ID).toString(), productEntity)
          .setHandler(res -> {
            if (res.succeeded()) {
              message.reply(Constants.MESSAGE_UPDATE_SUCCESS);
            } else {
              message.reply(Constants.MESSAGE_UPDATE_FAIL);
            }
          });
    });

    // delete product
    eb.consumer(ConstantsAddress.ADDRESS_EB_DELETE_PRODUCT, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_DELETE_PRODUCT,
          message.body());
      productRepositories.deleteProduct(message.body().toString()).setHandler(res -> {
        if (res.succeeded()) {
          message.reply(Constants.MESSAGE_DELETE_SUCCESS);
        } else {
          message.reply(Constants.MESSAGE_DELETE_FAIL);
        }
      });
    });
  }
}
