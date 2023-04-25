import com.google.gson.Gson;
import entity.Request;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import routine.Routines;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ProcessTest {
  public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    GroovyClassLoader loader =  new GroovyClassLoader();
    String s = "import func.TheFunc;\r\n def parseContent(String s){\n" +
      "    return \"[\" + s + \"]\"\n" +
      "}\r\n";
    s += "def msg(){\n" +
      "    return \"msg-test\"\n" +
      "}\r\n";
    s += "def write2file(Map<String,Object> param, TheFunc func) {\n" +
      "    param.put(\"content\",String.format(\"[%s]\",param.get(\"content\")));\n" +
      "    func.run(param);\n" +
      "}";
    Class clazz = loader.parseClass(s);
    GroovyObject object = (GroovyObject) clazz.getConstructor().newInstance();
    Object[] param = {"test"};
    String res =(String) object.invokeMethod("parseContent",param);
    System.out.println(res);
    Object[] p = {};
    System.out.println(object.invokeMethod("msg",p));
  }
  public static void main1(String[] args) {
    Gson gson = new Gson();
    String s = "{\"logic\":\"///\",\"traceId\":\"xxxx-x-x-x\",\"params\":{\"encode\":\"utf-8\",\"path\":\"/tmp/1\",\"content\":\"1234\"},\"funcName\":\"createFile\"}";
    Routines.defaultAsync(() -> gson.fromJson(s, Request.class)).thenApply((o)->{
      Request request = (Request) o;
      String name = request.getFuncName();


      return name;
    } ).whenComplete((o,error)->{
      if (null == error){
        System.out.println(o);
      }else {
        Throwable throwable = (Throwable) error;
        throwable.printStackTrace();
      }
    });

    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
