package com.skillbox.boxes.ejb.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

@Stateless
public class ProductEJB {
  public List<ProductBean> findAll() {
    final List<ProductBean> products = new ArrayList<ProductBean>();
    final ProductBean product1 = new ProductBean();
    product1.setName("Widget");
    product1.setPrice(new BigDecimal("1.99"));
    product1.setId(1l);

    final ProductBean product2 = new ProductBean();
    product2.setName("Gadget");
    product2.setPrice(new BigDecimal("12.99"));
    product2.setId(2l);

    products.add(product2);
    products.add(product2);
    return products;
  }

  public BigDecimal priceTotal() {
    BigDecimal total = new BigDecimal("0.00");

    final List<ProductBean> all = findAll();

    for (final ProductBean product : all) {
      total = total.add(product.getPrice());
    }

    return total;
  }
}