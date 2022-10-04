import java.io.FileNotFoundException;
import utils.ClusterDeloyVerticle;


public class ProductCategoryMain {

  public static void main(String[] args) {
    ClusterDeloyVerticle clusterDeloyVerticle = new ClusterDeloyVerticle();
    clusterDeloyVerticle.deloyVerticleCommon(ProductCategoryVerticle.class);
  }
}
