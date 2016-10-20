package embed;

public class S2V_node {
	
	public int req_count;
	public int[] req;
	public int[] vnode;
	public double[] cpu;
	public double rest_cpu;
	
	
	public S2V_node(){
		req = new int[1000];
		vnode = new int[1000];
		cpu = new double[1000];
	}
	
}
