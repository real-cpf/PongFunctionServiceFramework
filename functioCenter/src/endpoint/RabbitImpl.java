package endpoint;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import entity.Request;
import routine.Routines;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class RabbitImpl {
  private final static Gson gson = new Gson();
  private final String QUEUE_NAME;

  public RabbitImpl(String queue) {
    QUEUE_NAME = queue;
  }

  public void init() {
    try {
      TheConnectionFactory theConnectionFactory = new TheConnectionFactory();
      ConnectionFactory factory = theConnectionFactory.get();
      Connection connection = factory.newConnection();
      Channel channel = connection.createChannel();

      channel.queueDeclare(QUEUE_NAME, false, false, false, null);

      DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
        process(message);
      };

      channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
      });
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void process(String s) {

    Routines.defaultAsync(() -> gson.fromJson(s, Request.class)).thenApply((o)->{
      Request request = (Request) o;
      String name = request.getFuncName();
      return name;
    } ).whenComplete((o,error)->{
      if (null != error){
        System.out.println(o);
      }else {
        Throwable throwable = (Throwable) error;
        throwable.printStackTrace();
      }
    });
  }
}
