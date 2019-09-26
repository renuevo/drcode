# DRcode Library :tada:

Add bulid.gradle setting
```groovy
repositories {
    ...
   	maven { url 'https://jitpack.io' } //add repositories
}

dependencies {
    ...
    implementation 'com.github.renuevo:drcode_library:1.1'  //add releases current version
    ...
}
```  

**Example Sample Vo Class**
```java
class SampleVo{
    private String key;
    private String value;
    
    public SampleVo(String key, String value){
    }   

    public SampleVo(String key, String value){
        this.key = key;
        this.value = value;
    }   
 
    public void setKey(String key){
        this.key = key;
    }
    
    public void setValue(String value){
        this.value = value;
    }
    
    public String getKey(){
        return key;
    }

    public String getValue(){
        return value;
    }   
}
```

---

## CsvUtils  
**CsvUtils useful csv library!** :heavy_check_mark:  

This library need for getter/setter on VoClass
 
### Write  
>**1. Default Write**  
>>void **writeCsv**(List<String[]> list, String path, String charsetName) 
> ```java
> String[] header = {"name", "deokhwa"};
> 
> List<String[]> saveList = new ArrayList<>();
> saveList.add(header); //create sample list
> 
> csvUtils.writeCsv(saveList, path, "utf-8"); //write csv
>  ```
>   
> **Csv Out Result**  
>
> |name|deokhwa|  
> |----|----| 
>
><br/>
><br/>
>
>**2. List Model Write**
>>void **writeCsv**(List<T> list, String path, String charsetName, Class\<T> classType)  
>```java
>SampleVo sampleVo = new SampleVo("name","deokhwa");
>
>List<SampleVo> saveList = new ArrayList<>();
>saveList.add(sampleVo); //create sample list
>
>csvUtils.writeCsv(saveList, path, "utf-8", SampleVo.class);   //write csv
>```
>
> **Csv Out Result**  
>
> |key|value|  
> |----|----|  
> |name|deokhwa|  
>
> Csv Header Is Model Member Variable Name    
>
><br/>
><br/>
>
>**3. Map Model Write**
>>void **writeCsv**(Map<String, List<T>> writeMap, String path, String charsetName, Class\<T> classType)  
>```java
>SampleVo sampleVo1 = new SampleVo("name1","deokhwa");
>SampleVo sampleVo2 = new SampleVo("name2","renuevo");
>
>Map<String, List<SampleVo>> saveMap = new HashMap<>();
>saveMap.put("one", Arrays.asList(sampleVo1)); //create sample list
>saveMap.put("two", Arrays.asList(sampleVo2)); //create sample list
>
>csvUtils.writeCsv(saveMap, path, "utf-8", SampleVo.class);   //write csv
>```
>
> **Csv Out Result**  
>
> |key|value|  
> |----|----|  
> |name1|deokhwa|  
> |name2|renuevo|  
>
> Csv Header Is Model Member Variable Name  
>
<br/>
<br/>

### Read
>**1. Default Read**  
>List<String[]> **readCsv**(String path, String charsetName, int line)   
> ```java
> List<String[]> csvList = csvUtils.readCsv(path, "utf-8", 1); //write csv
>  ```
>
><br/>
><br/>
>
>**2. Model Read**  
>List\<T> **readModelCsv**(String path, String charsetName, Class\<T> classType)  
> ```java
> List<SampleVo> csvList = csvUtils.readModelCsv(path, "utf-8", SampleVo.class); //write csv
>  ```
> 
>If you have different csv header then add header name setter  
>
>**Csv Out Result**  
>|title|value|  
>|----|----|  
>|name|deokhwa|  
>
> ```java
> class SampleVo{
>    private String key;
>    ...
> 
>    //add header setter
>    public void setTitle(String title){
>         this.key = title;
>    } 
>    ...
> }
>  ```

<br/>


---

## VoMapperUtils  
**VoMapperUtils useful java reflection library!** :heavy_check_mark:  

**VoMapperUtils** default use in static method  

**Key Type**
>1. default : setKeyValue() -> keyValue  
>2. lower : setKeyValue() -> keyvalue  
>3. upper : setKeyValue() -> KEYVALUE
>4. underUpper : setKey_value() -> keyValue  
>5. upperUnder : setKeyValue() -> key_value
>6. all : all key type  1 ~ 5 pleases check duplication :exclamation:  

### Set  
>This utils return type :point_right: **Map<String, Method>**
> 
>Map<String, Method> **getFieldMehtods**(Class\<T> classType, String methodType) 
>```java
>Map<String, Method> setSampleVoMap = VoMapperUtils.getFieldMehtods(SampleVo.class, "set");  //keytype default
>String[] memberArray = {"key","value"};
>
>SampleVo sampleVo = new SampleVo();
>
>for(String key : memberArray){  //key is key/value
>    if(setSampleVoMap.containsKey(key)) setSampleVoMap.get(key).invoke(sampleVo, key);  //set SampleVo value
>}
>```
>**SampleVo Result**  
>
>|key|value|
>|----|----|
>|key|value|
>
><br/>
><br/>
>
>**Example keyType All**  
>Map<String, Method> **getFieldMehtods**(Class<T> classType, String methodType, String keyType) 
>```java
>//setSampleVoMap key -> value/Value key/Key 
>Map<String, Method> setSampleVoMap = VoMapperUtils.getFieldMehtods(SampleVo.class, "set", "all");
>```

<br/>
<br/>

### Get  
>This utils return type :point_right: **Map<String, Method>**
> 
>Map<String, Method> **getFieldMehtods**(Class\<T> classType, String methodType) 
>```java
>Map<String, Method> getSampleVoMap = VoMapperUtils.getFieldMehtods(SampleVo.class, "get");  //keytype default
>SampleVo sampleVo = new SampleVo("name", "deokhwa kim");
>
>System.out.println(getSampleVoMap.get("value").invoke(sampleVo)); //return Object Type
>```
>**Println Result** 
>```java
> deokhwa kim
>``` 
>
>***key type parameter same Set***  

---






