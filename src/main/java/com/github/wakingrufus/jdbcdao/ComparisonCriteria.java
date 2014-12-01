package com.github.wakingrufus.jdbcdao;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Class: com.github.wakingrufus.jdbcdao.ComparisonCriteria
 *
 */
public class ComparisonCriteria implements Criteria {

    private Field field;
    private ComparisonOperator operator;
    private Object value;

    public ComparisonCriteria(Field field, ComparisonOperator operator, Object value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public String toSql() {
        String columnName = field.getName();
        if (field.isAnnotationPresent(Column.class)) {
            columnName = (field.getAnnotation(Column.class).name());
        }
        return " (" + columnName + " " + operator.toSql() + " ?) ";
    }

    @Override
    public List<Object> getValues() {
        List<Object> values = new ArrayList<>();
        values.add(value);
        return values;
    }

}
