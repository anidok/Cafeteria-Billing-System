package cafeteria.ui.users;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import cafeteria.core.Customer;
import cafeteria.dao.CustomerDAO;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class CustomerSignUpDialog extends JDialog {
	
	private CustomerLoginDialog loginDialog;
	
	private CustomerDAO customerDAO;

	private final JPanel contentPanel = new JPanel();
	private JTextField firstNameTextField;
	private JTextField lastNameTextField;
	private JTextField emailTextField;
	private JLabel lblPassword;
	private JPasswordField passwordField;
	private JPasswordField cnfPasswordField;
	private JButton btnCreateAccount;

	public CustomerSignUpDialog(CustomerLoginDialog dialog, CustomerDAO customerDAO){
		this();
		this.customerDAO = customerDAO;
		
		//It"ll be used to make the dialog visible again
		this.loginDialog = dialog;
	}
	
	public CustomerSignUpDialog() {
		addWindowListener(new WindowAdapter() {

			  @Override
			  public void windowClosing(WindowEvent we)
			  { 
			    int PromptResult = JOptionPane.showConfirmDialog(null, "Exit application ?",
			    		"Confirm exit", JOptionPane.OK_CANCEL_OPTION);
			    if(PromptResult== JOptionPane.OK_OPTION)
			    {
			      System.exit(0);          
			    }
			  }
			});
		
		setTitle("Hungry Hobbit Cafeteria - Sign Up");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblFirstName = new JLabel("First Name");
		lblFirstName.setHorizontalAlignment(SwingConstants.CENTER);
		lblFirstName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblFirstName.setBounds(21, 11, 70, 14);
		contentPanel.add(lblFirstName);
		
		
		firstNameTextField = new JTextField();
		firstNameTextField.setBounds(112, 9, 190, 20);
		contentPanel.add(firstNameTextField);
		firstNameTextField.setColumns(10);
		
		
		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setHorizontalAlignment(SwingConstants.CENTER);
		lblLastName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblLastName.setBounds(21, 42, 70, 14);
		contentPanel.add(lblLastName);
		
		
		lastNameTextField = new JTextField();
		lastNameTextField.setColumns(10);
		lastNameTextField.setBounds(112, 40, 190, 20);
		contentPanel.add(lastNameTextField);
		
		
		JLabel lblEmail = new JLabel("E-mail");
		lblEmail.setHorizontalAlignment(SwingConstants.CENTER);
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEmail.setBounds(21, 73, 70, 14);
		contentPanel.add(lblEmail);
		
		
		emailTextField = new JTextField();
		emailTextField.setColumns(10);
		emailTextField.setBounds(112, 71, 190, 20);
		contentPanel.add(emailTextField);
				
		lblPassword = new JLabel("Password");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPassword.setBounds(21, 106, 70, 14);
		contentPanel.add(lblPassword);
		
		
		passwordField = new JPasswordField();
		passwordField.setBounds(112, 102, 190, 20);
		contentPanel.add(passwordField);
		
		JLabel lblConfirmPassword = new JLabel("Confirm Password");
		lblConfirmPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblConfirmPassword.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblConfirmPassword.setBounds(10, 135, 100, 14);
		contentPanel.add(lblConfirmPassword);
		
		cnfPasswordField = new JPasswordField();
		cnfPasswordField.setBounds(112, 133, 190, 20);
		contentPanel.add(cnfPasswordField);
		
		btnCreateAccount = new JButton("Create Account");
		btnCreateAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				createCustomer();				
			}
		});
		btnCreateAccount.setFont(new Font("Lucida Fax", Font.BOLD, 13));
		btnCreateAccount.setBounds(21, 176, 158, 32);
		contentPanel.add(btnCreateAccount);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				loginDialog.setVisible(true);
			}
		});
		btnBack.setFont(new Font("Lucida Fax", Font.BOLD, 13));
		btnBack.setBounds(213, 176, 89, 29);
		contentPanel.add(btnBack);
		
		//Adding window event to handle the operations performed as the signUo Dialog is closed
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				
				//display the login Dialog.
				loginDialog.setVisible(true);
			}
		});
	}
	
	private void createCustomer(){
		String firstName = firstNameTextField.getText();
		String lastName = lastNameTextField.getText();
		String email = emailTextField.getText();
		String password = new String(passwordField.getPassword());
		String cnfPassword = new String(cnfPasswordField.getPassword());
		
		if (!password.equals(cnfPassword)) {
			JOptionPane.showMessageDialog(this,
					"Passwords do not match.", "Error",
					JOptionPane.ERROR_MESSAGE);				
			return;
		}
		
		Customer customer = new Customer(firstName, lastName, email, password);
		
		try {
			customerDAO.addCustomer(customer);
			JOptionPane.showMessageDialog(loginDialog, "Customer created successfully",
					"Success!", JOptionPane.INFORMATION_MESSAGE);
			setVisible(false);
			loginDialog.setVisible(true);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(CustomerSignUpDialog.this, 
					"Error creating account" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		
	}
}
