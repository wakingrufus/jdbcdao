package com.github.wakingrufus.jdbcdao;

/**
 * Class: com.github.wakingrufus.jdbcdao.ComparisonOperator
 *
 */
public enum ComparisonOperator implements Operator {

    EQUALS("="), GREATER_THAN(">"), LESS_THAN("<"), NOT_EQUALS("!=");
    private String string;

    private ComparisonOperator(String string) {
        this.string = string;
    }

    @Override
    public String toSql() {
        return " " + string + " ";

    }

}
