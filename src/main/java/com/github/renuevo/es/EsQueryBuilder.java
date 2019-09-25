package com.github.renuevo.es;

import java.io.*;
import java.util.List;

/**
 * <pre>
 * @className : EsQueryBuilder
 * @author : Deokhwa.Kim
 * @since : 2019-09-25 (refactoring)
 * </pre>
 */
public class EsQueryBuilder {

    public static String getQuery(String query, Object... variables) {
        String[] queryList = queryTokenizer(query);
        return queryCombination(queryList, variables);
    }

    public static String getQueryArray(String query, Object[] variables) {
        String[] queryList = queryTokenizer(query);
        StringBuilder queryBuilder = new StringBuilder();
        String[] param = (String[]) variables;
        return queryCombination(queryList, param);
    }

    public static String getQueryArrayList(String query, List<Object> variables) {

        String[] queryList = queryTokenizer(query);
        StringBuilder queryBuilder = new StringBuilder();

        int index = 0;

        for (String str : queryList) {
            queryBuilder.append(str);
            if (index < variables.size()) {
                queryBuilder.append(variables.get(index));
                index++;
            }
        }

        return queryBuilder.toString();
    }

    private static String[] queryTokenizer(String query) {
        query = query.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "");
        return query.split("\"\\$[A-z]*\"");
    }

    private static String queryCombination(String[] queryList, Object[] variables) {

        StringBuilder queryBuilder = new StringBuilder();
        int index = 0;

        for (String str : queryList) {
            queryBuilder.append(str);
            if (index < variables.length) {

                if (variables[index] instanceof String)
                    variables[index] = "\"" + variables[index] + "\"";

                queryBuilder.append(variables[index]);
                index++;
            }
        }
        return queryBuilder.toString();
    }

    public static String getQueryTemplate(File qyFile) throws IOException {

        String str;
        StringBuilder qyTemplate = new StringBuilder();
        try (BufferedReader queryBr = new BufferedReader(new FileReader(qyFile))) {
            while ((str = queryBr.readLine()) != null) {
                qyTemplate.append(str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return qyTemplate.toString();
    }

}
