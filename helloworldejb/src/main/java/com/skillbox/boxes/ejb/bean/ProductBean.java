package com.skillbox.boxes.ejb.bean;

import java.math.BigDecimal;

class ProductBean {
  private Long mId;

  private String mName;

  private BigDecimal mPrice;

  public Long getId() {
    return mId;
  }

  public void setId(final Long id) {
    mId = id;
  }

  public String getName() {
    return mName;
  }

  public void setName(final String name) {
    mName = name;
  }

  public BigDecimal getPrice() {
    return mPrice;
  }

  public void setPrice(final BigDecimal price) {
    mPrice = price;
  }

}