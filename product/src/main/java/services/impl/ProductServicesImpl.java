package services.impl;

import DTO.ProductDetailDTO;
import entity.ProductCategoryEntity;
import entity.ProductEntity;
import entity.ProviderEntity;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.util.ArrayList;
import java.util.List;
import jdk.jfr.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.ProductRepositories;
import repositories.impl.ProductRepositoriesImpl;
import services.ProductServices;
import utils.Constants;
import utils.ConstantsAddress;
import utils.MongoDBClient;

public class ProductServicesImpl implements ProductServices {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProductServicesImpl.class);
  private static MongoClient mongoClient;
  private ProductRepositories repositories;

  private Vertx vertx;

  public ProductServicesImpl(Vertx vertx) {
    mongoClient = MongoDBClient.client(vertx);
    repositories = new ProductRepositoriesImpl(vertx);
    this.vertx = vertx;
  }

  @Override
  public Future<List<ProductDetailDTO>> getAllProductDetail() {
    Future<List<ProductDetailDTO>> future = Future.future();
    getAllProduct().setHandler(res -> {
      if (res.succeeded()) {
        List<ProductEntity> productEntityList = res.result();
        List<ProductDetailDTO> productDetailDTOList = new ArrayList<>();
        for (ProductEntity entity : productEntityList) {
          Future<ProductDetailDTO> futureProductDetail = Future.future();
          Future<ProductEntity> futureProduct = Future.future();
          Future<JsonObject> futureCategory = Future.future();
          Future<JsonObject> futureProvider = Future.future();

          vertx.eventBus().send(ConstantsAddress.ADDRESS_EB_GET_PRODUCT_CATEGORY_BY_ID,
              entity.getProduct_category_id(), resCategory -> {
                if (resCategory.succeeded()) {
                  JsonObject jsonCategory = JsonObject.mapFrom(resCategory.result().body());
                  jsonCategory.remove(Constants._ID);
                  futureCategory.complete(jsonCategory);
                } else {
                  future.fail(resCategory.cause());
                }
              });

          vertx.eventBus()
              .send(ConstantsAddress.ADDRESS_EB_GET_PROVIDER_BY_ID, entity.getProvider_id(),
                  resProvider -> {
                    if (resProvider.succeeded()) {
                      JsonObject jsonProvider = JsonObject.mapFrom(resProvider.result().body());
                      jsonProvider.remove(Constants._ID);
                      futureProvider.complete(jsonProvider);
                    } else {
                      future.fail(resProvider.cause());
                    }
                  });
          CompositeFuture.all(futureProduct, futureCategory, futureProvider).setHandler(event -> {
            if (event.succeeded()) {
              JsonObject jsonProduct = JsonObject.mapFrom(futureProduct.result());
              jsonProduct.remove(Constants.PRODUCT_CATEGORY_ID);
              jsonProduct.remove(Constants.PROVIDER_ID);
              JsonObject jsonProductDetail = jsonProduct.mergeIn(jsonProduct)
                  .mergeIn(futureCategory.result()).mergeIn(futureProvider.result());
              ProductDetailDTO productDetailDTO = jsonProductDetail.mapTo(ProductDetailDTO.class);
              futureProductDetail.complete(productDetailDTO);
            } else {
              futureProductDetail.fail(event.cause());
            }
          });
          productDetailDTOList.add(futureProductDetail.result());
        }
        future.complete(productDetailDTOList);
      } else {
        future.fail(res.cause());
      }
    });
    return future;
  }

  @Override
  public Future<ProductDetailDTO> getProductDetailByID(String id) {
    Future<ProductDetailDTO> future = Future.future();
    Future<ProductEntity> futureProduct = Future.future();
    Future<JsonObject> futureCategory = Future.future();
    Future<JsonObject> futureProvider = Future.future();
    findProductById(id).setHandler(res -> {
      if (res.succeeded()) {
        ProductEntity entity = res.result();
        futureProduct.complete(entity);

        vertx.eventBus().send(ConstantsAddress.ADDRESS_EB_GET_PRODUCT_CATEGORY_BY_ID,
            entity.getProduct_category_id(), resCategory -> {
              if (resCategory.succeeded()) {
                JsonObject jsonCategory = JsonObject.mapFrom(resCategory.result().body());
                jsonCategory.remove(Constants._ID);
                futureCategory.complete(jsonCategory);
              } else {
                future.fail(resCategory.cause());
              }
            });

        vertx.eventBus()
            .send(ConstantsAddress.ADDRESS_EB_GET_PROVIDER_BY_ID, entity.getProvider_id(),
                resProvider -> {
                  if (resProvider.succeeded()) {
                    JsonObject jsonProvider = JsonObject.mapFrom(resProvider.result().body());
                    jsonProvider.remove(Constants._ID);
                    futureProvider.complete(jsonProvider);
                  } else {
                    future.fail(resProvider.cause());
                  }
                });
      } else {
        futureProduct.fail(res.cause());
      }
    });

    CompositeFuture.all(futureProduct, futureCategory, futureProvider).setHandler(event -> {
      if (event.succeeded()) {
        JsonObject jsonProduct = JsonObject.mapFrom(futureProduct.result());
        jsonProduct.remove(Constants.PRODUCT_CATEGORY_ID);
        jsonProduct.remove(Constants.PROVIDER_ID);
        JsonObject jsonProductDetail = jsonProduct.mergeIn(jsonProduct)
            .mergeIn(futureCategory.result()).mergeIn(futureProvider.result());
        ProductDetailDTO productDetailDTO = jsonProductDetail.mapTo(ProductDetailDTO.class);

        future.complete(productDetailDTO);
      } else {
        future.fail(event.cause());
      }
    });
    return future;
  }

  @Override
  public Future<List<ProductEntity>> getAllProduct() {
    Future<List<ProductEntity>> future = Future.future();
    repositories.getProducts().setHandler(res -> {
      if (res.succeeded()) {
        future.complete(res.result());
      } else {
        future.fail(res.cause());
      }
    });
    return future;
  }

  @Override
  public Future<ProductEntity> findProductById(String id) {
    Future<ProductEntity> future = Future.future();
    repositories.findProductById(id).setHandler(res -> {
      if (res.succeeded()) {
        ProductEntity productEntity = res.result();
        future.complete(productEntity);
      } else {
        future.fail(res.cause());
      }
    });
    return future;
  }
}
