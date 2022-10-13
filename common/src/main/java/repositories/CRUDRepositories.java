package repositories;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;
import utils.MongoDBClient;

import java.util.List;
import java.util.Objects;

public class CRUDRepositories<T> {


    private static MongoClient mongoClient;
    private String collectionName;
    private Class<T> classType;
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    public CRUDRepositories(Vertx vertx) {
        mongoClient = MongoDBClient.client(vertx);
    }

    public static MongoClient getMongoClient() {
        return mongoClient;
    }

    public Class<T> getClassType() {
        return classType;
    }

    public void setClassType(Class<T> classType) {
        this.classType = classType;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }



    public Single<List<T>> getAll() {
        return Single.create(emitter -> {
            mongoClient.find(collectionName, new JsonObject(), res -> {
                List<JsonObject> data = res.result();
                List<T> entity;
                if (res.succeeded()) {
                    entity = data.stream().map(item ->
                            item.mapTo(classType)
                    ).toList();
                    logger.info("getAll:{}", entity);
                    emitter.onSuccess(entity);
                } else {
                    logger.error("getAll fail");
                    emitter.onError(res.cause());
                }
            });
        });
    }

    public Single<T> findById(String id) {
        return Single.create(emitter -> {
            mongoClient.findOne(collectionName, new JsonObject().put(Constants._ID, id), null,
                    res -> {
                        if (res.succeeded()) {
                            if (res.result() != null) {
                                T entity = res.result().mapTo(classType);
                                logger.info("findById:{}", entity);
                                emitter.onSuccess(entity);
                            } else {
                                logger.info("Find not found id:{}", id);
                                emitter.onError(new Throwable("Find not found user_id:" + id));
                            }
                        } else {
                            logger.info("findById fail");
                            emitter.onError(res.cause());
                        }
                    });
        });
    }

    public Single<String> insert(T entity) {

        return Single.create(emitter -> {
            JsonObject query = JsonObject.mapFrom(entity);

            mongoClient.insert(collectionName, query, res -> {
                if (res.succeeded()) {
                    logger.info("insertEntity:{}", query);
                    emitter.onSuccess(res.result());
                } else {
                    logger.info(Constants.MESSAGE_INSERT_FAIL + " query:{}", query);
                    emitter.onError(res.cause());
                }
            });

        });
    }

    public Single<T> update(String id, T entity) {
        return Single.create(emitter -> {
            JsonObject jsonUpdate = JsonObject.mapFrom(entity);
            JsonObject query = new JsonObject().put(Constants._ID, id);
            jsonUpdate.getMap().values().removeIf(Objects::isNull);
            JsonObject jsonQueryUpdate = new JsonObject().put(Constants.DOCUMENT_SET, jsonUpdate);
            mongoClient.findOneAndUpdate(collectionName, query, jsonQueryUpdate, res -> {
                if (res.succeeded()) {
                    if (res.result() != null) {
                        logger.info("updateEntity:{}", query);
                        T result = res.result().mapTo(classType);
                        emitter.onSuccess(result);
                    } else {
                        logger.info("Update entity fail. Find entity not found:{}", query);
                        emitter.onError(new Throwable("Update entity fail. Find entity not found"));
                    }
                } else {
                    logger.info(Constants.MESSAGE_UPDATE_FAIL + " updateEntity:{}", query);
                    emitter.onError(new Throwable(Constants.MESSAGE_UPDATE_FAIL));
                }
            });

        });
    }

    public Completable delete(String id) {

        return Completable.create(emitter -> {
            mongoClient.findOneAndDelete(collectionName, new JsonObject().put(Constants._ID, id),
                    event -> {
                        if (event.succeeded()) {
                            logger.info("deleteUser:{}", id);
                            emitter.onComplete();
                        } else {
                            logger.info(Constants.MESSAGE_DELETE_FAIL + " deleteUser:{}", id);
                            emitter.onError(new Throwable(Constants.MESSAGE_DELETE_FAIL));
                        }
                    });
        });
    }
}

