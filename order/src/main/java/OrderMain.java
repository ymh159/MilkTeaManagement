import utils.DeloyVerticle;


public class OrderMain {

  public static void main(String[] args) {
    DeloyVerticle deloyVerticle = new DeloyVerticle();
    deloyVerticle.deloyVerticleCommon(OrderVerticle.class);
  }
}
