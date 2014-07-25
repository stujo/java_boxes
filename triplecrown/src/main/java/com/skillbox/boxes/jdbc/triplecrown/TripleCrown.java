package com.skillbox.boxes.jdbc.triplecrown;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 * Example for JDBC
 */
public class TripleCrown {

  private static final String TABLE_NAME           = "HORSES";
  /*
   * private static final String JDBC_CLIENT_DRIVER =
   * "org.apache.derby.jdbc.ClientDriver";
   */
  private static final String JDBC_EMBEDDED_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
  private static final String JDBC_DRIVER          = JDBC_EMBEDDED_DRIVER;
  private static String       dbURL                = "jdbc:derby:directory:races";

  public static void main(String[] args) {
    Connection conn = createConnection();

    if (conn != null) {
      try {
        try {
          createSchema(conn);
          insertHorse(conn, "Sea Biscuit", "1933-05-23");
          insertHorse(conn, "Shenangins", "2001-02-05");
          selectHorses(conn);
        } finally {
          dropSchema(conn);
        }
      } finally {
        shutdown(conn);
      }
    } else {
      System.out.println("Unable to create connection to " + dbURL);
    }
  }

  static Connection createConnection() {
    try {
      // Load the Class Manually
      // This registers it with the driver manager
      Class.forName(JDBC_DRIVER).newInstance();
      // Get a connection
      return DriverManager.getConnection(dbURL + ";create=true");
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  static void createSchema(Connection conn) {
    System.out.println("Creating Schema");
    executeSQL(
        conn,
        "CREATE TABLE "
            + TABLE_NAME
            + " (ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)"
            + ", NAME VARCHAR(128)" + ", DOB DATE)");
  }

  static void insertHorse(Connection conn, String horseName, String dateOfBirth) {

    java.sql.Date birthDate = java.sql.Date.valueOf(dateOfBirth);

    PreparedStatement prepStmt = null;

    try {
      prepStmt = conn.prepareStatement("INSERT INTO " + TABLE_NAME
          + " (NAME, DOB) VALUES (?,?)");

      prepStmt.setString(1, horseName);
      prepStmt.setDate(2, birthDate);
      prepStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (prepStmt != null) {
          prepStmt.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  static void selectHorses(Connection conn) {
    System.out.println("Selecting Horses");

    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      ResultSet results = null;
      try {
        results = stmt.executeQuery("select * from " + TABLE_NAME);
        printOutColumnsAsHeaders(results);
        printOutResults(results);
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

  static void dropSchema(Connection conn) {
    System.out.println("Dropping Schema");
    executeSQL(conn, "DROP TABLE " + TABLE_NAME);
  }

  /**
   * Wrapper function to execute raw SQL
   * 
   * @return true if the statement was executed
   */
  static boolean executeSQL(Connection conn, String sql) {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      boolean isResultSet = stmt.execute(sql);

      if (isResultSet) {
        System.out.println("Executed '" + sql + "' and ignoring result set");
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

  /**
   * Use ResultSet next loop and prior knowledge of the table structure to print
   * out the results content
   */
  private static void printOutResults(ResultSet results) throws SQLException {
    while (results.next()) {
      int id = results.getInt(1);
      String horse = results.getString(2);
      Date dob = results.getDate(3);
      System.out.println(id + "\t\t" + horse + "\t" + dob);
    }
    System.out.println("-------------------------------------------------\n");
  }

  /**
   * Use ResultSet MetaData to print out column names
   */
  private static void printOutColumnsAsHeaders(ResultSet results)
      throws SQLException {
    ResultSetMetaData rsmd = results.getMetaData();

    System.out.println();

    int numberCols = rsmd.getColumnCount();
    for (int i = 1; i <= numberCols; i++) {
      // print Column Names
      System.out.print(rsmd.getColumnLabel(i) + "\t\t");
    }

    System.out.println("\n-------------------------------------------------");
  }

  /**
   * Derby Has Some Specific Shutdown instructions for Embedded mode
   */
  private static void shutdown(Connection conn) {

    System.out.println("Shutting Down Derby");
    /***
     * In embedded mode, an application should shut down Derby.
     * 
     * Shutdown throws the XJ015 exception to confirm success.
     ***/

    if (JDBC_DRIVER.equals("org.apache.derby.jdbc.EmbeddedDriver")) {
      boolean gotSQLExc = false;
      try {
        DriverManager.getConnection("jdbc:derby:;shutdown=true");
      } catch (SQLException se) {
        if (se.getSQLState().equals("XJ015")) {
          gotSQLExc = true;
        }
      }
      if (!gotSQLExc) {
        System.out.println("Database did not shut down normally");
      } else {
        System.out.println("Database shut down normally");
      }
    }
  }
}
