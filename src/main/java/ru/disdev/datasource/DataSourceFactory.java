package ru.disdev.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

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

    public JdbcTemplate getHelper() {
        return new JdbcTemplate(dataSource);
    }
}
