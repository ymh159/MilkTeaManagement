package services.impl;

import DTO.OrderInfoDTO;
import DTO.ProductOrderDTO;
import DTO.ProductOrderDetailDTO;
import entity.CustomerEntity;
import entity.OrderDetailEntity;
import entity.OrderEntity;
import entity.ProductEntity;
import entity.UserEntity;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import utils.AddressConstants;

public class OrderProductServicesImpl implements OrderProductSevices {

    private static final Logger logger = LoggerFactory.getLogger(OrderProductSevices.class);
    private final OrderDetailRepositories orderDetailRepositories;
    private final OrderRepositories orderRepositories;

    private final Vertx vertx;

    public OrderProductServicesImpl(Vertx vertx) {
        orderDetailRepositories = new OrderDetailRepositoriesImpl(vertx);
        orderRepositories = new OrderRepositoriesImpl(vertx);
        this.vertx = vertx;
    }

    @Override
    public Single<String> insertOrderDetail(OrderDetailEntity orderDetailEntity) {
        return Single.create(emitter -> {
            orderDetailRepositories.insertOrderDetail(orderDetailEntity).subscribe(res -> {
                        emitter.onSuccess(res);
                    }, throwable -> {
                        emitter.onError(throwable);
                    }
            );
        });
    }

    @Override
    public Single<String> insertOrder(OrderEntity orderEntity) {
        return Single.create(emitter -> {
            orderRepositories.insertOrder(orderEntity).setHandler(res -> {
                if (res.succeeded()) {
                    emitter.onSuccess(res.result());
                } else {
                    emitter.onError(res.cause());
                }
            });
        });
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
                            ProductOrderDetailDTO productOrderDetailDTO = JsonObject.mapFrom(resOrder.result())
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

    public Single<ProductEntity> updateQuantity(String id, int quantity,
                                                ProductEntity productEntity) {
        ProductEntity entity = new ProductEntity();
        productEntity.setQuantity(productEntity.getQuantity() - quantity);

        JsonObject jsonObject = new JsonObject();
        jsonObject.put(Constants._ID, id);
        jsonObject.put(Constants.JSON_UPDATE, JsonObject.mapFrom(productEntity));
        Single<ProductEntity> single = Single.create(emitter -> {
            vertx.eventBus().send(AddressConstants.ADDRESS_EB_UPDATE_PRODUCT, jsonObject, reply -> {
                JsonObject jsonMessage = JsonObject.mapFrom(reply.result().body());

                if (jsonMessage.getValue(Constants.STATUS).equals(Constants.PASS)) {
                    JsonObject jsonValue = JsonObject.mapFrom(jsonMessage.getValue(Constants.VALUE));
                    ProductEntity productBefore = jsonValue.mapTo(ProductEntity.class);

                    if (productBefore.getQuantity() - quantity >= 0) {
                        productBefore.setQuantity(productBefore.getQuantity() - quantity);
                        emitter.onSuccess(productBefore);
                    } else {
                        emitter.onError(new Throwable(new Throwable("Product: " + productBefore.getName()
                                + " quantity is not enough. Inventory quantity: " + productBefore.getQuantity())));
                    }

                } else {
                    emitter.onError(new Throwable(jsonMessage.getValue(Constants.MESSAGE).toString()));
                }
            });
        });

        return single;
    }

    private Future<UserEntity> findUserById(String id) {
        Future<UserEntity> future = Future.future();
        vertx.eventBus().send(AddressConstants.ADDRESS_EB_GET_USER_BY_ID, id, messageReply -> {
            if (messageReply.succeeded()) {
                JsonObject jsonMessage = JsonObject.mapFrom(messageReply.result().body());
                JsonObject jsonUser = (JsonObject) jsonMessage.getValue(Constants.VALUE);
                UserEntity userEntity = JsonObject.mapFrom(jsonUser).mapTo(UserEntity.class);
                future.complete(userEntity);
            } else {
                future.fail(messageReply.cause());
            }
        });
        return future;
    }

    private Future<CustomerEntity> findCustomerById(String id) {
        Future<CustomerEntity> future = Future.future();
        vertx.eventBus().send(AddressConstants.ADDRESS_EB_GET_CUSTOMER_BY_ID, id, messageReply -> {
            if (messageReply.succeeded()) {
                JsonObject jsonMessage = JsonObject.mapFrom(messageReply.result().body());
                JsonObject jsonCustomer = (JsonObject) jsonMessage.getValue(Constants.VALUE);
                CustomerEntity customerEntity = JsonObject.mapFrom(jsonCustomer)
                        .mapTo(CustomerEntity.class);
                future.complete(customerEntity);
            } else {
                future.fail(messageReply.cause());
            }
        });

        return future;
    }

    private Single<ProductEntity> findProductByID(String id) {
        return Single.create(emitter -> {
            vertx.eventBus().send(AddressConstants.ADDRESS_EB_GET_PRODUCT_BY_ID, id, reply -> {
                if (reply.succeeded()) {
                    JsonObject jsonMessage = JsonObject.mapFrom(reply.result().body());
                    JsonObject jsonObject = (JsonObject) jsonMessage.getValue(Constants.VALUE);
                    emitter.onSuccess(jsonObject.mapTo(ProductEntity.class));
                } else {
                    emitter.onError(reply.cause());
                }
            });
        });
    }

    @Override
    public Single<String> orderSingle(OrderInfoDTO orderInfoDTO) {
        List<ProductOrderDTO> productOrderDTOList = orderInfoDTO.getProduct_order();
        OrderEntity orderEntity = getOrderEntity(orderInfoDTO);

        Single<String> single = Single.create(emitter -> {

            insertOrder(orderEntity).subscribe(orderID -> {
                List<Single<String>> singleList = productOrderDTOList.stream().map(productOrderDTO -> {

                    int quantity = productOrderDTO.getQuantity();

                    Single<String> singleInsert = Single.create(emitterInsert -> {

                        findProductByID(productOrderDTO.getProduct_id()).subscribe(productEntityResult -> {


                            if (productEntityResult.getQuantity() - quantity >= 0) {
                                OrderDetailEntity orderDetailEntity = getOrderDetailEntity(productOrderDTO);
                                orderDetailEntity.setOrder_id(orderID);

                                JsonObject jsonUpdate = JsonObject.mapFrom(productEntityResult);

                                JsonObject jsonObject = new JsonObject();
                                jsonObject
                                        .put(Constants._ID, productOrderDTO.getProduct_id())
                                        .put(Constants.QUANTITY, quantity)
                                        .put(Constants.JSON_UPDATE, jsonUpdate.put(Constants.QUANTITY,
                                                productEntityResult.getQuantity() - quantity));

                                checkAndInsertOrderDetail(jsonObject, orderDetailEntity).subscribe(res -> {
                                    emitterInsert.onSuccess(res);
                                }, throwable -> {
                                    deleteOrderDetailByOrderId(orderID).subscribe();
                                    emitterInsert.onError(throwable);
                                });
                            } else {
                                deleteOrderDetailByOrderId(orderID).subscribe();
                                emitterInsert.onError(new Throwable(productEntityResult.getName()
                                        + " quantity is not enough. Inventory quantity: "
                                        + productEntityResult.getQuantity()));
                            }
                        }, throwable -> {
                            emitterInsert.onError(throwable);
                        });
                    });
                    return singleInsert;
                }).collect(Collectors.toList());

                Single.merge(singleList).subscribe(s -> {
                    emitter.onSuccess(orderID);
                }, throwable -> {
                    logger.info("delete order {}", orderID);
                    deleteOrder(orderID).subscribe();
                    emitter.onError(throwable);
                });

            }, throwable -> {
                emitter.onError(throwable);
            });
        });
        return single;
    }

    @Override
    public Single<ProductEntity> deleteOrder(String id) {
        return Single.create(emitter -> {
            orderRepositories.deleteOrder(id).subscribe(res -> {
                emitter.onSuccess(res);
            }, throwable -> {
                emitter.onError(throwable);
            });
        });
    }

    public Single<OrderDetailEntity> deleteOrderDetail(String id) {
        return Single.create(emitter -> {
            orderDetailRepositories.deleteOrderDetail(id).subscribe(orderDetailEntity -> {
                emitter.onSuccess(orderDetailEntity);
            }, throwable -> {
                emitter.onError(throwable);
            });
        });
    }

    @Override
    public Completable deleteOrderDetailByOrderId(String orderId) {
        return Completable.create(emitter -> {
            orderDetailRepositories.deleteOrderDetailByOrderId(orderId)
                    .subscribe(() -> emitter.onComplete());
        });
    }

    private OrderEntity getOrderEntity(OrderInfoDTO orderInfoDTO) {
        int totalQuantity = 0;
        int totalPrice = 0;
        List<ProductOrderDTO> productOrderDTOList = orderInfoDTO.getProduct_order();

        for (ProductOrderDTO productOrderDTO : productOrderDTOList) {
            totalQuantity += productOrderDTO.getQuantity();
            totalPrice += productOrderDTO.getPrice() * productOrderDTO.getQuantity();
        }

        DateFormat dateFormat = new SimpleDateFormat(Constants.FORMAT_DATE_JSON);
        JsonObject jsonOrder = new JsonObject();
        jsonOrder.put(Constants.DATE_ORDER, dateFormat.format(new Date()))
                .put(Constants.USER_ID, orderInfoDTO.getUser_id())
                .put(Constants.CUSTOMER_ID, orderInfoDTO.getCustomer_id())
                .put(Constants.TOTAL_QUANTITY, totalQuantity).put(Constants.TOTAL_PRICE, totalPrice);
        OrderEntity orderEntity = jsonOrder.mapTo(OrderEntity.class);

        return orderEntity;
    }

    private OrderDetailEntity getOrderDetailEntity(ProductOrderDTO productOrderDTO) {
        OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
        orderDetailEntity.setProduct_id(productOrderDTO.getProduct_id());
        orderDetailEntity.setQuantity(productOrderDTO.getQuantity());
        orderDetailEntity.setPrice(productOrderDTO.getPrice());
        orderDetailEntity.setName(productOrderDTO.getName());
        return orderDetailEntity;
    }

    private Single<String> checkAndInsertOrderDetail(JsonObject message, OrderDetailEntity orderDetailEntity) {

        return Single.create(emitter -> {
            vertx.eventBus()
                    .send(AddressConstants.ADDRESS_EB_CHECK_AND_UPDATE_PRODUCT, message, reply -> {
                        JsonObject jsonMessage = JsonObject.mapFrom(reply.result().body());

                        if (jsonMessage.getValue(Constants.STATUS).equals(Constants.PASS)) {

                            JsonObject jsonValue = JsonObject.mapFrom(
                                    jsonMessage.getValue(Constants.VALUE));

                            insertOrderDetail(orderDetailEntity).subscribe(res -> {
                                emitter.onSuccess(res);
                            }, throwable -> {
                                emitter.onError(throwable);
                            });
                        } else {
                            emitter.onError(new Throwable(new Throwable(orderDetailEntity.getName()
                                    + " quantity is not enough. Inventory quantity: "
                                    + orderDetailEntity.getQuantity())));
                        }
                    });
        });
    }
}
