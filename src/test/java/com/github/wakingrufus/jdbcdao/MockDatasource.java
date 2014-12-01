package com.github.wakingrufus.jdbcdao;

import com.googlecode.flyway.core.Flyway;
import org.apache.derby.jdbc.EmbeddedDataSource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Class: com.github.wakingrufus.jdbcdao.MockDatasource
 *
 */
public class MockDatasource {

    public static DataSource datasource;

    public static DataSource getInstance() {
        if (datasource == null) {
            EmbeddedDataSource derbyDataSource = new EmbeddedDataSource();
            derbyDataSource.setDatabaseName("memory:myDB");
            derbyDataSource.setCreateDatabase("create");
            Flyway flyway = new Flyway();
            flyway.setDataSource(derbyDataSource);
            flyway.setInitOnMigrate(true);
            List<String> defaultLocations = new ArrayList<>();
            flyway.migrate();
            datasource = derbyDataSource;
        }
        return datasource;
    }
}
