package ru.disdev.jdbchelper.ext;


import ru.disdev.jdbchelper.BeanCreator;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: erdinc
 * Date: Jul 23, 2009
 * Time: 1:12:32 PM
 */
public class LongBeanCreator implements BeanCreator<Long>
{
	private int index;

	public LongBeanCreator(int index)
	{
		this.index = index;
	}

	public LongBeanCreator()
	{
		index = 1;
	}

	public Long createBean(ResultSet rs) throws SQLException
	{
		return rs.getLong(index);
	}
}
