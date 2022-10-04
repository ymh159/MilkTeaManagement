import entity.ProductEntity;
import entity.TypeValueReply;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.ProductRepositories;
import repositories.impl.ProductRepositoriesImpl;
import services.ProductServices;
import services.impl.ProductServicesImpl;
import utils.Constants;
import utils.ConstantsAddress;
import utils.ReplyMessageEB;

public class ProductVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProductVerticle.class);

  @Override
  public void start() throws Exception {
    ProductRepositories productRepositories = new ProductRepositoriesImpl(vertx);
    ProductServices productServices = new ProductServicesImpl(vertx);
    EventBus eb = vertx.eventBus();
    ReplyMessageEB replyMessageEB = new ReplyMessageEB();

    // get product by id
    eb.consumer(ConstantsAddress.ADDRESS_EB_GET_PRODUCT_BY_ID, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
          ConstantsAddress.ADDRESS_EB_GET_PRODUCT_BY_ID,
          message.body());
      productRepositories.findProductById(message.body().toString()).setHandler(res -> {
        replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT);
      });
    });

    // get all product
    eb.consumer(ConstantsAddress.ADDRESS_EB_GET_PRODUCT, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_GET_PRODUCT,
          message.body());
      productServices.getAllProduct().setHandler(res -> {
        replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_ARRAY);
      });
    });

    // insert product
    eb.consumer(ConstantsAddress.ADDRESS_EB_INSERT_PRODUCT, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_INSERT_PRODUCT,
          message.body());
      JsonObject json = JsonObject.mapFrom(message.body());
      ProductEntity productEntity = json.mapTo(ProductEntity.class);
      productRepositories.insertProduct(productEntity).setHandler(res -> {
        replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT,
            Constants.MESSAGE_INSERT_SUCCESS);
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
            replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT,
                Constants.MESSAGE_UPDATE_SUCCESS);
          });
    });

    // delete product
    eb.consumer(ConstantsAddress.ADDRESS_EB_DELETE_PRODUCT, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_DELETE_PRODUCT,
          message.body());
      productRepositories.deleteProduct(message.body().toString()).setHandler(res -> {
        replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT,
            Constants.MESSAGE_DELETE_SUCCESS);
      });
    });

    // get product detail
    eb.consumer(ConstantsAddress.ADDRESS_EB_GET_PRODUCT_DETAIL_BY_ID, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
          ConstantsAddress.ADDRESS_EB_GET_PRODUCT_DETAIL_BY_ID,
          message.body());
      productServices.getProductDetailByID(message.body().toString()).setHandler(res -> {
        replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT);
      });
    });

    // get all product detail
    eb.consumer(ConstantsAddress.ADDRESS_EB_GET_ALL_PRODUCT_DETAIL, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
          ConstantsAddress.ADDRESS_EB_GET_ALL_PRODUCT_DETAIL,
          message.body());
      productServices.getAllProductDetail().setHandler(res -> {
        replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_ARRAY);
      });
    });
  }
}
