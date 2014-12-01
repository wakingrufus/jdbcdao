package com.github.wakingrufus.jdbcdao;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * Class: com.github.wakingrufus.jdbcdao.TestClass1
 *
 */
public class TestClass1 {

    @Id
    private int id;
    private String string1;
    private Date date1;
    private BigDecimal decimal1;
    private boolean boolean1;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the string1
     */
    public String getString1() {
        return string1;
    }

    /**
     * @param string1 the string1 to set
     */
    public void setString1(String string1) {
        this.string1 = string1;
    }

    /**
     * @return the date1
     */
    public Date getDate1() {
        return date1;
    }

    /**
     * @param date1 the date1 to set
     */
    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    /**
     * @return the decimal1
     */
    public BigDecimal getDecimal1() {
        return decimal1;
    }

    /**
     * @param decimal1 the decimal1 to set
     */
    public void setDecimal1(BigDecimal decimal1) {
        this.decimal1 = decimal1;
    }

    /**
     * @return the boolean1
     */
    public boolean isBoolean1() {
        return boolean1;
    }

    /**
     * @param boolean1 the boolean1 to set
     */
    public void setBoolean1(boolean boolean1) {
        this.boolean1 = boolean1;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TestClass1 other = (TestClass1) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.string1, other.string1)) {
            return false;
        }
        if (!Objects.equals(this.date1, other.date1)) {
            return false;
        }
        if (!Objects.equals(this.decimal1, other.decimal1)) {
            return false;
        }
        if (this.boolean1 != other.boolean1) {
            return false;
        }
        return true;
    }

}
