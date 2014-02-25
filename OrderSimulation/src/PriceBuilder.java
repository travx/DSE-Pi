import java.util.List;
import java.util.Random;


public class PriceBuilder {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String nodes[] = {"ras1", "ras2", "ras3"};
		Database db = new Database(nodes, "simulation");
		Random rand = new Random();
		
		db.connect();
		
		List<Product> products = db.getProducts();
		
		for (Product prod:products){
			ProductPrice pp = new ProductPrice(db.getSession());
			pp.setProduct_id(prod.getProduct_id());
			pp.setProduct_name(prod.getProduct_name());
			pp.setRecommendation(prod.getRecommendation());
			pp.setVendor(prod.getVendor());
			pp.setSale_price(prod.getUnit_price());
			pp.setPopularity(rand.nextDouble());
			pp.setRetail_price(prod.getUnit_price() + rand.nextInt(prod.getUnit_price().intValue()+1));
			pp.setCost(rand.nextInt(prod.getUnit_price().intValue()+2)/1.0);

			pp.writetoDB();
		}
		
	}

}
