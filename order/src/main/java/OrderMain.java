import java.io.FileNotFoundException;
import utils.ClusterDeloyVerticle;


public class OrderMain {

  public static void main(String[] args) {
    ClusterDeloyVerticle clusterDeloyVerticle = new ClusterDeloyVerticle();
    clusterDeloyVerticle.deloyVerticleCommon(OrderVerticle.class);
  }
}
