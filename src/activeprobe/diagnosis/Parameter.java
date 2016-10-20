package activeprobe.diagnosis;

import java.util.ArrayList;

public class Parameter {
	public static int virProbeNum= 3;  //虚拟探针数量
	public static int virLinkNum;    //虚拟链路数量（来自拓扑生成不可设置）
	public static int virNum= 20;    //虚拟节点数
	public static int phyProbeNum= 3;   //物理探针数量
	public static int phyLinkNum;    //物理链路数量
	public static int phyNum= 100;   //物理节点数
	public static double virU= 0.05;
	public static double virA= 0.001;
	public static double phyU= 0.005;
	public static double phyA= 0.002;
	
	public static ArrayList<Integer> virProbesNo= new ArrayList<Integer>();   //虚拟探测站点队列
	public static ArrayList<Integer> phyProbesNo= new ArrayList<Integer>();
	
	public static ArrayList<Integer> virFaultNo= new ArrayList<Integer>();
	public static ArrayList<Integer> phyFaultNo= new ArrayList<Integer>();
	
	public static ArrayList<String> virProbePath= new ArrayList<String>();
	public static ArrayList<String> phyProbePath= new ArrayList<String>();
	
	
}
