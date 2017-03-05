package cafeteria.ui.orders;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import cafeteria.core.Customer;
import cafeteria.core.FoodItem;
import cafeteria.dao.FoodDAO;
import cafeteria.ui.BillingApp;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.sql.SQLException;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class CheckoutDialog extends JDialog {
	
	private FoodMenuDialog foodMenuDialog;
	private BillingApp frame;
	
	private Customer customer;
	private FoodDAO foodDAO;

	private final JPanel contentPanel = new JPanel();
	private JTable cartTable;
	private JButton btnDelete;
	private JButton btnBack;
	private JButton btnProceed;
	private JLabel netAmountLabel;
	private int netAmount;
	
	private CartTableModel tableModel ;


	public CheckoutDialog(FoodMenuDialog foodMenuDialog, BillingApp frame, FoodDAO foodDAO, Customer customer) {
		
		this.foodMenuDialog = foodMenuDialog;
		this.frame = frame;
		this.foodDAO = foodDAO;
		this.customer = customer;
		
		addWindowListener(new WindowAdapter() {

			  @Override
			  public void windowClosing(WindowEvent we)
			  { 
			    int PromptResult = JOptionPane.showConfirmDialog(null, "Exit application ?",
			    		"Confirm exit", JOptionPane.OK_CANCEL_OPTION);
			    if(PromptResult== JOptionPane.OK_OPTION)
			    {
			    	foodDAO.vacateQuantityColumn();
			    	System.exit(0);          
			    }
			  }
			});
		
		setTitle("Hungry Hobbit Cafeteria - Checkout");
		setBounds(100, 100, 640, 380);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel topLabelPanel = new JPanel();
		contentPanel.add(topLabelPanel, BorderLayout.NORTH);
		topLabelPanel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("Checkout Section");
		lblNewLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		lblNewLabel.setFont(new Font("Sylfaen", Font.BOLD, 16));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		topLabelPanel.add(lblNewLabel, BorderLayout.NORTH);
						
		JLabel lblNewLabel_1 = new JLabel("Please review your order");
		lblNewLabel_1.setFont(new Font("Californian FB", Font.PLAIN, 14));
		topLabelPanel.add(lblNewLabel_1, BorderLayout.SOUTH);			
			
		Component verticalStrut = Box.createVerticalStrut(5);
		topLabelPanel.add(verticalStrut, BorderLayout.CENTER);		
		
		JPanel centerPanel = new JPanel();
		contentPanel.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		centerPanel.add(scrollPane, BorderLayout.CENTER);
		
		cartTable = new JTable();
		setCartTableModel();
		scrollPane.setViewportView(cartTable);
				
		JPanel deletePanel = new JPanel();
		centerPanel.add(deletePanel, BorderLayout.SOUTH);
		deletePanel.setLayout(new BorderLayout(0, 20));
		
		JLabel lblSelectTheProduct = new JLabel("Select the product and click Delete button to delete from cart");
		deletePanel.add(lblSelectTheProduct, BorderLayout.WEST);		
		
		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteItem();
			}
		});
		deletePanel.add(btnDelete, BorderLayout.EAST);
							
		JPanel bottomPanel = new JPanel();
		contentPanel.add(bottomPanel, BorderLayout.SOUTH);
		bottomPanel.setLayout(new BorderLayout(0, 0));

		JPanel bottomButtonPanel = new JPanel();
		bottomPanel.add(bottomButtonPanel, BorderLayout.SOUTH);
		bottomButtonPanel.setLayout(new BorderLayout(0, 0));		
		btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				foodMenuDialog.refreshFoodItemView();
				foodMenuDialog.setVisible(true);
			}
		});
		bottomButtonPanel.add(btnBack, BorderLayout.WEST);		
		
		btnProceed = new JButton("Place Order");
		btnProceed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				placeOrder();
			}
		});
		bottomButtonPanel.add(btnProceed, BorderLayout.EAST);		
		
		JPanel netAmountPanel = new JPanel();
		bottomPanel.add(netAmountPanel, BorderLayout.NORTH);
		netAmountPanel.setLayout(new BorderLayout(0, 0));		
		
		netAmountLabel = new JLabel("Net Amount: ");
		netAmountLabel.setFont(new Font("Garamond", Font.BOLD, 16));
		netAmountPanel.add(netAmountLabel, BorderLayout.EAST);	
		
		Component verticalStrut1;
		verticalStrut1 = Box.createVerticalStrut(20);
		netAmountPanel.add(verticalStrut1, BorderLayout.SOUTH);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		netAmountPanel.add(verticalStrut_1, BorderLayout.NORTH);		
		
	}
	
	private void alignTable(){
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		for(int i=0; i< cartTable.getColumnCount(); i++){
			cartTable.getColumnModel().getColumn(i).setCellRenderer( centerRenderer );
		}
	}
	
	public void setNetAmountLabel(){
		try {
			netAmount = foodDAO.getNetAmount();
			netAmountLabel.setText("Net Amount: " + netAmount);
		} 
		catch (SQLException e) {
			JOptionPane.showMessageDialog(CheckoutDialog.this, "Error setting net Amount",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void setCartTableModel(){
		try {
			tableModel = new CartTableModel(foodDAO.getCartItems());
			cartTable.setModel(tableModel);
			alignTable();
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(CheckoutDialog.this, "Error setting Table Model: " + e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	private void deleteItem(){
		int row = cartTable.getSelectedRow();
		if(row<0){
			JOptionPane.showMessageDialog(CheckoutDialog.this, "You must select an item before deleting.",
					"OOPS!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		FoodItem temp = (FoodItem) cartTable.getValueAt(row, CartTableModel.OBJECT_COL);
		try {
			
			foodDAO.setQuantityToNull(temp);
			setCartTableModel();
			setNetAmountLabel();
			JOptionPane.showMessageDialog(CheckoutDialog.this, "Item removed from cart.",
					"Cart", JOptionPane.INFORMATION_MESSAGE);
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(CheckoutDialog.this, "Error deleteing item from cart: " + e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	private void placeOrder(){
		if(cartTable.getRowCount()<1){
			JOptionPane.showMessageDialog(CheckoutDialog.this, "Your cart is empty",
					"Can't place order", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		try {
			int orderId = foodDAO.addOrder(customer, netAmount);	//insert the order details in order menu.
			foodDAO.vacateQuantityColumn();
			JOptionPane.showMessageDialog(CheckoutDialog.this, "Order id: " +orderId,
					"Order placed successfully", JOptionPane.INFORMATION_MESSAGE);
			
			dispose();	//dissolve current window and show main window.
			frame.setVisible(true);
			System.out.println("Order placed successfully");
			
		}
		catch (SQLException e) {
			JOptionPane.showMessageDialog(CheckoutDialog.this, "Error while placing order:"
					+ e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	
		
	}
}
