package com.skillbox.boxes.jdbcinventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
}
