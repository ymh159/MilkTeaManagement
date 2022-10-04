import java.io.FileNotFoundException;
import utils.ClusterDeloyVerticle;

public class HttpServerMain {

  public static void main(String[] args){
    ClusterDeloyVerticle clusterDeloyVerticle = new ClusterDeloyVerticle();
    clusterDeloyVerticle.deloyVerticleCommon(CreateHttpServer.class);
  }
}
