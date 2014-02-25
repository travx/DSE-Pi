import com.datastax.driver.core.Row;


public class VendorSummary {
	private String vendor;
	private long orders;
	private long revenue;
	private long profit;
	
	public VendorSummary(Row row){
		this.setVendor(row.getString("vendor"));
		this.setOrders(row.getLong("orders"));
		this.setProfit(row.getLong("profit"));
		this.setRevenue(row.getLong("revenue"));		
	}
	
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
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
