package services.impl;

import DTO.OrderInfoDTO;
import DTO.ProductOrderDTO;
import DTO.ProductOrderDetailDTO;
import entity.CustomerEntity;
import entity.OrderDetailEntity;
import entity.OrderEntity;
import entity.ProductEntity;
import entity.UserEntity;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.OrderDetailRepositories;
import repositories.OrderRepositories;
import repositories.impl.OrderDetailRepositoriesImpl;
import repositories.impl.OrderRepositoriesImpl;
import services.OrderProductSevices;
import utils.Constants;
import utils.ConstantsAddress;

public class OrderProductSevicesImpl implements OrderProductSevices {

  private static final Logger LOGGER = LoggerFactory.getLogger(OrderProductSevices.class);
  private final OrderDetailRepositories orderDetailRepositories;
  private final OrderRepositories orderRepositories;

  private final Vertx vertx;

  public OrderProductSevicesImpl(Vertx vertx) {
    orderDetailRepositories = new OrderDetailRepositoriesImpl(vertx);
    orderRepositories = new OrderRepositoriesImpl(vertx);
    this.vertx = vertx;
  }

  @Override
  public Future<Void> insertOrderDetail(OrderDetailEntity orderDetailEntity) {
    Future<Void> future = Future.future();
    orderDetailRepositories.insertOrderDetail(orderDetailEntity).setHandler(res -> {
      if (res.succeeded()) {
        future.complete();
      } else {
        future.fail(res.cause());
      }
    });
    return future;
  }

  @Override
  public Future<String> insertOrder(OrderEntity orderEntity) {
    Future<String> future = Future.future();
    orderRepositories.insertOrder(orderEntity).setHandler(res -> {
      if (res.succeeded()) {
        future.complete(res.result());
      } else {
        future.fail(res.cause());
      }
    });
    return future;
  }

  @Override
  public Future<String> orderProduct(OrderInfoDTO orderInfoDTO) {
    Future<String> future = Future.future();
    JsonObject jsonOrder = new JsonObject();
    List<ProductOrderDTO> productOrderDTOList = orderInfoDTO.getProduct_order();
    int total_value = 0;
    int total_price = 0;

    for (ProductOrderDTO productOrderDTO : productOrderDTOList
    ) {
      total_value += productOrderDTO.getValue();
      total_price += productOrderDTO.getValue() * productOrderDTO.getPrice();
    }

    DateFormat dateFormat = new SimpleDateFormat(Constants.FORMAT_DATE_JSON);
    jsonOrder
        .put(Constants.DATE_ORDER, dateFormat.format(new Date()))
        .put(Constants.USER_ID, orderInfoDTO.getUser_id())
        .put(Constants.CUSTOMER_ID, orderInfoDTO.getCustomer_id())
        .put(Constants.TOTAL_VALUE, total_value)
        .put(Constants.TOTAL_PRICE, total_price);
    OrderEntity orderEntity = jsonOrder.mapTo(OrderEntity.class);

    insertOrder(orderEntity).setHandler(res -> {
      List<Future<Void>> futureUpdateValueList = productOrderDTOList.stream()
          .map(productOrderDTO -> {
            OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
            orderDetailEntity.setOrder_id(res.result());
            orderDetailEntity.setProduct_id(productOrderDTO.getProduct_id());
            orderDetailEntity.setValue(productOrderDTO.getValue());
            orderDetailEntity.setPrice(productOrderDTO.getPrice());
            orderDetailEntity.setName(productOrderDTO.getName());

            // insert order detail
            Future<Void> futureInsertOrderDetail = insertOrderDetail(orderDetailEntity);
            Future<Void> futureUpdateValue = updateValueProduct(orderDetailEntity);
            Future<Void> futureResult = Future.future();

            CompositeFuture.all(futureInsertOrderDetail, futureUpdateValue)
                .setHandler(compositeUpdateValue -> {
                  if (compositeUpdateValue.succeeded()) {
                    futureResult.complete();
                  } else {
                    futureResult.fail(compositeUpdateValue.cause());
                  }
                });
            return futureResult;
          }).toList();

      CompositeFuture.all(new ArrayList<>(futureUpdateValueList)).setHandler(compositeOrder -> {
        if (compositeOrder.succeeded()) {
          future.complete(res.result());
        } else {
          future.fail(compositeOrder.cause());
        }
      });
    });

    return future;
  }

  @Override
  public Future<OrderEntity> findOrderById(String id) {
    Future<OrderEntity> future = Future.future();
    orderRepositories.findOrderById(id).setHandler(res -> {
      if (res.succeeded()) {
        future.complete(res.result());
      } else {
        future.fail(res.cause());
      }
    });
    return future;
  }

  @Override
  public Future<OrderDetailEntity> findOrderDetailById(String id) {
    Future<OrderDetailEntity> future = Future.future();
    orderDetailRepositories.findOrderDetailById(id).setHandler(res -> {
      if (res.succeeded()) {
        future.complete(res.result());
      } else {
        future.fail(res.cause());
      }
    });
    return future;
  }

  @Override
  public Future<List<OrderDetailEntity>> findOrderDetailByOrderId(String id) {
    Future<List<OrderDetailEntity>> future = Future.future();
    orderDetailRepositories.findOrderDetailByOrderId(id).setHandler(res -> {
      if (res.succeeded()) {
        future.complete(res.result());
      } else {
        future.fail(res.cause());
      }
    });
    return future;
  }

  @Override
  public Future<ProductOrderDetailDTO> getOrderProductDetail(String id) {
    Future<ProductOrderDetailDTO> future = Future.future();
    // get order
    findOrderById(id).setHandler(resOrder -> {

      if (resOrder.succeeded()) {
        Future<UserEntity> futureGetUser = findUserById(resOrder.result().getUser_id());
        Future<CustomerEntity> futureGetCustomer = findCustomerById(
            resOrder.result().getCustomer_id());
        Future<List<ProductOrderDTO>> futureListProDTO = Future.future();

        // get list order detail
        findOrderDetailByOrderId(id).setHandler(resOrderDetail -> {
          // get product order detail
          List<OrderDetailEntity> orderDetailEntityList = resOrderDetail.result();
          List<ProductOrderDTO> productOrderDTOList = orderDetailEntityList.stream()
              .map(orderDetailEntity -> {
                JsonObject jsonObject = JsonObject.mapFrom(orderDetailEntity);
                return jsonObject.mapTo(ProductOrderDTO.class);
              }).collect(Collectors.toList());
          futureListProDTO.complete(productOrderDTOList);
        });

        CompositeFuture.all(futureListProDTO, futureGetUser, futureGetCustomer)
            .setHandler(compose -> {
              ProductOrderDetailDTO productOrderDetailDTO
                  = JsonObject.mapFrom(resOrder.result())
                  .mapTo(ProductOrderDetailDTO.class);
              UserEntity userEntity = compose.result().resultAt(1);
              CustomerEntity customerEntity = compose.result().resultAt(2);
              productOrderDetailDTO.setProduct_order(compose.result().resultAt(0));
              productOrderDetailDTO.setUser_name(userEntity.getUser_name());
              productOrderDetailDTO.setCustomer_name(customerEntity.getCustomer_name());
              productOrderDetailDTO.setPhone(customerEntity.getPhone());

              future.complete(productOrderDetailDTO);
            });

      } else {
        future.fail(resOrder.cause());
      }

    });
    return future;
  }

  public Future<Void> updateValueProduct(OrderDetailEntity orderDetailEntity) {
    Future<Void> future = Future.future();

    vertx.eventBus().send(ConstantsAddress.ADDRESS_EB_GET_PRODUCT_BY_ID,
        orderDetailEntity.getProduct_id(), resGetProduct -> {
          if (resGetProduct.succeeded()) {
            ProductEntity productEntity = JsonObject.mapFrom(resGetProduct.result().body())
                .mapTo(ProductEntity.class);
            if (productEntity.getValue() - orderDetailEntity.getValue() >= 0) {
              productEntity.setValue(productEntity.getValue() - orderDetailEntity.getValue());
              JsonObject jsonObject = new JsonObject();
              jsonObject.put(Constants._ID, productEntity.getId());
              jsonObject.put(Constants.JSON_UPDATE, JsonObject.mapFrom(productEntity));
              vertx.eventBus().send(ConstantsAddress.ADDRESS_EB_UPDATE_PRODUCT, jsonObject
                  , resUpdateProduct -> {
                    if (resUpdateProduct.succeeded()) {
                      future.complete();
                    } else {
                      future.fail(resUpdateProduct.cause());
                    }
                  });
            } else {
              LOGGER.info("Can't order product. Value product:{}, Value order:{}",
                  productEntity.getValue(), orderDetailEntity.getValue());
              future.fail(new IllegalArgumentException("Product " + productEntity.getName() + " Sold out"));
            }
          } else {
            future.fail(resGetProduct.cause());
          }
        });

    return future;
  }

  Future<UserEntity> findUserById(String id) {
    Future<UserEntity> future = Future.future();
    vertx.eventBus()
        .send(ConstantsAddress.ADDRESS_EB_GET_USER_BY_ID, id,
            messageReply -> {
              if (messageReply.succeeded()) {
                UserEntity userEntity = JsonObject.mapFrom(messageReply.result().body())
                    .mapTo(UserEntity.class);
                future.complete(userEntity);
              } else {
                future.fail(messageReply.cause());
              }
            });
    return future;
  }

  Future<CustomerEntity> findCustomerById(String id) {
    Future<CustomerEntity> future = Future.future();
    vertx.eventBus().send(ConstantsAddress.ADDRESS_EB_GET_CUSTOMER_BY_ID, id, messageReply -> {
      if (messageReply.succeeded()) {
        CustomerEntity customerEntity = JsonObject.mapFrom(messageReply.result().body())
            .mapTo(CustomerEntity.class);
        future.complete(customerEntity);
      } else {
        future.fail(messageReply.cause());
      }
    });

    return future;
  }

}
