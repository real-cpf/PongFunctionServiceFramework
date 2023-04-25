package routine;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class Routines {
  private final static ExecutorService DEFAULT = Executors.newVirtualThreadPerTaskExecutor();
  public static ExecutorService newRoutinePool() {
    return Executors.newVirtualThreadPerTaskExecutor();
  }

  public static <U> CompletableFuture defaultAsync(Supplier<U> supplier) {
    return CompletableFuture.supplyAsync(supplier,DEFAULT);
  }
  public static void defaultAsync(Runnable runnable){
    CompletableFuture.runAsync(runnable,DEFAULT);
  }
}
