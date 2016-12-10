package ru.disdev.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.disdev.jdbchelper.JdbcHelper;
import ru.disdev.utils.IOUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class DataSourceFactory {
    private static DataSourceFactory ourInstance = new DataSourceFactory();

    public static DataSourceFactory getInstance() {
        return ourInstance;
    }

    private final DataSource dataSource;

    private DataSourceFactory() {
        Properties properties = new Properties();
        try (Reader reader = IOUtils.getResourceAsReader("/props.properties")) {
            properties.load(reader);
        } catch (IOException ignored) {

        }
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(properties.getProperty("url"));
        config.setUsername(properties.getProperty("user"));
        config.setPassword(properties.getProperty("pass"));
        config.setMaximumPoolSize(2);
        dataSource = new HikariDataSource(config);
    }

    public JdbcHelper getHelper() {
        return new JdbcHelper(dataSource);
    }
}
