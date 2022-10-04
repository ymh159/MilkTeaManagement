import io.vertx.core.Vertx;
import utils.DeloyVerticle;

public class HttpServerMain {

  public static void main(String[] args) {
    DeloyVerticle deloyVerticle = new DeloyVerticle();
    deloyVerticle.deloyVerticleCommon(CreateHttpServer.class);
  }
}
