package com.skillbox.boxes.jdbcinventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class InventoryRepositoryTest {
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
    assertEquals(new Integer(1), flatscreenInventory.getId());
    assertEquals("Flat Screen TV", flatscreenInventory.getProductName());
    assertEquals(new Integer(42), flatscreenInventory.getStockLevel());
  }

  @Test
  public void testFindsAndReadsExistingInventoryById() throws Exception {
    final InventoryRepository repository = new InventoryRepository(
        TestHelper.dataSource());
    final Inventory lampInventory = repository.findInventoryById(4);
    assertNotNull(lampInventory);
    assertEquals("Pink Lamp", lampInventory.getProductName());
    assertEquals(new Integer(12), lampInventory.getStockLevel());
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
      } catch (final InterruptedException e) {
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

}