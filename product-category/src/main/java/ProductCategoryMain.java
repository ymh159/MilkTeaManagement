import utils.DeloyVerticle;


public class ProductCategoryMain {

  public static void main(String[] args) {
    DeloyVerticle deloyVerticle = new DeloyVerticle();
    deloyVerticle.deloyVerticleCommon(ProductCategoryVerticle.class);
  }
}
