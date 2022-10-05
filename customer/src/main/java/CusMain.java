import io.vertx.core.Vertx;
import utils.AddressConstants;

public class CusMain {

  public static void main(String[] args) {
    Vertx vertx =Vertx.vertx();
    vertx.deployVerticle(new CustomerConsumer());
    vertx.eventBus().send(AddressConstants.ADDRESS_EB_DELETE_CUSTOMER,null, event -> {
      System.out.println("send "+event.result().body());
    });
    vertx.eventBus().send(AddressConstants.ADDRESS_EB_GET_CUSTOMER,null, event -> {
      System.out.println("send "+event.result().body());
    });
  }
}
