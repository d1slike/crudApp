package ru.disdev.jdbchelper.ext;


import ru.disdev.jdbchelper.BeanCreator;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: erdinc
 * Date: Jul 23, 2009
 * Time: 12:58:52 PM
 */
public class DateBeanCreator implements BeanCreator<Date>
{
	private int index;

	public DateBeanCreator(int index)
	{
		this.index = index;
	}

	public DateBeanCreator()
	{
		index = 1;
	}

	public Date createBean(ResultSet rs) throws SQLException
	{
		return rs.getDate(index);
	}
}
