import au.com.bytecode.opencsv.CSVReader;
import com.github.renuevo.common.VoMapperUtils;
import com.github.renuevo.csv.CsvUtils;
import org.junit.Test;
import vo.CsvVo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * <pre>
 * @className : CsvUtilsTest
 * @author : Deokhwa.Kim
 * @since : 2019-09-23
 * </pre>
 */
public class CsvUtilsTest {

    final private CsvUtils csvUtils = new CsvUtils();
    final private List<CsvVo> csvVoList = Arrays.asList(new CsvVo("test1", "10"), new CsvVo("test2", "20"), new CsvVo("test3", "30"));

    @Test
    public void csvListModelWriteTest() throws IOException {

        //delete sample file
        File csvFile = new File(getClass().getResource("/csv_sample_write.csv").getFile());
        String path = csvFile.getAbsolutePath();

        if (csvFile.exists()) csvFile.delete();

        assertFalse("Sample File Delete", csvFile.exists());

        //csv file output
        csvUtils.writeCsv(csvVoList, path, "utf-8", CsvVo.class);

        //check sample file
        csvFile = new File(getClass().getResource("/csv_sample_write.csv").getFile());
        assertTrue("File Create : ", csvFile.exists());

        if (csvFile.exists()) {
            List<CsvVo> csvVoReadList = readCsvFile(csvFile.getAbsolutePath());
            assertTrue("Csv List Model Write Value Check", csvValueCheck(csvVoReadList));
        }

    }

    @Test
    public void csvMapModelWriteTest() throws IOException {

        //delete sample file
        File csvFile = new File(getClass().getResource("/csv_sample_write.csv").getFile());
        String path = csvFile.getAbsolutePath();

        if (csvFile.exists()) csvFile.delete();

        assertFalse("Sample File Delete", csvFile.exists());

        Map<String, List<CsvVo>> csvVoMap = new HashMap<>();

        for (CsvVo csvVo : csvVoList) {
            csvVoMap.put(csvVo.getKey(), Collections.singletonList(csvVo));
        }

        //csv file output
        csvUtils.writeCsv(csvVoMap, path, "utf-8", CsvVo.class);

        //check sample file
        csvFile = new File(getClass().getResource("/csv_sample_write.csv").getFile());
        assertTrue("File Create : ", csvFile.exists());

        if (csvFile.exists()) {
            List<CsvVo> csvVoReadList = readCsvFile(csvFile.getAbsolutePath());
            assertTrue("Csv Model Write Value Check", csvValueCheck(csvVoReadList));
        }
    }

    @Test
    public void defaultWriteTest() throws IOException {

        //delete sample file
        File csvFile = new File(getClass().getResource("/csv_sample_write.csv").getFile());
        String path = csvFile.getAbsolutePath();

        if (csvFile.exists()) csvFile.delete();

        assertFalse("Sample File Delete", csvFile.exists());

        //create save list
        List<String[]> saveList = new ArrayList<>();

        String[] header = {"key", "value"};
        saveList.add(header);

        for (CsvVo csvVo : csvVoList) {
            String[] value = {csvVo.getKey(), csvVo.getValue()};
            saveList.add(value);
        }

        csvUtils.writeCsv(saveList, path, "utf-8");

        csvFile = new File(getClass().getResource("/csv_sample_write.csv").getFile());
        assertTrue("File Create : ", csvFile.exists());

        if (csvFile.exists()) {
            List<CsvVo> csvVoReadList = readCsvFile(csvFile.getAbsolutePath());
            assertTrue("Csv Default Write Value Check", csvValueCheck(csvVoReadList));
        }

    }

    @Test
    public void csvModelReadTest() throws IOException {
        List<CsvVo> csvVoReadList = csvUtils.readModelCsv(getClass().getResource("/csv_sample.csv").getPath(), "utf-8", CsvVo.class);
        assertTrue("Csv Model Read Value Check", csvValueCheck(csvVoReadList));

    }

    @Test
    public void defaultReadTest() throws IOException {
        List<String[]> csvVoReadList = csvUtils.readCsv(getClass().getResource("/csv_sample.csv").getPath(), "utf-8", 1);
        for (int index = 0; index < csvVoList.size(); index++) {
            assertEquals("Csv Key Equals Check", csvVoList.get(index).getKey(), csvVoReadList.get(index)[0]);
            assertEquals("Csv Value Equals Check", csvVoList.get(index).getValue(), csvVoReadList.get(index)[1]);
        }
    }

    /**
     * <pre>
     *  @methodName : readCsvFile
     *  @author : Deokhwa.Kim
     *  @since : 2019-09-25 AM 10:56
     *  @summary : commonCsvReadMethod
     *  @param : [filePath]
     *  @return : java.util.List<vo.CsvVo>
     * </pre>
     */
    private List<CsvVo> readCsvFile(String filePath) throws IOException {

        CSVReader csvReader = null;
        List<CsvVo> csvVoReadList = new ArrayList<>();

        Map<String, Method> setMethodMap = VoMapperUtils.getFieldMehtods(CsvVo.class, "set");

        try {
            csvReader = new CSVReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8), ',', '\"', 0);

            String[] csvLine;
            String[] csvTitle;

            CsvVo csvVo;
            csvTitle = csvReader.readNext();

            while ((csvLine = csvReader.readNext()) != null) {
                csvVo = new CsvVo();
                for (int i = 0; i < csvTitle.length; i++) {
                    if (setMethodMap.containsKey(csvTitle[i]))
                        setMethodMap.get(csvTitle[i]).invoke(csvVo, csvLine[i]);
                }
                csvVoReadList.add(csvVo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (csvReader != null) csvReader.close();
        }
        return csvVoReadList;
    }

    /**
     * <pre>
     *  @methodName : csvValueCheck
     *  @author : Deokhwa.Kim
     *  @since : 2019-09-25 AM 10:49
     *  @summary : csvValueCheck
     *  @param : [readCsvVoList]
     *  @return : boolean
     * </pre>
     */
    private boolean csvValueCheck(List<CsvVo> readCsvVoList) {
        boolean resultBool = false;

        if (readCsvVoList.size() == csvVoList.size()) {
            for (int i = 0; i < readCsvVoList.size(); i++) {
                if (!readCsvVoList.get(i).equals(csvVoList.get(i))) {
                    resultBool = true;
                    break;
                }
            }
        }
        return resultBool;
    }

}
