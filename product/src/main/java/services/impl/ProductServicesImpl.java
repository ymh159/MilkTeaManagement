package services.impl;

import DTO.ProductDetailDTO;
import entity.ProductCategoryEntity;
import entity.ProductEntity;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
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
        Single<List<ProductEntity>> listProductSingle = getAllProduct();
        Single<List<ProductDetailDTO>> single = Single.create(emitter -> {
            listProductSingle.subscribe(productEntities -> {
                List<Single<ProductDetailDTO>> singleList = productEntities.stream().map(productEntity -> {
                    JsonObject jsonProductDetailDTO = new JsonObject();

                    Single<JsonObject> providerSingle = sendAndReplyJsonObject(
                            productEntity.getProvider_id(),
                            AddressConstants.ADDRESS_EB_GET_PROVIDER_BY_ID);
                    Single<JsonObject> categorySingle = sendAndReplyJsonObject(
                            productEntity.getProduct_category_id(),
                            AddressConstants.ADDRESS_EB_GET_PRODUCT_CATEGORY_BY_ID);

                    Single<ProductDetailDTO> singleProductDetail = Single.create(emitterProductDetail -> {
                        Single.zip(categorySingle, providerSingle, (entries, entries2) -> {
                            jsonProductDetailDTO.mergeIn(entries).mergeIn(entries2)
                                    .mergeIn(JsonObject.mapFrom(productEntity));
                            return jsonProductDetailDTO;
                        }).subscribe(entries -> {
                            emitterProductDetail.onSuccess(jsonProductDetailDTO.mapTo(ProductDetailDTO.class));
                        }, throwable -> {
                            emitterProductDetail.onError(throwable);
                        });
                    });
                    return singleProductDetail;
                }).collect(Collectors.toList());

                Single.zip(new ArrayList<>(singleList), allProductDetail -> {
                    List<ProductDetailDTO> productDetailDTOList = Arrays.stream(allProductDetail)
                            .map(productDetail -> {
                                return (ProductDetailDTO) productDetail;
                            }).collect(Collectors.toList());
                    return productDetailDTOList;
                }).subscribe(allProductDetail -> {
                    emitter.onSuccess(allProductDetail);
                }, throwable -> {
                    emitter.onError(throwable);
                });

            });
        });
        return single;
    }

    private Single<JsonObject> sendAndReplyJsonObject(String id, String address) {
        return Single.create(emitter -> {
            vertx.eventBus().send(address, id, reply -> {
                if (reply.succeeded()) {
                    JsonObject jsonMessage = JsonObject.mapFrom(reply.result().body());
                    JsonObject jsonObject = (JsonObject) jsonMessage.getValue(Constants.VALUE);
                    jsonObject.remove(Constants._ID);
                    emitter.onSuccess(jsonObject);
                } else {
                    emitter.onError(reply.cause());
                }
            });
        });
    }

    @Override
    public Single<List<ProductEntity>> getAllProduct() {

        return Single.create(emitter -> {
            repositories.getAll().subscribe( res -> {
                List<ProductEntity> productEntityList = (List<ProductEntity>) res;
                emitter.onSuccess(productEntityList);
            },throwable -> {
                emitter.onError((Throwable) throwable);
            });
        });
    }


    @Override
    public Single<ProductDetailDTO> getProductDetailByID(String id) {

        Single<ProductEntity> productSingle = repositories.findById(id);     JsonObject jsonProductDetailDTO = new JsonObject();

        Single<ProductDetailDTO> single = Single.create(emitter -> {

            productSingle.subscribe(productEntity -> {

                Single<JsonObject> providerSingle = sendAndReplyJsonObject(
                        productEntity.getProvider_id(),
                        AddressConstants.ADDRESS_EB_GET_PROVIDER_BY_ID);
                Single<JsonObject> categorySingle = sendAndReplyJsonObject(
                        productEntity.getProduct_category_id(),
                        AddressConstants.ADDRESS_EB_GET_PRODUCT_CATEGORY_BY_ID);

                Single.zip(categorySingle, providerSingle, (entries, entries2) -> {
                    jsonProductDetailDTO.mergeIn(entries).mergeIn(entries2)
                            .mergeIn(JsonObject.mapFrom(productEntity));
                    return jsonProductDetailDTO;
                }).subscribe(entries -> {
                    emitter.onSuccess(entries.mapTo(ProductDetailDTO.class));
                }, throwable -> {
                    emitter.onError(throwable);
                });

            }, throwable -> {
                emitter.onError(throwable);
            });
        });

        return single;
    }
}
