package cafeteria.ui.orders;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import cafeteria.core.FoodItem;

@SuppressWarnings("serial")
public class CartTableModel extends AbstractTableModel {

	public static final int OBJECT_COL = -1;
	private static final int PRODUCT_SERIAL = 0;
	private static final int PRODUCT_CODE_COL = 1;
	private static final int PRODUCT_NAME_COL = 2;
	private static final int QUANTITY_COL = 3;
	private static final int PRODUCT_SUB_TOTAL = 4;
	
	private String columnNames[] = {"Product Serial", "Product Code", "Product Name", "Quantity", "Product SubTotal"};
	
	private List<FoodItem> cartItems;
	
	public CartTableModel(List<FoodItem> list){
		cartItems = list;
	}
	
	public int getColumnCount(){
		return columnNames.length;
	}
	
	public int getRowCount(){		
		return cartItems.size();
	}
	
	public String getColumnName(int col){
		return columnNames[col];
	}
	
	public Object getValueAt(int row, int col){
		FoodItem tempFood = cartItems.get(row);
		switch(col){		
			case PRODUCT_SERIAL:
				return row+1;
			case PRODUCT_CODE_COL:
				return tempFood.getProductCode();
			case PRODUCT_NAME_COL:
				return tempFood.getProductName();
			case QUANTITY_COL:
				return tempFood.getQuantity();
			case PRODUCT_SUB_TOTAL:
				return tempFood.getQuantity() * tempFood.getPrice();
			case OBJECT_COL:
				return tempFood;
			default:
				return tempFood.getProductName();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int col){
		return getValueAt(0, col).getClass();
	}
}
