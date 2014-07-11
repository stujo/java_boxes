package com.skillbox.boxes.jdbcinventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;

import javax.sql.DataSource;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class InventoryRepositoryTest {
  private static final String JDBC_DRIVER = org.h2.Driver.class.getName();
  private static final String JDBC_URL = "jdbc:h2:mem:inventory;DB_CLOSE_DELAY=-1";
  private static final String USER = "sa";
  private static final String PASSWORD = "";
  private static final String SCHEMA_FILE = "./src/test/sql/inventory_schema.sql";
  private static final String DATA_FILE = "./src/test/sql/inventory_data.xml";

  @BeforeClass
  public static void createSchema() throws Exception {
    RunScript.execute(JDBC_URL, USER, PASSWORD, SCHEMA_FILE, null, false);
  }

  @Before
  public void importDataSet() throws Exception {
    final IDataSet dataSet = readDataSet();
    cleanlyInsert(dataSet);
  }

  private IDataSet readDataSet() throws Exception {
    return new FlatXmlDataSetBuilder().build(new File(DATA_FILE));
  }

  private void cleanlyInsert(final IDataSet dataSet) throws Exception {
    final IDatabaseTester databaseTester = new JdbcDatabaseTester(JDBC_DRIVER,
        JDBC_URL, USER, PASSWORD);
    databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
    databaseTester.setDataSet(dataSet);
    databaseTester.onSetup();
  }

  @Test
  public void testFindsAndReadsExistingInventoryByProductName()
      throws Exception {
    final InventoryRepository repository = new InventoryRepository(dataSource());
    final Inventory flatscreenInventory = repository
        .findInventoryByProductName("Flat Screen TV");

    assertNotNull(flatscreenInventory);
    assertEquals("Flat Screen TV", flatscreenInventory.getProductName());
    assertEquals(new Integer(42), flatscreenInventory.getStockLevel());
  }

  @Test
  public void testFindsAndReadsExistingInventoryById() throws Exception {
    final InventoryRepository repository = new InventoryRepository(dataSource());
    final Inventory lampInventory = repository.findInventoryById(4);
    assertNotNull(lampInventory);
    assertEquals("Pink Lamp", lampInventory.getProductName());
    assertEquals(new Integer(12), lampInventory.getStockLevel());
  }

  @Test
  public void testDecementInventoryById() throws Exception {
    final int soldCount = 4;
    final InventoryRepository repository = new InventoryRepository(dataSource());
    final int initialLampInventory = repository.getStockLevelById(4);
    repository.decrementStockLevelById(4, soldCount);
    final int currentLampInventory = repository.getStockLevelById(4);
    assertEquals("Should decrement inventory by " + soldCount,
        initialLampInventory - soldCount, currentLampInventory);
  }

  @Test
  public void testCountInventory() throws Exception {
    final InventoryRepository repository = new InventoryRepository(dataSource());
    assertEquals(3, repository.count());
  }

  @Test
  public void testReturnsNullWhenInventoryCannotBeFoundByProductName()
      throws Exception {
    final InventoryRepository repository = new InventoryRepository(dataSource());
    final Inventory inventory = repository
        .findInventoryByProductName("iDoNotExist");

    assertNull(inventory);
  }

  private DataSource dataSource() {
    final JdbcDataSource dataSource = new JdbcDataSource();
    dataSource.setURL(JDBC_URL);
    dataSource.setUser(USER);
    dataSource.setPassword(PASSWORD);
    return dataSource;
  }
}