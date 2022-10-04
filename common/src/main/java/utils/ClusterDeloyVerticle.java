package utils;

import com.hazelcast.config.Config;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClusterDeloyVerticle {

  public void deloyVerticleCommon(Class clazz){
    String verticleID;
    verticleID = clazz.getName();

    Set<String> strings = new HashSet<>();
    Enumeration enumeration = null;
    try {
      enumeration = NetworkInterface.getNetworkInterfaces();
    }catch (SocketException ex){
      ex.printStackTrace();
    }
    while (enumeration.hasMoreElements()){
      NetworkInterface networkInterface = (NetworkInterface) enumeration.nextElement();
      Enumeration ee = networkInterface.getInetAddresses();
      while (ee.hasMoreElements()){
        InetAddress inetAddress = (InetAddress) ee.nextElement();
        Pattern pattern = Pattern.compile(ReadFileProperties.read().getProperty(Constants.IP_ADDRESS));
        Matcher matcher = pattern.matcher(inetAddress.getHostAddress());
        if (matcher.find()){
          strings.add(inetAddress.getHostAddress());
        }
      }
    }

    Config config = new Config();
    config.getNetworkConfig().getJoin().getMulticastConfig().setTrustedInterfaces(strings);

//    hazelcastConfig.getNetworkConfig().getJoin().getTcpIpConfig().addMember("127.0.0.1").setEnabled(true);
//    hazelcastConfig.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
    //hazelcastConfig.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(false);
    //hazelcastConfig.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(true);
    ClusterManager mgr = new HazelcastClusterManager(config);
    VertxOptions options = new VertxOptions().setClusterManager(mgr);
    Vertx.clusteredVertx(options, res -> {
      if (res.succeeded()) {
        Vertx vertx = res.result();
        vertx.deployVerticle(verticleID);
      } else {
        throw new RuntimeException(Constants.CAN_NOT_DELOY);
      }
    });
  }
}
