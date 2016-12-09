package ru.disdev.jdbchelper.ext;


import ru.disdev.jdbchelper.BeanCreator;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: erdinc
 * Date: Jul 23, 2009
 * Time: 1:02:13 PM
 */
public class DoubleBeanCreator implements BeanCreator<Double>
{
	private int index;

	public DoubleBeanCreator(int index)
	{
		this.index = index;
	}

	public DoubleBeanCreator()
	{
		index = 1;
	}

	public Double createBean(ResultSet rs) throws SQLException
	{
		return rs.getDouble(index);
	}
}
