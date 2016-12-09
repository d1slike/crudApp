package ru.disdev.jdbchelper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * Author: Erdinc YILMAZEL
 * Date: Dec 30, 2008
 * Time: 4:52:36 PM
 */
public class MappingBatchFeeder<T> extends IteratorBatchFeeder<T>
{
	StatementMapper<T> mapper;

	public MappingBatchFeeder(Iterator<T> i, StatementMapper<T> m)
	{
		super(i);
		mapper = m;
	}

	@Override
	public void feedStatement(PreparedStatement stmt, T object) throws SQLException
	{
		mapper.mapStatement(stmt, object);
	}
}
