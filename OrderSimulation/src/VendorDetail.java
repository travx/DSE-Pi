import com.datastax.driver.core.Row;


public class VendorDetail {
	private String vendor;
	private int seconds;
	private long orders;
	private long revenue;
	private long profit;
	
	public VendorDetail(Row row){
		this.setVendor(row.getString("vendor"));
		this.setSeconds(row.getInt("seconds"));
		this.setOrders(row.getLong("orders"));
		this.setRevenue(row.getLong("revenue"));
		this.setProfit(row.getLong("profit"));
	}
	
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
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
