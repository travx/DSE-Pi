import com.datastax.driver.core.Row;


public class Summary {
	private String metric;
	private long orders;
	private long revenue;
	private long profit;

	public Summary(Row row){
		this.setOrders(row.getLong("orders"));
		this.setProfit(row.getLong("profit"));
		this.setRevenue(row.getLong("revenue"));
		this.setMetric(row.getString("metric"));
	}
	
	public String getMetric() {
		return metric;
	}

	public void setMetric(String metric) {
		this.metric = metric;
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
