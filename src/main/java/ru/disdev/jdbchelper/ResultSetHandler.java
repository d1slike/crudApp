package ru.disdev.jdbchelper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A ResultSet handler may be implemented as a business logic that iterates over a
 * Jdbc ResultSet object.
 */
public abstract class ResultSetHandler
{
	/**
	 * Creates a ResultSetHandler with default fetchsize, maxRows and timeOut
	 * parameters of the underlying Jdbc driver
	 */
	public ResultSetHandler()
	{}

	/**
	 * Creates a new ResultSetHandler with the specified resultset fetch size.
	 * The fetch size parameter is important for selecting a large amount of rows
	 * from the database. If not set, you may see OutOfMemory errors.
	 *
	 * @param fetchSize
	 * 		ResultSet fetch size
	 */
	public ResultSetHandler(int fetchSize)
	{
		this.fetchSize = fetchSize;
	}

	public ResultSetHandler(int fetchSize, int maxRows)
	{
		this.fetchSize = fetchSize;
		this.maxRows = maxRows;
	}

	public ResultSetHandler(int fetchSize, int maxRows, int timeOut)
	{
		this.fetchSize = fetchSize;
		this.maxRows = maxRows;
		this.timeOut = timeOut;
	}

	int fetchSize;
	int maxRows;
	int timeOut;
	int rowNo;

	public int getRowNo()
	{
		return rowNo;
	}

	/**
	 * Should do whatever is needed for the row.
	 * The next() method of the resultset is called prior to calling this method
	 * so you should never call the next() method within the processRow method
	 * unless you know what you are doing.
	 *
	 * @param rs
	 * 		Fetched resultSet
	 *
	 * @throws SQLException
	 * 		Thrown in case of a db error
	 */
	public abstract void processRow(ResultSet rs) throws SQLException;
}
