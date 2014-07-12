package com.skillbox.boxes.jdbcinventory;

import java.io.File;
import java.net.MalformedURLException;

import javax.sql.DataSource;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;

public class TestHelper {

  static final String JDBC_DRIVER = org.h2.Driver.class.getName();
  static final String JDBC_URL = "jdbc:h2:mem:inventory;DB_CLOSE_DELAY=-1";
  static final String USER = "sa";
  static final String PASSWORD = "";
  static final String SCHEMA_FILE = "./src/test/sql/inventory_schema.sql";
  static final String DATA_FILE = "./src/test/sql/inventory_data.xml";

  public static void createSchema() throws Exception {
    RunScript.execute(JDBC_URL, USER, PASSWORD, SCHEMA_FILE, null, false);
  }

  public static void importDataSet() throws Exception {
    final IDataSet dataSet = readDataSet();
    cleanlyInsert(dataSet);
  }

  private static IDataSet readDataSet() throws MalformedURLException,
  DataSetException {
    return new FlatXmlDataSetBuilder().build(new File(TestHelper.DATA_FILE));
  }

  private static void cleanlyInsert(final IDataSet dataSet) throws Exception {
    final IDatabaseTester databaseTester = new JdbcDatabaseTester(
        TestHelper.JDBC_DRIVER, TestHelper.JDBC_URL, TestHelper.USER,
        TestHelper.PASSWORD);
    databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
    databaseTester.setDataSet(dataSet);
    databaseTester.onSetup();
  }

  static DataSource dataSource() {
    final JdbcDataSource dataSource = new JdbcDataSource();
    dataSource.setURL(TestHelper.JDBC_URL);
    dataSource.setUser(TestHelper.USER);
    dataSource.setPassword(TestHelper.PASSWORD);
    return dataSource;
  }

}
