package cafeteria.dao;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import cafeteria.core.Customer;
import cafeteria.util.PasswordUtils;

public class CustomerDAO {

	private Connection myConn;
	
	public CustomerDAO() throws Exception{
		
		//get db properties
		Properties props = new Properties();
		props.load(new FileInputStream("cafe.properties"));
		
		String user = props.getProperty("user");
		String password = props.getProperty("password");
		String dburl = props.getProperty("dburl");
		
		myConn = DriverManager.getConnection(dburl, user, password);
	
		System.out.println("Customer DAO - DB connection succesful to " + dburl);
		
	}
	
	public void addCustomer(Customer customer) throws SQLException {
		PreparedStatement myStmt = null;
		try{
			
			myStmt = myConn.prepareStatement("insert into customers"
					+"(first_name, last_name, email, password)"
					+ " values (?, ?, ?, ?)");
			myStmt.setString(1, customer.getFirstName());
			myStmt.setString(2, customer.getLastName());
			myStmt.setString(3, customer.getEmail());
			
			String encryptedPassword = PasswordUtils.encryptPassword(customer.getPassword());
			myStmt.setString(4, encryptedPassword);
			
			myStmt.executeUpdate();			
		}
		finally{
			if(myStmt!= null)
				myStmt.close();
		}
	}
	
	public boolean authenticate(String plainTextPassword, Customer customer){
		String encryptedPassword = customer.getPassword();
		return PasswordUtils.checkPassword(plainTextPassword, encryptedPassword);
	}
	
	public Customer searchCustomer(String email) throws SQLException{
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		try{
	
			myStmt = myConn.prepareStatement("select * from customers where email=?");
			myStmt.setString(1, email);
			myRs = myStmt.executeQuery();
			
			//Statement.executeQuery() never returns null if resultset is empty.
			if(!myRs.next()){
				return null;
			}
			else{
				Customer customer = convertRowToCustomer(myRs);
				return customer;
			}
		}
		finally{
			if(myRs!= null)
				myRs.close();
			if(myStmt!= null)
				myStmt.close();
		}
	}
	
	private Customer convertRowToCustomer(ResultSet myRs) throws SQLException{
		int id = myRs.getInt("id");
		String firstName = myRs.getString("first_name");
		String lastName = myRs.getString("last_name");
		String email = myRs.getString("email");
		String encryptedPassword = myRs.getString("password");
		Customer customer = new Customer(id, firstName, lastName, email, encryptedPassword);
		
		return customer;
	}
}
