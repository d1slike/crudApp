package ru.disdev.jdbchelper;

import java.math.BigDecimal;
import java.sql.*;

/**
 * Author: Erdinc YILMAZEL
 * Date: Jan 30, 2009
 * Time: 4:15:47 PM
 */
public class QueryResult implements AutoCloseable
{
	private Connection con;
	private Statement stmt;
	private ResultSet result;
	private JdbcHelper jdbc;
	private Object[] params;
	private String sql;
	private boolean closed;
	private boolean queried;
	private boolean autoClose;

	QueryResult(final JdbcHelper jdbc, final String sql, final Object[] params)
	{
		this.jdbc = jdbc;
		this.params = params;
		this.sql = sql;

		try
		{
			con = jdbc.getConnection();

			if(params.length == 0)
			{
				stmt = con.createStatement();
			}
			else
			{
				stmt = jdbc.fillStatement(con.prepareStatement(sql), params);
			}
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e, sql);
			}
			throw new JdbcException("Error running query:\n" + sql + "\n\nError: " + e.getMessage(), e);
		}
	}

	public void setAutoClose(final boolean a)
	{
		autoClose = a;
	}

	public void setFetchSize(final int rows)
	{
		try
		{
			stmt.setFetchSize(rows);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public void setMaxRows(final int rows)
	{
		try
		{
			stmt.setMaxRows(rows);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public void setTimeOut(final int t)
	{
		try
		{
			stmt.setQueryTimeout(t);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	private void query()
	{
		try
		{
			if(params.length == 0)
			{
				result = stmt.executeQuery(sql);
			}
			else
			{
				result = ((PreparedStatement) stmt).executeQuery();
			}

			queried = true;

		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException("Error running query:\n" + sql + "\n\nError: " + e.getMessage(), e);
		}
	}

	public boolean next()
	{
		try
		{
			if(!queried)
			{
				query();
			}
			final boolean next = result.next();
			if(!next && autoClose)
			{
				close();
			}

			return next;
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	@Override
	public void close()
	{
		if(!closed)
		{
			JdbcUtil.close(stmt, result);
			jdbc.freeConnection(con);
			closed = true;
		}
	}

	public boolean isBeforeFirst()
	{
		try
		{
			return result.isBeforeFirst();
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public boolean isAfterLast()
	{
		try
		{
			return result.isAfterLast();
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public boolean isFirst()
	{
		try
		{
			return result.isFirst();
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public boolean isLast()
	{
		try
		{
			return result.isLast();
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public void beforeFirst()
	{
		try
		{
			result.beforeFirst();
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public void afterLast()
	{
		try
		{
			result.afterLast();
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public boolean first()
	{
		try
		{
			return result.first();
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public boolean last()
	{
		try
		{
			return result.last();
		}
		catch(final SQLException e)
		{
			throw new JdbcException(e);
		}
	}

	public int getRow()
	{
		try
		{
			return result.getRow();
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public boolean absolute(final int row)
	{
		try
		{
			return result.absolute(row);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public boolean relative(final int rows)
	{
		try
		{
			return result.relative(rows);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public boolean previous()
	{
		try
		{
			return result.previous();
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public String getString(final int columnIndex)
	{
		try
		{
			return result.getString(columnIndex);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public boolean getBoolean(final int columnIndex)
	{
		try
		{
			return result.getBoolean(columnIndex);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public byte getByte(final int columnIndex)
	{
		try
		{
			return result.getByte(columnIndex);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public short getShort(final int columnIndex)
	{
		try
		{
			return result.getShort(columnIndex);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public int getInt(final int columnIndex)
	{
		try
		{
			return result.getInt(columnIndex);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public long getLong(final int columnIndex)
	{
		try
		{
			return result.getLong(columnIndex);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public float getFloat(final int columnIndex)
	{
		try
		{
			return result.getFloat(columnIndex);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public double getDouble(final int columnIndex)
	{
		try
		{
			return result.getDouble(columnIndex);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public BigDecimal getBigDecimal(final int columnIndex)
	{
		try
		{
			return result.getBigDecimal(columnIndex);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public byte[] getBytes(final int columnIndex)
	{
		try
		{
			return result.getBytes(columnIndex);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public Date getDate(final int columnIndex)
	{
		try
		{
			return result.getDate(columnIndex);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public Time getTime(final int columnIndex)
	{
		try
		{
			return result.getTime(columnIndex);
		}
		catch(final SQLException e)
		{
			throw new JdbcException(e);
		}
	}

	public Timestamp getTimestamp(final int columnIndex)
	{
		try
		{
			return result.getTimestamp(columnIndex);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public java.io.InputStream getAsciiStream(final int columnIndex)
	{
		try
		{
			return result.getAsciiStream(columnIndex);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}


	public java.io.InputStream getBinaryStream(final int columnIndex)
	{
		try
		{
			return result.getBinaryStream(columnIndex);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public String getString(final String columnLabel)
	{
		try
		{
			return result.getString(columnLabel);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public boolean getBoolean(final String columnLabel)
	{
		try
		{
			return result.getBoolean(columnLabel);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public byte getByte(final String columnLabel)
	{
		try
		{
			return result.getByte(columnLabel);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public short getShort(final String columnLabel)
	{
		try
		{
			return result.getShort(columnLabel);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public int getInt(final String columnLabel)
	{
		try
		{
			return result.getInt(columnLabel);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public long getLong(final String columnLabel)
	{
		try
		{
			return result.getLong(columnLabel);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public float getFloat(final String columnLabel)
	{
		try
		{
			return result.getFloat(columnLabel);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public double getDouble(final String columnLabel)
	{
		try
		{
			return result.getDouble(columnLabel);
		}
		catch(final SQLException e)
		{
			throw new JdbcException(e);
		}
	}

	public BigDecimal getBigDecimal(final String columnLabel)
	{
		try
		{
			return result.getBigDecimal(columnLabel);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public byte[] getBytes(final String columnLabel)
	{
		try
		{
			return result.getBytes(columnLabel);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public Date getDate(final String columnLabel)
	{
		try
		{
			return result.getDate(columnLabel);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public Time getTime(final String columnLabel)
	{
		try
		{
			return result.getTime(columnLabel);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public Timestamp getTimestamp(final String columnLabel)
	{
		try
		{
			return result.getTimestamp(columnLabel);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public java.io.InputStream getAsciiStream(final String columnLabel)
	{
		try
		{
			return result.getAsciiStream(columnLabel);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}

	public java.io.InputStream getBinaryStream(final String columnLabel)
	{
		try
		{
			return result.getBinaryStream(columnLabel);
		}
		catch(final SQLException e)
		{
			if(jdbc.logger != null)
			{
				jdbc.logger.log(e);
			}
			throw new JdbcException(e);
		}
	}
}
