package utils;

import com.hazelcast.config.Config;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class DeloyVerticle {

  public VertxOptions setOptionHazelcast() {
    Config hazelcastConfig = new Config();
//    hazelcastConfig.getNetworkConfig().getJoin().getTcpIpConfig().addMember("127.0.0.1").setEnabled(true);
//    hazelcastConfig.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
    hazelcastConfig.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(false);
    hazelcastConfig.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(true);
    ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);
    VertxOptions options = new VertxOptions().setClusterManager(mgr);
    return options;
  }

  public void deloyVerticleCommon(Class clazz) {
    Config hazelcastConfig = new Config();
    String verticleID;

    verticleID = clazz.getName();
//    hazelcastConfig.getNetworkConfig().getJoin().getTcpIpConfig().addMember("127.0.0.1").setEnabled(true);
//    hazelcastConfig.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
    hazelcastConfig.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(false);
    hazelcastConfig.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(true);
    ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);
    VertxOptions options = new VertxOptions().setClusterManager(mgr);
    Vertx.clusteredVertx(options, res -> {
      if (res.succeeded()) {
        Vertx vertx = res.result();
        vertx.deployVerticle(verticleID);
      } else {
      }
    });
  }
}
