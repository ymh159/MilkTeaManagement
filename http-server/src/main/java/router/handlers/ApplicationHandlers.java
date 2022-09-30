package router.handlers;

import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ultils.SendMessageAndReponse;
import utils.Constants;
import utils.ConstantsAddress;

public class ApplicationHandlers {

  private Vertx vertx;
  SendMessageAndReponse sendMessageAndReponse;

  public ApplicationHandlers(Vertx vertx) {
    this.vertx = vertx;
    sendMessageAndReponse = new SendMessageAndReponse(vertx);
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationHandlers.class);

  public void customerHandler(RoutingContext routingContext) {
    String paramId = routingContext.request().getParam(Constants.ID);
    routingContext.request().bodyHandler(handler -> {
      String addressEvent = Constants.BLANK;
      Object message = null;

      switch (routingContext.request().method()) {
        case GET -> {
          if (paramId != null && paramId != Constants.BLANK) {
            message = paramId;
            addressEvent = ConstantsAddress.ADDRESS_EB_GET_CUSTOMER_BY_ID;
          } else {
            addressEvent = ConstantsAddress.ADDRESS_EB_GET_CUSTOMER;
          }
        }
        case POST -> {
          message = handler.toJsonObject();
          addressEvent = ConstantsAddress.ADDRESS_EB_INSERT_CUSTOMER;
        }
        case PATCH -> {
          JsonObject jsonObject = new JsonObject();
          jsonObject.put(Constants._ID, paramId);
          jsonObject.put(Constants.JSON_UPDATE, handler.toJsonObject());
          message = jsonObject;
          addressEvent = ConstantsAddress.ADDRESS_EB_UPDATE_CUSTOMER;
        }
        case DELETE -> {
          message = paramId;
          addressEvent = ConstantsAddress.ADDRESS_EB_DELETE_CUSTOMER;
        }
      }
      sendMessageAndReponse.send(routingContext, addressEvent, message);
    });
  }

  public void orderDetailHandler(RoutingContext routingContext) {
    String paramId = routingContext.request().getParam(Constants.ID);
    routingContext.request().bodyHandler(handler -> {
      String addressEvent = Constants.BLANK;
      Object message = null;

      switch (routingContext.request().method()) {
        case GET -> {
          if (paramId != null && paramId != Constants.BLANK) {
            message = paramId;
            addressEvent = ConstantsAddress.ADDRESS_EB_GET_ORDER_DETAIL_BY_ID;
          } else {
            addressEvent = ConstantsAddress.ADDRESS_EB_GET_ORDER_DETAIL;
          }
        }
        case POST -> {
          message = handler.toJsonObject();
          addressEvent = ConstantsAddress.ADDRESS_EB_INSERT_ORDER_DETAIL;
        }
        case PATCH -> {
          JsonObject jsonObject = new JsonObject();
          jsonObject.put(Constants._ID, paramId);
          jsonObject.put(Constants.JSON_UPDATE, handler.toJsonObject());
          message = jsonObject;
          addressEvent = ConstantsAddress.ADDRESS_EB_UPDATE_ORDER_DETAIL;
        }
        case DELETE -> {
          message = paramId;
          addressEvent = ConstantsAddress.ADDRESS_EB_DELETE_ORDER_DETAIL;
        }
      }
      sendMessageAndReponse.send(routingContext, addressEvent, message);
    });
  }

  public void orderHandler(RoutingContext routingContext) {
    String paramId = routingContext.request().getParam(Constants.ID);
    routingContext.request().bodyHandler(handler -> {
      String addressEvent = Constants.BLANK;
      Object message = null;

      switch (routingContext.request().method()) {
        case GET -> {
          if (paramId != null && paramId != Constants.BLANK) {
            message = paramId;
            addressEvent = ConstantsAddress.ADDRESS_EB_GET_ORDER_BY_ID;
          } else {
            addressEvent = ConstantsAddress.ADDRESS_EB_GET_ORDER;
          }
        }
        case POST -> {
          message = handler.toJsonObject();
          addressEvent = ConstantsAddress.ADDRESS_EB_INSERT_ORDER;
        }
        case PATCH -> {
          JsonObject jsonObject = new JsonObject();
          jsonObject.put(Constants._ID, paramId);
          jsonObject.put(Constants.JSON_UPDATE, handler.toJsonObject());
          message = jsonObject;
          addressEvent = ConstantsAddress.ADDRESS_EB_UPDATE_ORDER;
        }
        case DELETE -> {
          message = paramId;
          addressEvent = ConstantsAddress.ADDRESS_EB_DELETE_ORDER;
        }
      }
      sendMessageAndReponse.send(routingContext, addressEvent, message);
    });
  }

  public void productCategoryHandler(RoutingContext routingContext) {
    String paramId = routingContext.request().getParam(Constants.ID);
    routingContext.request().bodyHandler(handler -> {
      String addressEvent = Constants.BLANK;
      Object message = null;

      switch (routingContext.request().method()) {
        case GET -> {
          if (paramId != null && paramId != Constants.BLANK) {
            message = paramId;
            addressEvent = ConstantsAddress.ADDRESS_EB_GET_PRODUCT_CATEGORY_BY_ID;
          } else {
            addressEvent = ConstantsAddress.ADDRESS_EB_GET_PRODUCT_CATEGORY;
          }
        }
        case POST -> {
          message = handler.toJsonObject();
          addressEvent = ConstantsAddress.ADDRESS_EB_INSERT_PRODUCT_CATEGORY;
        }
        case PATCH -> {
          JsonObject jsonObject = new JsonObject();
          jsonObject.put(Constants._ID, paramId);
          jsonObject.put(Constants.JSON_UPDATE, handler.toJsonObject());
          message = jsonObject;
          addressEvent = ConstantsAddress.ADDRESS_EB_UPDATE_PRODUCT_CATEGORY;
        }
        case DELETE -> {
          message = paramId;
          addressEvent = ConstantsAddress.ADDRESS_EB_DELETE_PRODUCT_CATEGORY;
        }
      }
      sendMessageAndReponse.send(routingContext, addressEvent, message);
    });
  }

  public void productHandler(RoutingContext routingContext) {
    String paramId = routingContext.request().getParam(Constants.ID);
    routingContext.request().bodyHandler(handler -> {
      String addressEvent = Constants.BLANK;
      Object message = null;

      switch (routingContext.request().method()) {
        case GET -> {
          if (paramId != null && paramId != Constants.BLANK) {
            message = paramId;
            addressEvent = ConstantsAddress.ADDRESS_EB_GET_PRODUCT_BY_ID;
          } else {
            addressEvent = ConstantsAddress.ADDRESS_EB_GET_PRODUCT;
          }
        }
        case POST -> {
          message = handler.toJsonObject();
          addressEvent = ConstantsAddress.ADDRESS_EB_INSERT_PRODUCT;
        }
        case PATCH -> {
          JsonObject jsonObject = new JsonObject();
          jsonObject.put(Constants._ID, paramId);
          jsonObject.put(Constants.JSON_UPDATE, handler.toJsonObject());
          message = jsonObject;
          addressEvent = ConstantsAddress.ADDRESS_EB_UPDATE_PRODUCT;
        }
        case DELETE -> {
          message = paramId;
          addressEvent = ConstantsAddress.ADDRESS_EB_DELETE_PRODUCT;
        }
      }
      sendMessageAndReponse.send(routingContext, addressEvent, message);
    });
  }

  public void providerHandler(RoutingContext routingContext) {
    String paramId = routingContext.request().getParam(Constants.ID);
    routingContext.request().bodyHandler(handler -> {
      String addressEvent = Constants.BLANK;
      Object message = null;

      switch (routingContext.request().method()) {
        case GET -> {
          if (paramId != null && paramId != Constants.BLANK) {
            message = paramId;
            addressEvent = ConstantsAddress.ADDRESS_EB_GET_PROVIDER_BY_ID;
          } else {
            addressEvent = ConstantsAddress.ADDRESS_EB_GET_PROVIDER;
          }
        }
        case POST -> {
          message = handler.toJsonObject();
          addressEvent = ConstantsAddress.ADDRESS_EB_INSERT_PROVIDER;
        }
        case PATCH -> {
          JsonObject jsonObject = new JsonObject();
          jsonObject.put(Constants._ID, paramId);
          jsonObject.put(Constants.JSON_UPDATE, handler.toJsonObject());
          message = jsonObject;
          addressEvent = ConstantsAddress.ADDRESS_EB_UPDATE_PROVIDER;
        }
        case DELETE -> {
          message = paramId;
          addressEvent = ConstantsAddress.ADDRESS_EB_DELETE_PROVIDER;
        }
      }
      sendMessageAndReponse.send(routingContext, addressEvent, message);
    });
  }

  public void userHandler(RoutingContext routingContext) {
    String paramId = routingContext.request().getParam(Constants.ID);
    routingContext.request().bodyHandler(handler -> {
      String addressEvent = Constants.BLANK;
      Object message = null;

      switch (routingContext.request().method()) {
        case GET -> {
          if (paramId != null && paramId != Constants.BLANK) {
            message = paramId;
            addressEvent = ConstantsAddress.ADDRESS_EB_GET_USER_BY_ID;
          } else {
            addressEvent = ConstantsAddress.ADDRESS_EB_GET_USER;
          }
        }
        case POST -> {
          message = handler.toJsonObject();
          addressEvent = ConstantsAddress.ADDRESS_EB_INSERT_USER;
        }
        case PATCH -> {
          JsonObject jsonObject = new JsonObject();
          jsonObject.put(Constants._ID, paramId);
          jsonObject.put(Constants.JSON_UPDATE, handler.toJsonObject());
          message = jsonObject;
          addressEvent = ConstantsAddress.ADDRESS_EB_UPDATE_USER;
        }
        case DELETE -> {
          message = paramId;
          addressEvent = ConstantsAddress.ADDRESS_EB_DELETE_USER;
        }
      }
      sendMessageAndReponse.send(routingContext, addressEvent, message);
    });
  }


  public void getProductDetail(RoutingContext routingContext) {
    String paramId = routingContext.request().getParam(Constants.ID);
    if(paramId!=null && !paramId.isEmpty()){
      sendMessageAndReponse.send(routingContext, ConstantsAddress.ADDRESS_EB_GET_PRODUCT_DETAIL_BY_ID,
          paramId);
    }else{
      sendMessageAndReponse.send(routingContext, ConstantsAddress.ADDRESS_EB_GET_ALL_PRODUCT_DETAIL,null);
    }
  }
  public void orderProduct(RoutingContext routingContext){
    routingContext.request().bodyHandler(handler -> {
      JsonObject jsonObject = handler.toJsonObject();
      sendMessageAndReponse.send(routingContext, ConstantsAddress.ADDRESS_EB_ORDER_PRODUCT,
          jsonObject);
    });

  }
}


