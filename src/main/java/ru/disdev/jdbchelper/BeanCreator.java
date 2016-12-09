package ru.disdev.jdbchelper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * To create a java bean from an sql result set, users of JdbcHelper can define
 * BeanCreators. BeanCreators are reusable piece of code that may be used to map result
 * sets to actual java objects.
 *
 * @param <T>
 * 		Type of the bean that will be created
 */
@FunctionalInterface
public interface BeanCreator<T>
{
	/**
	 * This method will be called for every item in the result set and should read
	 * the column values from the resultset and set the properties on the newly created
	 * object.
	 *
	 * This method normally should not call the next() method on the resultset object
	 *
	 * @param rs
	 * 		The resultset
	 *
	 * @return Returns the newly created bean
	 *
	 * @throws SQLException
	 * 		May be thrown by the underlying Jdbc driver
	 * @see org.mmocore.commons.jdbchelper.JdbcHelper#queryForObject(String, org.mmocore.commons.jdbchelper.BeanCreator, Object...)
	 */
	T createBean(ResultSet rs) throws SQLException;
}
