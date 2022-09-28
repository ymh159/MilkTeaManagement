import entity.OrderDetailEntity;
import entity.OrderEntity;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.OrderDetailRepositories;
import repositories.OrderRepositories;
import repositories.impl.OrderDetailRepositoriesImpl;
import repositories.impl.OrderRepositoriesImpl;
import utils.Constants;
import utils.ConstantsAddress;

public class OrderVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(OrderVerticle.class);

  @Override
  public void start() throws Exception {
    eventBusOrder(vertx);
    eventBusOrderDetail(vertx);
  }

  public void eventBusOrder(Vertx vertx){
    OrderRepositories orderRepositories = new OrderRepositoriesImpl(vertx);
    EventBus eb = vertx.eventBus();

    // get order by id
    eb.consumer(ConstantsAddress.ADDRESS_EB_GET_ORDER_BY_ID, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_GET_ORDER_BY_ID,
          message.body());
      orderRepositories.findOrderById(message.body().toString()).setHandler(res -> {
        if (res.succeeded()) {
          OrderEntity orderEntity = res.result();
          JsonObject jsonObject = JsonObject.mapFrom(orderEntity);
          message.reply(jsonObject);
        } else {
          message.reply(Constants.MESSAGE_GET_FAIL);
        }
      });
    });

    // get all order
    eb.consumer(ConstantsAddress.ADDRESS_EB_GET_ORDER, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_GET_ORDER,
          message.body());
      orderRepositories.getOrders().setHandler(res -> {
        if (res.succeeded()) {
          List<OrderEntity> orderEntityList = res.result();
          JsonArray jsonArray = new JsonArray(orderEntityList);
          message.reply(jsonArray);
        } else {
          message.reply(Constants.MESSAGE_GET_FAIL);
        }
      });
    });

    // insert order
    eb.consumer(ConstantsAddress.ADDRESS_EB_INSERT_ORDER, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_INSERT_ORDER,
          message.body());
      JsonObject json = JsonObject.mapFrom(message.body());
      OrderEntity orderEntity = json.mapTo(OrderEntity.class);
      orderRepositories.insertOrder(orderEntity).setHandler(res -> {
        if (res.succeeded()) {
          message.reply(Constants.MESSAGE_INSERT_SUCCESS);
        } else {
          message.reply(Constants.MESSAGE_INSERT_FAIL);
        }
      });
    });

    // update order
    eb.consumer(ConstantsAddress.ADDRESS_EB_UPDATE_ORDER, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_UPDATE_ORDER,
          message.body());
      JsonObject json = JsonObject.mapFrom(message.body());
      JsonObject jsonUpdate = JsonObject.mapFrom(json.getValue(Constants.JSON_UPDATE));
      OrderEntity orderEntity = jsonUpdate.mapTo(OrderEntity.class);
      orderRepositories.updateOrder(json.getValue(Constants._ID).toString(), orderEntity)
          .setHandler(res -> {
            if (res.succeeded()) {
              message.reply(Constants.MESSAGE_UPDATE_SUCCESS);
            } else {
              message.reply(Constants.MESSAGE_UPDATE_FAIL);
            }
          });
    });

    // delete order
    eb.consumer(ConstantsAddress.ADDRESS_EB_DELETE_ORDER, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_DELETE_ORDER,
          message.body());
      orderRepositories.deleteOrder(message.body().toString()).setHandler(res -> {
        if (res.succeeded()) {
          message.reply(Constants.MESSAGE_DELETE_SUCCESS);
        } else {
          message.reply(Constants.MESSAGE_DELETE_FAIL);
        }
      });
    });
  }

  public void eventBusOrderDetail(Vertx vertx){
    OrderDetailRepositories orderDetailRepositories = new OrderDetailRepositoriesImpl(vertx);
    EventBus eb = vertx.eventBus();

    // get orderDetail by id
    eb.consumer(ConstantsAddress.ADDRESS_EB_GET_ORDER_DETAIL_BY_ID, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_GET_ORDER_DETAIL_BY_ID,
          message.body());
      orderDetailRepositories.findOrderDetailById(message.body().toString()).setHandler(res -> {
        if (res.succeeded()) {
          OrderDetailEntity orderDetailEntity = res.result();
          JsonObject jsonObject = JsonObject.mapFrom(orderDetailEntity);
          message.reply(jsonObject);
        } else {
          message.reply(Constants.MESSAGE_GET_FAIL);
        }
      });
    });

    // get all orderDetail
    eb.consumer(ConstantsAddress.ADDRESS_EB_GET_ORDER_DETAIL, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_GET_ORDER_DETAIL,
          message.body());
      orderDetailRepositories.getOrderDetails().setHandler(res -> {
        if (res.succeeded()) {
          List<OrderDetailEntity> orderDetailEntityList = res.result();
          JsonArray jsonArray = new JsonArray(orderDetailEntityList);
          message.reply(jsonArray);
        } else {
          message.reply(Constants.MESSAGE_GET_FAIL);
        }
      });
    });

    // insert orderDetail
    eb.consumer(ConstantsAddress.ADDRESS_EB_INSERT_ORDER_DETAIL, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_INSERT_ORDER_DETAIL,
          message.body());
      JsonObject json = JsonObject.mapFrom(message.body());
      OrderDetailEntity orderDetailEntity = json.mapTo(OrderDetailEntity.class);
      orderDetailRepositories.insertOrderDetail(orderDetailEntity).setHandler(res -> {
        if (res.succeeded()) {
          message.reply(Constants.MESSAGE_INSERT_SUCCESS);
        } else {
          message.reply(Constants.MESSAGE_INSERT_FAIL);
        }
      });
    });

    // update orderDetail
    eb.consumer(ConstantsAddress.ADDRESS_EB_UPDATE_ORDER_DETAIL, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_UPDATE_ORDER_DETAIL,
          message.body());
      JsonObject json = JsonObject.mapFrom(message.body());
      JsonObject jsonUpdate = JsonObject.mapFrom(json.getValue(Constants.JSON_UPDATE));
      OrderDetailEntity orderDetailEntity = jsonUpdate.mapTo(OrderDetailEntity.class);
      orderDetailRepositories.updateOrderDetail(json.getValue(Constants._ID).toString(), orderDetailEntity)
          .setHandler(res -> {
            if (res.succeeded()) {
              message.reply(Constants.MESSAGE_UPDATE_SUCCESS);
            } else {
              message.reply(Constants.MESSAGE_UPDATE_FAIL);
            }
          });
    });

    // delete orderDetail
    eb.consumer(ConstantsAddress.ADDRESS_EB_DELETE_ORDER_DETAIL, message -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, ConstantsAddress.ADDRESS_EB_DELETE_ORDER_DETAIL,
          message.body());
      orderDetailRepositories.deleteOrderDetail(message.body().toString()).setHandler(res -> {
        if (res.succeeded()) {
          message.reply(Constants.MESSAGE_DELETE_SUCCESS);
        } else {
          message.reply(Constants.MESSAGE_DELETE_FAIL);
        }
      });
    });
  }
}
