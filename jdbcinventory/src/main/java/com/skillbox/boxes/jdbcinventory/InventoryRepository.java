package com.skillbox.boxes.jdbcinventory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import au.com.bytecode.opencsv.CSVReader;

public class InventoryRepository {

  private static final String TABLE_SQL = "INVENTORY";
  private static final String FIELDS_SQL = "*";
  private static final String COUNT_SQL = "COUNT(*)";
  private DataSource mDataSource;
  private Connection mConnection;
  private final Logger mLogger;

  public InventoryRepository(final DataSource dataSource) throws SQLException {
    mDataSource = dataSource;
    mConnection = mDataSource.getConnection();
    mLogger = Logger.getLogger(this.getClass().getName());
  }

  public Inventory findInventoryByProductName(final String productName)
      throws SQLException {

    final PreparedStatement prepStmt = mConnection.prepareStatement("SELECT "
        + FIELDS_SQL + " FROM " + TABLE_SQL + " WHERE NAME = ?");
    prepStmt.setString(1, productName);

    return lookupSingleItem(prepStmt);
  }

  Inventory lookupSingleItem(final PreparedStatement prepStmt)
      throws SQLException {
    Inventory item = null;
    try {
      final ResultSet result = prepStmt.executeQuery();
      if (result.next()) {
        item = Inventory.fromResult(result);
      }
    } finally {
      prepStmt.close();
    }

    return item;
  }

  /**
   * Return the current stock_level for a product
   *
   * @param productId
   *
   * @return Current Stock Level
   * @throws SQLException
   */
  public Inventory findInventoryById(final long productId) throws SQLException {
    final PreparedStatement prepStmt = mConnection.prepareStatement("SELECT "
        + FIELDS_SQL + " FROM " + TABLE_SQL + " WHERE ID = ?");

    prepStmt.setLong(1, productId);

    return lookupSingleItem(prepStmt);
  }

  /**
   * Return the count of the number of product inventory records.
   *
   * @return The number of rows in the table
   *
   * @throws SQLException
   */
  public int count() throws SQLException {

    final Statement stmt = mConnection.createStatement();

    try {
      final ResultSet rs = stmt.executeQuery("SELECT " + COUNT_SQL
          + " as CNT FROM " + TABLE_SQL + " WHERE 1");
      rs.next();
      return rs.getInt("CNT");
    } finally {
      stmt.close();
    }
  }

  /**
   * Return Current Stock level
   *
   * @param productId
   * @return
   * @throws SQLException
   */
  public int getStockLevelById(final int productId) throws SQLException {
    final Inventory inventory = findInventoryById(productId);
    return inventory != null ? inventory.getStockLevel() : 0;
  }

  /**
   * Decrement Stock Quantity without regard for current stock level
   *
   * @param productId
   * @param soldCount
   * @return
   * @throws SQLException
   */
  public boolean decrementStockLevelById(final long productId,
      final int soldCount) throws SQLException {
    final PreparedStatement prepStmt = mConnection.prepareStatement("UPDATE "
        + TABLE_SQL + " SET STOCK_LEVEL = STOCK_LEVEL - ? WHERE ID = ?");

    try {
      prepStmt.setInt(1, soldCount);
      prepStmt.setLong(2, productId);
      final int rowsUpdated = prepStmt.executeUpdate();
      return (rowsUpdated > 0);
    } finally {
      prepStmt.close();
    }
  }

  /**
   * Increment Stock Quantity
   *
   * @param productId
   * @param quantity
   * @return
   * @throws SQLException
   */
  public boolean incrementStockLevelById(final long productId,
      final int quantity) throws SQLException {
    final PreparedStatement prepStmt = mConnection.prepareStatement("UPDATE "
        + TABLE_SQL + " SET STOCK_LEVEL = STOCK_LEVEL + ? WHERE ID = ?");

    try {
      prepStmt.setInt(1, quantity);
      prepStmt.setLong(2, productId);
      final int rowsUpdated = prepStmt.executeUpdate();
      return (rowsUpdated > 0);
    } finally {
      prepStmt.close();
    }
  }

  /**
   * Delete All Product Inventory Data
   *
   * @return
   * @throws SQLException
   */
  public int deleteAll() throws SQLException {
    final PreparedStatement prepStmt = mConnection
        .prepareStatement("DELETE FROM " + TABLE_SQL + " WHERE 1");
    try {
      return prepStmt.executeUpdate();
    } finally {
      prepStmt.close();
    }
  }

  /**
   * Atomic Decrement of STOCK_LEVEL.
   * <p>
   * Attempts to decrement the stock level and returns true if enough units were
   * on hand and the stock level was reduced
   *
   * @param productId
   *          Product ID
   * @param soldCount
   *          Quantity Requested
   * @return true if the units were allocated to you or false
   * @throws SQLException
   * @throws InterruptedException
   */
  public boolean attemptToBuyItems(final long productId, final int soldCount)
      throws SQLException {
    final PreparedStatement prepStmt = mConnection
        .prepareStatement("UPDATE "
            + TABLE_SQL
            + " SET STOCK_LEVEL = STOCK_LEVEL - ? WHERE ID = ? AND STOCK_LEVEL >= ?");

    try {
      prepStmt.setInt(1, soldCount);
      prepStmt.setLong(2, productId);
      prepStmt.setInt(3, soldCount);

      return prepStmt.executeUpdate() > 0;
    } finally {
      prepStmt.close();
    }
  }

  public boolean save(final Inventory lampInventory) {
    try {
      lampInventory.save(mConnection, TABLE_SQL);
      return true;
    } catch (final SQLException e) {
      mLogger.log(Level.SEVERE, "Error Saving Inventory", e);
    }
    return false;
  }

  public void createTable(final String ddlColumnsSQL) throws SQLException {
    final Statement stmt = mConnection.createStatement();
    final String sql = "CREATE TABLE " + TABLE_SQL + " (" + ddlColumnsSQL + ")";
    try {
      stmt.executeUpdate(sql);
    } finally {
      stmt.close();
    }
  }

  public void dropTable() throws SQLException {
    final Statement stmt = mConnection.createStatement();

    final String sql = "DROP TABLE " + TABLE_SQL;
    try {
      stmt.executeUpdate(sql);
    } finally {
      stmt.close();
    }
  }

  public void close() {
    mDataSource = null;
    if (mConnection != null) {
      try {
        mConnection.close();
      } catch (final SQLException e) {
      }
      mConnection = null;
    }
  }

  /**
   * Attempts to load inventory data from file
   *
   * @param is
   *          - File Location
   * @return The count of rows imported
   */
  public int loadInventory(final InputStream is) {
    int importedCount = 0;

    if (null == is) {
      throw new NullPointerException("InputStream cannot be null");
    }

    CSVReader csvReader = null;
    try {
      csvReader = new CSVReader(new InputStreamReader(is));

      String[] nextLine = csvReader.readNext();
      if (nextLine != null) {
        if (nextLine[0].equals("NAME") && nextLine[1].equals("STOCK_LEVEL")) {
          while ((nextLine = csvReader.readNext()) != null) {
            if (nextLine.length == 2) {
              final int stockLevel = Integer.parseInt(nextLine[1]);
              updateStockLevelByProductName(nextLine[0], stockLevel);
              importedCount++;
            }
          }
        } else {
          mLogger.log(Level.WARNING,
              "Invalid header row in inventory import file");
        }
      }
    } catch (final IOException e) {
      mLogger.log(Level.SEVERE, "Error loading inventory", e);
    } catch (final SQLException e) {
      mLogger.log(Level.SEVERE, "Error saving inventory", e);
    } finally {
      if (csvReader != null) {
        try {
          csvReader.close();
        } catch (final IOException e) {
        }
      }
    }
    return importedCount;
  }

  private void updateStockLevelByProductName(final String productName,
      final int stockLevel) throws SQLException {

    Inventory inv = findInventoryByProductName(productName);

    if (inv != null) {

      if (inv.getStockLevel() != stockLevel) {
        final PreparedStatement prepStmt = mConnection
            .prepareStatement("UPDATE " + TABLE_SQL
                + " SET STOCK_LEVEL = ? WHERE ID = ?");

        try {
          prepStmt.setInt(1, stockLevel);
          prepStmt.setLong(2, inv.getId());
          prepStmt.executeUpdate();
        } finally {
          prepStmt.close();
        }
      }
    } else {
      inv = Inventory.buildForStockLevel(stockLevel);
      inv.setProductName(productName);
      inv.save(mConnection, TABLE_SQL);
    }
  }

  public void addProduct(final String productName, final int quantity)
      throws SQLException {
    final Inventory inv = Inventory.buildForStockLevel(quantity);
    inv.setProductName(productName);
    inv.save(mConnection, TABLE_SQL);
  }
}
