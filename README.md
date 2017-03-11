# Cafeteria Billing System

*Cafeteria Billing System is a simple desktop application for calculating the bill of food products ordered by the customers and store the order details in database. The objective of this project was just to understand and implementing the concept of databases in Java. It is written is `Java`, alongwith `Java-Swing`, `jasypt` (for password encryption) and `sql`.* 

## Running the application
`Cafeteria Billing System` has a few dependencies:
* jasypt
* jdbc-connector

The above libraries have been provided in the `lib` directory.

### Steps
* Install `MYSQL Server` and `MYSQL Workbench` (not necessary) in your system.
* You can create your database using following commands:

````
mysql -u user -p -h hostname;
create database cafe;
use cafe;
source table-setup.sql;
````
`table-setup.sql` is present in `sql` directory. It is preferred to provide the full path of the sql file in import command (i.e, `source path\table-setup.sql`).

