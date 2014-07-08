import com.datastax.driver.core.Row;


public class ProductSummary {
	private String vendor;
	private String product_id;
	private long orders;
	private long profit;
	private long revenue;

	public ProductSummary(Row row){
		this.setProduct_id(row.getString("product_id"));
		this.setProfit(row.getLong("profit"));
		this.setVendor(row.getString("vendor"));
		this.setOrders(row.getLong("orders"));
		this.setRevenue(row.getLong("revenue"));
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
	
	public long getOrders() {
		return orders;
	}

	public void setOrders(long orders) {
		this.orders = orders;
	}

	public long getRevenue() {
		return revenue;
	}

	public void setRevenue(long revenue) {
		this.revenue = revenue;
	}
	
}
