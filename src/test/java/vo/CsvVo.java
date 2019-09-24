package vo;


public class CsvVo {
    private String key;
    private String value;

    public CsvVo(){}
    public CsvVo(String key, String value){
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean equals(Object obj) {
        if(obj instanceof CsvVo){
            CsvVo csvVo = (CsvVo) obj;
            return csvVo.key.equals(this.key) && csvVo.value.equals(this.value);
        }
        return false;
    }

}
