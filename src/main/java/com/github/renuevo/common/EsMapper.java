package com.github.renuevo.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EsMapper {

    private ObjectMapper objectMapper;

    public EsMapper() {
        this.objectMapper = new ObjectMapper();
    }

    public EsMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public int getSearchCount(String response) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(response);
        jsonNode = jsonNode.get("hits");
        return jsonNode.get("total").asInt();
    }

    /**
     * <pre>
     *  @mathodName : getCount
     *  @author : Deokhwa.Kim
     *  @since : 2018-03-21 오후 1:49
     *  @description : _count query 대응 매핑
     *  @param response
     *  @return long
     * </pre>
     */
    public long getCount(String response) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(response);
        return Long.parseLong(jsonNode.get("count").toString());
    }

    /**
     * <pre>
     *  @mathodName : getSearch
     *  @author : Deokhwa.Kim
     *  @since : 2017-12-29 오전 11:17
     *  @param response
     *  @description : 검색 결과를 전체를 반환 _index, _version등을 다 포함
     *  @return java.util.List<T>
     * </pre>
     */
    public <T> List<T> getSearch(String response) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(response);
        jsonNode = jsonNode.get("hits");
        return objectMapper.readValue(jsonNode.get("hits").toString(), new TypeReference<List<T>>() {
        });
    }

    /**
     * <pre>
     *  @mathodName : getSearchSource
     *  @author : Deokhwa.Kim
     *  @since : 2017-12-29 오전 11:18
     *  @param response
     *  @description : 검색 결과의 source만 추출하여 반환
     *  @return java.util.List<T>
     * </pre>
     */
    public <T> List<T> getSearchSource(String response, Class<T> classType) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(response);
        jsonNode = jsonNode.get("hits");

        List<T> resultList = new ArrayList<>();

        T voObject;

        if (jsonNode.get("hits").isArray()) {
            for (JsonNode jsonHits : jsonNode.get("hits")) {
                voObject = objectMapper.readValue(jsonHits.get("_source").toString(), classType);
                resultList.add(voObject);
            }
        }

        return resultList;
    }

    public <T> List<T> getAggsBuckets(String response, String aggsName) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(response);
        jsonNode = jsonNode.get("aggregations");
        jsonNode = jsonNode.get(aggsName);
        return objectMapper.readValue(jsonNode.get("buckets").toString(), new TypeReference<List<T>>() {
        });
    }

    public <T> List<T> getAggsBuckets(String response, String aggsName, Class<T> classType) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(response);
        jsonNode = jsonNode.get("aggregations");
        jsonNode = jsonNode.get(aggsName);

        List<T> resultList = new ArrayList<>();
        T voObject;

        if (jsonNode.get("buckets").isArray()) {
            for (JsonNode jsonHits : jsonNode.get("buckets")) {
                voObject = objectMapper.readValue(jsonHits.toString(), classType);
                resultList.add(voObject);
            }
        }

        return resultList;
    }

    public <T> List<T> getAggsBuckets(String response) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(response);
        return getAggsBuckets(response, getFieldName(jsonNode.get("aggregations"), 0));   //처음 field 값으로 aggsregations_name 고정
    }

    /**
     * <pre>
     *  @mathodName : getFieldName
     *  @author : Deokhwa.Kim
     *  @since : 2017-12-19 오전 10:28
     *  @param jsonNode, index
     *  @description : index로 jsonNode의 field값을 가져옴
     *  @return java.lang.String
     * </pre>
     */
    private String getFieldName(JsonNode jsonNode, int index) {
        int idx = 0;
        String fieldName = "";
        Iterator<String> nameIter = jsonNode.fieldNames();

        while (nameIter.hasNext()) {
            fieldName = nameIter.next();
            if (index == idx)
                break;
            idx++;
        }

        return fieldName;
    }


}
