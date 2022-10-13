package eb;


import DTO.OrderInfoDTO;
import entity.OrderDetailEntity;
import entity.OrderEntity;
import entity.TypeValueReply;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.OrderDetailRepositories;
import repositories.OrderRepositories;
import repositories.impl.OrderDetailRepositoriesImpl;
import repositories.impl.OrderRepositoriesImpl;
import services.OrderProductSevices;
import services.impl.OrderProductServicesImpl;
import utils.AddressConstants;
import utils.Constants;
import utils.ReplyMessageEB;
import utils.ReplyMessageSingle;

public class OrderEventBusServices {
    private static final Logger logger = LoggerFactory.getLogger(OrderEventBusServices.class);
    private final ReplyMessageEB replyMessageEB;
    private final OrderProductSevices orderProductSevices;
    private final OrderRepositories orderRepositories;
    private final OrderDetailRepositories orderDetailRepositories;

    public OrderEventBusServices(Vertx vertx) {
        this.orderProductSevices = new OrderProductServicesImpl(vertx);
        this.replyMessageEB = new ReplyMessageEB();
        this.orderDetailRepositories = new OrderDetailRepositoriesImpl(vertx);
        this.orderRepositories = new OrderRepositoriesImpl(vertx);
    }

    public <T> void getOrderProductDetail(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
                AddressConstants.ADDRESS_EB_GET_ORDER_PRODUCT_DETAIL,
                message.body());
        orderProductSevices.getOrderProductDetail(message.body().toString()).setHandler(res -> {
            replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT);
        });
    }

    public <T> void orderProductServices(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_ORDER_PRODUCT,
                message.body());
        JsonObject jsonObject = JsonObject.mapFrom(message.body());
        OrderInfoDTO orderInfoDTO = jsonObject.mapTo(OrderInfoDTO.class);

        JsonObject jsonMessage = new JsonObject();
        orderProductSevices.orderSingle(orderInfoDTO).subscribe(res -> {

            orderProductSevices.getOrderProductDetail(res)
                    .setHandler(resGet -> {
                        replyMessageEB.replyMessage(message, resGet, TypeValueReply.JSON_OBJECT);
                    });
        }, throwable -> {
            jsonMessage
                    .put(Constants.STATUS, Constants.FAIL)
                    .put(Constants.MESSAGE, throwable.getMessage());
            message.reply(jsonMessage);
        });
    }

    public <T> void getOrderById(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_GET_ORDER_BY_ID,
                message.body());
        orderRepositories.findOrderById(message.body().toString()).setHandler(res -> {
            replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT);
        });
    }

    public <T> void getOrders(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_GET_ORDER,
                message.body());
        orderRepositories.getOrders().setHandler(res -> {
            replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_ARRAY);
        });
    }

    public <T> void insertOrder(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_INSERT_ORDER,
                message.body());
        JsonObject json = JsonObject.mapFrom(message.body());
        OrderEntity orderEntity = json.mapTo(OrderEntity.class);
        orderRepositories.insertOrder(orderEntity).setHandler(res -> {
            replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT,
                    Constants.MESSAGE_INSERT_SUCCESS);
        });
    }

    public <T> void updateOrder(Message<T> message) {
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
    }

    public <T> void deleteOrder(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, AddressConstants.ADDRESS_EB_DELETE_ORDER,
                message.body());
        ReplyMessageSingle.replyMessage(message,
                orderRepositories.deleteOrder(message.body().toString()),
                Constants.MESSAGE_DELETE_SUCCESS);
    }

    public <T> void getOrderDetailById(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
                AddressConstants.ADDRESS_EB_GET_ORDER_DETAIL_BY_ID,
                message.body());
        orderDetailRepositories.findOrderDetailById(message.body().toString()).setHandler(res -> {
            replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_OBJECT);
        });
    }

    public <T> void getOrderDetailByOrderId(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
                AddressConstants.ADDRESS_EB_GET_ORDER_DETAIL_BY_ORDER_ID,
                message.body());
        orderProductSevices.findOrderDetailByOrderId(message.body().toString()).setHandler(res -> {
            replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_ARRAY);
        });
    }

    public <T> void getAllOrderDetail(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
                AddressConstants.ADDRESS_EB_GET_ORDER_DETAIL,
                message.body());
        orderDetailRepositories.getOrderDetails().setHandler(res -> {
            replyMessageEB.replyMessage(message, res, TypeValueReply.JSON_ARRAY);
        });
    }

    public <T> void insertOrderDetail(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
                AddressConstants.ADDRESS_EB_INSERT_ORDER_DETAIL,
                message.body());
        JsonObject json = JsonObject.mapFrom(message.body());
        OrderDetailEntity orderDetailEntity = json.mapTo(OrderDetailEntity.class);
        ReplyMessageSingle.replyMessage(message,
                orderDetailRepositories.insertOrderDetail(orderDetailEntity),
                Constants.MESSAGE_INSERT_SUCCESS);
    }

    public <T> void updateOrderDetail(Message<T> message) {
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
    }

    public <T> void deleteOrderDetail(Message<T> message) {
        logger.info(Constants.LOGGER_ADDRESS_AND_MESSAGE,
                AddressConstants.ADDRESS_EB_DELETE_ORDER_DETAIL,
                message.body());
        ReplyMessageSingle.replyMessage(message,
                orderDetailRepositories.deleteOrderDetail(message.body().toString()),
                Constants.MESSAGE_DELETE_SUCCESS);
    }
}

