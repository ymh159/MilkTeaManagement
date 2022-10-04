package utils;

import entity.TypeValueReply;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public class ReplyMessageEB {

  public void replyMessage(Message<Object> message,
      AsyncResult<?> res, TypeValueReply typeValueReply, String stringMessage) {
    JsonObject jsonMessage = new JsonObject();
    if (res.succeeded()) {
      if (TypeValueReply.JSON_ARRAY == typeValueReply) {
        JsonArray jsonValue = new JsonArray((List) res.result());

        jsonMessage
            .put(Constants.STATUS, Constants.PASS)
            .put(Constants.MESSAGE,stringMessage)
            .put(Constants.VALUE, jsonValue);
        message.reply(jsonMessage);
      } else {
        JsonObject jsonValue = JsonObject.mapFrom(res.result());
        jsonMessage
            .put(Constants.STATUS, Constants.PASS)
            .put(Constants.MESSAGE,stringMessage)
            .put(Constants.VALUE, jsonValue);
        message.reply(jsonMessage);
      }
    } else {
      jsonMessage
          .put(Constants.STATUS, Constants.FAIL)
          .put(Constants.MESSAGE, res.cause().getMessage());
      message.reply(jsonMessage);
    }
  }

  public void replyMessage(io.vertx.core.eventbus.Message<Object> message,
      AsyncResult<?> res, TypeValueReply typeValueReply) {
    JsonObject jsonMessage = new JsonObject();

    if (res.succeeded()) {
      if (TypeValueReply.JSON_ARRAY == typeValueReply) {
        JsonArray jsonValue = new JsonArray((List) res.result());

        jsonMessage
            .put(Constants.STATUS, Constants.PASS)
            .put(Constants.VALUE, jsonValue);
        message.reply(jsonMessage);
      } else {
        JsonObject jsonValue = JsonObject.mapFrom(res.result());
        jsonMessage
            .put(Constants.STATUS, Constants.PASS)
            .put(Constants.VALUE, jsonValue);
        message.reply(jsonMessage);
      }

    } else {
      jsonMessage
          .put(Constants.STATUS, Constants.FAIL)
          .put(Constants.MESSAGE, res.cause().getMessage());
      message.reply(jsonMessage);
    }
  }
}
