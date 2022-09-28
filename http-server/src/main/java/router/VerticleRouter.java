package router;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import router.handlers.ApplicationHandlers;
import utils.Constants;

public class VerticleRouter extends AbstractVerticle {

  public Router getRouter(Vertx vertx) {
    ApplicationHandlers applicationHandlers = new ApplicationHandlers(vertx);
    Router router = Router.router(vertx);
    // =
    router.route(Constants.PATH_CUSTOMER).handler(applicationHandlers::customerHandler);
    router.route(Constants.PATH_ORDER_DETAIL).handler(applicationHandlers::orderDetailHandler);
    router.route(Constants.PATH_ORDER).handler(applicationHandlers::orderHandler);
    router.route(Constants.PATH_PRODUCT_CATEGORY)
        .handler(applicationHandlers::productCategoryHandler);
    router.route(Constants.PATH_PRODUCT).handler(applicationHandlers::productHandler);
    router.route(Constants.PATH_PROVIDER).handler(applicationHandlers::providerHandler);
    router.route(Constants.PATH_USER).handler(applicationHandlers::userHandler);

    // path/:id
    router.route(Constants.PATH_CUSTOMER + Constants.PATH_ID).handler(applicationHandlers::customerHandler);
    router.route(Constants.PATH_ORDER_DETAIL + Constants.PATH_ID).handler(applicationHandlers::orderDetailHandler);
    router.route(Constants.PATH_ORDER + Constants.PATH_ID).handler(applicationHandlers::orderHandler);
    router.route(Constants.PATH_PRODUCT_CATEGORY)
        .handler(applicationHandlers::productCategoryHandler);
    router.route(Constants.PATH_PRODUCT + Constants.PATH_ID).handler(applicationHandlers::productHandler);
    router.route(Constants.PATH_PROVIDER + Constants.PATH_ID).handler(applicationHandlers::providerHandler);
    router.route(Constants.PATH_USER + Constants.PATH_ID).handler(applicationHandlers::userHandler);
    router.route(Constants.PATH_USER + Constants.PATH_ID).handler(applicationHandlers::userHandler);

    return router;
  }
}
