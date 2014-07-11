package com.skillbox.boxes.jdbcinventory;

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
    result.setStockLevel(rs.getInt("STOCK_LEVEL"));
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

  public void setStockLevel(final Integer stockLevel) {
    mStockLevel = stockLevel;
  }
}
