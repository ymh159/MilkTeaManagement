package services.impl;

import DTO.ProductDetailDTO;
import entity.ProductCategoryEntity;
import entity.ProductEntity;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.ProductRepositories;
import repositories.impl.ProductRepositoriesImpl;
import services.ProductServices;
import utils.Constants;
import utils.AddressConstants;
import utils.ConvertUtils;

public class ProductServicesImpl implements ProductServices {

  private static final Logger logger = LoggerFactory.getLogger(ProductServicesImpl.class);
  private final ProductRepositories repositories;

  private final Vertx vertx;

  public ProductServicesImpl(Vertx vertx) {
    repositories = new ProductRepositoriesImpl(vertx);
    this.vertx = vertx;
  }

  @Override
  public Single<List<ProductDetailDTO>> getAllProductDetail() {

    Single<List<ProductEntity>> listProductSingle = ConvertUtils.futureToSingle(getAllProduct());
    List<ProductDetailDTO> productDetailDTOList = new ArrayList<>();
    Single<List<ProductDetailDTO>> single = Single.create(emitter -> {
      listProductSingle.subscribe(productEntities -> {
          productEntities.forEach(productEntity -> {
            JsonObject jsonProductDetailDTO = new JsonObject();
            Future<JsonObject> categoryFuture = sendAndReplyJsonObject(
                productEntity.getProduct_category_id(),
                AddressConstants.ADDRESS_EB_GET_PRODUCT_CATEGORY_BY_ID);
            Future<JsonObject> providerFuture = sendAndReplyJsonObject(
                productEntity.getProvider_id(),
                AddressConstants.ADDRESS_EB_GET_PROVIDER_BY_ID);
            Single<JsonObject> providerSingle = ConvertUtils.futureToSingle(providerFuture);
            Single<JsonObject> categorySingle = ConvertUtils.futureToSingle(categoryFuture);

            Single.zip(categorySingle, providerSingle, (entries, entries2) -> {
              jsonProductDetailDTO.mergeIn(entries).mergeIn(entries2)
                  .mergeIn(JsonObject.mapFrom(productEntity));
              return jsonProductDetailDTO;

            }).subscribe(entries -> {
              productDetailDTOList.add(entries.mapTo(ProductDetailDTO.class));
            });
          });
          emitter.onSuccess(productDetailDTOList);

      });
    });
    return single;
  }
//  public Future<List<ProductDetailDTO>> getAllProductDetail() {
//    return getAllProduct().compose(resProduct -> {
//      Future<List<ProductDetailDTO>> future = Future.future();
//      List<Future<ProductDetailDTO>> collect = resProduct.stream().map(
//          productEntity -> {
//            Future<JsonObject> futureCategory = sendAndReplyJsonObject(
//                productEntity.getProduct_category_id(),
//                AddressConstants.ADDRESS_EB_GET_PRODUCT_CATEGORY_BY_ID);
//            Future<JsonObject> futureProvider = sendAndReplyJsonObject(
//                productEntity.getProvider_id(),
//                AddressConstants.ADDRESS_EB_GET_PROVIDER_BY_ID);
//
//            Future<ProductDetailDTO> productDetailDTOFuture = Future.future();
//            CompositeFuture all = CompositeFuture.all(futureCategory, futureProvider);
//            all.setHandler(ar -> {
//              if (ar.succeeded()) {
//                JsonObject catJo = all.resultAt(0);
//                JsonObject proJo = all.resultAt(1);
//                JsonObject jsonProduct = JsonObject.mapFrom(productEntity);
//                JsonObject jsonProductDetail = jsonProduct.mergeIn(jsonProduct)
//                    .mergeIn(catJo)
//                    .mergeIn(proJo);
//                ProductDetailDTO productDetailDTO = jsonProductDetail.mapTo(ProductDetailDTO.class);
//                productDetailDTOFuture.complete(productDetailDTO);
//              } else {
//                productDetailDTOFuture.fail(ar.cause());
//              }
//            });
//
//            return productDetailDTOFuture;
//          }).toList();
//      CompositeFuture.all(new ArrayList<>(collect))
//          .setHandler(hl -> {
//            if (hl.succeeded()) {
//              List<ProductDetailDTO> collect1 = hl.result().list().stream()
//                  .map(ent -> (ProductDetailDTO) ent).collect(Collectors.toList());
//              future.complete(collect1);
//            } else {
//              future.fail(hl.cause());
//            }
//          });
//      return future;
//    });
//  }


  private Future<JsonObject> sendAndReplyJsonObject(String id, String address) {
    Future<JsonObject> jsonObjectFuture = Future.future();
    vertx.eventBus().send(address, id, reply -> {
      if (reply.succeeded()) {
        JsonObject jsonMessage = JsonObject.mapFrom(reply.result().body());
        JsonObject jsonObject = (JsonObject) jsonMessage.getValue(Constants.VALUE);
        jsonObject.remove(Constants._ID);
        jsonObjectFuture.complete(jsonObject);
      } else {
        jsonObjectFuture.fail(reply.cause());
      }
    });
    return jsonObjectFuture;
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

  @Override
  public Single<ProductDetailDTO> getProductDetailByID(String id) {

    Single<ProductEntity> productSingle = ConvertUtils.futureToSingle(findProductById(id));
    JsonObject jsonProductDetailDTO = new JsonObject();

    Single<ProductDetailDTO> single = Single.create(emitter -> {

      productSingle.subscribe(productEntity -> {

        Future<JsonObject> categoryFuture = sendAndReplyJsonObject(
            productEntity.getProduct_category_id(),
            AddressConstants.ADDRESS_EB_GET_PRODUCT_CATEGORY_BY_ID);
        Future<JsonObject> providerFuture = sendAndReplyJsonObject(
            productEntity.getProvider_id(),
            AddressConstants.ADDRESS_EB_GET_PROVIDER_BY_ID);

        Single<JsonObject> providerSingle = ConvertUtils.futureToSingle(providerFuture);
        Single<JsonObject> categorySingle = ConvertUtils.futureToSingle(categoryFuture);

        Single.zip(categorySingle, providerSingle, (entries, entries2) -> {
          jsonProductDetailDTO.mergeIn(entries).mergeIn(entries2)
              .mergeIn(JsonObject.mapFrom(productEntity));
          return jsonProductDetailDTO;

        }).subscribe(entries -> {
          emitter.onSuccess(entries.mapTo(ProductDetailDTO.class));
        });
      });
    });

    return single;
  }
}
