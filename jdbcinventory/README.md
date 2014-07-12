JDBC Inventory
--------------

A sample library which uses [H2](http://www.h2database.com/html/main.html) as a database backend for an inventory system demo

The Tests Use JDBC to establish an in-memory H2 database to test the InventoryRepository demo code

The Tests Use DBUnit to create and populate the database instance

* The schema is defined here:
  ``src/test/sql/inventory_schema.sql``

* The Test Data is defined here:
  ``src/test/sql/inventory_data.xml``

