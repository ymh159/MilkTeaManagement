package ultils;

import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;

public class SendMessageAndReponse {

  private static final Logger LOGGER = LoggerFactory.getLogger(SendMessageAndReponse.class);

  private Vertx vertx;

  public SendMessageAndReponse(Vertx vertx) {
    this.vertx = vertx;
  }

  public void send(RoutingContext routingContext, String address, Object message) {
    vertx.eventBus().send(address, message, reply -> {
      LOGGER.info(Constants.LOGGER_ADDRESS_AND_MESSAGE, address,
          message);
      if (reply.succeeded()) {
        LOGGER.info("reply: {}", reply.result().body());
        JsonObject jsonObject = JsonObject.mapFrom(reply.result().body());
        if (jsonObject.getValue(Constants.STATUS).equals(Constants.PASS)) {
          if (jsonObject.getValue(Constants.VALUE) != null) {
            routingContext.response()
                .putHeader(Constants.CONTENT_TYPE, Constants.CONTENT_VALUE_JSON)
                .end(Json.encodePrettily(jsonObject.getValue(Constants.VALUE)));
          } else {
            routingContext.response()
                .putHeader(Constants.CONTENT_TYPE, Constants.CONTENT_VALUE_JSON)
                .end(Json.encodePrettily(jsonObject.getValue(Constants.MESSAGE)));
          }
        }
      } else {
        routingContext.response()
            .putHeader(Constants.CONTENT_TYPE, Constants.CONTENT_VALUE_JSON)
            .end(Json.encodePrettily(reply.cause().getMessage()));
      }
    });
  }
}
