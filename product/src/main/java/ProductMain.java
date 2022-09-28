import utils.DeloyVerticle;


public class ProductMain {

  public static void main(String[] args) {
    DeloyVerticle deloyVerticle = new DeloyVerticle();
    deloyVerticle.deloyVerticleCommon(ProductVerticle.class);
  }
}
