import com.datastax.driver.core.Row;


public class ProductSummary {
	private String vendor;
	private String product_id;
	private long profit;
	
	public ProductSummary(Row row){
		this.setProduct_id(row.getString("product_id"));
		this.setProfit(row.getLong("profit"));
		this.setVendor(row.getString("vendor"));
	}
	
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public long getProfit() {
		return profit;
	}
	public void setProfit(long profit) {
		this.profit = profit;
	}
	
}
