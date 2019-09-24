import com.github.renuevo.csv.CsvUtils;
import org.junit.Test;
import vo.CsvVo;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class CsvUtilsTest {

    final private CsvUtils csvUtils = new CsvUtils();
    final private List<CsvVo> csvVoList = Arrays.asList(new CsvVo("test1", "10"), new CsvVo("test2", "20"), new CsvVo("test3", "30"));


    @Test
    public void csvListModelWriteTest() throws IOException {

        //delete sample file
        File csvFile = new File(getClass().getResource("/csv_sample.csv").getFile());
        String path = csvFile.getAbsolutePath();

        if (csvFile.exists()) csvFile.delete();

        assertFalse("Sample File Delete : ", csvFile.exists());

        //csv file output
        csvUtils.writeCsv(csvVoList, path, "utf-8", CsvVo.class);

        //check sample file
        csvFile = new File(getClass().getResource("/csv_sample.csv").getFile());
        assertTrue("File Create : ", csvFile.exists());
    }

    @Test
    public void csvMapModelWriteTest() throws IOException {

        //delete sample file
        File csvFile = new File(getClass().getResource("/csv_sample_map.csv").getFile());
        String path = csvFile.getAbsolutePath();

        if (csvFile.exists()) csvFile.delete();

        assertFalse("Sample File Delete : ", csvFile.exists());

        Map<String, List<CsvVo>> csvVoMap = new HashMap<>();

        for (CsvVo csvVo : csvVoList) {
            csvVoMap.put(csvVo.getKey(), Collections.singletonList(csvVo));
        }

        //csv file output
        csvUtils.writeCsv(csvVoMap, path, "utf-8", CsvVo.class);

        //check sample file
        csvFile = new File(getClass().getResource("/csv_sample_map.csv").getFile());
        assertTrue("File Create : ", csvFile.exists());
    }


    @Test
    public void csvModelReadTest() throws IOException {

        //sample file init
        csvListModelWriteTest();

        List<CsvVo> csvVoReadList = csvUtils.readModelCsv(getClass().getResource("/csv_sample.csv").getPath(), "utf-8", CsvVo.class);

        for (int index = 0; index < csvVoList.size(); index++) {
            assertEquals("CsvVo Equals Check", csvVoList.get(index), csvVoReadList.get(index));
        }

    }


}
