import com.datastax.driver.core.Row;


public class Product {
	private String product_id;
	private String product_name;
	private String recommendation;
	private Double unit_price;
	private String vendor;
	
	public Product(Row row){
		this.setProduct_id(row.getString("product_id"));
		this.setProduct_name(row.getString("product_name"));
		this.setRecommendation(row.getString("recommendation"));
		this.setUnit_price(row.getDouble("unit_price"));
		this.setVendor(row.getString("vendor"));
	}
	
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getRecommendation() {
		return recommendation;
	}
	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
	}
	public Double getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(Double unit_price) {
		this.unit_price = unit_price;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	
}
