import java.io.FileNotFoundException;
import utils.ClusterDeloyVerticle;


public class UserMain {

  public static void main(String[] args) {
    ClusterDeloyVerticle clusterDeloyVerticle = new ClusterDeloyVerticle();
    clusterDeloyVerticle.deloyVerticleCommon(UserVerticle.class);
  }
}
