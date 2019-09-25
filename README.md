# DRcode Library :tada:


## CsvUtils  
**CsvUtils useful csv library!**  

### Write  
>1. default writer  
>>writeCsv(List<String[]> list, String path, String charsetName) 
> ```java
> String[] header = {"key", "value"};
> List<String[]> saveList = new ArrayList<>();
> saveList.add(header);
> csvUtils.writeCsv(saveList, path, "utf-8");
>  ```
> |key|value|
> |----|----| 
