import com.datastax.driver.core.Row;


public class ProductDetail {
	private String product_id;
	private int seconds;
	private long orders;
	private long revenue;
	private long profit;
	
	public ProductDetail(Row row){
		this.setProduct_id(row.getString("product_id"));
		this.setSeconds(row.getInt("seconds"));
		this.setOrders(row.getLong("orders"));
		this.setRevenue(row.getLong("revenue"));
		this.setProfit(row.getLong("profit"));		
	}
	
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public int getSeconds() {
		return seconds;
	}
	public void setSeconds(int seconds) {
		this.seconds = seconds;
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
	public long getProfit() {
		return profit;
	}
	public void setProfit(long profit) {
		this.profit = profit;
	}
	
}
