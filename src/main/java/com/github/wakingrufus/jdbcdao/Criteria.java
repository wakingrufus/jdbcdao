package com.github.wakingrufus.jdbcdao;

import java.util.List;

/**
 * Class: com.github.wakingrufus.jdbcdao.Criteria
 *
 */
public interface Criteria {

    public String toSql();

    /**
     * @return the values
     */
    public List<Object> getValues();

}
