package utils;

import io.reactivex.rxjava3.core.Single;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

public class ConvertUtils {

  public static Single futureToSingle(Future<?> future){
    Single single = Single.create(emitter -> {
      future.setHandler(res ->{
        if (res.succeeded()){
          emitter.onSuccess(res.result());
        }else{
          emitter.onError(new Throwable(res.cause().getMessage()));
        }
      });
    });
    return single;
  }

  public Future singleToFuture(Single single){
    Future future = Future.future();
    single.subscribe((res, throwable) -> {
      if(throwable!=null){
        future.complete(res);
      }else{
        future.fail((Throwable) throwable);
      }
    });
    return future;
  }
}
