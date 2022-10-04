import java.io.FileNotFoundException;
import utils.ClusterDeloyVerticle;


public class ProductMain {

  public static void main(String[] args) {
    ClusterDeloyVerticle clusterDeloyVerticle = new ClusterDeloyVerticle();
    clusterDeloyVerticle.deloyVerticleCommon(ProductVerticle.class);
  }
}
