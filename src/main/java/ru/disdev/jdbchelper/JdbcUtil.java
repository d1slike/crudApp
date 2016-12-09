package ru.disdev.jdbchelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Author: Erdinc YILMAZEL
 * Date: Dec 25, 2008
 * Time: 12:29:19 AM
 */
public class JdbcUtil
{
	public static void close(Statement stmt, ResultSet rs)
	{
		close(stmt);
		close(rs);
	}

	/**
	 * Close the given JDBC Connection and ignore any thrown exception.
	 * This is useful for typical finally blocks in manual JDBC code.
	 *
	 * @param con
	 * 		the JDBC Connection to close (may be <code>null</code>)
	 */
	public static void close(Connection con)
	{
		if(con != null)
		{
			try
			{
				con.close();
			}
			catch(SQLException ex)
			{
				//
			}
			catch(Throwable ex)
			{
				//
			}
		}
	}

	/**
	 * Close the given JDBC Statement and ignore any thrown exception.
	 * This is useful for typical finally blocks in manual JDBC code.
	 *
	 * @param stmt
	 * 		the JDBC Statement to close (may be <code>null</code>)
	 */
	public static void close(Statement stmt)
	{
		if(stmt != null)
		{
			try
			{
				stmt.close();
			}
			catch(SQLException ex)
			{
				//
			}
			catch(Throwable ex)
			{
				//
			}
		}
	}

	/**
	 * Close the given JDBC ResultSet and ignore any thrown exception.
	 * This is useful for typical finally blocks in manual JDBC code.
	 *
	 * @param rs
	 * 		the JDBC ResultSet to close (may be <code>null</code>)
	 */
	public static void close(ResultSet rs)
	{
		if(rs != null)
		{
			try
			{
				rs.close();
			}
			catch(SQLException ex)
			{
				//
			}
			catch(Throwable ex)
			{
				//
			}
		}
	}
}
