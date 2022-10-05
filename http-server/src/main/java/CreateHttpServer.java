import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import router.VerticleRouter;
import router.handlers.ApplicationHandlers;

public class CreateHttpServer extends AbstractVerticle {

  @Override
  public void start() {
    VerticleRouter verRouter = new VerticleRouter();
    vertx.createHttpServer()
        .requestHandler(verRouter.getRouter(vertx))
        .listen(8080);
  }
}
