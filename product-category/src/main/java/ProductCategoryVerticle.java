import eb.ProductCategoryRegisterEBAddress;
import io.vertx.core.AbstractVerticle;

public class ProductCategoryVerticle extends AbstractVerticle {

    @Override
    public void start() {
        new ProductCategoryRegisterEBAddress(vertx);
    }
}