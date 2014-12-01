package com.github.wakingrufus.jdbcdao;

import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.sql.DataSource;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class: com.github.wakingrufus.jdbcdao.JdbcDao
 * This class will give you CRUD operations on any POJO. the following annotations are supported:
 * javax.persistence.Table, javax.persistence.Id (required), javax.persistence.Column.
 * the Id field must be a long, Long, Integer, or int
 *
 * @param <T>
 * @param <PK>
 */
public class JdbcDao<T, PK extends Serializable> {

    protected DataSource dataSource;
    protected JdbcTemplate jdbcTemplate;
    private final Class<T> classObject;
    protected final RowMapper<T> rowMapper;
    protected Field keyField;
    protected String keyName;
    protected List<String> columnNames = new ArrayList<>();
    protected List<String> columnNamesWithIdLast = new ArrayList<>();
    protected List<String> columnNamesWithoutId = new ArrayList<>();
    protected String tableName;

    public JdbcDao(DataSource dataSource, Class<T> classObj) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.classObject = classObj;
        tableName = classObject.getSimpleName();
        if (classObject.isAnnotationPresent(Table.class)) {
            tableName = (classObject.getAnnotation(Table.class).name());
        }
        for (Field field : classObject.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                keyField = field;
                keyName = convertFieldToColumnName(field);
            }
        }
        for (Field field : findNonTransientFields(classObject)) {
            columnNames.add(convertFieldToColumnName(field));
        }
        columnNamesWithIdLast = new ArrayList<>(columnNames);
        columnNamesWithIdLast.remove(getKeyName());
        columnNamesWithIdLast.add(getKeyName());
        columnNamesWithoutId = new ArrayList<>(columnNames);
        columnNamesWithoutId.remove(getKeyName());
        rowMapper = new RowMapper<T>() {

            @Override
            @SuppressWarnings({ "unchecked", "rawtypes" })
            public T mapRow(ResultSet rs, int i) throws SQLException {
                T obj = null;
                try {
                    obj = classObject.newInstance();
                    for (Field field : findNonTransientFields(classObject)) {
                        try {
                            field.setAccessible(true);
                            if (field.getType().isEnum()) {
                                if (rs.getString(convertFieldToColumnName(field)) != null) {
                                    field.set(obj, Enum.valueOf((Class<Enum>) field.getType(),
                                            rs.getString(convertFieldToColumnName(field))));
                                }
                            } else {
                                field.set(obj, rs.getObject(convertFieldToColumnName(field)));
                            }
                        } catch (IllegalAccessException ex) {
                            Logger.getLogger(JdbcDao.class.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            field.setAccessible(false);
                        }
                    }
                } catch (InstantiationException | IllegalAccessException ex) {
                    Logger.getLogger(JdbcDao.class.getName()).log(Level.SEVERE, null, ex);
                }
                return obj;
            }
        };
    }

    private List<Field> findNonTransientFields(Class<T> classObject) {
        List<Field> fields = new ArrayList<>();
        for (Field field : classObject.getDeclaredFields()) {
            // jacoco adds synthetic fields
            if (!field.isSynthetic()) {
                if (!field.isAnnotationPresent(Transient.class)) {
                    fields.add(field);
                }
            }
        }
        return fields;
    }

    public T findOne(PK id) {
        QueryBuilder qb = new QueryBuilder();
        qb.select(columnNames).from(tableName).where(new ComparisonCriteria(keyField, ComparisonOperator.EQUALS, id));
        T result = null;
        List<T> list = jdbcTemplate.query(qb.build(), new Object[]{id}, rowMapper);
        if (list.size() > 0) {
            result = list.get(0);
        }
        return result;
    }

    public <S extends T> S update(S s) {
        jdbcTemplate.update(generateUpdateQuery(), getValues(s, columnNamesWithIdLast));
        return s;
    }

    private PreparedStatementCreatorFactory generateCreatePreparedStatementCreator() {
        PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(generateCreateQuery());
        factory.setReturnGeneratedKeys(true);
        for (String fieldName : columnNamesWithoutId) {
            factory.addParameter(new SqlParameter(fieldName, SqlTypeValue.TYPE_UNKNOWN));
        }
        return factory;
    }

    public <S extends T> S create(S s) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(generateCreatePreparedStatementCreator()
                .newPreparedStatementCreator(getValues(s, columnNamesWithoutId)),
                keyHolder);
        try {
            keyField.setAccessible(true);
            if (keyField.getType() == Long.TYPE) {
                keyField.setLong(s, keyHolder.getKey().longValue());
            } else if (keyField.getType() == Integer.TYPE) {
                keyField.setInt(s, keyHolder.getKey().intValue());
            } else if (keyField.getType() == Long.class) {
                keyField.set(s, (keyHolder.getKey().longValue()));
            } else if (keyField.getType() == Integer.class) {
                keyField.set(s, (keyHolder.getKey().intValue()));
            } else {
                throw new RuntimeException("invalid key type: " + keyField.getType().toString());
            }
            keyField.setAccessible(false);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(JdbcDao.class.getName()).log(Level.SEVERE, null, ex);
        }


        return s;
    }

    public List<T> findAll() {
        QueryBuilder qb = new QueryBuilder();
        qb.select(columnNames).from(tableName);
        return jdbcTemplate.query(qb.build(), rowMapper);
    }

    protected final String convertFieldToColumnName(Field field) {
        String columnName = field.getName();
        if (field.isAnnotationPresent(Column.class)) {
            columnName = (field.getAnnotation(Column.class).name());
        }
        return columnName;
    }

    protected List<T> findWhereCriteria(Criteria criteria) {
        QueryBuilder qb = new QueryBuilder();
        qb.select(columnNames).from(tableName).where(criteria);
        return jdbcTemplate.query(qb.build(), criteria.getValues().toArray(), rowMapper);
    }

    public void delete(T t) {
        String deleteQuery = "DELETE FROM " + tableName + " WHERE " + keyName + "=?";
        Object keyValue = null;
        try {
            keyField.setAccessible(true);
            keyValue = keyField.get(t);
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } finally {
            keyField.setAccessible(false);
        }
        jdbcTemplate.update(deleteQuery, keyValue);
    }

    private Map<String, Object> mapColumns(T t) {
        Map<String, Object> valuesMap = new HashMap<>();
        for (Field field : findNonTransientFields(classObject)) {
            field.setAccessible(true);
            try {
                Object value = field.get(t);
                if (field.getType().isEnum() && field.get(t) != null) {
                    value = field.get(t).toString();
                }
                valuesMap.put(convertFieldToColumnName(field), value);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(JdbcDao.class.getName()).log(Level.SEVERE, null, ex);
            }
            field.setAccessible(false);
        }
        return valuesMap;
    }

    private Object[] getValues(T instance, List<String> columnNames) {
        List<Object> columnValues = new ArrayList<>();
        Map<String, Object> mappedData = mapColumns(instance);
        for (String field : columnNames) {
            columnValues.add(mappedData.get(field));
        }
        return columnValues.toArray();
    }

    private String getKeyName() {
        return keyName;
    }

    private String generateUpdateQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ").append(tableName).append(" SET ");
        for (Iterator<String> field = columnNamesWithoutId.iterator(); field.hasNext(); ) {
            String fieldString = field.next();
            sb.append(fieldString).append("=?");
            if (field.hasNext()) {
                sb.append(",");
            }
        }
        sb.append(" WHERE ").append(getKeyName()).append("=?");
        return sb.toString();
    }

    private String generateCreateQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(tableName).append(" (");
        for (Iterator<String> field = columnNamesWithoutId.iterator(); field.hasNext(); ) {
            String fieldString = field.next();
            if (!fieldString.equals(getKeyName())) {
                sb.append(fieldString);
                if (field.hasNext()) {
                    sb.append(",");
                }
            }
        }
        sb.append(") VALUES (");
        for (Iterator<String> field = columnNamesWithoutId.iterator(); field.hasNext(); ) {
            String fieldString = field.next();
            if (!fieldString.equals(getKeyName())) {
                sb.append("?");
                if (field.hasNext()) {
                    sb.append(",");
                }
            }
        }
        sb.append(")");
        return sb.toString();
    }

    private static final Logger LOG = Logger.getLogger(JdbcDao.class.getName());
}
