
import env.EnvLoader;
import server.instance.UdsServerSocket;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
  public static void main(String[] args) throws ExecutionException, InterruptedException {
    EnvLoader.get();
  }
}
