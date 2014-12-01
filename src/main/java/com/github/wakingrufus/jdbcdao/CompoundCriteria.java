package com.github.wakingrufus.jdbcdao;

import java.util.ArrayList;
import java.util.List;

/**
 * Class: com.github.wakingrufus.jdbcdao.ComparisonCriteria
 *
 */
public class CompoundCriteria implements Criteria {

    private Criteria criteria1;
    private CompoundOperator operator;
    private Criteria criteria2;

    public CompoundCriteria(Criteria criteria1, CompoundOperator operator, Criteria criteria2) {
        this.criteria1 = criteria1;
        this.operator = operator;
        this.criteria2 = criteria2;
    }

    @Override
    public String toSql() {
        String compiledSql = " (" + criteria1.toSql() + " " + operator.toSql() + " " + criteria2.toSql() + ") ";
        getValues().addAll(criteria1.getValues());
        getValues().addAll(criteria2.getValues());
        return compiledSql;
    }

    @Override
    public List<Object> getValues() {
        List<Object> values = new ArrayList<>();
        values.addAll(criteria1.getValues());
        values.addAll(criteria2.getValues());
        return values;
    }

}
