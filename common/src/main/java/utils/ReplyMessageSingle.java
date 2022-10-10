package utils;

import entity.TypeValueReply;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReplyMessageSingle {
  private static final Logger logger = LoggerFactory.getLogger(ReplyMessageSingle.class);

  public static void replyMessage(Message<Object> message,
      Single<?> single, String stringMessage) {

    JsonObject jsonMessage = new JsonObject();
    single.subscribe(res -> {
      JsonObject jsonValue = new JsonObject().put(Constants.VALUE, res);
      jsonMessage
          .put(Constants.STATUS, Constants.PASS)
          .put(Constants.MESSAGE, stringMessage)
          .put(Constants.VALUE, jsonValue);
      logger.info("Message reply:{}", jsonMessage);
      message.reply(jsonMessage);
    }, throwable -> {
      jsonMessage
          .put(Constants.STATUS, Constants.FAIL)
          .put(Constants.MESSAGE, throwable.getMessage());
      logger.info("Message reply:{}", jsonMessage);
      message.reply(jsonMessage);
    });
  }

  public static void replyMessage(Message<Object> message,
      Single<?> single) {

    JsonObject jsonMessage = new JsonObject();
    single.subscribe(res -> {
      JsonObject jsonValue = new JsonObject().put(Constants.VALUE, res);
      jsonMessage
          .put(Constants.STATUS, Constants.PASS)
          .put(Constants.VALUE, jsonValue);
      logger.info("Message reply:{}", jsonMessage);
      message.reply(jsonMessage);
    }, throwable -> {
      jsonMessage
          .put(Constants.STATUS, Constants.FAIL)
          .put(Constants.MESSAGE, throwable.getMessage());
      logger.info("Message reply:{}", jsonMessage);
      message.reply(jsonMessage);
    });
  }
}
