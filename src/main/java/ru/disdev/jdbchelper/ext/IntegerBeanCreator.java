package ru.disdev.jdbchelper.ext;


import ru.disdev.jdbchelper.BeanCreator;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: erdinc
 * Date: Jul 23, 2009
 * Time: 12:51:40 PM
 */
public class IntegerBeanCreator implements BeanCreator<Integer>
{
	private int index;

	public IntegerBeanCreator()
	{
		index = 1;
	}

	public IntegerBeanCreator(int index)
	{
		this.index = index;
	}

	public Integer createBean(ResultSet rs) throws SQLException
	{
		return rs.getInt(index);
	}
}
