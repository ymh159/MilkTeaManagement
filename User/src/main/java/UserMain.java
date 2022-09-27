import io.vertx.core.AbstractVerticle;
import utils.DeloyVerticle;


public class UserMain {

  public static void main(String[] args) {
    DeloyVerticle deloyVerticle = new DeloyVerticle();
    deloyVerticle.deloyVerticleCommon(UserMain.class);
  }
}
