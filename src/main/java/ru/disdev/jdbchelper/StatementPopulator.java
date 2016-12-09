package ru.disdev.jdbchelper;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Author: Erdinc YILMAZEL
 * Date: Dec 30, 2008
 * Time: 4:44:02 PM
 */
@FunctionalInterface
public interface StatementPopulator
{
	void populateStatement(PreparedStatement stmt) throws SQLException;
}
