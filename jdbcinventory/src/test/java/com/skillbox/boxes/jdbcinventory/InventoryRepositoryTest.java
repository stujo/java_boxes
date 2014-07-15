package com.skillbox.boxes.jdbcinventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class InventoryRepositoryTest {
  private static final String SQL_H2_INVENTORY_COLUMNS = "ID bigint auto_increment, NAME VARCHAR(255), STOCK_LEVEL INTEGER, PRIMARY KEY ( id )";

  @BeforeClass
  public static void createSchema() throws Exception {
    TestHelper.createSchema();
  }

  @Before
  public void importDataSet() throws Exception {
    TestHelper.importDataSet();
  }

  @Test
  public void testFindsAndReadsExistingInventoryByProductName()
      throws Exception {
    final InventoryRepository repository = new InventoryRepository(
        TestHelper.dataSource());
    final Inventory flatscreenInventory = repository
        .findInventoryByProductName("Flat Screen TV");
    assertNotNull(flatscreenInventory);
    assertEquals(new Long(1), flatscreenInventory.getId());
    assertEquals("Flat Screen TV", flatscreenInventory.getProductName());
    assertEquals(42, flatscreenInventory.getStockLevel());
  }

  @Test
  public void testFindsAndReadsExistingInventoryById() throws Exception {
    final InventoryRepository repository = new InventoryRepository(
        TestHelper.dataSource());
    final Inventory lampInventory = repository.findInventoryById(4);
    assertNotNull(lampInventory);
    assertEquals("Pink Lamp", lampInventory.getProductName());
    assertEquals(12, lampInventory.getStockLevel());
  }

  @Test
  public void testDecementInventoryById() throws Exception {
    final int soldCount = 4;
    final InventoryRepository repository = new InventoryRepository(
        TestHelper.dataSource());
    final int initialLampInventory = repository.getStockLevelById(4);
    repository.decrementStockLevelById(4, soldCount);
    final int currentLampInventory = repository.getStockLevelById(4);
    assertEquals("Should decrement inventory by " + soldCount,
        initialLampInventory - soldCount, currentLampInventory);
  }

  @Test
  public void testDeleteAllInventory() throws Exception {
    final InventoryRepository repository = new InventoryRepository(
        TestHelper.dataSource());
    repository.deleteAll();
    assertEquals(0, repository.count());
  }

  @Test
  public void testInventoryByMissingId() throws Exception {
    final InventoryRepository repository = new InventoryRepository(
        TestHelper.dataSource());
    final int missing = repository.getStockLevelById(-1);
    assertEquals("Should be zero", 0, missing);
  }

  @Test
  public void testCountInventory() throws Exception {
    final InventoryRepository repository = new InventoryRepository(
        TestHelper.dataSource());
    assertEquals(3, repository.count());
  }

  @Test
  public void testReturnsNullWhenInventoryCannotBeFoundByProductName()
      throws Exception {
    final InventoryRepository repository = new InventoryRepository(
        TestHelper.dataSource());
    final Inventory inventory = repository
        .findInventoryByProductName("iDoNotExist");

    assertNull(inventory);
  }

  class ShoppingCartClient implements Runnable {
    static final int PRODUCT_ID = 1;
    static final int NUMBER_TO_PURCHASE = 30;
    private final String mName;
    private final CountDownLatch mLatch;
    private final Logger mLogger;

    public ShoppingCartClient(final String name, final CountDownLatch latch,
        final Logger logger) {
      mName = name;
      mLatch = latch;
      mLogger = logger;
    }

    public void run() {
      try {
        mLogger.log(Level.FINE, "Starting " + mName);
        final InventoryRepository repository = new InventoryRepository(
            TestHelper.dataSource());
        final int stockLevel = repository.getStockLevelById(1);
        mLogger.log(Level.FINE, "Stock Level " + stockLevel);

        final boolean bought = repository.attemptToBuyItems(PRODUCT_ID,
            NUMBER_TO_PURCHASE);

        mLogger.log(Level.FINE, "Bought " + NUMBER_TO_PURCHASE + "? " + bought);

      } catch (final SQLException e) {
        mLogger.log(Level.SEVERE, "Error in " + mName, e);
      } finally {
        mLogger.log(Level.FINE, "Ending " + mName);
        mLatch.countDown();
      }
    }
  }

  @Test
  public void testEnforcedAtomicity() throws InterruptedException, SQLException {
    final int threadCount = 5;
    final Logger logger = Logger.getLogger(this.getClass().getName());
    final CountDownLatch latch = new CountDownLatch(threadCount);
    final InventoryRepository repository = new InventoryRepository(
        TestHelper.dataSource());

    logger.log(Level.FINE, "Starting Threads");
    logger
    .log(Level.FINE, "Pre Stock Level " + repository.getStockLevelById(1));

    for (int i = 0; i < threadCount; i++) {
      new Thread(new ShoppingCartClient("cart-" + i, latch, logger)).start();
    }

    logger.log(Level.FINE, "Awaiting Threads");
    latch.await();
    logger.log(Level.FINE, "After Threads");
    logger.log(
        Level.FINE,
        "Post Stock Level "
            + repository.getStockLevelById(ShoppingCartClient.PRODUCT_ID));

    assertEquals("Only one purchase of "
        + ShoppingCartClient.NUMBER_TO_PURCHASE + " should succeed ", 12,
        repository.getStockLevelById(ShoppingCartClient.PRODUCT_ID));

  }

  @Test
  public void testListAll() throws SQLException {
    final InventoryRepository repository = new InventoryRepository(
        TestHelper.dataSource());
    final ArrayList<Inventory> items = repository.findAll();
    assertEquals(3, items.size());
  }

  @Test
  public void testDropAndCreateSchema() throws SQLException {

    final InventoryRepository repository = new InventoryRepository(
        TestHelper.dataSource());

    assertEquals(3, repository.count());

    repository.dropTable();

    repository.createTable(SQL_H2_INVENTORY_COLUMNS);

    assertEquals(0, repository.count());

    repository.close();
  }

  @Test
  public void testImportCSV() throws SQLException, FileNotFoundException {
    final InventoryRepository repository = new InventoryRepository(
        TestHelper.dataSource());
    InputStream is = null;
    try {
      repository.dropTable();

      repository.createTable(SQL_H2_INVENTORY_COLUMNS);

      assertEquals(0, repository.count());

      final String resourceName = "./src/test/java/com/skillbox/boxes/jdbcinventory/testImportCSV.csv";
      is = new FileInputStream(new File(resourceName));
      repository.loadInventory(is);

      assertEquals(3, repository.count());
      assertEquals(12, repository.findInventoryByProductName("Red Foot Stool")
          .getStockLevel());
      assertEquals(3, repository
          .findInventoryByProductName("Red Chaise Lounge").getStockLevel());
      assertEquals(0, repository.findInventoryByProductName("Red Sofa")
          .getStockLevel());

    } finally {
      if (is != null) {
        try {
          is.close();
        } catch (final IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}