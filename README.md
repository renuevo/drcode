# DRcode Library :tada:


## CsvUtils  
**CsvUtils useful csv library!**  

### Write  
>**1. Default Write**  
>>**writeCsv**(List<String[]> list, String path, String charsetName) 
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
> |name|deokhwa|  
> |----|----| 

<br/>

>**2. List Model Write**
>>**writeCsv**(List<T> list, String path, String charsetName, Class<T> classType)  
>```java
>//SampleVo member variable String key, value; 
>//need for getter on member variable
>SampleVo sampleVo = new SampleVo("name","deokhwa");
>
>List<SampleVo> saveList = new ArrayList<>();
>saveList.add(sampleVo); //create sample list
>
>csvUtils.writeCsv(saveList, path, "utf-8", SampleVo.class);   //write csv
>```
>
> **Csv Out Result**  
> |key|value|  
> |----|----|  
> |name|deokhwa|
> Csv Header Is Model Member Variable Name  

<br/>

>**3. Map Model Write**
>>** writeCsv(Map<String, List<T>> writeMap, String path, String charsetName, Class<T> classType)  
>```java
>//SampleVo member variable String key, value; 
>//need for getter on member variable
>SampleVo sampleVo1 = new SampleVo("name1","deokhwa");
>SampleVo sampleVo2 = new SampleVo("name2","renuevo");
>
>Map<String, List<SampleVo>> saveMap = new HashMap<>();
>saveList.put("one", Arrays.asList(sampleVo1)); //create sample list
>saveList.put("two", Arrays.asList(sampleVo2)); //create sample list
>
>csvUtils.writeCsv(saveList, path, "utf-8", SampleVo.class);   //write csv
>```
>
> **Csv Out Result**  
> |key|value|  
> |----|----|  
> |name1|deokhwa|  
> |name2|renuevo|  
> Csv Header Is Model Member Variable Name  

<br/>

### Read
