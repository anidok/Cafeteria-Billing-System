# Cafeteria Billing System

*Cafeteria Billing System is a simple desktop application for calculating the bill of food products ordered by the customers and store the order details in database. The objective of this project was just to understand and implementing the concept of databases in Java. It is written is `Java`, along with `Java-Swing`, `jasypt` (for password encryption) and `sql`.* 

## Running the application

### Steps
* Install `MYSQL Server` and `MYSQL Workbench` (not necessary) in your system.
* To create the database, switch to `bin` directory of `MYSQL Server` and execute the following commands:

````
mysql -u user -p -h hostname;
create database cafe;
use cafe;
source table-setup.sql;
````
`table-setup.sql` is present in `sql` directory. It is preferred to provide the full path of the sql file in import command (i.e, *source path\table-setup.sql*).

**Note**

*`cafe` is the schema name used in the codebase. So, if you want to give your own schema name, then you need to provide that name is `cafe.properties` also.*

`Cafeteria Billing System` has a few dependencies:
* jasypt
* jdbc-connector

The above libraries have been provided in the `lib` directory.

* Import the project in eclipse or any other IDE.
* Add `jasypt` and `mysql-connector` jar files provided in `lib` directory to build path of the project.
* Run src/cafeteria/ui/BillingApp.java.
