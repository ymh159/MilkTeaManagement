import utils.DeloyVerticle;


public class ProviderMain {

  public static void main(String[] args) {
    DeloyVerticle deloyVerticle = new DeloyVerticle();
    deloyVerticle.deloyVerticleCommon(ProviderVerticle.class);
  }
}
