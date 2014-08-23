package com.skillbox.boxes.carrepo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.PropertyConfigurator;

/**
 * CarShowroomApp
 *
 */
public class CarShowroomApp {

	final static String DATABASE_URL = "jdbc:derby:directory:carshowroom;create=true";
	final static String JDBC_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

	public static Connection getConnection() {
		Connection connection = null;

		try {

			System.out.println("Loading driver '" + JDBC_DRIVER + "'");

			Class.forName(JDBC_DRIVER).newInstance();

			connection = DriverManager.getConnection(DATABASE_URL);

			System.out.println("Established Connection to '" + DATABASE_URL
					+ "'");

		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		} catch (final SQLException e) {
			e.printStackTrace();
		} catch (final InstantiationException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		}

		return connection;
	}

	public static void main(String[] args) throws SQLException {
		System.out.println("Welcome to CarShowroomApp!");

		// Configure Log4J
		PropertyConfigurator.configure(CarShowroomApp.class.getClassLoader()
				.getResource("log4j.properties"));

		Connection connection = getConnection();

		try {
			CarRepository repository = new CarRepository(connection);
			try {
				repository.initialize();
				repository.processCSVFile("cars.csv");
			} finally {
				repository.destroy();
			}

		} finally {
			if (null != connection) {
				connection.close();
			}
		}
	}

}
