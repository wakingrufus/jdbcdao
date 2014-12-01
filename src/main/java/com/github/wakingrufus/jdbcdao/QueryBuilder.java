package com.github.wakingrufus.jdbcdao;

import java.util.Iterator;
import java.util.List;

/**
 * Class: com.github.wakingrufus.jdbcdao.QueryBuilder
 *
 */
public class QueryBuilder {

    private final StringBuilder sb = new StringBuilder();

    public QueryBuilder select(List<String> fields) {
        sb.append("SELECT ");
        for (Iterator<String> iterator = fields.iterator(); iterator.hasNext();) {
            String fieldName = iterator.next();
            sb.append(fieldName);
            if (iterator.hasNext()) {
                sb.append(",");
            }
        }
        sb.append(" ");
        return this;
    }

    public QueryBuilder from(String tableName) {
        sb.append(" FROM ");
        sb.append(tableName);
        return this;
    }

    public QueryBuilder where(Criteria criteria1) {
        sb.append(" WHERE ");
        sb.append(criteria1.toSql());
        return this;
    }

    public String build() {
        return sb.toString();
    }
}
