import com.github.renuevo.common.VoMapperUtils;
import org.junit.Test;
import vo.SampleVo;

import java.lang.reflect.Method;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class VoMapperTest {

    @Test
    public void defualtVoMapperTest() throws Exception {

        //VoMapper Set
        Map<String, Method> setSampleVoMap = VoMapperUtils.getFieldMehtods(SampleVo.class, "set");
        SampleVo sampleVo = new SampleVo();

        setSampleVoMap.get("key").invoke(sampleVo, "key");
        setSampleVoMap.get("value").invoke(sampleVo, "value");

        //VoMapper Get
        Map<String, Method> getSampleVoMap = VoMapperUtils.getFieldMehtods(SampleVo.class, "get");
        assertEquals("Vo Key Check", getSampleVoMap.get("key").invoke(sampleVo), "key");
        assertEquals("Vo Value Check", getSampleVoMap.get("value").invoke(sampleVo), "value");

    }

}
