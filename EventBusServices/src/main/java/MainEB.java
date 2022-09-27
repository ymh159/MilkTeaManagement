import utils.DeloyVerticle;

public class MainEB {

  public static void main(String[] args) {
    DeloyVerticle deloyVerticle = new DeloyVerticle();
    deloyVerticle.deloyVerticleCommon(EBVerticle.class);
  }
}
