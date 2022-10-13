package utils;

import io.reactivex.rxjava3.core.Single;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

public class ConvertUtils {

  public static <T> Single futureToSingle(Future<T> future) {
    Single single = Single.create(emitter -> {
      future.setHandler(res -> {
        if (res.succeeded()) {
          emitter.onSuccess(res.result());
        } else {
          emitter.onError(res.cause());
        }
      });
    });
    return single;
  }

  public static <T> Future<T> singleToFuture(Single<T> single) {
    Future future = Future.future();
    single.subscribe(
            res ->
                    future.complete(res),
            throwable -> {
              future.fail((Throwable) throwable);
            });

    return future;
  }
}
