package com.skillbox.boxes.carshowroom;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class SQLTools {

	/**
	 * Wrapper function to execute raw SQL
	 * 
	 * @return true if the statement was executed
	 */
	public static boolean executeSQL(Connection conn, String sql) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();

			System.out.println("Attempting SQL:[" + sql + "]");

			boolean isResultSet = stmt.execute(sql);

			if (isResultSet) {
				System.out.println("Executed '" + sql
						+ "' and ignoring result set");
			} else {
				System.out.println("Executed '" + sql + "' and "
						+ stmt.getUpdateCount() + " rows were affected");
			}

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	static void readDBTableIntoSet(Set<String> set, DatabaseMetaData dbmeta)
			throws SQLException {

		ResultSet rs = dbmeta.getTables(null, null, null, null);
		while (rs.next()) {
			set.add(rs.getString("TABLE_NAME").toLowerCase());
		}
	}

	public static Set<String> getDBTables(Connection targetDBConn)
			throws SQLException {
		Set<String> set = new HashSet<String>();
		DatabaseMetaData dbmeta = targetDBConn.getMetaData();
		readDBTableIntoSet(set, dbmeta);
		return set;
	}

	/**
	 * Use ResultSet next loop and prior knowledge of the table structure to
	 * print out the results content
	 */
	public static void printOutResults(ResultSet results) throws SQLException {
		ResultSetMetaData rsmd = results.getMetaData();
		printOutColumnsAsHeaders(rsmd);
		int numberCols = rsmd.getColumnCount();

		while (results.next()) {

			for (int i = 1; i <= numberCols; i++) {
				System.out.print(results.getObject(i).toString() + "\t");
			}
			System.out.println();

		}
		System.out
				.println("-------------------------------------------------\n");
	}

	/**
	 * Use ResultSet MetaData to print out column names
	 */
	private static void printOutColumnsAsHeaders(ResultSetMetaData rsmd)
			throws SQLException {

		System.out.println();

		int numberCols = rsmd.getColumnCount();
		for (int i = 1; i <= numberCols; i++) {
			// print Column Names
			System.out.print(rsmd.getColumnLabel(i) + "\t");
		}

		System.out
				.println("\n-------------------------------------------------");
	}

}
