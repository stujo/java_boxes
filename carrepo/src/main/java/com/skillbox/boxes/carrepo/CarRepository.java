package com.skillbox.boxes.carrepo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class CarRepository {
	Connection mConnection;

	public CarRepository(Connection connection) {
		mConnection = connection;
	}

	public void initialize() {
		try {
			executeSQLWithComment(Car.CREATE_SCHEMA_SQL, "Creating Cars Table");
		} finally {

		}
	}

	public void destroy() {
		try {
			executeSQLWithComment(Car.DROP_SCHEMA_SQL, "Dropping Cars Table");
		} finally {

		}
	}

	void executeSQLWithComment(final String sql, final String comment) {
		Statement stmt = null;
		try {
			stmt = mConnection.createStatement();
			stmt.execute(sql);

			System.out.println(comment);

		} catch (final SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (final SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param csvResourceName
	 * @return Number of rows processed
	 */
	public int processCSVFile(String csvResourceName) {
		int rowsProcessed = 0;
		CSVReader csvReader = null;

		try {
			csvReader = new CSVReader(new InputStreamReader(
					CarShowroomApp.class.getClassLoader().getResourceAsStream(
							csvResourceName)));

			List<String[]> content = csvReader.readAll();

			// Ignore Header Row
			for (int i = 1; i < content.size(); i++) {
				String[] row = content.get(i);

				Car car = new Car();
				car.setYear(Integer.parseInt(row[0]));
				car.setMake(row[1]);
				car.setModel(row[2]);
				car.setDescription(row[3]);
				car.setPrice(new BigDecimal(row[4]));

				// This will do an insert
				car.save(mConnection);

				// This will do an update
				car.save(mConnection);

				// This will delete the row
				car.delete(mConnection);

				rowsProcessed++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != csvReader) {
				try {
					csvReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return rowsProcessed;
	}
}
