import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import router.VerticleRouter;
import router.handlers.ApplicationHandlers;

public class CreateHttpServer extends AbstractVerticle {
  private static final Logger LOGGER = LoggerFactory.getLogger(CreateHttpServer.class);

  @Override
  public void start() throws Exception {
    VerticleRouter verRouter = new VerticleRouter();
    vertx.createHttpServer()
        .requestHandler(verRouter.getRouter(vertx))
        .listen(8080);
  }
}
