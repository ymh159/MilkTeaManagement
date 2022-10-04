import java.io.FileNotFoundException;
import utils.ClusterDeloyVerticle;


public class ProviderMain {

  public static void main(String[] args) {
    ClusterDeloyVerticle clusterDeloyVerticle = new ClusterDeloyVerticle();
    clusterDeloyVerticle.deloyVerticleCommon(ProviderVerticle.class);
  }
}
