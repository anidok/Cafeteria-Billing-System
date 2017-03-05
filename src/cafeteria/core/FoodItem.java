package cafeteria.core;


public class FoodItem {
	private int productCode;
	private String productName;
	private String productCategory;
	private int price;
	private int quantity;
	
	public FoodItem(int productCode, String productName, String productCategory, int price, int quantity){
		
		this.productCode = productCode;
		this.productName = productName;
		this.productCategory = productCategory;
		this.price = price;
		this.quantity = quantity;
	}
	
	public int getProductCode(){
		return productCode;
	}
	
	public void setProductCode(int productCode){
		this.productCode = productCode;
	}
	
	public void setQuantity(int quantity){
		this.quantity = quantity;
	}
	
	public int getQuantity(){
		return quantity;
	}

	public String getProductName(){
		return productName;
	}
	
	public void setProductName(String productName){
		this.productName = productName;
	}
	
	public String getProductCategory(){
		return productCategory;
	}
	
	public void setProductCateory(String productCateory){
		this.productCategory = productCateory;
	}
	
	public int getPrice(){
		return price;
	}
	
	public void setPrice(int price){
		this.price = price;
	}
	
	@Override
	public String toString(){
		return String.format("Product [Code= %s, Name= %s, Category= %s, Price= %s]",
				 productCode, productName, productCategory, price);
	}

}
