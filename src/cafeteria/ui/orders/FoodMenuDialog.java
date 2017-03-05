package cafeteria.ui.orders;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import cafeteria.core.Customer;
import cafeteria.core.FoodItem;
import cafeteria.dao.FoodDAO;
import cafeteria.ui.BillingApp;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class FoodMenuDialog extends JDialog {
	
	private BillingApp frame;
	
	private FoodDAO foodDAO;
	private Customer customer;

	private final JPanel contentPanel = new JPanel();
	JScrollPane tabelscrollPane;
	private JTextField textField;
	
	private JTable foodtable;
	private FoodTabelModel tableModel;
	
	JButton btnAddToCart;
	JButton btnBack;
	JButton btnProceedToCheckout;


	public FoodMenuDialog(BillingApp frame, FoodDAO foodDAO, Customer customer) {
		
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
			      System.exit(0);          
			    }
			  }
			});
		
		setTitle("Hungry Hobbit Cafeteria - Order Menu");		
		setBounds(100, 100, 680, 520);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel centerpanel = new JPanel();
		contentPanel.add(centerpanel, BorderLayout.CENTER);
		centerpanel.setLayout(new BorderLayout(0, 0));
		
		JPanel addpanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) addpanel.getLayout();
		flowLayout.setVgap(10);
		flowLayout.setAlignment(FlowLayout.LEFT);
		centerpanel.add(addpanel, BorderLayout.SOUTH);
		
		JLabel lblProductQuantity = new JLabel("Product Quantity");
		lblProductQuantity.setHorizontalAlignment(SwingConstants.CENTER);
		addpanel.add(lblProductQuantity);
		
		Component horizontalStrut = Box.createHorizontalStrut(15);
		addpanel.add(horizontalStrut);
		
		textField = new JTextField();
		addpanel.add(textField);
		textField.setColumns(10);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(35);
		addpanel.add(horizontalStrut_2);
		
		btnAddToCart = new JButton("Add to Cart");
		btnAddToCart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
				try{
					int row = foodtable.getSelectedRow();
					if(row<0){
						JOptionPane.showMessageDialog(FoodMenuDialog.this, "You must select a food item",
								"Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					//System.out.println(row);					
					//System.out.println(tempFoodItem);
					
					FoodItem tempFoodItem = (FoodItem)foodtable.getValueAt(row, FoodTabelModel.OBJECT_COL);					
					String s = textField.getText();
					
					/*
					 if(s == "" || s.trim() == ""){
					 
						JOptionPane.showMessageDialog(FoodMenuDialog.this, "You must select a food item",
								"Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					*/
					
					int quantity = Integer.parseInt(s);
					//System.out.println(quantity);
					foodDAO.updateQuantity(tempFoodItem, quantity);
					
					refreshFoodItemView();
					JOptionPane.showMessageDialog(FoodMenuDialog.this, "Food item added to cart.",
							"Cart", JOptionPane.INFORMATION_MESSAGE);				
				}
				catch(Exception e){
					JOptionPane.showMessageDialog(FoodMenuDialog.this, "Error adding product to cart: "
							+ e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				
			
			}
		});
		addpanel.add(btnAddToCart);
		
		JPanel toplabelpanel = new JPanel();
		centerpanel.add(toplabelpanel, BorderLayout.NORTH);
		toplabelpanel.setLayout(new GridLayout(3, 1, 0, 0));
		
		JLabel lblFoodOrderMenu = new JLabel("Food Order Menu");
		lblFoodOrderMenu.setHorizontalAlignment(SwingConstants.CENTER);
		lblFoodOrderMenu.setFont(new Font("Tahoma", Font.BOLD, 15));
		toplabelpanel.add(lblFoodOrderMenu);
		
		Component rigidArea = Box.createRigidArea(new Dimension(20, 5));
		toplabelpanel.add(rigidArea);
		
		JLabel lblPleaseSelectThe = new JLabel("Please select the item and click Add");
		lblPleaseSelectThe.setHorizontalAlignment(SwingConstants.CENTER);
		lblPleaseSelectThe.setFont(new Font("Tahoma", Font.BOLD, 12));
		toplabelpanel.add(lblPleaseSelectThe);
		
		tabelscrollPane = new JScrollPane();
		centerpanel.add(tabelscrollPane, BorderLayout.CENTER);
		
		foodtable = new JTable();
		
		
		try{
			tableModel = new FoodTabelModel(foodDAO.getAllFoodItems());
			foodtable.setModel(tableModel);
			
			//tabelscrollPane.add(foodtable.getTableHeader(), BorderLayout.NORTH);
			tabelscrollPane.setViewportView(foodtable);
			alignTable();
			
			
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		JPanel bottombtnpanel = new JPanel();
		contentPanel.add(bottombtnpanel, BorderLayout.SOUTH);
		bottombtnpanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				frame.setVisible(true);
			}
		});
		bottombtnpanel.add(btnBack);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(180);
		bottombtnpanel.add(horizontalStrut_1);
		
		btnProceedToCheckout = new JButton("Proceed to Checkout");
		btnProceedToCheckout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
				try {
					boolean check = foodDAO.isEmptyQuantityColumn();
					if(!check){
						JOptionPane.showMessageDialog(FoodMenuDialog.this, "Your cart is empty",
								"Can't proceed", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				CheckoutDialog dialog = new CheckoutDialog(FoodMenuDialog.this, frame, foodDAO, customer);
				dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
				dispose();	//dissolve the current window
				dialog.setVisible(true);
				dialog.setNetAmountLabel();
			}
		});
		bottombtnpanel.add(btnProceedToCheckout);
	}
	
	private void alignTable(){
		//Setting renderer for table cells alignment
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		
		//Centering all columns containing String Data
		foodtable.setDefaultRenderer(String.class, centerRenderer);

		//Centering particular columns
		foodtable.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
		foodtable.getColumnModel().getColumn(3).setCellRenderer( centerRenderer );
		foodtable.getColumnModel().getColumn(4).setCellRenderer( centerRenderer );
		// can use this also
		//foodtable.setDefaultRenderer(foodtable.getColumnClass(0), centerRenderer);
		
	}
	
	public void refreshFoodItemView(){		
		try {
			tableModel = new FoodTabelModel(foodDAO.getAllFoodItems());
			foodtable.setModel(tableModel);
			alignTable();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error : " + e, "Error refreshing table view",
					JOptionPane.ERROR_MESSAGE);
		}		
	}

}
