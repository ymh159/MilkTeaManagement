package utils;

import entity.TypeValueReply;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReplyMessageEB {
  private static final Logger logger = LoggerFactory.getLogger(ReplyMessageEB.class);

  public  <T>void replyMessage(Message <T> message,
      AsyncResult<?> res, TypeValueReply typeValueReply, String stringMessage) {
    JsonObject jsonMessage = new JsonObject();
    if (res.succeeded()) {
      if (TypeValueReply.JSON_OBJECT == typeValueReply) {
        JsonObject jsonValue = JsonObject.mapFrom(res.result());
        jsonMessage
            .put(Constants.STATUS, Constants.PASS)
            .put(Constants.MESSAGE, stringMessage)
            .put(Constants.VALUE, jsonValue);
        message.reply(jsonMessage);
      } else {
        JsonArray jsonValue = new JsonArray((List) res.result());
        jsonMessage
            .put(Constants.STATUS, Constants.PASS)
            .put(Constants.MESSAGE, stringMessage)
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

  public  <T,E>void replyMessage(Message<T> message,
      AsyncResult<E> res, TypeValueReply typeValueReply) {
    JsonObject jsonMessage = new JsonObject();

    if (res.succeeded()) {
      if (TypeValueReply.JSON_OBJECT == typeValueReply) {
        JsonObject jsonValue = JsonObject.mapFrom(res.result());
        jsonMessage
            .put(Constants.STATUS, Constants.PASS)
            .put(Constants.VALUE, jsonValue);
        logger.info("Message reply:{}",jsonMessage);
        message.reply(jsonMessage);
      } else {
        JsonArray jsonValue = new JsonArray((List) res.result());

        jsonMessage
            .put(Constants.STATUS, Constants.PASS)
            .put(Constants.VALUE, jsonValue);
        logger.info("Message reply:{}",jsonMessage);
        message.reply(jsonMessage);
      }

    } else {
      logger.info("Message reply:{}",jsonMessage);
      jsonMessage
          .put(Constants.STATUS, Constants.FAIL)
          .put(Constants.MESSAGE, res.cause().getMessage());
      logger.info("Message reply after:{}",jsonMessage);
      message.reply(jsonMessage);
    }
  }
}
