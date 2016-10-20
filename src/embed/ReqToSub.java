package embed;
//映射结果

public class ReqToSub {
	
	public int[] snode;   //snode[i]表示第i个虚拟节点映射到的物理节点
	public Path[] spath; //spath[i]表示第i个链路映射到的物理路径
	
	public ReqToSub(){
		snode = new int[100];
		spath= new Path[100];
	}
}
