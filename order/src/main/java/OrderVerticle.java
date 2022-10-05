import DTO.OrderInfoDTO;
import entity.OrderDetailEntity;
import entity.OrderEntity;
import entity.TypeValueReply;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.OrderDetailRepositories;
import repositories.OrderRepositories;
import repositories.impl.OrderDetailRepositoriesImpl;
import repositories.impl.OrderRepositoriesImpl;
import services.OrderProductSevices;
import services.impl.OrderProductServicesImpl;
import utils.Constants;
import utils.AddressConstants;
import utils.ReplyMessageEB;

public class OrderVerticle extends AbstractVerticle {

  private static final Logger logger = LoggerFactory.getLogger(OrderVerticle.class);
  ReplyMessageEB replyMessageEB;

  @Override
  public void start() {
    eventBusOrder(vertx);
    eventBusOrderDetail(vertx);
    eventBusOrderProduct(vertx);
    replyMessageEB = new ReplyMessageEB();
  }

  public void eventBusOrderProduct(Vertx vertx) {
    OrderProductSevices orderProductSevices = new OrderProductServicesImpl(vertx);
    EventBus eb = vertx.eventBus();

    // get order product detail
    eb.consumer(AddressConstants.ADDRESS_EB_GET_ORDER_PRODUCT_DETAIL, message -> {
      logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
          AddressConstants.ADDRESS_EB_GET_ORDER_DETAIL,
          message.body());
      orderProductSevices.getOrderProductDetail(message.body().toString()).setHandler(res -> {
        replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT);
      });
    });

    eb.consumer(AddressConstants.ADDRESS_EB_ORDER_PRODUCT, message -> {
      logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_ORDER_PRODUCT,
          message.body());
      JsonObject jsonObject = JsonObject.mapFrom(message.body());
      OrderInfoDTO orderInfoDTO = jsonObject.mapTo(OrderInfoDTO.class);
      orderProductSevices.orderProduct(orderInfoDTO).setHandler(resOrder -> {
        JsonObject jsonMessage = new JsonObject();
        if (resOrder.succeeded()) {
          orderProductSevices.getOrderProductDetail(resOrder.result())
              .setHandler(resGet -> {
                replyMessageEB.replyMessage(message, resGet, TypeValueReply.JSON_OBJECT);
              });
        } else {
          jsonMessage
              .put(Constants.STATUS, Constants.FAIL)
              .put(Constants.MESSAGE, resOrder.cause().getMessage());
          message.reply(jsonMessage);
        }
      });
    });
  }

  public void eventBusOrder(Vertx vertx) {
    OrderRepositories orderRepositories = new OrderRepositoriesImpl(vertx);
    EventBus eb = vertx.eventBus();

    // get order by id
    eb.consumer(AddressConstants.ADDRESS_EB_GET_ORDER_BY_ID, message -> {
      logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_GET_ORDER_BY_ID,
          message.body());
      orderRepositories.findOrderById(message.body().toString()).setHandler(res -> {
        replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT);
      });
    });

    // get all order
    eb.consumer(AddressConstants.ADDRESS_EB_GET_ORDER, message -> {
      logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_GET_ORDER,
          message.body());
      orderRepositories.getOrders().setHandler(res -> {
        replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_ARRAY);
      });
    });

    // insert order
    eb.consumer(AddressConstants.ADDRESS_EB_INSERT_ORDER, message -> {
      logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_INSERT_ORDER,
          message.body());
      JsonObject json = JsonObject.mapFrom(message.body());
      OrderEntity orderEntity = json.mapTo(OrderEntity.class);
      orderRepositories.insertOrder(orderEntity).setHandler(res -> {
        replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT,
            Constants.MESSAGE_INSERT_SUCCESS);
      });
    });

    // update order
    eb.consumer(AddressConstants.ADDRESS_EB_UPDATE_ORDER, message -> {
      logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_UPDATE_ORDER,
          message.body());
      JsonObject json = JsonObject.mapFrom(message.body());
      JsonObject jsonUpdate = JsonObject.mapFrom(json.getValue(Constants.JSON_UPDATE));
      OrderEntity orderEntity = jsonUpdate.mapTo(OrderEntity.class);
      orderRepositories.updateOrder(json.getValue(Constants._ID).toString(), orderEntity)
          .setHandler(res -> {
            replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT,
                Constants.MESSAGE_UPDATE_SUCCESS);
          });
    });

    // delete order
    eb.consumer(AddressConstants.ADDRESS_EB_DELETE_ORDER, message -> {
      logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_DELETE_ORDER,
          message.body());
      orderRepositories.deleteOrder(message.body().toString()).setHandler(res -> {
        replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT,
            Constants.MESSAGE_DELETE_SUCCESS);
      });
    });
  }

  public void eventBusOrderDetail(Vertx vertx) {
    OrderDetailRepositories orderDetailRepositories = new OrderDetailRepositoriesImpl(vertx);
    OrderProductSevices orderProductSevices = new OrderProductServicesImpl(vertx);
    EventBus eb = vertx.eventBus();

    // get orderDetail by id
    eb.consumer(AddressConstants.ADDRESS_EB_GET_ORDER_DETAIL_BY_ID, message -> {
      logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
          AddressConstants.ADDRESS_EB_GET_ORDER_DETAIL_BY_ID,
          message.body());
      orderDetailRepositories.findOrderDetailById(message.body().toString()).setHandler(res -> {
        replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT);
      });
    });

    // get orderDetail by order_id
    eb.consumer(AddressConstants.ADDRESS_EB_GET_ORDER_DETAIL_BY_ORDER_ID, message -> {
      logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
          AddressConstants.ADDRESS_EB_GET_ORDER_DETAIL_BY_ORDER_ID,
          message.body());
      orderProductSevices.findOrderDetailByOrderId(message.body().toString()).setHandler(res -> {
        replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_ARRAY);
      });
    });

    // get all orderDetail
    eb.consumer(AddressConstants.ADDRESS_EB_GET_ORDER_DETAIL, message -> {
      logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
          AddressConstants.ADDRESS_EB_GET_ORDER_DETAIL,
          message.body());
      orderDetailRepositories.getOrderDetails().setHandler(res -> {
        replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_ARRAY);
      });
    });

    // insert orderDetail
    eb.consumer(AddressConstants.ADDRESS_EB_INSERT_ORDER_DETAIL, message -> {
      logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
          AddressConstants.ADDRESS_EB_INSERT_ORDER_DETAIL,
          message.body());
      JsonObject json = JsonObject.mapFrom(message.body());
      OrderDetailEntity orderDetailEntity = json.mapTo(OrderDetailEntity.class);
      orderDetailRepositories.insertOrderDetail(orderDetailEntity).setHandler(res -> {
        replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT,
            Constants.MESSAGE_INSERT_SUCCESS);
      });
    });

    // update orderDetail
    eb.consumer(AddressConstants.ADDRESS_EB_UPDATE_ORDER_DETAIL, message -> {
      logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
          AddressConstants.ADDRESS_EB_UPDATE_ORDER_DETAIL,
          message.body());
      JsonObject json = JsonObject.mapFrom(message.body());
      JsonObject jsonUpdate = JsonObject.mapFrom(json.getValue(Constants.JSON_UPDATE));
      OrderDetailEntity orderDetailEntity = jsonUpdate.mapTo(OrderDetailEntity.class);
      orderDetailRepositories.updateOrderDetail(json.getValue(Constants._ID).toString(),
              orderDetailEntity)
          .setHandler(res -> {
            replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT,
                Constants.MESSAGE_UPDATE_SUCCESS);
          });
    });

    // delete orderDetail
    eb.consumer(AddressConstants.ADDRESS_EB_DELETE_ORDER_DETAIL, message -> {
      logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
          AddressConstants.ADDRESS_EB_DELETE_ORDER_DETAIL,
          message.body());
      orderDetailRepositories.deleteOrderDetail(message.body().toString()).setHandler(res -> {
        replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT,
            Constants.MESSAGE_DELETE_SUCCESS);
      });
    });
  }
}
