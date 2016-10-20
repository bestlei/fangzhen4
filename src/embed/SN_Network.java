package embed;
//物理网络类

public class SN_Network {
	
	public int nodes;	//节点个数
	public int links;	//链路个数
	public double[] cpu;	//节点的CPU,原C++使用了宏定义MAX_REQ_NODES = 100;
	public Link[] link;//链路集，原C++使用了宏定义MAX_REQ_LinkS = 100;
	public int[] x;
	public int[] y;
	
	
	public SN_Network(){
		cpu = new double[1000];
		link = new Link[10000];
		x = new int[1000];
		y = new int[1000];
	}
}
