import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;


public class ProductPrice {
	private String product_id;
	private String product_name;
	private String recommendation;
	private String vendor;
	private Double cost;
	private Double popularity;
	private Double retail_price;
	private Double sale_price;
	private Session session;
	
	
	public ProductPrice(Session session){
		this.session = session;
	}
	
	public ProductPrice(Row row){
		refresh(row);
	}
	
	public void refresh(Row row){
		this.setCost(row.getDouble("cost"));
		this.setPopularity(row.getDouble("popularity"));
		this.setProduct_id(row.getString("product_id"));
		this.setProduct_name(row.getString("product_name"));
		this.setRecommendation(row.getString("recommendation"));
		this.setRetail_price(row.getDouble("retail_price"));
		this.setSale_price(row.getDouble("sale_price"));
		this.setVendor(row.getString("vendor"));
	}
	 
	
	public void writetoDB(){
		Statement statement = QueryBuilder.insertInto("product_price")
				.value("product_id", this.getProduct_id())
				.value("product_name", this.getProduct_name())
				.value("recommendation", this.getRecommendation())
				.value("vendor", this.getVendor())
				.value("cost", this.getCost())
				.value("popularity", this.getPopularity())
				.value("retail_price", this.getRetail_price())
				.value("sale_price", this.getSale_price());
		session.execute(statement);		 
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
	
	public Double getDiscountPct(){
		return (retail_price - sale_price)/retail_price;
	}
	
	public Double getOrderPct(){
		return this.getPopularity() * this.getDiscountPct();
	}
	
	public long getRetail_price_long(){
		Double price = this.retail_price * 100;
		return price.intValue();
	}
	
	public long getSale_price_long(){
		Double price = this.sale_price * 100;
		return price.intValue();
	}
	
	public long getCost_long(){
		Double price = this.cost * 100;
		return price.intValue();
	}
	
	public Double getProfit(){
		return this.sale_price - this.cost;
	}
	
	public long getProfit_long(){
		Double profit = this.getProfit() * 100;
		return profit.intValue();
	}
}
