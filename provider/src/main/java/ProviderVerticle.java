import eb.ProviderRegisterEBAddress;
import io.vertx.core.AbstractVerticle;

public class ProviderVerticle extends AbstractVerticle {

    @Override
    public void start() {
        new ProviderRegisterEBAddress(vertx);
    }
}