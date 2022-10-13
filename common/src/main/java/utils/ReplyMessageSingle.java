package utils;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReplyMessageSingle {
    private static final Logger logger = LoggerFactory.getLogger(ReplyMessageSingle.class);

    public static <T> void replyMessage(Message<T> message,
                                        Single<?> single, String stringMessage) {

        JsonObject jsonMessage = new JsonObject();
        single.subscribe(res -> {
            JsonObject jsonValue = new JsonObject().put(Constants.VALUE, JsonObject.mapFrom(res));
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

    public static <T> void replyMessage(Message<T> message,
                                        Single<?> single) {

        JsonObject jsonMessage = new JsonObject();
        single.subscribe(res -> {
            JsonObject jsonValue = new JsonObject().put(Constants.VALUE, JsonObject.mapFrom(res));
//      JsonObject jsonValue = new JsonObject().put(Constants.VALUE, new JsonArray((List) res));

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

    public static <T> void replyMessage(Message<T> message,
                                        Completable completable) {

        JsonObject jsonMessage = new JsonObject();
        completable.subscribe(() -> {

            jsonMessage
                    .put(Constants.STATUS, Constants.PASS);
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
