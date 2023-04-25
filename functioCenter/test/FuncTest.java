import dsl.GroovyRun;
import dsl.JavaFunc;
import func.FileFunc;
import func.TheFunc;

import java.util.HashMap;
import java.util.Map;

public class FuncTest {
  public static void main(String[] args) {
    runCreateFile();
  }
  private static void runCreateFile(){
    String s = "import dsl.JavaFunc\n" +
      "import func.TheFunc\n" +
      "\n" +
      "def main(Map<String,Object> param) {\n" +
      "    TheFunc func = JavaFunc.theFuncMap.get(param.get(\"javaFunc\"));\n" +
      "    param.put(\"content\",String.format(\"[%s]\",param.get(\"content\")));\n" +
      "    return func.run(param);\n" +
      "}";

    String name = "write2file";
    TheFunc theFunc = new FileFunc();
    String javaFuncName = FileFunc.class.getName();
    JavaFunc.theFuncMap.put(javaFuncName,theFunc);
    GroovyRun.get().newGroovy(s,name);
    Map<String,Object> map = new HashMap<>(4);
    map.put("path","/tmp/abc.txt");
    map.put("content","hello world");
    map.put("javaFunc",javaFuncName);
    GroovyRun.get().doRun(name,map);
  }
}
