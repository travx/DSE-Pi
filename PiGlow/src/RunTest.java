
public class RunTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String nodes[] = {"ras5","ras15"};
		Database db = new Database(nodes);
		
		db.printMetrics();
	}

}
