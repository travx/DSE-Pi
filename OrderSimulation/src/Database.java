import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.policies.ConstantReconnectionPolicy;
import com.datastax.driver.core.policies.DowngradingConsistencyRetryPolicy;
import com.google.gson.Gson;


public class Database {
	private String[] nodes;
	
	private String keyspace;
	private Cluster cluster;
	private Session session;
	
	private PreparedStatement psProducts;
	private PreparedStatement psProduct;
	private PreparedStatement psProductPrices;
	private PreparedStatement psProductInfo;
	private PreparedStatement psSummary;
	private PreparedStatement psGetSummary;
	private PreparedStatement psVendorSummary;
	private PreparedStatement psGetVendorSummary;
	private PreparedStatement psProductSummary;
	private PreparedStatement psVendorDetails;
	private PreparedStatement psGetVendorDetails;
	private PreparedStatement psGetProductSummary;
	private PreparedStatement psGetProductSummaryAll;
	private PreparedStatement psProductDetail;
	private PreparedStatement psGetProductDetail;
	private PreparedStatement psUpdateProductPrices;
	
	public String[] getNodes() {
		return nodes;
	}

	public void setNodes(String[] nodes) {
		this.nodes = nodes;
	}

	public Database(String nodes[], String keyspace){
		setNodes(nodes);
		setKeyspace(keyspace);
		connect();		
	}
	
	public void connect() {
		Builder builder = Cluster.builder();
		builder.addContactPoints(nodes);
		
		//Connection Options
		builder.socketOptions().setConnectTimeoutMillis(60000);
		builder.socketOptions().setReadTimeoutMillis(60000);
		builder.withRetryPolicy(DowngradingConsistencyRetryPolicy.INSTANCE);
		builder.withReconnectionPolicy(new ConstantReconnectionPolicy(100));
		
		cluster = builder.build();
		session = cluster.connect(keyspace);
		init();
	}
	
	public void init(){
		psProducts = session.prepare("select * from product");
		psProduct = session.prepare("select * from product_price where product_id=?");
		psProductPrices = session.prepare("select * from product_price");
		psProductInfo = session.prepare("select * from product_price where product_id=?");
		psSummary = session.prepare("update summary set orders=orders+1, revenue=revenue+?, profit=profit+? where metric='total';");
		psGetSummary = session.prepare("select * from summary where metric='total';");
		psVendorSummary = session.prepare("update vendor_summary set orders=orders+1, revenue=revenue+?, profit=profit+? where vendor=?;");
		psGetVendorSummary = session.prepare("select * from vendor_summary");
		psProductSummary = session.prepare("update product_summary set orders=orders+1, revenue=revenue+?, profit=profit+? where vendor=? and product_id=?;");
		psVendorDetails = session.prepare("update vendor_detail set orders=orders+1, revenue=revenue+?, profit=profit+? where vendor=? and seconds=?;");
		psGetVendorDetails = session.prepare("select * from vendor_detail where vendor=? limit 20;");
		psGetProductSummary = session.prepare("select * from product_summary where vendor=?");
		psGetProductSummaryAll = session.prepare("select * from product_summary");
		psProductDetail = session.prepare("update product_detail set orders=orders+1, revenue=revenue+?, profit=profit+? where product_id=? and seconds=?;");
		psGetProductDetail = session.prepare("select * from product_detail where product_id=? limit 20;");
		psUpdateProductPrices = session.prepare("update product_price set retail_price=?, sale_price=?, cost=?, popularity=? where product_id=?");

	}
	
	public void refreshProductPrice(ProductPrice pp){
		pp.refresh(session.execute(psProduct.bind(pp.getProduct_id())).one());
	}
	
	public void placeOrder(ProductPrice pp){
		writeSummary(pp);
		writeVendorSummary(pp);
		writeProductSummary(pp);
		writeVendorDetails(pp);
		writeProductDetails(pp);
	}
	
	public void writeSummary(ProductPrice pp){
		session.execute(psSummary.bind(pp.getRetail_price_long(), pp.getProfit_long()));
	}
	
	public void writeVendorSummary(ProductPrice pp){
		session.execute(psVendorSummary.bind(pp.getRetail_price_long(), pp.getProfit_long(), pp.getVendor()));
	}
	
	public void writeProductSummary(ProductPrice pp){
		session.execute(psProductSummary.bind(pp.getRetail_price_long(), pp.getProfit_long(), pp.getVendor(), pp.getProduct_id()));
	}
	
	public void writeVendorDetails(ProductPrice pp){
		DateTime dt = new DateTime();
		Double seconds = (double) (dt.getDayOfYear() * 100000) + (dt.getSecondOfDay()/10.0);
		session.execute(psVendorDetails.bind(pp.getRetail_price_long(), pp.getProfit_long(), pp.getVendor(), seconds.intValue()));
	}	

	public void writeProductDetails(ProductPrice pp){
		DateTime dt = new DateTime();
		Double seconds = (double) (dt.getDayOfYear() * 100000) + (dt.getSecondOfDay()/10.0);
		session.execute(psProductDetail.bind(pp.getRetail_price_long(), pp.getProfit_long(), pp.getProduct_id(), seconds.intValue()));
	}		
	
	public List<Product> getProducts(){
		ResultSet results = session.execute(psProducts.bind());
		List<Product> products = new ArrayList<Product>();
		
		for (Row row : results){
			products.add(new Product(row));
		}
		
		return products;
	}
	
	public List<ProductPrice> getProductPrices(){
		ResultSet results = session.execute(psProductPrices.bind());
		List<ProductPrice> productprices = new ArrayList<ProductPrice>();
		
		for (Row row : results){
			productprices.add(new ProductPrice(row));
		}
		
		return productprices;
	}
	
	public String getSummary(){
		List<Summary> sumlist = new ArrayList<Summary>();
		sumlist.add(new Summary(session.execute(psGetSummary.bind()).one()));
		return new Gson().toJson(sumlist);
	}
	
	public String getVendorSummary(){
		ResultSet results = session.execute(psGetVendorSummary.bind());
		List<VendorSummary> vslist = new ArrayList<VendorSummary>();
		
		for (Row row:results){
			vslist.add(new VendorSummary(row));
		}
		
		return new Gson().toJson(vslist);
	}
	
	public String getVendorDetails(String vendor){
		ResultSet results = session.execute(psGetVendorDetails.bind(vendor));
		List<VendorDetail> vlist = new ArrayList<VendorDetail>();
		
		for (Row row:results){
			vlist.add(new VendorDetail(row));
		}
		
		return new Gson().toJson(vlist);
	}	
	
	public String getProductSummary(String vendor){
		ResultSet results = session.execute(psGetProductSummary.bind(vendor));
		List<ProductSummary> pslist = new ArrayList<ProductSummary>();
		
		for (Row row:results){
			pslist.add(new ProductSummary(row));
		}
		
		return new Gson().toJson(pslist);		
	}
	
	public String getProductSummary(){
		ResultSet results = session.execute(psGetProductSummaryAll.bind());
		List<ProductSummary> pslist = new ArrayList<ProductSummary>();
		
		for (Row row:results){
			pslist.add(new ProductSummary(row));
		}
		
		return new Gson().toJson(pslist);		
	}	
	
	public String getProductDetail(String product_id){
		ResultSet results = session.execute(psGetProductDetail.bind(product_id));
		List<ProductDetail> products = new ArrayList<ProductDetail>();
		
		for (Row row:results){
			products.add(new ProductDetail(row));
		}
		
		return new Gson().toJson(products);		
	}	
	
	public String getProductInfo(String product_id){
		ResultSet results = session.execute(psProductInfo.bind(product_id));
		List<ProductInfo> productprices = new ArrayList<ProductInfo>();
		
		for (Row row : results){
			productprices.add(new ProductInfo(row));
		}
		
		return new Gson().toJson(productprices);
	}	
	
	public void updateProductPrice(double retail_price, double sale_price, double cost, double popularity, String product_id){
		session.execute(psUpdateProductPrices.bind(retail_price, sale_price, cost, popularity, product_id));
	}
	
	public String getKeyspace() {
		return keyspace;
	}
	public void setKeyspace(String keyspace) {
		this.keyspace = keyspace;
	}
	public Cluster getCluster() {
		return cluster;
	}
	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
}
