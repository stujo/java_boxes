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

  public Inventory findInventoryByProductName(final String name)
      throws SQLException {

    final PreparedStatement preStatement = mConnection
        .prepareStatement("SELECT " + FIELDS_SQL + " FROM " + TABLE_SQL
            + " WHERE NAME = ?");
    preStatement.setString(1, name);

    return lookupSingleItem(preStatement);
  }

  Inventory lookupSingleItem(final PreparedStatement preStatement)
      throws SQLException {
    final ResultSet result = preStatement.executeQuery();

    Inventory item = null;

    if (result.next()) {
      item = Inventory.fromResult(result);
    }

    return item;
  }

  public Inventory findInventoryById(final int id) throws SQLException {
    final PreparedStatement preStatement = mConnection
        .prepareStatement("SELECT " + FIELDS_SQL + " FROM " + TABLE_SQL
            + " WHERE ID = ?");

    preStatement.setInt(1, id);

    return lookupSingleItem(preStatement);
  }

  public int count() throws SQLException {

    final Statement stmt = mConnection.createStatement();

    final ResultSet rs = stmt.executeQuery("SELECT " + COUNT_SQL
        + " as CNT FROM " + TABLE_SQL + " WHERE 1");

    return rs.next() ? rs.getInt("CNT") : 0;
  }

  public int getStockLevelById(final int id) throws SQLException {
    final Inventory inventory = findInventoryById(id);
    return inventory != null ? inventory.getStockLevel() : 0;
  }

  public int decrementStockLevelById(final int id, final int soldCount)
      throws SQLException {
    final PreparedStatement preStatement = mConnection
        .prepareStatement("UPDATE " + TABLE_SQL
            + " SET STOCK_LEVEL = STOCK_LEVEL - ? WHERE ID = ?");
    preStatement.setInt(1, id);
    preStatement.setInt(2, id);

    final int result = preStatement.executeUpdate();

    return result;

  }
}
