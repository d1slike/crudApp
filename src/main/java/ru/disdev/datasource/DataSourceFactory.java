package ru.disdev.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.disdev.jdbchelper.JdbcHelper;

import javax.sql.DataSource;

public class DataSourceFactory {
    private static DataSourceFactory ourInstance = new DataSourceFactory();

    private static final String USER = "test";
    private static final String PASS = "jcnhjdcrjuj18";
    private static final String URL = "jdbc:mysql://185.5.54.139:3306/lab?characterEncoding=utf-8";

    public static DataSourceFactory getInstance() {
        return ourInstance;
    }

    private final DataSource dataSource;

    private DataSourceFactory() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASS);
        config.setMaximumPoolSize(2);
        dataSource = new HikariDataSource(config);
    }

    public JdbcHelper getHelper() {
        return new JdbcHelper(dataSource);
    }
}
