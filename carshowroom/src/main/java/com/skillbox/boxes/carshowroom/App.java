package com.skillbox.boxes.carshowroom;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Hello world!
 *
 */
public class App {

	private static final String DB_URL = "jdbc:derby:directory:cars";
	private static final String JDBC_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	private static final String CARS_TABLE = "CARS";
	private static final String DROP_CARS_TABLE_SQL = "DROP TABLE "
			+ CARS_TABLE;

	private static final String INSERT_CAR_SQL = "INSERT INTO "
			+ CARS_TABLE
			+ "(VIN, MODEL_YEAR, MODEL_MAKE, MODEL_NAME, MODEL_DESCRIPTION, PRICE) VALUES (?,?,?,?,?,?)";

	private static final String UPDATE_CAR_SQL = "UPDATE "
			+ CARS_TABLE
			+ " SET MODEL_YEAR = ?, MODEL_MAKE = ?, MODEL_NAME = ?, MODEL_DESCRIPTION = ?, PRICE = ? WHERE ID = ?";

	private static final String LOOKUP_CAR_SQL = "SELECT ID FROM " + CARS_TABLE
			+ " WHERE VIN = ?";

	private static final String CREATE_CARS_TABLE_SQL = "CREATE TABLE "
			+ CARS_TABLE
			+ " (ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)"
			+ ", VIN VARCHAR(24)" + ", MODEL_YEAR INT"
			+ ", MODEL_MAKE VARCHAR(128)" + ", MODEL_NAME VARCHAR(128)"
			+ ", MODEL_DESCRIPTION VARCHAR(256)" + ", PRICE DECIMAL(7,2)" + ")";

	// Year,Make,Model,Description,Price

	public static InputStream resourceStream(String resourceName) {
		ClassLoader cLoader = App.class.getClassLoader();
		return cLoader.getResourceAsStream(resourceName);
	}

	static class Importer {

		int mRowNumber;
		Connection mConnection;
		PreparedStatement mInsertStatement;
		PreparedStatement mLookupStatement;
		PreparedStatement mUpdateStatement;

		void importRow(String[] row) throws SQLException {
			mRowNumber++;
			System.out.println("Importing row#" + mRowNumber);

			int id = existingCarId(row);
			if (id > 0) {
				updateImpl(id, row);
			} else {
				insertImpl(row);
			}
		}

		private void updateImpl(int id, String[] row) throws SQLException {
			if (mUpdateStatement == null) {
				mUpdateStatement = mConnection.prepareStatement(UPDATE_CAR_SQL);
				// MODEL_YEAR = ?, MODEL_MAKE = ?, MODEL_NAME = ?,
				// MODEL_DESCRIPTION = ?, PRICE = ? WHERE ID = ?";
			}

			mUpdateStatement.setInt(1, Integer.parseInt(row[1])); // YEAR
			mUpdateStatement.setString(2, row[2]); // MAKE
			mUpdateStatement.setString(3, row[3]); // NAME
			mUpdateStatement.setString(4, row[4]); // DESC
			mUpdateStatement.setBigDecimal(5, new BigDecimal(row[5])); // PRICE
			mUpdateStatement.setInt(6, id);

			mUpdateStatement.execute();

			System.out.println("Executed " + UPDATE_CAR_SQL);

		}

		private int existingCarId(String[] row) throws SQLException {
			if (mLookupStatement == null) {
				mLookupStatement = mConnection.prepareStatement(LOOKUP_CAR_SQL);
				// VIN
			}

			mLookupStatement.setString(1, row[0]); // VIN

			int id = 0;

			ResultSet rs = null;

			try {
				rs = mLookupStatement.executeQuery();

				if (rs.next()) {
					id = rs.getInt(1);
				}
				System.out.println("Executed " + LOOKUP_CAR_SQL);

			} finally {
				rs.close();
			}

			return id;
		}

		private void insertImpl(String[] row) throws SQLException {
			if (mInsertStatement == null) {
				mInsertStatement = mConnection.prepareStatement(INSERT_CAR_SQL);
				// VIN, MODEL_YEAR, MODEL_MAKE, MODEL_NAME, MODEL_DESCRIPTION,
				// PRICE
			}

			mInsertStatement.setString(1, row[0]); // VIN
			mInsertStatement.setInt(2, Integer.parseInt(row[1])); // YEAR
			mInsertStatement.setString(3, row[2]); // MAKE
			mInsertStatement.setString(4, row[3]); // NAME
			mInsertStatement.setString(5, row[4]); // DESC
			mInsertStatement.setBigDecimal(6, new BigDecimal(row[5])); // PRICE

			mInsertStatement.execute();
			System.out.println("Executed " + INSERT_CAR_SQL);

		}

		public void close() {
			try {
				if (mInsertStatement != null) {
					mInsertStatement.close();
				}
				if (mLookupStatement != null) {
					mLookupStatement.close();
				}
				if (mUpdateStatement != null) {
					mUpdateStatement.close();
				}
				if (mConnection != null) {
					mConnection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		public void open() {
			try {
				Class.forName(JDBC_DRIVER).newInstance();
				mConnection = DriverManager.getConnection(DB_URL
						+ ";create=true");
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		public void executeSQLIfTableNotExists(String tableName, String sql)
				throws SQLException {
			Set<String> tables = SQLTools.getDBTables(mConnection);

			if (!tables.contains(tableName.toLowerCase())) {
				SQLTools.executeSQL(mConnection, sql);
			}
		}

		public void executeSQLIfTableExists(String tableName, String sql)
				throws SQLException {
			Set<String> tables = SQLTools.getDBTables(mConnection);

			if (tables.contains(tableName.toLowerCase())) {
				SQLTools.executeSQL(mConnection, sql);
			}
		}

		public void dumpTable() {
			System.out.println("DUMPING " + CARS_TABLE);

			Statement stmt = null;
			try {
				stmt = mConnection.createStatement();
				ResultSet results = null;
				try {
					results = stmt.executeQuery("select * from " + CARS_TABLE);
					SQLTools.printOutResults(results);
				} finally {
					if (results != null) {
						results.close();
					}
				}
			} catch (SQLException sqlExcept) {
				sqlExcept.printStackTrace();
			} finally {
				if (null != stmt) {
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		String csvFilename = "cars.csv";

		Importer importer = new Importer();

		Reader reader = null;

		try {
			reader = new InputStreamReader(resourceStream(csvFilename));
			importer.open();

			importer.executeSQLIfTableNotExists(CARS_TABLE,
					CREATE_CARS_TABLE_SQL);

			importCSV(reader, importer);

			importer.dumpTable();

			importer.executeSQLIfTableExists(CARS_TABLE, DROP_CARS_TABLE_SQL);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {

					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			importer.close();
		}
	}

	private static void importCSV(Reader reader, Importer importer)
			throws SQLException {
		CSVReader csvReader = null;
		try {
			csvReader = new CSVReader(reader);

			String[] headerRow = csvReader.readNext();

			String[] row = null;
			while ((row = csvReader.readNext()) != null) {
				importer.importRow(row);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (csvReader != null) {
				try {
					csvReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
