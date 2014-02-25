import java.util.List;
import java.util.Random;


public class RunSimulation {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		int ITERATIONS;
		int WAIT;
		int ORDERS=0;
		
		if(args.length == 0)
	    {
	        System.out.println("No arguments passed. Using defaults.");
	        ITERATIONS = 100;
	        WAIT=40;
	    }
		else if(args.length == 1) {
			ITERATIONS = Integer.parseInt(args[0]);
			WAIT=40;
		}
		else {
			ITERATIONS = Integer.parseInt(args[0]);
			WAIT = Integer.parseInt(args[1]);
		}
		
		System.out.println("Connecting to Cluster.");
		
		String nodes[] = {"ras1", "ras2", "ras3"};
		//String nodes[] = {"debian1"};
		Database db = new Database(nodes, "simulation");
		db.connect();
		
		System.out.println("Success! Cluster connection established. Ready to process orders.");
		System.out.println("Running simulation.");
		System.out.println("Iterations: " + ITERATIONS);
		System.out.println("Wait: " + WAIT);
		
		Random rand = new Random();
				
		List<ProductPrice> productprices = db.getProductPrices();
		
		for (int i=1; i<=ITERATIONS; i++){
			for (ProductPrice pp : productprices){
				if (rand.nextDouble() < pp.getOrderPct()){
					//System.out.println(pp.getProduct_id());
					//System.out.println(pp.getVendor());
					db.refreshProductPrice(pp);
					db.placeOrder(pp);
					ORDERS++;
				}
				Thread.sleep(WAIT);
				
			}
			
			if (i%10==0){
				System.out.println("Iterations Completed: " + i + "     Orders Processed: " + ORDERS);
			}			
		}
		
		System.out.println("Simulation Complete.");
	}

}
