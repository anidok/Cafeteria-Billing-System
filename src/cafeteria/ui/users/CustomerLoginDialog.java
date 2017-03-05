package cafeteria.ui.users;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import cafeteria.core.Customer;
import cafeteria.dao.CustomerDAO;
import cafeteria.dao.FoodDAO;
import cafeteria.dao.OrderDAO;
import cafeteria.ui.BillingApp;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;


@SuppressWarnings("serial")
public class CustomerLoginDialog extends JDialog {
	
	private FoodDAO foodDAO;
	private CustomerDAO customerDAO;
	private OrderDAO orderDAO;

	private final JPanel contentPanel = new JPanel();
	private JLabel lblWelcomeToCafeteria;
	private JPanel credentialpanel;
	private JTextField emailTextField;
	private JPasswordField passwordField;

	public CustomerLoginDialog(CustomerDAO customerDAO, FoodDAO foodDAO, OrderDAO orderDAO){
		this();
		this.customerDAO = customerDAO;
		this.foodDAO = foodDAO;
		this.orderDAO = orderDAO;
		
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
		
	}	

	public CustomerLoginDialog() {
		//this.setResizable(false);
		
		setTitle("Hungry Hobbit Cafeteria - Log In");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel lblpanel = new JPanel();
			FlowLayout flowLayout = (FlowLayout) lblpanel.getLayout();
			flowLayout.setVgap(10);
			contentPanel.add(lblpanel, BorderLayout.NORTH);
			{
				lblWelcomeToCafeteria = new JLabel("Welcome to  Cafeteria Billing System");
				lblWelcomeToCafeteria.setFont(new Font("Sylfaen", Font.BOLD, 16));
				lblpanel.add(lblWelcomeToCafeteria);
			}
		}
		{
			credentialpanel = new JPanel();
			contentPanel.add(credentialpanel, BorderLayout.CENTER);
			credentialpanel.setLayout(null);
			
			JLabel lblEmail = new JLabel("E-mail");
			lblEmail.setFont(new Font("Sylfaen", Font.PLAIN, 15));
			lblEmail.setBounds(21, 19, 46, 14);
			credentialpanel.add(lblEmail);
			
			emailTextField = new JTextField();
			emailTextField.setBounds(86, 16, 180, 20);
			credentialpanel.add(emailTextField);
			emailTextField.setColumns(30);		
			
			JLabel lblPassword = new JLabel("Password");
			lblPassword.setFont(new Font("Sylfaen", Font.PLAIN, 15));
			lblPassword.setBounds(10, 50, 70, 14);
			credentialpanel.add(lblPassword);
			
			JButton btnLogIn = new JButton("Log in");
			btnLogIn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
		
					performLogin();
				}
			});
			btnLogIn.setBounds(177, 78, 89, 23);
			credentialpanel.add(btnLogIn);
			
			JLabel lblNewUser = new JLabel("New user ?");
			lblNewUser.setFont(new Font("Sylfaen", Font.PLAIN, 15));
			lblNewUser.setBounds(34, 121, 80, 14);
			credentialpanel.add(lblNewUser);
			
			JButton btnSignUp = new JButton("Sign up");
			btnSignUp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					CustomerSignUpDialog dialog = new CustomerSignUpDialog(CustomerLoginDialog.this, customerDAO);
					dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
					
					//dissolve current dialog and create new dialog
					dispose();
					//setVisible(false);    can use this also but dispose() is preferred to release memory
					dialog.setVisible(true);
					
				}
			});
			btnSignUp.setBounds(110, 117, 89, 23);
			credentialpanel.add(btnSignUp);
			
			passwordField = new JPasswordField();
			passwordField.setBounds(86, 47, 180, 20);
			credentialpanel.add(passwordField);
		
		}
	}
	
	private void performLogin(){
		String email = emailTextField.getText();
		String plainTextPassword = new String(passwordField.getPassword());
		
		try {
			Customer customer = customerDAO.searchCustomer(email);	//if not NULL, customer records found in  database
			if(customer == null){
				JOptionPane.showMessageDialog(CustomerLoginDialog.this, "Customer not found", "OOPS!",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			//Authentication check
			boolean check = customerDAO.authenticate(plainTextPassword, customer);
			if(check){
				System.out.println("Customer authenticated");
				emailTextField.setText("");
				passwordField.setText("");
				BillingApp frame = new BillingApp(CustomerLoginDialog.this, orderDAO, foodDAO, customer);
				frame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);;
				dispose();
				frame.setVisible(true);
			}
			else{
				JOptionPane.showMessageDialog(CustomerLoginDialog.this, "Invalid password!", "Invalid login",
						JOptionPane.ERROR_MESSAGE);
				return;
			}			
		} 
		catch (SQLException e) {
			JOptionPane.showMessageDialog(CustomerLoginDialog.this, "Error logging in: "
					+ e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
