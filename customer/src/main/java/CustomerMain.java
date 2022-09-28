import utils.DeloyVerticle;


public class CustomerMain {

  public static void main(String[] args) {
    DeloyVerticle deloyVerticle = new DeloyVerticle();
    deloyVerticle.deloyVerticleCommon(CustomerVerticle.class);
  }
}
