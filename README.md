# DRcode Library :tada:


## CsvUtils  
**CsvUtils useful csv library!** :heavy_check_mark:  

**Example Sample Vo Class**
```java
class SampleVo{
    private String key;
    private String value;
    
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
This Library need for getter/setter on VoClass
 

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

<br/>
<br/>

>**2. List Model Write**
>>void **writeCsv**(List<T> list, String path, String charsetName, Class<T> classType)  
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

<br/>
<br/>

>**3. Map Model Write**
>>void **writeCsv**(Map<String, List<T>> writeMap, String path, String charsetName, Class<T> classType)  
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

<br/>
<br/>

### Read
>**1. Default Read**  
>>List<String[]> **readCsv**(String path, String charsetName, int line)   
> ```java
> List<String[]> csvList = csvUtils.readCsv(path, "utf-8", 1); //write csv
>  ```

<br/>
<br/>

>**2. Model Read**  
>>List<T> **readModelCsv**(String path, String charsetName, Class<T> classType)  
> ```java
> List<SampleVo> csvList = csvUtils.readModelCsv(path, "utf-8", SampleVo.class); //write csv
>  ```
> 
>If you have different csv header then add header name setter  
>
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
<br/>

