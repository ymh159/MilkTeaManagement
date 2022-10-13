import eb.UserRegisterEBAddress;
import io.vertx.core.AbstractVerticle;

public class UserVerticle extends AbstractVerticle {

    @Override
    public void start() {
        new UserRegisterEBAddress(vertx);
    }
}
