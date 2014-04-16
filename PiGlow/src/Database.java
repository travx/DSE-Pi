import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Metrics;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.policies.ConstantReconnectionPolicy;



public class Database {
	private String[] nodes;
	private Cluster cluster;
	private Session session;
	
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String[] getNodes() {
		return nodes;
	}

	public void setNodes(String[] nodes) {
		this.nodes = nodes;
	}

	public Cluster getCluster() {
		return cluster;
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	public Database(String nodes[]){
		setNodes(nodes);
		connect();		
	}
	
	private void connect() {
		System.out.println("Creating Cluster");
		Builder builder = Cluster.builder();
		builder.addContactPoints(nodes);
	
		System.out.println("Setting Options");		
		builder.socketOptions().setConnectTimeoutMillis(30000);
		builder.withReconnectionPolicy(new ConstantReconnectionPolicy(100));
		
		System.out.println("Building Cluster");		
		cluster = builder.build();
		
		System.out.println("Connecting...");
		session = cluster.connect();
		
		System.out.println("Connected to Cluster");
	}
	
	public void printMetrics() {
		   System.out.println("Metrics");
		   Metrics metrics = getCluster().getMetrics();
		   //Gauge<Integer> gauge = metrics.getConnectedToHosts();
		   Integer numberOfHosts = metrics.getConnectedToHosts().value();
		   Integer knownHosts = metrics.getKnownHosts().value();
		   System.out.printf("Number of hosts: %d\n", numberOfHosts);
		   System.out.printf("Known hosts: %d\n", knownHosts);
	}
	
	public int getHosts(){
		   Metrics metrics = getCluster().getMetrics();
		   return metrics.getConnectedToHosts().value();
	}
	
}
