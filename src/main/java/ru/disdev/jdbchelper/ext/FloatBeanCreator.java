package ru.disdev.jdbchelper.ext;


import ru.disdev.jdbchelper.BeanCreator;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: erdinc
 * Date: Jul 23, 2009
 * Time: 1:02:57 PM
 */
public class FloatBeanCreator implements BeanCreator<Float>
{
	private int index;

	public FloatBeanCreator(int index)
	{
		this.index = index;
	}

	public FloatBeanCreator()
	{
		index = 1;
	}

	public Float createBean(ResultSet rs) throws SQLException
	{
		return rs.getFloat(index);
	}
}
