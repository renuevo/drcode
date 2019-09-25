import com.github.renuevo.es.EsMapper;
import com.github.renuevo.es.EsQueryBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vo.KeyowrdVo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;


public class EsTest {

    private final Logger log = LoggerFactory.getLogger(EsTest.class);

    @Test
    public void esMapperTest() {
        EsMapper esMapper = new EsMapper();
        List<KeyowrdVo> list = null;
        List<String> responseList;
        StringBuilder response = new StringBuilder();
        int count = 0;

        try {

            Stream<String> stream = Files.lines(Paths.get(getClass().getResource("/elastic_response.json").toURI()));
            responseList = stream.collect(Collectors.toList());

            for (String line : responseList) {
                response.append(line);
            }

            list = esMapper.getSearchSource(response.toString(), KeyowrdVo.class);
            count = esMapper.getSearchCount(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        List<KeyowrdVo> keyowrdVoList = new ArrayList<>();
        keyowrdVoList.add(new KeyowrdVo("java", "java title"));
        keyowrdVoList.add(new KeyowrdVo("c#", "c# title"));

        assert list != null;
        for (int i = 0; i < keyowrdVoList.size(); i++) {
            assertEquals("Is EsMapper Keyowrd Test", list.get(i).getKeyword(), keyowrdVoList.get(i).getKeyword());
            assertEquals("Is EsMapper Title Test", list.get(i).getTitle(), keyowrdVoList.get(i).getTitle());
        }

        assertEquals("Is EsMapper Count Test", list.size(), count);
    }

    @Test
    public void esQueryBuilderTest() throws IOException {

        String query = EsQueryBuilder.getQuery(EsQueryBuilder.getQueryTemplate(new File(getClass().getResource("/elastic_sample_search_query.json").getFile())), "test", 2019, 10);
        String result = "{  \"query\": {" +
                                "\"bool\": {" +
                                        "\"must\": [" +
                                                "{ \"term\": { " +
                                                    "\"search\": {\"value\": \"test\"}" +
                                                        "} }, " +
                                                "{ \"range\": { " +
                                                    "\"date\": {  \"gte\": 2019, \"lte\": 10, \"format\": \"yyyy-mm-dd\"}          " +
                                                   "}} " +
                                                "]}" +
                         "}}";

        query = query.replaceAll("\\p{Z}","");
        result = result.replaceAll("\\p{Z}","");

        assertEquals("Query Create Check", query, result);
    }

}
