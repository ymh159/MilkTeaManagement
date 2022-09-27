package utils;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class MongoDBClient {
    public MongoClient client(Vertx vertx) {
        JsonObject config = new JsonObject()
                .put(Constants.CONFIG_MONGO_HOST_KEY, Constants.CONFIG_MONGO_HOST_VALUE)
                .put(Constants.CONFIG_MONGO_DB_NAME_KEY, Constants.CONFIG_MONGO_DB_NAME_VALUE);
        return MongoClient.createShared(vertx, config);
    }
}
