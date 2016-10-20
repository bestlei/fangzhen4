package activeprobe.diagnosis;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import activeprobe.matrixgen.matrixGenerate;
import activeprobe.dataclass.*;

public class JavaMat {
	public static DependencyMatrix dMatrix1=null;//虚拟网络
	public static DependencyMatrix dMatrix2;//物理网络
	public static Hashtable<String,String> NodeMap=new Hashtable<String,String>();//节点映射
	public static Hashtable<Topo_Path,String> EdgeMap=new Hashtable<Topo_Path,String>();//链路映射
	public static ArrayList<Topo_Path> SubEdge=new ArrayList<Topo_Path>();
	public static MappingRelationship map=null;//映射关系
	private static ArrayList<Probe> allSubProbes=null;
	private static ArrayList<Probe> allVirtualProbes=null;
	public static ArrayList<Probe> allShrinkProbes=new ArrayList<Probe>();
	private static Diagnostor diagnostor;
	public static ArrayList<Integer> probesNo=new ArrayList<Integer>();//探针编号
	public static ArrayList<Integer> detResult=new ArrayList<Integer>();//探测结果0,1
	public static ArrayList<String> suspectNodes=new ArrayList<String>();//疑似节点
	public static ArrayList<Integer> probesNoTemp=new ArrayList<Integer>();
	public static ArrayList<Integer> detResultTemp=new ArrayList<Integer>();
	public static ArrayList<String> suspectNodesTemp=new ArrayList<String>();
	public static String phyDiagnost_Result = "";      //保存物理探测结果
	public static String virDiagnost_Result = "";      //保存虚拟探测结果
	public static final String initUrl= "D:\\flexW\\experiment_teacher\\";
	//private static String folderNo;

	/**
	 * fileNo1虚拟节点文件，初始为20.txt
	 * probeStationNum1虚拟探针数
	 * fileNo2物理网络文件，初始为100.txt
	 * probStationNum2物理探针数
	 * fileNo3为映射的结果文件，示例为20-100.txt
	 * f1,f2,f3,f4故障生成中的高斯参数
	 * f5 节点噪声率，f6探头噪声故障率
	 * 该函数返回故障生成信息
	 */
	public static ArrayList<String> Initial(int fileNo1,int probeStationNum1,int fileNo2,int probeStationNum2,String fileNo3,double f1,double f2,double f3,double f4,float f5,float f6,int virMaxNum,int phyMaxNum){
		//根据生成拓扑 生成DM文件
		JavaMat.initAllVariable();   //变量初始化
		ArrayList<String> faultGenerate_Result = new ArrayList<String>();
	
		dMatrix1 = matrixGenerate.getMatrix(1,initUrl+"input\\"+fileNo1+".txt",probeStationNum1);//虚拟网络
		dMatrix2 = matrixGenerate.getMatrix(2,initUrl+"input\\"+fileNo2+".txt",probeStationNum2);//物理网络
		map = new MappingRelationship();
		map.init(initUrl+"input\\"+fileNo3+".txt");
		NodeMap=map.GetNode();    //获取物理和虚拟网络节点信息
		EdgeMap=map.GetEdge();    //获取物理和虚拟网络链路信息
		SubEdge=map.GetSubEdge(); //获取虚拟网络链路对应的物理链路
		
		allVirtualProbes=dMatrix1.GetAllProbes();   //获取虚拟探针信息
		allSubProbes=dMatrix2.GetAllProbes();    //获取物理探针信息
		//写探测依赖矩阵文件
		
		OutputToFile.arrayListToFile(1,dMatrix1,allVirtualProbes,"nodedependencymatrix1.txt");
		OutputToFile.arrayListToFile(2,dMatrix2, allSubProbes, "nodedependencymatrix2.txt"); 
		OutputToFile.relationshipToFile(map,dMatrix2.getNodeCount(),"maprelationshipmatrix.txt");
		diagnostor=new Diagnostor(dMatrix1,dMatrix2,f1,f2,f3,f4,virMaxNum,phyMaxNum);
		System.out.println("-------------------虚拟网络------------------");
		System.out.println("节点数："+dMatrix1.getNodeCount()+" ，链路数："+dMatrix1.getEdgeCount());
		Parameter.virLinkNum= dMatrix1.getEdgeCount();
		//diagnostor.CreateFaultNodes_Virtual(NodeMap,EdgeMap,SubEdge);
		diagnostor.CreateFaultNodes_Virtual();       //虚拟网络故障生成
		String virFault = diagnostor.getvirFaultInformation();
		faultGenerate_Result.add(virFault);     //添加虚拟故障生成结果
		System.out.println("-------------------物理网络------------------");
		System.out.println("节点数："+dMatrix2.getNodeCount()+" ，链路数："+dMatrix2.getEdgeCount());
		Parameter.phyLinkNum= dMatrix2.getEdgeCount();
		diagnostor.CreateFaultNodes_Substrate( );    //物理网络故障生成
		String phyFault = diagnostor.getphyFaultInformation();
		faultGenerate_Result.add(phyFault);
		return faultGenerate_Result;
	}
	/**
	 * 
	 * 返回故障探测信息
	 */
	public ArrayList<String> fault_Detection(String virRestrian, String virLeak, String phyRestrian, String phyLeak){
		ArrayList<String> faultDetection_Result = new ArrayList<String>();
		float vir_rateofnodenoise =  Float.parseFloat(virRestrian);
		float vir_faultrateoprobenoise = Float.parseFloat(virLeak);
		float phy_rateofnodenoise = Float.parseFloat(phyRestrian);
		float phy_faultrateonprobenoise = Float.parseFloat(phyLeak);
		Diagnostor.InitialDetectStatus(vir_rateofnodenoise, vir_faultrateoprobenoise, phy_rateofnodenoise, phy_faultrateonprobenoise);
	    JavaMat.phyDiagnost_Result = Detection(0,allVirtualProbes);
		JavaMat.virDiagnost_Result = Detection(1,allSubProbes);
		faultDetection_Result.add(virDiagnost_Result);
		faultDetection_Result.add(phyDiagnost_Result);
		OutputToFile.stringToFile(JavaMat.phyDiagnost_Result, "VirProbeResult.txt");   //将探测结果写入文件
		OutputToFile.stringToFile(JavaMat.virDiagnost_Result, "PhyProbeResult.txt");
		return faultDetection_Result;
	}
	public String Detection(int index,ArrayList<Probe> probes){
		//Vector<Integer> result=new Vector<Integer>();
		//result=diagnostor.DiagnostRound(index,probes);
		String diagnost_result = diagnostor.DiagnostRound(index,probes);
		return diagnost_result;
	}

	private static void initAllVariable(){
		JavaMat.allShrinkProbes=new ArrayList<Probe>();
		JavaMat.detResult=new ArrayList<Integer>();
		JavaMat.detResultTemp=new ArrayList<Integer>();
		JavaMat.diagnostor=null;
		//JavaMat.folderNo=null;
		JavaMat.probesNo=new ArrayList<Integer>();
		JavaMat.probesNoTemp=new ArrayList<Integer>();
		JavaMat.suspectNodes=new ArrayList<String>();
		JavaMat.suspectNodesTemp=new ArrayList<String>();
	}
	
}
