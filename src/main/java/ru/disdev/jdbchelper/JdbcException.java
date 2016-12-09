package ru.disdev.jdbchelper;

/**
 * Author: Erdinc YILMAZEL
 * Date: Jan 30, 2009
 * Time: 4:19:01 PM
 */
public class JdbcException extends RuntimeException
{
	public JdbcException()
	{
	}

	public JdbcException(String message)
	{
		super(message);
	}

	public JdbcException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public JdbcException(Throwable cause)
	{
		super(cause);
	}
}
