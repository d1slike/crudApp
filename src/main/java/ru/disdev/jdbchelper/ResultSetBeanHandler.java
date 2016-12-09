package ru.disdev.jdbchelper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Author: Erdinc YILMAZEL
 * Date: Jan 21, 2009
 * Time: 11:13:27 AM
 */
public abstract class ResultSetBeanHandler<T> extends ResultSetHandler
{
	BeanCreator<T> c;

	public ResultSetBeanHandler(BeanCreator<T> creator)
	{
		c = creator;
	}

	public ResultSetBeanHandler(int fetchSize, BeanCreator<T> c)
	{
		super(fetchSize);
		this.c = c;
	}

	public ResultSetBeanHandler(int fetchSize, int maxRows, BeanCreator<T> c)
	{
		super(fetchSize, maxRows);
		this.c = c;
	}

	public ResultSetBeanHandler(int fetchSize, int maxRows, int timeOut, BeanCreator<T> c)
	{
		super(fetchSize, maxRows, timeOut);
		this.c = c;
	}

	@Override
	public void processRow(ResultSet rs) throws SQLException
	{
		processBean(c.createBean(rs));
	}

	public abstract void processBean(T bean);
}
