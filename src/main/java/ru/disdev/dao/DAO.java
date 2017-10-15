package ru.disdev.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.disdev.datasource.DataSourceFactory;
import ru.disdev.entity.Crud;

import java.util.List;

public abstract class DAO<T extends Crud> {
    protected static final JdbcTemplate helper = DataSourceFactory.getInstance().getHelper();

    public abstract List<T> load();

    public abstract T save(T crud);

    public abstract boolean delete(String id);
}
