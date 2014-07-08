import java.io.IOException;


public class RunLights {

	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		int NUMBER_OF_LEDS = 18;
		int NUMBER_OF_NODES = 10;
		int BRIGHTNESS_STEP = 14;
		
		String nodes[] = {"ras1","ras11"};
		Database db = new Database(nodes);
		
        final PiGlow piGlow = new PiGlow();

        System.out.println("Press ctrl-c to stop");

        Runtime.getRuntime().addShutdownHook(new ShutDownTask(piGlow));

        //Flash each LED through varying levels of brightness
        int lights=0;
        
        while (true){	
        	lights = (int) (db.getHosts()*NUMBER_OF_LEDS/NUMBER_OF_NODES);
        	byte[] brightness = new byte[18];
        	piGlow.shineLights(brightness);
        	Thread.sleep(500);
        	
        	for (int j=0; j<lights; j++){       		
        		for (int i=0; i<=j; i++){
        			brightness[i] = (byte)(255 - (BRIGHTNESS_STEP * (j-i)));
        		}
        		piGlow.shineLights(brightness);
        		Thread.sleep(200);
        	}
        	Thread.sleep(1000);
        }
	}
}
