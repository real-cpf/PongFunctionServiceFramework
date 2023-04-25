package func;

import entity.FuncResult;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;

public class FileFunc implements TheFunc {
  public FuncResult run(Map<String,Object> param){
    if (param.size() != 3){
      throw new IllegalArgumentException("least 2 param");
    }
    String path = (String) param.get("path");
    String content = (String) param.get("content");
    try{
      Path filePath = Path.of(path);
      Files.write(filePath,content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE,StandardOpenOption.WRITE);
    }catch (Exception e){
      return FuncResult.FAIL.setDetail(e.getMessage());
    }
    return FuncResult.SUCCESS.setDetail("ok");
  }
}
