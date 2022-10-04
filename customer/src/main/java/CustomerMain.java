import utils.ClusterDeloyVerticle;


public class CustomerMain {

  public static void main(String[] args){
    ClusterDeloyVerticle clusterDeloyVerticle = new ClusterDeloyVerticle();
    clusterDeloyVerticle.deloyVerticleCommon(CustomerVerticle.class);
  }
}
