package dsl;

import entity.FuncResult;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

final public class GroovyRun {
  private final static Map<String, GroovyObject > FUNC_CACHE = new HashMap<>(16);
  private GroovyClassLoader loader;
  private GroovyRun(){
     loader = new GroovyClassLoader();
  }
  public void doRun(String name,Map<String,Object> params) {
    if (FUNC_CACHE.containsKey(name)) {
      FuncResult result = (FuncResult) FUNC_CACHE.get(name).invokeMethod("main",new Object[]{params});
      System.out.println(result);
    }else {
      System.out.println("no func");
      new RuntimeException("no func");
    }
  }
  public void newGroovy(String script,String name){
    Class clazz = loader.parseClass(script);
    try {
      GroovyObject object = (GroovyObject) clazz.getConstructor().newInstance();
      FUNC_CACHE.put(name,object);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  private static class GroovyRunHolder{
    static GroovyRun INSTANCE = new GroovyRun();
  }
  public static GroovyRun get(){
    return GroovyRunHolder.INSTANCE;
  }
}
