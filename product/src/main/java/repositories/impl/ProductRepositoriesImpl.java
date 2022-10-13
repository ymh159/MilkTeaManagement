package repositories.impl;

import com.mongodb.session.ClientSession;
import entity.ProductEntity;
import entity.UserEntity;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.List;
import java.util.Objects;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.CRUDRepositories;
import repositories.ProductRepositories;
import utils.Constants;
import utils.MongoDBClient;

public class ProductRepositoriesImpl extends CRUDRepositories implements ProductRepositories {

    private static final Logger logger = LoggerFactory.getLogger(ProductRepositoriesImpl.class);

    public ProductRepositoriesImpl(Vertx vertx) {
        super(vertx);
        setCollectionName(Constants.COLLECTION_USER);
        setClassType(UserEntity.class);
    }

    @Override
    public Single<ProductEntity> checkAndUpdateProduct(String id,int quantity, ProductEntity productEntity) {
        JsonObject query = new JsonObject()
                .put(Constants._ID, id)
                .put(Constants.QUANTITY, new JsonObject().put("$gte", quantity));
        JsonObject jsonUpdate = JsonObject.mapFrom(productEntity);
        jsonUpdate.getMap().values().removeIf(Objects::isNull);
        JsonObject jsonQueryUpdate = new JsonObject().put(Constants.DOCUMENT_SET, jsonUpdate);

        return Single.create(emitter -> {
            getMongoClient().findOneAndUpdate(Constants.COLLECTION_PRODUCT, query, jsonQueryUpdate, event -> {
                if (event.succeeded()) {
                    if(event.result()!= null){
                        ProductEntity entity = JsonObject.mapFrom(event.result()).mapTo(ProductEntity.class);
                        logger.info("updateProduct:{}", query);
                        emitter.onSuccess(entity);
                    }else {
                        emitter.onError(new Throwable("Product not found"));
                    }
                } else {
                    logger.info(Constants.MESSAGE_UPDATE_FAIL + " updateProduct:{}", query);
                    emitter.onError(event.cause());
                }
            });
        });
    }


}
