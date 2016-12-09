package ru.disdev.jdbchelper;

import java.sql.SQLException;

/**
 * Author: Erdinc Yilmazel
 * Since: 10/27/11
 */
public interface ExceptionLogger
{
	void log(SQLException ex);

	void log(SQLException ex, String sql);
}
