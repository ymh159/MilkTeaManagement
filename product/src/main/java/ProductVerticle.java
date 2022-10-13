import eb.ProductRegisterEBAddress;
import io.vertx.core.AbstractVerticle;

public class ProductVerticle extends AbstractVerticle {


    @Override
    public void start() throws Exception {
        new ProductRegisterEBAddress(vertx);
    }
}
