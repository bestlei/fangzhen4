package embed;
//虚拟网络请求类
//import embed.Request_Relate;
public class VN_Request {
	
	public int nodes;			//节点个数
	public int links;			//链路个数
	public int split;			//是否允许分割
	public int time;			//到达时间
	public int topo;			//拓扑类型
	public int duration;		//持续时间	
	
	public double revenue;		//单位时间带来的收益
	public double[] cpu;		//节点的CPU,
	public Link[] link;			//链路集，
//public Request_Relate[] relate;
	
	public VN_Request(){
		cpu = new double[100];		//原C++使用了宏定义MAX_REQ_NODES = 100;
		link = new Link[100];		//原C++使用了宏定义MAX_REQ_LinkS = 100;
		
	}
}