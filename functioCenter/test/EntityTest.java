import com.google.gson.Gson;
import entity.FuncResult;

public class EntityTest {
  public static void main(String[] args) {
    FuncResult funcResult = FuncResult.FAIL;
    funcResult.setDetail("this is error");
    System.out.println(funcResult);
  }
}
