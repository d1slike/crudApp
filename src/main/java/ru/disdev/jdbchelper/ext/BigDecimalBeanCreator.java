package ru.disdev.jdbchelper.ext;


import ru.disdev.jdbchelper.BeanCreator;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: erdinc
 * Date: Jul 23, 2009
 * Time: 1:13:13 PM
 */
public class BigDecimalBeanCreator implements BeanCreator<BigDecimal>
{
	private int index;

	public BigDecimalBeanCreator(int index)
	{
		this.index = index;
	}

	public BigDecimalBeanCreator()
	{
		index = 1;
	}

	public BigDecimal createBean(ResultSet rs) throws SQLException
	{
		return rs.getBigDecimal(index);
	}
}
