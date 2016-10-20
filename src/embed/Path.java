package embed;
//路径类

public class Path {
	
	 public int len; //路径的长度,也即路径的跳数
	 public int[] link;	//链路数
	 public double bw;//路径的带宽
	 
	 
	 public Path(){
		 link = new int[100];
	 }
}
