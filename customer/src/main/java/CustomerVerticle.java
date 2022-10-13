import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.CustomerRepositories;
import repositories.eb.CustomerRegisterEBAddress;
import repositories.impl.CustomerRepositoriesImpl;
import utils.ReplyMessageEB;

public class CustomerVerticle extends AbstractVerticle {


    @Override
    public void start() {
        new CustomerRegisterEBAddress(vertx);
    }
}
