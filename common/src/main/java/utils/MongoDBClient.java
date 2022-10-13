package utils;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.io.FileNotFoundException;
import java.util.Properties;

public class MongoDBClient {

  public static MongoClient client(Vertx vertx) {
    Properties properties = ReadFileProperties.read();
    String connect_string = properties.getProperty(Constants.CONFIG_MONGO_HOST_KEY);
    String db_name = properties.getProperty(Constants.CONFIG_MONGO_DB_NAME_KEY);

    connect_string = connect_string != null ? connect_string : Constants.CONFIG_MONGO_HOST_VALUE;
    db_name = db_name != null ? db_name : Constants.CONFIG_MONGO_DB_NAME_VALUE;

    JsonObject config = new JsonObject()
        .put(Constants.CONFIG_MONGO_HOST_KEY, connect_string)
        .put(Constants.CONFIG_MONGO_DB_NAME_KEY, db_name);

    return MongoClient.createShared(vertx, config);
  }
}
