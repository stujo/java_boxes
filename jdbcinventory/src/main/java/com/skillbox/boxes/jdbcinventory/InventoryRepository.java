package com.skillbox.boxes.jdbcinventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

public class InventoryRepository {

  private static final String TABLE_SQL = "INVENTORY";
  private static final String FIELDS_SQL = "*";
  private static final String COUNT_SQL = "COUNT(*)";
  private final DataSource mDataSource;
  private final Connection mConnection;

  public InventoryRepository(final DataSource dataSource) throws SQLException {
    mDataSource = dataSource;
    mConnection = mDataSource.getConnection();
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
    final ResultSet result = prepStmt.executeQuery();

    Inventory item = null;

    if (result.next()) {
      item = Inventory.fromResult(result);
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
  public Inventory findInventoryById(final int productId) throws SQLException {
    final PreparedStatement prepStmt = mConnection.prepareStatement("SELECT "
        + FIELDS_SQL + " FROM " + TABLE_SQL + " WHERE ID = ?");

    prepStmt.setInt(1, productId);

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

    final ResultSet rs = stmt.executeQuery("SELECT " + COUNT_SQL
        + " as CNT FROM " + TABLE_SQL + " WHERE 1");

    rs.next();

    return rs.getInt("CNT");
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
  public boolean decrementStockLevelById(final int productId,
      final int soldCount) throws SQLException {
    final PreparedStatement prepStmt = mConnection.prepareStatement("UPDATE "
        + TABLE_SQL + " SET STOCK_LEVEL = STOCK_LEVEL - ? WHERE ID = ?");
    prepStmt.setInt(1, soldCount);
    prepStmt.setInt(2, productId);

    final int rowsUpdated = prepStmt.executeUpdate();

    return (rowsUpdated > 0);
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
    final int result = prepStmt.executeUpdate();
    return result;
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
  public boolean attemptToBuyItems(final int productId, final int soldCount)
      throws SQLException, InterruptedException {

    boolean purchased = false;

    final PreparedStatement prepStmt = mConnection
        .prepareStatement("UPDATE "
            + TABLE_SQL
            + " SET STOCK_LEVEL = STOCK_LEVEL - ? WHERE ID = ? AND STOCK_LEVEL >= ?");
    prepStmt.setInt(1, soldCount);
    prepStmt.setInt(2, productId);
    prepStmt.setInt(3, soldCount);

    final int updated = prepStmt.executeUpdate();

    purchased = updated > 0;

    return purchased;
  }
}
