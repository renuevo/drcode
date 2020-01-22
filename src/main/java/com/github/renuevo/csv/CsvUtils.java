package com.github.renuevo.csv;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.github.renuevo.common.VoMapperUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * @className : CsvUtils
 * @author : Deokhwa.Kim
 * @description : csv 유틸
 * @version : 1.6 -> read_encoding 방식 통일
 * @update : 2018-10-08
 * @since : 2018-08-03
 * </pre>
 */

public class CsvUtils {

    private static final String UTF8_BOM = "\uFEFF";

    public void writeCsv(List<String[]> list, String path, String charsetName) throws IOException {

        if (!path.substring(path.length() - 4).equalsIgnoreCase(".csv"))
            path += ".csv";

        FileOutputStream outputStream = null;
        CSVWriter csvWriter = null;
        try {
            outputStream = new FileOutputStream(path);

            //UTF-8 BOM으로 인코딩 설정
            if (charsetName.equalsIgnoreCase("utf-8")) {
                outputStream.write(0xEF);
                outputStream.write(0xBB);
                outputStream.write(0xBF);
            }

            csvWriter = new CSVWriter(new OutputStreamWriter(outputStream));
            csvWriter.writeAll(list);
        } finally {
            if (csvWriter != null) csvWriter.close();
        }
    }

    /**
     * <pre>
     *  @methodName : readCsv
     *  @author : Deokhwa.Kim
     *  @since : 2018-01-24 오후 7:41
     *  @param path, charsetName, line
     *  @description : line 추가로 몇 라인부터 읽어올지 정할수 있게 되었다
     *  @return java.util.List<java.lang.String [ ]>
     * </pre>
     */
    public List<String[]> readCsv(String path, String charsetName, int line) throws IOException {

        if (!path.substring(path.length() - 4).equalsIgnoreCase(".csv"))
            path += ".csv";

        CSVReader csvReader = null;
        List<String[]> resultList = new ArrayList<>();

        try {
            csvReader = new CSVReader(new InputStreamReader(new FileInputStream(path), charsetName.toUpperCase()), ',', '\"', line);

            String[] csvLine;
            boolean firstLine = true;

            while ((csvLine = csvReader.readNext()) != null) {

                if (firstLine) {
                    csvLine[0] = removeUTF8BOM(csvLine[0]);
                    firstLine = false;
                }

                resultList.add(csvLine);
            }
        } finally {
            if (csvReader != null) csvReader.close();
        }

        return resultList;
    }


    public <T> List<T> readModelCsv(String path, String charsetName, Class<T> classType) throws IOException {

        if (!path.substring(path.length() - 4).equalsIgnoreCase(".csv"))
            path += ".csv";

        CSVReader csvReader = null;
        List<T> resultList = new ArrayList<>();

        Map<String, Method> setMethodMap = VoMapperUtils.getFieldMehtods(classType, "set");

        //if (!charsetName.equalsIgnoreCase("utf-8"))
        try {
            csvReader = new CSVReader(new InputStreamReader(new FileInputStream(path), charsetName.toUpperCase()), ',', '\"', 0);
            //else
            //   csvReader = new CSVReader(new FileReader(path));

            String[] csvLine;
            String[] csvTitle;

            T classTemplate = null;
            csvTitle = csvReader.readNext();
            csvTitle[0] = removeUTF8BOM(csvTitle[0]);

            while ((csvLine = csvReader.readNext()) != null) {
                classTemplate = classType.getDeclaredConstructor().newInstance();
                for (int i = 0; i < csvTitle.length; i++) {
                    if (setMethodMap.containsKey(csvTitle[i]))
                        setMethodMap.get(csvTitle[i]).invoke(classTemplate, csvLine[i]);
                }
                resultList.add(classTemplate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (csvReader != null) csvReader.close();
        }

        return resultList;
    }


    public <T> void writeCsv(List<T> list, String path, String charsetName, Class<T> classType) throws IOException {

        if (!path.substring(path.length() - 4).equalsIgnoreCase(".csv"))
            path += ".csv";

        FileOutputStream outputStream = null;
        CSVWriter csvWriter = null;
        try {
            outputStream = new FileOutputStream(path);

            //UTF-8 BOM으로 인코딩 설정
            if (charsetName.equalsIgnoreCase("utf-8")) {
                outputStream.write(0xEF);
                outputStream.write(0xBB);
                outputStream.write(0xBF);
            }


            csvWriter = new CSVWriter(new OutputStreamWriter(outputStream));

            //제목 라인 input
            Field[] fields = classType.getDeclaredFields();
            String[] outStrings = new String[fields.length];
            String[] methodNames = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                outStrings[i] = fields[i].getName();
                methodNames[i] = "get" + fields[i].getName().substring(0, 1).toUpperCase() + fields[i].getName().substring(1);
            }
            csvWriter.writeNext(outStrings);

            for (T object : list) {
                for (int i = 0; i < fields.length; i++) {
                    outStrings[i] = (String) object.getClass().getDeclaredMethod(methodNames[i]).invoke(object);
                }
                csvWriter.writeNext(outStrings);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (csvWriter != null) csvWriter.close();
        }


    }

    public <T> void writeCsv(Map<String, List<T>> writeMap, String path, String charsetName, Class<T> classType) throws IOException {

        if (!path.substring(path.length() - 4).equalsIgnoreCase(".csv"))
            path += ".csv";

        FileOutputStream outputStream;
        CSVWriter csvWriter = null;

        try {
            outputStream = new FileOutputStream(path);

            //UTF-8 BOM으로 인코딩 설정
            if (charsetName.equalsIgnoreCase("utf-8")) {
                outputStream.write(0xEF);
                outputStream.write(0xBB);
                outputStream.write(0xBF);
            }

            csvWriter = new CSVWriter(new OutputStreamWriter(outputStream));

            //제목 라인 input
            Field[] fields = classType.getDeclaredFields();
            String[] outStrings = new String[fields.length];
            String[] methodNames = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                outStrings[i] = fields[i].getName();
                methodNames[i] = "get" + fields[i].getName().substring(0, 1).toUpperCase() + fields[i].getName().substring(1);
            }
            csvWriter.writeNext(outStrings);

            for (String key : writeMap.keySet()) {
                for (T object : writeMap.get(key)) {
                    for (int i = 0; i < fields.length; i++) {
                        outStrings[i] = (String) object.getClass().getDeclaredMethod(methodNames[i]).invoke(object);
                    }
                    csvWriter.writeNext(outStrings);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (csvWriter != null) csvWriter.close();
        }
    }


    public void writeCsv(Map<String, String> map, String path, String charsetName, String keyTitle, String valueTitle) throws IOException {

        if (!path.substring(path.length() - 4).equalsIgnoreCase(".csv"))
            path += ".csv";

        FileOutputStream outputStream = null;
        CSVWriter csvWriter = null;
        try {
            outputStream = new FileOutputStream(path);

            //UTF-8 BOM으로 인코딩 설정
            if (charsetName.equalsIgnoreCase("utf-8")) {
                outputStream.write(0xEF);
                outputStream.write(0xBB);
                outputStream.write(0xBF);
            }


            csvWriter = new CSVWriter(new OutputStreamWriter(outputStream));

            //제목 라인 input
            String[] outStrings = new String[2];
            outStrings[0] = keyTitle;
            outStrings[1] = valueTitle;
            csvWriter.writeNext(outStrings);

            for (String key : map.keySet()) {
                outStrings[0] = key;
                outStrings[1] = map.get(key);
                csvWriter.writeNext(outStrings);
            }
        } finally {
            if (csvWriter != null) csvWriter.close();
        }

    }

    private static String removeUTF8BOM(String str) {
        if (str.startsWith(UTF8_BOM))
            return str.substring(1);
        return str;
    }

}
