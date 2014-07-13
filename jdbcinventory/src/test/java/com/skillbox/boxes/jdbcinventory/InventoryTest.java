package com.skillbox.boxes.jdbcinventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class InventoryTest {

  @BeforeClass
  public static void createSchema() throws Exception {
    TestHelper.createSchema();
  }

  @Before
  public void importDataSet() throws Exception {
    TestHelper.importDataSet();
  }

  @Test
  public void testSaveInventoryById() throws Exception {
    final InventoryRepository repository = new InventoryRepository(
        TestHelper.dataSource());
    final Inventory lampInventory = repository.findInventoryById(4);
    assertNotNull(lampInventory);
    lampInventory.setProductName("Purple Lamp");
    repository.save(lampInventory);
    final Inventory lampInventory2 = repository.findInventoryById(4);
    assertEquals("Purple Lamp", lampInventory2.getProductName());
  }

  @Test
  public void testNewInventory() throws Exception {
    final InventoryRepository repository = new InventoryRepository(
        TestHelper.dataSource());

    final int startCount = repository.count();

    final Inventory newInventory = Inventory.buildForStockLevel(5);
    newInventory.setProductName("Black Leather Sofa");

    assertTrue(newInventory.isNew());
    repository.save(newInventory);
    assertFalse(newInventory.isNew());

    final Inventory sofa = repository.findInventoryById(newInventory.getId());
    assertEquals("Black Leather Sofa", sofa.getProductName());

    assertEquals(startCount + 1, repository.count());
  }
}
