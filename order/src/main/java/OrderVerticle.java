import eb.OrderRegisterEBAddress;
import io.vertx.core.AbstractVerticle;


public class OrderVerticle extends AbstractVerticle {


  @Override
  public void start() {
    new OrderRegisterEBAddress(vertx);
  }
}
