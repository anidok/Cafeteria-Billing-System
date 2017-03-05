package cafeteria.ui.orders;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableCellRenderer;

import cafeteria.core.Customer;
import cafeteria.core.Order;
import cafeteria.dao.OrderDAO;
import cafeteria.ui.BillingApp;

import javax.swing.JLabel;
import java.awt.Font;
import java.sql.SQLException;
import java.util.List;

import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class OrderHistoryDialog extends JDialog {
	private BillingApp frame;
	
	private OrderDAO orderDAO;
	private JTable orderHistoryTable;
	private Customer customer;
	
	private JPanel buttonPanel;
	
	public OrderHistoryDialog(BillingApp frame, OrderDAO orderDAO, Customer customer){
		this();
		this.frame = frame;
		this.orderDAO = orderDAO;
		this.customer = customer;
	}

	public OrderHistoryDialog() {
		setTitle("Hungry Hobbit Cafeteria");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		
		JLabel lblPastOrderDetails = new JLabel("Past Order Details");
		lblPastOrderDetails.setHorizontalAlignment(SwingConstants.CENTER);
		lblPastOrderDetails.setFont(new Font("Sylfaen", Font.BOLD, 18));
		getContentPane().add(lblPastOrderDetails, BorderLayout.NORTH);
		
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		orderHistoryTable = new JTable();
		scrollPane.setViewportView(orderHistoryTable);		
		
		buttonPanel = new JPanel();
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				frame.setVisible(true);
				
			}
		});
		buttonPanel.add(btnBack);		
	}
	
	public void setTableModel() throws SQLException{
		List<Order> list = orderDAO.getOrderHistory(customer);
		OrderHistoryTableModel tableModel = new OrderHistoryTableModel(list);
		orderHistoryTable.setModel(tableModel);
		alignTable();
	}
	
	private void alignTable(){
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		for(int i=0; i< orderHistoryTable.getColumnCount(); i++){
			orderHistoryTable.getColumnModel().getColumn(i).setCellRenderer( centerRenderer );
		}
	}

}
