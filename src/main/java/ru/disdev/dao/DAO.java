package ru.disdev.dao;

import ru.disdev.datasource.DataSourceFactory;
import ru.disdev.entity.Crud;
import ru.disdev.jdbchelper.JdbcHelper;

import java.util.List;

public abstract class DAO<T extends Crud> {
    protected static final JdbcHelper helper = DataSourceFactory.getInstance().getHelper();

    public abstract List<T> load();

    public abstract T save(T crud);

    public abstract boolean delete(String id);
}
