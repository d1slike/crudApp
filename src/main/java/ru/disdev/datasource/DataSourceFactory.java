package ru.disdev.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.disdev.jdbchelper.JdbcHelper;

import javax.sql.DataSource;

public class DataSourceFactory {
    private static DataSourceFactory ourInstance = new DataSourceFactory();

    public static DataSourceFactory getInstance() {
        return ourInstance;
    }

    private final DataSource dataSource;

    private DataSourceFactory() {
        HikariConfig config = new HikariConfig("/ds.properties");
        dataSource = new HikariDataSource(config);
    }

    public JdbcHelper getHelper() {
        return new JdbcHelper(dataSource);
    }
}
