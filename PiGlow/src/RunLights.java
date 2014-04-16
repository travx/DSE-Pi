import java.io.IOException;


public class RunLights {

	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		int NUMBER_OF_LEDS = 18;
		int NUMBER_OF_NODES = 10;
		
		String nodes[] = {"ras5","ras15"};
		Database db = new Database(nodes);
		
        final PiGlow piGlow = new PiGlow();

        System.out.println("Press ctrl-c to stop");

        Runtime.getRuntime().addShutdownHook(new ShutDownTask(piGlow));

        //Flash each LED through varying levels of brightness
        byte[] brightness = {(byte)8, (byte)64, (byte)255, (byte)0};
        int lights=0;
        
        while (true){	
        	lights = (int) (db.getHosts()*NUMBER_OF_LEDS/NUMBER_OF_NODES);
        
        	for (int b=0; b<brightness.length; b++){
        		for (int i=0; i<lights; i++){
        			piGlow.shineLights(brightness[b],i);
        			Thread.sleep(500);
        		}	
        	}
        	
        	Thread.sleep(1000);
        }
	}
}
