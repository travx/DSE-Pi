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
		
		String nodes[] = {"ras1", "ras11"};
		//String nodes[] = {"ubuntu"};
		//String nodes[] = {"172.16.232.146"};
		//String nodes[] = {"localhost"};
		Database db = new Database(nodes, "simulation");
		
		System.out.println("Success! Cluster connection established. Ready to process orders.");
		System.out.println("Running simulation.");
		System.out.println("Iterations: " + ITERATIONS);
		System.out.println("Wait: " + WAIT);
		
		Random rand = new Random();
				
		List<ProductPrice> productprices = db.getProductPrices();
		
		for (int i=0; i!=ITERATIONS; i++){
			
			if (i%10==0){
				System.out.println("Iterations Completed: " + i + "     Orders Processed: " + ORDERS);
			}	
			
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
					
		}
		
		System.out.println("Simulation Complete.");
		System.out.println("Total Orders Processed: " + ORDERS);
	}

}
