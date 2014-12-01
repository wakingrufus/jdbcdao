package com.github.wakingrufus.jdbcdao;

import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * Class: com.github.wakingrufus.jdbcdao.TestClass2
 */
public class TestClass2 {

    @Column(name = "stringData")
    private String string2;
    @Id
    @Column(name = "class2Id")
    private long id;
    @Column(name = "decimalField")
    private BigDecimal bigDecimal;

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the string2
     */
    public String getString2() {
        return string2;
    }

    /**
     * @param string2 the string2 to set
     */
    public void setString2(String string2) {
        this.string2 = string2;
    }

    /**
     * @return the bigDecimal
     */
    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    /**
     * @param bigDecimal the bigDecimal to set
     */
    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

}
