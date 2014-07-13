package com.skillbox.boxes.jdbcinventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Inventory {

  private static final String SQL_INSERT = " (ID, NAME,STOCK_LEVEL) VALUES (NULL,?,?)";
  private Long mId;
  private String mProductName;
  private Integer mStockLevel;

  public static Inventory buildForStockLevel(final int stockLevel) {
    final Inventory result = new Inventory();
    result.mStockLevel = stockLevel;
    return result;
  }

  public static Inventory fromResult(final ResultSet rs) throws SQLException {
    final Inventory result = new Inventory();
    result.mId = rs.getLong("ID");
    result.setProductName(rs.getString("NAME"));
    result.mStockLevel = rs.getInt("STOCK_LEVEL");
    return result;
  }

  public Long getId() {
    return mId;
  }

  public String getProductName() {
    return mProductName;
  }

  public void setProductName(final String productName) {
    mProductName = productName;
  }

  public int getStockLevel() {
    return mStockLevel == null ? 0 : mStockLevel.intValue();
  }

  void save(final Connection conn, final String tableName) throws SQLException {

    if (isNew()) {
      insert(conn, tableName);
    } else {
      update(conn, tableName);
    }
  }

  void update(final Connection conn, final String tableName)
      throws SQLException {
    final PreparedStatement prepStmt = conn.prepareStatement("UPDATE "
        + tableName + " SET NAME = ? WHERE ID = ?");
    prepStmt.setString(1, getProductName());
    prepStmt.setLong(2, getId());
    prepStmt.executeUpdate();
    prepStmt.close();
  }

  void insert(final Connection conn, final String tableName)
      throws SQLException {
    PreparedStatement prepStmt = null;
    ResultSet generatedKeys = null;

    try {

      prepStmt = conn.prepareStatement("INSERT INTO " + tableName + SQL_INSERT,
          Statement.RETURN_GENERATED_KEYS);
      prepStmt.setString(1, getProductName());
      prepStmt.setInt(2, getStockLevel());

      final int affectedRows = prepStmt.executeUpdate();
      if (affectedRows == 0) {
        throw new SQLException("Creating inventory failed, no rows affected.");
      }

      generatedKeys = prepStmt.getGeneratedKeys();
      if (generatedKeys.next()) {
        mId = generatedKeys.getLong(1);
      } else {
        throw new SQLException(
            "Creating inventory failed, no generated key obtained.");
      }
    } finally {
      if (generatedKeys != null) {
        try {
          generatedKeys.close();
        } catch (final SQLException logOrIgnore) {
        }
      }
      if (prepStmt != null) {
        try {
          prepStmt.close();
        } catch (final SQLException logOrIgnore) {
        }
      }
    }
  }

  public boolean isNew() {
    return null == mId;
  }
}
