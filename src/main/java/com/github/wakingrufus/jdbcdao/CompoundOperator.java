package com.github.wakingrufus.jdbcdao;

/**
 * Class: com.github.wakingrufus.jdbcdao.ComparisonOperator
 *
 */
public enum CompoundOperator implements Operator {

    AND("AND"), OR("OR");
    private String string;

    private CompoundOperator(String string) {
        this.string = string;
    }

    @Override
    public String toSql() {
        return " " + string + " ";

    }

}
