package com.skillbox.boxes.checkout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import asg.cliche.Command;
import asg.cliche.Param;
import asg.cliche.ShellFactory;

import com.skillbox.boxes.jdbcinventory.Inventory;
import com.skillbox.boxes.jdbcinventory.InventoryRepository;

/**
 * Checkout Time
 *
 */
public class App {

  private static final String CONFIG_PROPERTIES = "com/skillbox/boxes/checkout/config.properties";

  private static final String SQL_SQLITE_INVENTORY_COLUMNS = "ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME VARCHAR(255), STOCK_LEVEL INTEGER";

  private final Logger mLogger;

  private InventoryRepository mInventoryRepository;

  @Command
  public String createTable() {
    try {
      mInventoryRepository.createTable(SQL_SQLITE_INVENTORY_COLUMNS);
      return "Inventory Table Created";
    } catch (final SQLException e) {
      e.printStackTrace();
    }
    return "Error Creating Inventory Table";
  }

  @Command
  public String dropTable() {
    try {
      mInventoryRepository.dropTable();
      return "Inventory Table Dropped";
    } catch (final SQLException e) {
      e.printStackTrace();
    }
    return "Error Dropping Inventory Table";
  }

  @Command
  public String restock(@Param(name = "productName") final String productName,
      @Param(name = "quantity") final int quantity) {

    try {
      final Inventory inv = mInventoryRepository
          .findInventoryByProductName(productName);

      if (inv != null) {
        mInventoryRepository.incrementStockLevelById(inv.getId(), quantity);
        return "Restocked!";
      } else {
        mInventoryRepository.addProduct(productName, quantity);
        return "Added!";
      }
    } catch (final SQLException e) {
      e.printStackTrace();
      return "SQL problem:" + e.getMessage();
    } finally {

    }
  }

  @Command
  public String buy(@Param(name = "productName") final String productName,
      @Param(name = "quantity") final int quantity) {

    try {
      final Inventory inv = mInventoryRepository
          .findInventoryByProductName(productName);

      if (inv != null) {
        final boolean result = mInventoryRepository.attemptToBuyItems(
            inv.getId(), quantity);

        if (result) {
          return "SUCCESS: Purchase of " + quantity + " units of '"
              + productName + "'!";
        } else {
          return "Out of Stock: Purchase of " + quantity + " units of '"
              + productName + "' failed! (only " + inv.getStockLevel()
              + " available)";
        }

      } else {
        return "Could not find product named '" + productName + "'";
      }

    } catch (final SQLException e) {
      e.printStackTrace();
      return "SQL problem:" + e.getMessage();
    }
  }

  @Command
  public String findAll() {
    try {
      final ArrayList<Inventory> items = mInventoryRepository.findAll();
      final StringBuffer sb = new StringBuffer();

      for (final Inventory item : items) {
        sb.append(String.format("%s : %d%n", item.getProductName(),
            item.getStockLevel()));
      }

      return sb.toString();
    } catch (final SQLException e) {
      e.printStackTrace();
      return "SQL Error: " + e.getMessage();
    }
  }

  @Command
  public String loadInventory(
      @Param(name = "inventoryFile", description = "The file path to import e.g. inventory.xml") final String inventoryFile) {
    FileInputStream fis = null;

    try {
      fis = new FileInputStream(new File(inventoryFile));
      final int inCount = mInventoryRepository.loadInventory(fis);
      if (inCount > 0) {
        return "Inventory File Imported " + inCount + " records";
      } else {
        return "No Inventory Imported";
      }
    } catch (final FileNotFoundException e) {
      return "File not found :" + inventoryFile;
    } finally {
      if (fis != null) {
        try {
          fis.close();
        } catch (final IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  @Command
  public String countRecords() {
    int count = 0;
    try {
      count = mInventoryRepository.count();
    } catch (final SQLException e) {
      mLogger.log(Level.SEVERE, "Error accessing InventoryRepository", e);
    }

    return "Inventory Record Count is " + count;
  }

  App() {
    System.out.println("Welcome to the checkout!");

    mLogger = Logger.getLogger(App.class.getName());

    final Properties props = loadProperties();

    final DataSource ds = getDataSourceFromProperties(props);

    try {
      mInventoryRepository = new InventoryRepository(ds);
    } catch (final SQLException e) {
      mLogger.log(Level.SEVERE, "Error creating InventoryRepository", e);
    }
  }

  public static void main(final String[] args) throws IOException {

    final App theApp = new App();

    if (theApp.isOkToRun()) {
      ShellFactory.createConsoleShell("checkout", "", theApp).commandLoop();
    }
  }

  private boolean isOkToRun() {
    return mInventoryRepository != null;
  }

  private DataSource getDataSourceFromProperties(final Properties props) {

    final SQLiteConfig config = new SQLiteConfig(props);

    final SQLiteDataSource ds = new SQLiteDataSource(config);

    ds.setUrl("jdbc:sqlite:" + props.getProperty("dburl", "db/checkout.db"));

    return ds;
  }

  public Properties loadProperties() {

    final Properties prop = new Properties();
    InputStream inputStream = null;
    try {
      inputStream = App.class.getClassLoader().getResourceAsStream(
          CONFIG_PROPERTIES);
      if (inputStream != null) {
        prop.load(inputStream);
      } else {
        mLogger.log(Level.SEVERE, "Missing Database Configuration File "
            + CONFIG_PROPERTIES);
      }

    } catch (final IOException e) {
      mLogger.log(Level.SEVERE, "Error Reading Database Configuration File "
          + CONFIG_PROPERTIES, e);
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (final IOException e) {
          e.printStackTrace();
        }
      }
    }
    return prop;
  }
}
