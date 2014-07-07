package com.skillbox.boxes.ejb.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProductEJBTest {
  private static EJBContainer ejbContainer;
  private static Context ctx;

  @BeforeClass
  public static void setUp() {
    ejbContainer = EJBContainer.createEJBContainer();
    ctx = ejbContainer.getContext();
  }

  @AfterClass
  public static void tearDown() {
    ejbContainer.close();
  }

  @Test
  public void testFindAll() {
    try {
      final ProductEJB productEJB = (ProductEJB) ctx
          .lookup("java:global/classes/ProductEJB!com.skillbox.boxes.ejb.bean.ProductEJB");
      assertNotNull(productEJB);
      final List<ProductBean> products = productEJB.findAll();
      assertNotNull(products);
      assertEquals(2, products.size());
    } catch (final NamingException e) {
      throw new AssertionError(e);
    }
  }

  @Test
  public void testTotalPrice() {
    try {
      final ProductEJB productEJB = (ProductEJB) ctx
          .lookup("java:global/classes/ProductEJB!com.skillbox.boxes.ejb.bean.ProductEJB");
      assertNotNull(productEJB);

      final BigDecimal total = productEJB.priceTotal();
      assertEquals(new BigDecimal("25.98"), total);
    } catch (final NamingException e) {
      throw new AssertionError(e);
    }
  }
}