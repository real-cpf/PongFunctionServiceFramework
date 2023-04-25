package entity;

public enum FuncResult {

  SUCCESS("msg"),
  FAIL("value");
  String value;
  FuncResult(String v){
    this.value = v;
  }
  Object detail;
  public FuncResult setDetail(Object detail){
    this.detail = detail;
    return this;
  }

  @Override
  public String toString() {
    return String.format("{\"result\":\"%s\",\"detail\":\"%s\"}",this.value,this.detail);
  }
}
