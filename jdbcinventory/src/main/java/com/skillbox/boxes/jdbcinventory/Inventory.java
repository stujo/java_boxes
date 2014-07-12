package com.skillbox.boxes.jdbcinventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Inventory {

  private Integer mId;
  private String mProductName;
  private Integer mStockLevel;

  public static Inventory fromResult(final ResultSet rs) throws SQLException {
    final Inventory result = new Inventory();
    result.mId = rs.getInt("ID");
    result.setProductName(rs.getString("NAME"));
    result.mStockLevel = rs.getInt("STOCK_LEVEL");
    return result;
  }

  public Integer getId() {
    return mId;
  }

  public String getProductName() {
    return mProductName;
  }

  public void setProductName(final String productName) {
    mProductName = productName;
  }

  public Integer getStockLevel() {
    return mStockLevel;
  }

  void save(final Connection conn, final String tableName) throws SQLException {
    final PreparedStatement prepStmt = conn.prepareStatement("UPDATE "
        + tableName + " SET NAME = ? WHERE ID = ?");
    prepStmt.setString(1, getProductName());
    prepStmt.setInt(2, getId());
    prepStmt.executeUpdate();
  }
}
