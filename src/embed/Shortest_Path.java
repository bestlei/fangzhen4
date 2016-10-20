package embed;

public class Shortest_Path {
	
	 public int length;	//最短路径的跳数
	 public int next;		//最短路径的下一跳(Dijkstra最短路径)
	 public int linkid;	//from到next链路的编号
	 public double bw;		//剩余带宽
	 public double rate;	//利用率
}
