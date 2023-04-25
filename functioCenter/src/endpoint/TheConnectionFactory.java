package endpoint;

import com.rabbitmq.client.ConnectionFactory;
import env.EnvLoader;

public class TheConnectionFactory {
  private final ConnectionFactory factory;
  public TheConnectionFactory(){
    factory = new ConnectionFactory();
    String host = EnvLoader.get().getByName("mq.host");
    String user = EnvLoader.get().getByName("mq.user");
    String passwd = EnvLoader.get().getByName("mq.passwd");
    String portValue  = EnvLoader.get().getByName("mq.port");
    int port = 5672;
    if (portValue.chars().allMatch(Character::isDigit)){
      port = Integer.parseInt(portValue);
    }
    factory.setHost(host);
    factory.setPort(port);
    factory.setUsername(user);
    factory.setPassword(passwd);
  }
  public ConnectionFactory get(){
    return factory;
  }
}
