import com.datastax.driver.core.Row;


public class ProductInfo {
	private String product_id;
	private String product_name;
	private String recommendation;
	private String vendor;
	private Double cost;
	private Double popularity;
	private Double retail_price;
	private Double sale_price;
	
	public ProductInfo(Row row){
		this.setCost(row.getDouble("cost"));
		this.setPopularity(row.getDouble("popularity"));
		this.setProduct_id(row.getString("product_id"));
		this.setProduct_name(row.getString("product_name"));
		this.setRecommendation(row.getString("recommendation"));
		this.setRetail_price(row.getDouble("retail_price"));
		this.setSale_price(row.getDouble("sale_price"));
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
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public Double getPopularity() {
		return popularity;
	}
	public void setPopularity(Double popularity) {
		this.popularity = popularity;
	}
	public Double getRetail_price() {
		return retail_price;
	}
	public void setRetail_price(Double retail_price) {
		this.retail_price = retail_price;
	}
	public Double getSale_price() {
		return sale_price;
	}
	public void setSale_price(Double sale_price) {
		this.sale_price = sale_price;
	}
	
}
