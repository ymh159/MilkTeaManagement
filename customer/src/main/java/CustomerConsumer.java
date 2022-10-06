import entity.TypeValueReply;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import java.util.ArrayList;
import java.util.List;
import utils.AddressConstants;

public class CustomerConsumer extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    consumer();
  }

  public void consumer(){
    EventBus eb = vertx.eventBus();
    // get customer by id
    List<String> list = new ArrayList<>();
    list.add(AddressConstants.ADDRESS_EB_DELETE_CUSTOMER);
    list.add(AddressConstants.ADDRESS_EB_GET_CUSTOMER_BY_ID);
    list.add(AddressConstants.ADDRESS_EB_GET_CUSTOMER);
    list.add(AddressConstants.ADDRESS_EB_INSERT_CUSTOMER);
    list.add(AddressConstants.ADDRESS_EB_UPDATE_CUSTOMER);
    list.forEach(s -> {
      eb.consumer(s,event -> {
        System.out.println("consumer "+s);
        event.reply(s);
      });
    });

  }
}
