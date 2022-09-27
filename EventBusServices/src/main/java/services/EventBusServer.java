package services;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import utils.Constants;

public class EventBusServer extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    EventBus eb = vertx.eventBus();
    // eventBus get customer
    eb.consumer(Constants.ADDRESS_EB_GET_CUSTOMER, message -> message.body());
    // eventBus get order_detail
    eb.consumer(Constants.ADDRESS_EB_GET_ORDER_DETAIL, message -> message.body());
    // eventBus get order
    eb.consumer(Constants.ADDRESS_EB_GET_ORDER, message -> message.body());
    // eventBus get product_category
    eb.consumer(Constants.ADDRESS_EB_GET_PRODUCT_CATEGORY, message -> message.body());
    // eventBus get product
    eb.consumer(Constants.ADDRESS_EB_GET_PRODUCT, message -> message.body());
    // eventBus get provider
    eb.consumer(Constants.ADDRESS_EB_GET_PROVIDER, message -> message.body());
    // eventBus get user
    eb.consumer(Constants.ADDRESS_EB_GET_USER, message -> message.body());
  }

}
