package embed;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class MapAlgorithm {
	
	//考虑使用ArrayList等java容器代替C++的指针如何
	public SN_Network sub;         		//底层网络
	public static SN_Network virSub;
	public SN_Network sub_monitor;     //底层网络状态监测时使用，
	public ArrayList<VN_Request> req;				//虚拟网请求,原C++代码是用指针的动态数组表示,VN_Request
	public int n;					//虚拟网的个数
	public ArrayList<ReqToSub> v2s;				//虚拟网络到物理网络 ReqToSub
	public ArrayList<Shortest_Path> spath;  		//最短路径 Shortest_Path
	public double sum_bw_res;		//物理网络中剩余的带宽资源
	public String reqfileName;			//虚拟网络输入文件所在的目录
	public int vital;					//开始映射的节点,也表示迭代次数,它决定了虚拟网络映射到的区域
	
	public int currentIndex;			//当前正在映射的虚拟网络的编号
	public Vector<Integer> mappedNodeList;   	 //已经映射的虚拟节点,可以用ArrayList替换吧
	public Vector<Integer> unmappedNodeList; 	//未映射的虚拟节点
	
	public Vector<Integer> mappedSubNodeList;	
	public Vector<Integer> unmappedSubNodeList;		//未映射的物理节点
	
	public Vector<Integer> mappedLinkList;
	public Vector<Integer> unmappedLinkList;		//未映射的虚拟链路
	
	public Vector<Integer> subNodeList;		//映射时保持的范围,暂时未启
	
	public Vector<Integer> failList;		//映射失败的虚拟请求
	public Vector<Integer> successList;		//映射成功的虚拟请求
	
	
	//映射时的跳数约束
	public int linkHop;					//先链路映射时,链路允许的跳数
	public	int maxHop;					//与选择的链路相连接的链路的最大跳数
	public double maxRate;				//最大接受率
	public	double minRate;				//最小接受率
	
	public int setCurrentIndex;		
	
	public Vector<Integer> my_unmappedNodeList;
	public int[][] mapingResult;	//把每一次的映射都写在这儿, [i][100]表示第i种情况下，是否成功映射
	public double bw[];			//每种情况下消耗的带宽资源
	
	public Vector<Integer> freeLinkList;
	
	
		
	private double jiedianbeifen[];		//节点备份 此处C++原来通过MAX_CPU宏定义定义
	private double R;						//收益
	private double C;						//消耗
	private double RCompareToC_sum;			//消耗收益之比
	private int time;						//时间轴
	private int success;					//成功的虚拟网络个数
	private int arrived;					//到达的虚拟网络个数
	private double successTime;				//成功映射虚拟网络所花费的总时间
	private double failTime;				//失败映射虚拟网络所花费的总时间
	private double rate;					//接受率
	private int vital_sum_fail;				//成功映射虚拟网络的总迭代次数
	private int vital_sum_success;			//失败映射虚拟网络的总迭代次数
	
	
	//***********************************************************************************************//

	
	//构造函数
	public MapAlgorithm(String subname,String reqfilename , int count){
		
		System.out.println("*********MapAlgorithm***********");
		mapingResult = new int[1][101];
		bw = new double[1];
		jiedianbeifen = new double[100];
		
		linkHop = 4;
		maxHop = 10;
		vital = 0;
		//reqfileName = "D:\\work\\top\\requests-500-0-10-10\\";
		reqfileName = reqfilename;
		maxRate = 0.0;
		minRate = 1.0;
		
		//Vector初始化
		mappedNodeList = new Vector<Integer>();
		unmappedNodeList = new Vector<Integer>();
		
		mappedSubNodeList = new Vector<Integer>();
		unmappedSubNodeList = new Vector<Integer>();
		
		mappedLinkList = new Vector<Integer>();
		unmappedLinkList = new Vector<Integer>();
		
		subNodeList = new Vector<Integer>();
		
		failList = new Vector<Integer>();
		successList = new Vector<Integer>();
		
		my_unmappedNodeList = new Vector<Integer>();
		freeLinkList = new Vector<Integer>();
		
		
		n = count;//虚拟网请求的个数
		System.out.println("-----------------------n=  "+n);
		initNetworkData(subname);
		
	}
	
public MapAlgorithm(String subname, String reqfilename, ArrayList virReqFile, int customCount){
		
		System.out.println("*********MapAlgorithm***********");
		mapingResult = new int[1][101];
		bw = new double[1];
		jiedianbeifen = new double[100];
		
		linkHop = 4;
		maxHop = 10;
		vital = 0;
		//reqfileName = "D:\\work\\top\\requests-500-0-10-10\\";
		reqfileName = reqfilename;
		maxRate = 0.0;
		minRate = 1.0;
		
		//Vector初始化
		mappedNodeList = new Vector<Integer>();
		unmappedNodeList = new Vector<Integer>();
		
		mappedSubNodeList = new Vector<Integer>();
		unmappedSubNodeList = new Vector<Integer>();
		
		mappedLinkList = new Vector<Integer>();
		unmappedLinkList = new Vector<Integer>();
		
		subNodeList = new Vector<Integer>();
		
		failList = new Vector<Integer>();
		successList = new Vector<Integer>();
		
		my_unmappedNodeList = new Vector<Integer>();
		freeLinkList = new Vector<Integer>();
		
		
		n = customCount;//虚拟网请求的个数
		System.out.println("-----------------------n=  "+n);
		
		initCustomNetworkData(subname,virReqFile,customCount);
		
	}
public MapAlgorithm(){
}

//自定义拓扑
public void initCustomNetworkData(String subname,ArrayList customReqfile,int customCount){
	System.out.println("*********initNetworkData***********");
			
	req = new ArrayList<VN_Request>(n);
	sub = new SN_Network();
	sub_monitor = new SN_Network();
	
	//产生v2s对象数组，初始为n个，v2s[i]记录这虚拟网络i映射的结果
	v2s = new ArrayList<ReqToSub>(n);
	for(int k=0;k<n;k++){
		ReqToSub reqtosub = new ReqToSub();
		for(int k1=0;k1<100;k1++){
			reqtosub.spath[k1] = new Path();
		}
		v2s.add(reqtosub);
	}
	
	//从xml物理网拓扑文件中读取
	readSubFromXML(subname);
	
	//从txt物理网拓扑中读取读取物理网络
	//readSubFromTXT(subname);

	System.out.println("Node number:"+sub.nodes+",link num:"+sub.links);
	System.out.println("*********before readReqFile***********");
	//读取虚拟网络拓扑
	readCustomReqFile(customReqfile,customCount);
	
	System.out.println("*********readReqFile***********");
	//构建spath数组，存储最短路径，对应着原C++的数组的内存分配
	spath = new ArrayList<Shortest_Path>(sub.nodes*sub.nodes);
	for(int k=0;k<sub.nodes*sub.nodes;k++){
		Shortest_Path temp = new Shortest_Path();
		spath.add(temp);
	}
	System.out.println("*********calc_shortest_path***********");
	calc_shortest_path();
	//计算任意两个物理节点之间的最短路径的可用带宽,即为该路径中所有链路的带宽最小值
	calc_resource_for_spath();
	
	currentIndex = 0;
	//测试用
	//calc_bw_for_node(90, 0);
	//print_shortest_path(23, 43);
	sp_hop();
	System.out.println("*********init end***********");
}



//自定义拓扑
public void readCustomReqFile(ArrayList customReq,int customCount){
//	//依次把虚拟网请求的文件读进来
//	System.out.println("++++++++   readReqFile    +++++");
//	int line = 0;     //读取的行号
//	File file = null;
//	BufferedReader reader = null;
//	String filename = null;
//	System.out.println("for start");
//
//	for (int i=0;i<customCount;i++) {//requests-500-7-10-10
//		String customFileName = customReq.get(i).toString();
//		filename = reqfileName+customFileName+".txt";
//		System.out.println("读来自 "+filename+"的文件");
//		try{				
//			file = new File(filename);
//			reader = new BufferedReader(new FileReader(file));
//			String tempArray[] = new String[10]; //存储一行的多个数据 
//			String temp;
//			line=1; //当前读取的行号
//			int temp_nodes = 0;//存储虚拟网络的节点数目
//			VN_Request temp_vn = new VN_Request();
//			
//			while((temp = reader.readLine()) !=null){
//				if(line == 1){//读取虚拟网请求的参数,默认在第一行。依次为nodes,links,split,time,duration,topo
//					tempArray = temp.split(" ");
//					temp_vn.nodes = Integer.parseInt(tempArray[0]);
//					temp_vn.links = Integer.parseInt(tempArray[1]);
//					temp_vn.split = Integer.parseInt(tempArray[2]);
//					temp_vn.time = Integer.parseInt(tempArray[3]);
//					temp_vn.duration = Integer.parseInt(tempArray[4]);
//					temp_vn.topo = Integer.parseInt(tempArray[5]);	
//					
//					temp_nodes = temp_vn.nodes;
//					
//				} else if (line>1&&line<(temp_nodes+2)){//读取所有的虚拟节点
//					
//					temp_vn.cpu[line-2] = Double.parseDouble(temp);
//					
//				} else if(line>(temp_nodes+1)){//读取虚拟网络的链路信息
//					tempArray = temp.split(" ");
//					
//					//对象内部对象数组的初始化
//					if(temp_vn.link[line-temp_nodes-2]==null){
//						temp_vn.link[line-temp_nodes-2] = new Link();
//					}
//					temp_vn.link[line-temp_nodes-2].from = Integer.parseInt(tempArray[0]);
//					temp_vn.link[line-temp_nodes-2].to = Integer.parseInt(tempArray[1]);
//					temp_vn.link[line-temp_nodes-2].bw = Double.parseDouble(tempArray[2]);
//					
//					temp_vn.revenue += temp_vn.link[line-temp_nodes-2].bw;
//					System.out.println("temp_vn.revenue += temp_vn.link[line-temp_nodes-2].bw;"+temp_vn.link[line-temp_nodes-2].bw);
//				}
//				
//				line++;	
//			}
//			
//			req.add(temp_vn);//读取完一个虚拟网请求文件之后，将其放到请求数组中
//		} catch (IOException e){
//			e.printStackTrace();
//		} finally {
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (IOException e1) {
//                	e1.printStackTrace();
//                	System.out.println("文件关闭异常");
//                }
//            }
//        }
//		
//	}
//	System.out.println("----readReqFile end--------");
	
	
	
	
	//依次把虚拟网请求的文件读进来
	System.out.println("++++++++   readReqFile    +++++");
	int line = 0;     //读取的行号
	File file = null;
	BufferedReader reader = null;
	String filename = null;
	System.out.println("for start");
	
	for (int i=0;i<customCount;i++) {//requests-500-7-10-10
		String customFileName = customReq.get(i).toString();
		filename = reqfileName+customFileName+".txt";
		System.out.println("读来自 "+filename+"的文件");
		try{				
			file = new File(filename);
			reader = new BufferedReader(new FileReader(file));
			String tempArray[] = new String[10]; //存储一行的多个数据 
			String temp;
			line=1; //当前读取的行号
			int temp_nodes = 0;//存储虚拟网络的节点数目
			VN_Request temp_vn = new VN_Request();
			
			while((temp = reader.readLine()) !=null){
				if(line == 1){//读取虚拟网请求的参数,默认在第一行。依次为nodes,links,split,time,duration,topo
					tempArray = temp.split(" ");
					temp_vn.nodes = Integer.parseInt(tempArray[0]);
					temp_vn.links = Integer.parseInt(tempArray[1]);
					temp_vn.split = Integer.parseInt(tempArray[2]);
					temp_vn.time = Integer.parseInt(tempArray[3]);
					temp_vn.duration = Integer.parseInt(tempArray[4]);
					temp_vn.topo = Integer.parseInt(tempArray[5]);	
					
					temp_nodes = temp_vn.nodes;
					
				} else if (line>1&&line<(temp_nodes+2)){//读取所有的虚拟节点
					
					temp_vn.cpu[line-2] = Double.parseDouble(temp);
					
				} else if(line>(temp_nodes+1)){//读取虚拟网络的链路信息
					tempArray = temp.split(" ");
					
					//对象内部对象数组的初始化
					if(temp_vn.link[line-temp_nodes-2]==null){
						temp_vn.link[line-temp_nodes-2] = new Link();
					}
					temp_vn.link[line-temp_nodes-2].from = Integer.parseInt(tempArray[0]);
					temp_vn.link[line-temp_nodes-2].to = Integer.parseInt(tempArray[1]);
					temp_vn.link[line-temp_nodes-2].bw = Double.parseDouble(tempArray[2]);
					
					temp_vn.revenue += temp_vn.link[line-temp_nodes-2].bw;
					System.out.println("temp_vn.revenue += temp_vn.link[line-temp_nodes-2].bw;"+temp_vn.link[line-temp_nodes-2].bw);
				}
				
				line++;	
			}
			
			req.add(temp_vn);//读取完一个虚拟网请求文件之后，将其放到请求数组中
		} catch (IOException e){
			e.printStackTrace();
		} finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                	e1.printStackTrace();
                	System.out.println("文件关闭异常");
                }
            }
        }
		
	}
	
	System.out.println("----readReqFile end--------");
}
	
	
	public void initNetworkData(String subname){
		System.out.println("*********initNetworkData***********");
				
		req = new ArrayList<VN_Request>(n);
		sub = new SN_Network();
		sub_monitor = new SN_Network();
		
		//产生v2s对象数组，初始为n个，v2s[i]记录这虚拟网络i映射的结果
		v2s = new ArrayList<ReqToSub>(n);
		for(int k=0;k<n;k++){
			ReqToSub reqtosub = new ReqToSub();
			for(int k1=0;k1<100;k1++){
				reqtosub.spath[k1] = new Path();
			}
			v2s.add(reqtosub);
		}
		
		//从xml物理网拓扑文件中读取
		readSubFromXML(subname);
		
		//从txt物理网拓扑中读取读取物理网络
		//readSubFromTXT(subname);
	
		System.out.println("Node number:"+sub.nodes+",link num:"+sub.links);
		System.out.println("*********before readReqFile***********");
		//读取虚拟网络拓扑
		readReqFile();
		System.out.println("*********readReqFile***********");
		//构建spath数组，存储最短路径，对应着原C++的数组的内存分配
		spath = new ArrayList<Shortest_Path>(sub.nodes*sub.nodes);
		for(int k=0;k<sub.nodes*sub.nodes;k++){
			Shortest_Path temp = new Shortest_Path();
			spath.add(temp);
		}
		System.out.println("*********calc_shortest_path***********");
		calc_shortest_path();
		//计算任意两个物理节点之间的最短路径的可用带宽,即为该路径中所有链路的带宽最小值
		calc_resource_for_spath();
		
		currentIndex = 0;
		//测试用
		//calc_bw_for_node(90, 0);
		//print_shortest_path(23, 43);
		sp_hop();
		System.out.println("*********init end***********");
	}
	
	public void readSubFromXML(String filename){
		SAXReader saxReader = new SAXReader();
		Document doc = null;
		try {
			doc = saxReader.read(new File(filename));
		} catch (DocumentException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		Element rootnode = doc.getRootElement();
		List nodeList = doc.selectNodes("/root/nodes/node");
		Iterator it = nodeList.iterator();
		
		sub_monitor.nodes = sub.nodes = Integer.parseInt(rootnode.attributeValue("nodes"));
		sub_monitor.links = sub.links = Integer.parseInt(rootnode.attributeValue("links"));
		int line = 0;
		Element tempnode = null;
		Element templink = null;
		while(it.hasNext()){//循环读取节点数据
			 tempnode = (Element)it.next();
			 sub_monitor.cpu[line] = sub.cpu[line] = Double.parseDouble(tempnode.attributeValue("CPU"));
			 sub_monitor.x[line] = sub.x[line] = Integer.parseInt(tempnode.attributeValue("x"));
			 sub_monitor.y[line] = sub.y[line] = Integer.parseInt(tempnode.attributeValue("y"));
			 jiedianbeifen[line] = sub.cpu[line];
			 line++;
		}
		
		List linkList = doc.selectNodes("/root/links/link");
		Iterator it2 = linkList.iterator();
		line = 0;
		while(it2.hasNext()){//循环读取链路数据
			templink = (Element)it2.next();
			if(sub.link[line] == null){
				sub.link[line] = new Link();
			}
			if(sub_monitor.link[line] == null){
				sub_monitor.link[line] = new Link();
			}
			sub_monitor.link[line].from = sub.link[line].from = Integer.parseInt(templink.attributeValue("fromNode"));
			sub_monitor.link[line].to = sub.link[line].to = Integer.parseInt(templink.attributeValue("toNode"));
			sub_monitor.link[line].bw = sub.link[line].bw = Double.parseDouble(templink.attributeValue("Bandwidth"));
			sum_bw_res += sub.link[line].bw;
			
			line++;
		}
	}
	
	public void readSubFromTXT(String filename){
		File file = null;
		BufferedReader reader = null;
		try{
			file = new File(filename);
			reader = new BufferedReader(new FileReader(file));
			String tempArray[] = new String[10]; //存储一行的多个数据 
			String temp;
			int line=1; //当前读取的行号
			
			sum_bw_res = 0;
			while((temp = reader.readLine()) !=null){
				if(line == 1){//读取底层网络的节点数和链路数,默认在第一行
					tempArray = temp.split(" ");
					
					sub.nodes = Integer.parseInt(tempArray[0]);
					sub.links = Integer.parseInt(tempArray[1]);
				} else if (line>1&&line<(sub.nodes+2)){//读取所有的节点
					
					sub.cpu[line-2] = Double.parseDouble(temp);
					
					jiedianbeifen[line-2] = sub.cpu[line-2];
					
				} else if(line>(sub.nodes+1)){//读取底层网络的链路信息
					tempArray = temp.split(" ");
					
					//对象内部对象数组的初始化
					if(sub.link[line-sub.nodes-2]==null){
						sub.link[line-sub.nodes-2] = new Link();
					}
					
					sub.link[line-sub.nodes-2].from = Integer.parseInt(tempArray[0]);
					sub.link[line-sub.nodes-2].to = Integer.parseInt(tempArray[1]);
					sub.link[line-sub.nodes-2].bw = Double.parseDouble(tempArray[2]);
					
					sum_bw_res += sub.link[line-sub.nodes-2].bw;
				}
				
				line++;
				
			}
		} catch (IOException e){
			e.printStackTrace();
		} finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                	e1.printStackTrace();
                	System.out.println("文件关闭异常");
                }
            }
        }
	}
	
	public void readReqFile(){
		//依次把虚拟网请求的文件读进来
		System.out.println("++++++++   readReqFile    +++++");
		int line = 0;     //读取的行号
		File file = null;
		BufferedReader reader = null;
		String filename = null;
		System.out.println("for start");
		
		for (int i = 0; i < n; i ++) {//requests-500-7-10-10
			filename = reqfileName+"req"+i+".txt";
			System.out.println("读来自 "+filename+"的文件");
			try{				
				file = new File(filename);
				reader = new BufferedReader(new FileReader(file));
				String tempArray[] = new String[10]; //存储一行的多个数据 
				String temp;
				line=1; //当前读取的行号
				int temp_nodes = 0;//存储虚拟网络的节点数目
				VN_Request temp_vn = new VN_Request();
				
				while((temp = reader.readLine()) !=null){
					if(line == 1){//读取虚拟网请求的参数,默认在第一行。依次为nodes,links,split,time,duration,topo
						tempArray = temp.split(" ");
						temp_vn.nodes = Integer.parseInt(tempArray[0]);
						temp_vn.links = Integer.parseInt(tempArray[1]);
						temp_vn.split = Integer.parseInt(tempArray[2]);
						temp_vn.time = Integer.parseInt(tempArray[3]);
						temp_vn.duration = Integer.parseInt(tempArray[4]);
						temp_vn.topo = Integer.parseInt(tempArray[5]);	
						
						temp_nodes = temp_vn.nodes;
						
					} else if (line>1&&line<(temp_nodes+2)){//读取所有的虚拟节点
						
						temp_vn.cpu[line-2] = Double.parseDouble(temp);
						
					} else if(line>(temp_nodes+1)){//读取虚拟网络的链路信息
						tempArray = temp.split(" ");
						
						//对象内部对象数组的初始化
						if(temp_vn.link[line-temp_nodes-2]==null){
							temp_vn.link[line-temp_nodes-2] = new Link();
						}
						temp_vn.link[line-temp_nodes-2].from = Integer.parseInt(tempArray[0]);
						temp_vn.link[line-temp_nodes-2].to = Integer.parseInt(tempArray[1]);
						temp_vn.link[line-temp_nodes-2].bw = Double.parseDouble(tempArray[2]);
						
						temp_vn.revenue += temp_vn.link[line-temp_nodes-2].bw;
					}
					
					line++;	
				}
				
				req.add(temp_vn);//读取完一个虚拟网请求文件之后，将其放到请求数组中
			} catch (IOException e){
				e.printStackTrace();
			} finally {
	            if (reader != null) {
	                try {
	                    reader.close();
	                } catch (IOException e1) {
	                	e1.printStackTrace();
	                	System.out.println("文件关闭异常");
	                }
	            }
	        }
			
		}
		
		System.out.println("----readReqFile end--------");
	}
	
	//用Floyd算法,以跳数为权重,计算所有物理节点之间的最短路径
	//输入:全局变量的物理网络sub
	//输出:任意两个物理节点i,j之间的最短路径spath[i][j]
	public void calc_shortest_path(){
		System.out.println("**********calc_shortest_path************");
		int i,j,k;
		int n = sub.nodes;
		
		for (i = 0; i < n; i ++) {
			for (j = 0; j < n; j ++) {
				if (i == j) {
					(spath.get(i*n+j)).length = 0;
					(spath.get(i*n+j)).next = i;
					(spath.get(i*n+j)).bw = 0;
				} else {
					(spath.get(i*n+j)).length = 999999999;// does this look like infinity ?
					(spath.get(i*n+j)).next = -1;
					
				}
			}
		}
		
		for (k=0; k<sub.links; k++) {
			if (sub.link[k].from == -1 && sub.link[k].to == -1) continue;
			i = sub.link[k].from;
			j = sub.link[k].to;
			(spath.get(i*n+j)).length = 1;
			(spath.get(i*n+j)).next = j;      //i到j的最短路径
			System.out.println("i="+i+"--- j="+j+"---n="+n);
			(spath.get(j*n+i)).length = 1;
			(spath.get(j*n+i)).next = i;      //j到i的最短路径
			
			(spath.get(j*n+i)).bw = sub.link[k].bw;      //j到i的带宽
			(spath.get(i*n+j)).bw = sub.link[k].bw;      //j到i的带宽
		}
		
		
		for (k=0; k<n; k++)
	    {
			for (i=0; i<n; i++)
	        {
				for (j=0; j<n; j++)
	            {
					if ( (spath.get(i*n+k)).length + (spath.get(k*n+j)).length < (spath.get(i*n+j)).length)
	                {
						// A[i][j] = A[i][k] + A[k][j];
						(spath.get(i*n+j)).length = (spath.get(i*n+k)).length+ (spath.get(k*n+j)).length;
						(spath.get(i*n+j)).next = (spath.get(i*n+k)).next;
	                }
	            }
	        }
	    }
		
	}
	
	public void calc_resource_for_spath(){
		int i,j,k;
		int len;
		double min_bw;
		
		int linkid;
		int from,to;
		for (i = 0; i < sub.nodes; i ++)
		{
			for (j = 0; j < sub.nodes; j ++)
			{
				len = (spath.get(sub.nodes * i + j)).length;	
				
				from = i;
				to = (spath.get(sub.nodes * from + j)).next;
				//System.out.println("#########"+sub.nodes+"#"+from+"#"+to+"#"+(sub.nodes * from + to)+"############");
				min_bw = (spath.get(sub.nodes * from + to)).bw;
				linkid = get_sub_link_id(from,to);
				for (k = 1; k < len; k ++)
				{
					from = to;
					to = (spath.get(sub.nodes * from + j)).next;
					if (min_bw > (spath.get(sub.nodes * from + to)).bw)
					{
						min_bw = (spath.get(sub.nodes * from + to)).bw;
					}
				}
				(spath.get(sub.nodes * i + j)).bw = min_bw;
				(spath.get(sub.nodes * i + j)).linkid = linkid;
			}
		}
		
	}
	
	
//读取单个虚拟请求文件的信息
	
	public void readFromTXT(String filename){
		System.out.println(filename);
		File file = null;
		BufferedReader reader = null;
		String fileName = filename; 
		virSub = new SN_Network();
		
		String temparray[] = new String[10];
		temparray = fileName.split("//");
		String curfile = temparray[5];
		String preName[]  = new String[4]; 
		preName = curfile.split(".txt");
		String filename1 = preName[0];
		System.out.println(filename1);
		
		BufferedWriter phyBw;
		
		try{
			//写文件
			File phyOutFile = new File("D:\\flexW\\fangzhen4\\flex_src\\XML\\newXml\\"+filename1+".xml");
			phyBw = new BufferedWriter(new FileWriter(phyOutFile));
			//读文件
			file = new File(fileName);
			reader = new BufferedReader(new FileReader(file));
			String tempArray[] = new String[10]; //存储一行的多个数据 
			String temp;
			int line=1; //当前读取的行号
			int nodesl = 0;	
			
			sum_bw_res = 0;
			while((temp = reader.readLine()) !=null){
				if(line == 1){//读取底层网络的节点数和链路数,默认在第一行
					tempArray = temp.split(" ");
					try{
						virSub.nodes = Integer.parseInt(tempArray[0]);
						nodesl = virSub.nodes;
						virSub.links = Integer.parseInt(tempArray[1]);
						phyBw.write("<"+"?xml "+"version="+"\"1.0\""+" encoding="+"\"UTF-8\""+"?>");
				    	phyBw.newLine();
				    	phyBw.newLine();
				    	phyBw.write("<root nodes="+"\""+virSub.nodes+"\""+" links="+"\""+virSub.links+"\">");
				    	phyBw.newLine();
				    	phyBw.write("  <nodes>");
				    	phyBw.newLine();
					}catch(NumberFormatException e){
						e.printStackTrace();
					}
				} else if (line>1&&line<(virSub.nodes+2)){//读取所有的节点
					
					virSub.cpu[line-2] = Double.parseDouble(temp);
					phyBw.write("    <node CPU="+"\""+virSub.cpu[line-2]+"\""+"/>");
					phyBw.newLine();
					if(line == virSub.nodes+1){
						phyBw.write("  </nodes>");
				    	phyBw.flush();
				    	phyBw.newLine();
				    	phyBw.write("  <links>");
				    	phyBw.newLine();
					}
					
//					jiedianbeifen[line-2] = virSub.cpu[line-2];
					
				} else if(line>(virSub.nodes+1)){//读取底层网络的链路信息
					tempArray = temp.split(" ");
					
					//对象内部对象数组的初始化
					if(virSub.link[line-virSub.nodes-2]==null){
						virSub.link[line-virSub.nodes-2] = new Link();
					}
					
					virSub.link[line-virSub.nodes-2].from = Integer.parseInt(tempArray[0]);
					virSub.link[line-virSub.nodes-2].to = Integer.parseInt(tempArray[1]);
					virSub.link[line-virSub.nodes-2].bw = Double.parseDouble(tempArray[2]);
//					System.out.println(virSub.link[line-virSub.nodes-2].bw);
					phyBw.write("    <link fromNode="+"\""+virSub.link[line-virSub.nodes-2].from+"\""+" toNode="+"\""+virSub.link[line-virSub.nodes-2].to+"\""+" Bandwidth="+"\""+virSub.link[line-virSub.nodes-2].bw+"\""+"/>");
					phyBw.newLine();
					if(line == (virSub.nodes+virSub.links+1)){
						phyBw.write("  </links>");
			    		phyBw.newLine();
			    		phyBw.write("</root>");
			    		phyBw.flush();
			    		phyBw.close();
					}
					sum_bw_res += virSub.link[line-virSub.nodes-2].bw;
				}
				
				line++;
				
			}
			System.out.println(virSub);
		} catch (IOException e){
			e.printStackTrace();
		} finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                	e1.printStackTrace();
                	System.out.println("文件关闭异常");
                }
            }
        }
	}
	
	//计算物理节点from和to所确定的物理链路的编号,如果不存在物理链路[from, to],则返回-1
	public int get_sub_link_id(int from, int to){
		int i;
		for (i = 0; i < sub.links; i++)
		{
			if ((sub.link[i].from == from && sub.link[i].to == to)||(sub.link[i].from == to && sub.link[i].to == from))
				return i;
		}
		return -1;
	}
	
	//与sf相连，但是另一个终点不是st的链路的带宽只和
	public double calc_bw_for_node(int sf, int st){
		
		double bw = 0.0;
		int from, to;
		for (int i = 0; i < sub.links; i ++)
		{
			from = sub.link[i].from;
			to = sub.link[i].to;
			if ((from == sf && to != st) || (to == sf && from != st))
			{
				bw += sub.link[i].bw;
			}
		}
		//printf("%lf\n", bw);
		return bw;
	}
	
	//统计所有最短路径的条数
	public void sp_hop(){
		int[] sp = new int[10];
		for (int i = 0; i < sub.nodes; i ++)
		{
			for (int j = i + 1; j < sub.nodes; j++)
			{
				int len = (spath.get(sub.nodes * i + j)).length;
				sp[len] ++;
			}
		}
		for (int k = 0; k < 10; k ++)
		{
			System.out.println(k+"跳: "+sp[k]);
		}
	}
	
	public void print_shortest_path(int from,int to){
		
		int[] path = new int[1000];
	    int i;
	    int length = (spath.get(sub.nodes * from + to)).length;
	    i = 0;
	    path[i] = from;
	    
	    System.out.println("路径的带宽: "+(spath.get(sub.nodes * from + to)).bw);
		
		while (from != to)
	    {
			System.out.println("from="+from+"; to="+to+"; linkid="+(spath.get(sub.nodes * from + to)).linkid+",");
	        i++;
	        from = (spath.get(sub.nodes * from + to)).next;
	        path[i] = from;    
		}
		System.out.print("最短路径是:");
	    path[i] = to;
		
		
	    for ( i = 0; i < length; i++)
	    {
	    	System.out.print(path[i]+"->");
	    }
	    System.out.println(path[i]);
	}
	
	
	public int test_embededRequest(int reqid){
		System.out.println("********test_embededRequest*********");
		vital = 0;
		int flag = -1;

		int hop = 2;
		linkHop = 1;
		while(linkHop <= hop)
		{
			do 
			{
				//reqid == 1628 && vital == 94时死循环
				embedRequest(reqid);
				flag = check_is_failed(reqid);
				System.out.println("flag="+flag);
				if (flag == 1)		//失败了
				{
					remove_failed_map_of_current(reqid);
				}
				vital ++;
			} while (flag == 1 && vital < sub.nodes);
			if (flag == 1 && vital >= sub.nodes)		//失败了
			{
				linkHop ++;
				vital = 0;
				//remove_failed_map_of_current(reqid);
			}
			else if (flag == -1)
			{
				break;
			}
		}
		
		//当flag = -1时表示成功映射
		//当flag = 1表示失败

		System.out.println("vital="+vital);
		if (flag == -1)
		{//1表示成功
			//print_map(reqid);
			return 1;
		}
		else
		{
			System.out.println("失败");
			return -1;		
		}
	}
	
	public void embedRequest(int index){
		
		//System.out.println("*************embedRequest**************");
		int i,j,k;
		currentIndex = index;
		mappedLinkList.clear();
		mappedNodeList.clear();			//清空已经映射的虚拟节点列表
		mappedSubNodeList.clear();		//清空已经映射的物理节点列表
		unmappedNodeList.clear();
		unmappedLinkList.clear();
		unmappedSubNodeList.clear();

		//要映射的虚拟网络是r 	************************注意此处
		VN_Request r = req.get(index);	
		
		for (i = 0; i < r.nodes; i ++)
		{
			unmappedNodeList.add(i);
		}
		for (i = 0; i < r.links; i ++)
		{
			unmappedLinkList.add(i);
		}
		/************************************************************/
		//unmappedSubNodeList与论文中的集合S对应
		//S = {vital, vital + 1, ..., sub.nodes - 1}
		//因此unmappedSubNodeList被初始化为S,代码如下
		/************************************************************/
		for (i = vital; i < sub.nodes; i ++)
		{
			unmappedSubNodeList.add(i);
		}

		
		int firstLinkIndex;
		int vf,vt,sf,st;
		double vbw;
		int firstLinkMappedflag = -1;
		Vector<Integer> candSubNodeList = new Vector<Integer>();
		int flag = -1;
		while (mappedLinkList.size() != r.links)
		{
			//获取最大独立链路
			firstLinkIndex = get_free_link();
			System.out.println("最大独立链路:"+firstLinkIndex);//888888888888888888888888888888888888888
			if (firstLinkIndex != -1)
			{
				vf = r.link[firstLinkIndex].from;
				vt = r.link[firstLinkIndex].to;
				vbw = r.link[firstLinkIndex].bw;
				//System.out.println("映射虚拟链路:link="+firstLinkIndex+" "+vf+" "+vt+" "+vbw);//888888888888888
	
				firstLinkMappedflag  = -1;
				//System.out.println("0未映射节点数："+unmappedNodeList.size()+",未映射链路数："+unmappedLinkList.size());
				//关系矩阵中的一半
				for (int a = 0; a < unmappedSubNodeList.size(); a ++)
				{
					for (int b=a+1; b < unmappedSubNodeList.size(); b++)
					{
						i = unmappedSubNodeList.get(a);
						j = unmappedSubNodeList.get(b);
						
						if (spath.get(i * sub.nodes + j).length == linkHop && calc_resource_for(i, j) >= vbw)
						{
							if ((sub.cpu[i] > r.cpu[vf] && sub.cpu[j] > r.cpu[vt]) || (sub.cpu[i] > r.cpu[vt] && sub.cpu[j] > r.cpu[vf]))
							{
								sf = i;
								st = j;
								//System.out.println("映射到:");//888888888888888888888888888888
								//print_shortest_path(i,j);//88888888888888888888888888888888888

								//这两个if是为了保证简单的节点负载均衡
								//基本原则是:将需求大的虚拟节点映射到资源量充足的物理节点上
								if (r.cpu[vf] < r.cpu[vt])
								{
									int ab = vf;
									vf = vt;
									vt = ab;
								}
								if (sub.cpu[sf] < sub.cpu[st])
								{
									int ab = sf;
									sf = st;
									st = ab;
								}
								//System.out.println("sf="+sf+" vf="+vf+" st="+st+" vt="+vt);
								add_node_map(null, v2s,sf,index,vf);
								add_node_map(null, v2s,st,index,vt);
								updateNodeData(sf,vf);
								updateNodeData(st,vt);
								//may be
								//a --;
								//b --;
								//System.out.println("1未映射节点数："+unmappedNodeList.size()+",未映射链路数："+unmappedLinkList.size()+","+unmappedLinkList.get(0)+" "+unmappedLinkList.get(1));
								//System.out.println("firstLinkIndex="+firstLinkIndex);
								updateLinkData(firstLinkIndex);
								//System.out.println("2未映射节点数："+unmappedNodeList.size()+",未映射链路数："+unmappedLinkList.size()+","+unmappedLinkList.get(0));
								//把路径记录下来，表示映射
								

								//把物理路径记录下来,需要做到:将虚拟链路freeLinkIndex映射到的物理路径(sf,st)的所有物理链路记录下来
								int tempsf = sf;
								do
								{
									//spath[tempsf * sub.nodes + tempst].bw -= r.link[k].bw;
									//printf("%d,%d,linkid = %d,",tempsf,st,spath[tempsf * sub.nodes + st].linkid);
									add_link_map(null, spath.get(tempsf * sub.nodes + st).linkid,index,firstLinkIndex);
									tempsf = spath.get(tempsf * sub.nodes + st).next;
								}while (tempsf != st);

								firstLinkMappedflag = 1;
								break;
							}
						}
					}
					if (firstLinkMappedflag == 1)
					{
						//firstLinkMappedflag = -1;	
						break;
					}
//	 				else if (a == unmappedSubNodeList.size() - 1)
//	 				{
//	 					printf("\n*************选择的链路映射失败！**************");
//	 					return;
//	 				}
				}
				if (firstLinkMappedflag == -1)
				{
					System.out.println("\n*************选择的链路映射失败！**************");
					return;
				}

				//printf("\n---------------------------\n");
				//System.out.println("2未映射节点数："+unmappedNodeList.size()+",未映射链路数："+unmappedLinkList.size());
				//开始映射与当前自由组件相连接的受限链路
				Vector<Integer> unmappedLinkListCopy = new Vector<Integer>();
				unmappedLinkListCopy.clear();
				for (i = 0; i < unmappedLinkList.size(); i ++)
				{
					unmappedLinkListCopy.add(unmappedLinkList.get(i));
				}
				for (i = 0; i < unmappedLinkListCopy.size(); i ++)
				{
					k = unmappedLinkListCopy.get(i);
					int v = -1;
					int tempsf;
					if ((r.link[k].from == vf && r.link[k].to != vt)||(r.link[k].to == vf && r.link[k].from != vt)||(r.link[k].from != vf && r.link[k].to == vt)||(r.link[k].to != vf && r.link[k].from == vt))
					{
						//优化->把与之相连的链路单独计算
						//System.out.println("\n映射相连接的虚拟链路:link=%d(%d,%d;%lf)",k, r.link[k].from,r.link[k].to, r.link[k].bw);

						if ((r.link[k].from == vf && r.link[k].to != vt)||(r.link[k].to != vf && r.link[k].from == vt))
						{
							v = r.link[k].to;
							tempsf = v2s.get(index).snode[r.link[k].from];
						}
						else {
							v = r.link[k].from;
							tempsf = v2s.get(index).snode[r.link[k].to];
						}
						if (hasMapped(v))
						{
							continue;
						}
						//映射v
						candSubNodeList.clear();
						for (j = 0; j < unmappedSubNodeList.size(); j ++)
						{
							int b = unmappedSubNodeList.get(j);
							if (sub.cpu[b] > r.cpu[v])
							{
								candSubNodeList.add(b);
							}
						}
						//可选节点均满足CPU要求

						System.out.println("\nv="+v+"可选"+candSubNodeList.size()+"个节点");
						for(int kk=0;kk<mappedNodeList.size();kk++){
							System.out.print(mappedNodeList.get(kk)+" ");
						}
						System.out.print("###");
						for(int kk=0;kk<mappedSubNodeList.size();kk++){
							System.out.print(mappedSubNodeList.get(kk)+" ");
						}
						
						//System.out.println("1未映射节点数："+unmappedNodeList.size()+",未映射链路数："+unmappedLinkList.size());
						//花费
						double bw_cost = 0;
						flag = -1;
						double min_bw_cost = 99999;
						int selectedNode = -1;
						for (int b = 0; b < candSubNodeList.size(); b ++)
						{
							bw_cost = 0;
							//对于每一个可选的物理节点
							int cand_s_n = candSubNodeList.get(b);
							for (int c = 0; c < mappedNodeList.size(); c++)
							{
								//映射的虚拟节点
								int v_node = mappedNodeList.get(c);
								//该虚拟节点映射到的物理节点
								int s_node = mappedSubNodeList.get(c);
								
								//if(b<5){	System.out.print("v_node="+v_node+" s_node="+mappedSubNodeList.get(c)+"# "); }
								
								Vector<Integer> mappedNodeListCopy = new Vector<Integer>();
								for (int kaobeiyiyingshejiediandei = 0; kaobeiyiyingshejiediandei < mappedNodeList.size(); kaobeiyiyingshejiediandei++)
								{
									mappedNodeListCopy.add(mappedNodeList.get(kaobeiyiyingshejiediandei));
								}
								for (int d = 0; d < r.links; d++)
								{
									if ((r.link[d].from == v && r.link[d].to == v_node)||(r.link[d].from == v_node && r.link[d].to == v))
									{
										//printf("**********%d(%d,%d)\n",k,v,v_node);
										//计算可选节点cand_s_n与所有与v相连的虚拟节点所映射到的物理节点的带宽消耗，去最小的。
										//if (spath[sub.nodes * cand_s_n + s_node].bw >= r.link[d].bw)
										if (calc_resource_for(cand_s_n, s_node) >= r.link[d].bw)
										{
											bw_cost += spath.get(sub.nodes * cand_s_n + s_node).length*r.link[d].bw;
										}
										else
										{
											//printf("\n%lf,%lf",spath[sub.nodes * cand_s_n + s_node].bw,r.link[d].bw);
											//表示这个可选节点不可用
											flag = 1;
											break;
										}
									}
								}
								if (flag == 1)
								{
									break;
								}
							}
							
							if (flag == 1)
							{
								flag = -1;
								continue;
							}
							if (bw_cost < min_bw_cost)
							{
								min_bw_cost = bw_cost;
								selectedNode = cand_s_n;
							}
						}
						//System.out.println("1未映射节点数："+unmappedNodeList.size()+",未映射链路数："+unmappedLinkList.size());
						//遍历完所有的可选节点之后
						if (selectedNode != -1)
						{
							//printf("\n成功映射相连的%d-》%d",v,selectedNode);
							System.out.println("成功映射相连的"+v+"->"+selectedNode);
							add_node_map(null,v2s,selectedNode,index,v);
							updateNodeData(selectedNode, v);
							updateLinkData(k);
							//System.out.println("1未映射节点数："+unmappedNodeList.size()+",未映射链路数："+unmappedLinkList.size());
							//tempsf:映射的链路的物理节点
							//tempst:端点
							int tempst = selectedNode;
							do
							{
								//spath[tempsf * sub.nodes + tempst].bw -= r.link[k].bw;
								//printf("\n%d,%d,linkid = %d;",tempsf,tempst,spath[tempsf * sub.nodes + tempst].linkid);

								add_link_map(null, spath.get(tempsf * sub.nodes + tempst).linkid,index,k);
								tempsf = spath.get(tempsf * sub.nodes + tempst).next;
							}while (tempsf != tempst);
						}
						else
						{
							System.out.println("\n----在映射相连的链路时，失败!-----");
							return;
						}
					}
				
				}





			}
			//没有独立的链路可选择了
			else 
			{
				break;
			}
		}
		//System.out.println("2未映射节点数："+unmappedNodeList.size()+",未映射链路数："+unmappedLinkList.size());
		embedUnmappedNodes();	//映射虚拟网络中未映射的受限组件和固定组件
		embedUnmappedLinks();	//映射那些两个端点已经映射，但是链路还没有映射的，所以放在最后面
		
	}
	
	//获取下一个要映射的虚拟链路，选出最大带宽且未映射的那一个链路
	public int get_free_link(){
		double max_v_bw=0.0;
		int index;
		int vf,vt;
		double vbw;
		int firstLinkIndex = -1;
		VN_Request r = req.get(currentIndex);
		for (int i = 0; i < unmappedLinkList.size(); i ++){
			index = unmappedLinkList.get(i);
			vf = r.link[index].from;
			vt = r.link[index].to;
			vbw = r.link[index].bw;
			
			//这个链路的两个节点都还没有映射，确保独立链路
			int j;
			for (j = 0; j < mappedNodeList.size(); j++){
				if (vf == mappedNodeList.get(j) || vt == mappedNodeList.get(j)){
					break;
				}
			}
			if (j < mappedNodeList.size()) {
				continue;
			}
			if (vbw > max_v_bw) {
				max_v_bw = vbw;
				firstLinkIndex = index;
	        }
		}

		return firstLinkIndex;
	}
	
	//检查编号为reqid的虚拟网络是否映射失败
	public int check_is_failed(int reqid){
		int i, j;
		int isfailed = -1;
		for (i = reqid; i <= reqid; i ++)
		{
			for (j = 0; j < req.get(i).links; j ++)
			{
				int vfrom = req.get(i).link[j].from;
				int vto = req.get(i).link[j].to;
				int sfrom = v2s.get(i).snode[vfrom];
				int sto = v2s.get(i).snode[vto];
				if (calc_resource_for(sfrom, sto) < 0)
				{
					isfailed = 1;
				}
			}
		}
		if (mappedLinkList.size() != req.get(reqid).links || unmappedNodeList.size() != 0)
		{
			isfailed = 1;
		}
		return isfailed;
		
		//返回1表示失败
	}
	
	public void remove_failed_map_of_current(int reqid){
		//恢复节点
		int vnode;
		int snode;
		int i=0;
		for (i = 0; i < mappedNodeList.size(); i ++)
		{
			vnode = mappedNodeList.get(i);
			snode = v2s.get(reqid).snode[vnode];
			sub.cpu[snode] += req.get(reqid).cpu[vnode];
			//v2s[reqid].snode[vnode] = -1;
			//remove_node_map(s2v_n, snode, reqid);
		}
		
		//恢复链路
		int slink;
		for (i = 0; i < req.get(reqid).links; i ++)
		{
			int len = v2s.get(reqid).spath[i].len;
			for (int j = 0; j < len; j ++)
			{
				slink = v2s.get(reqid).spath[i].link[j];
				sub.link[slink].bw += req.get(reqid).link[i].bw;
				//remove_link_map(s2v_l, slink, reqid);
			}
			v2s.get(reqid).spath[i].len = 0;
		}
	}
	
	//计算物理网络中的物理节点i,j之间的最短路径的可用带宽资源
	//物理路径的可用带宽资源是物理路径上的所有链路带宽的最小值
	public double calc_resource_for(int i, int j){
		int len = spath.get(sub.nodes * i + j).length;	
		
		int from = i;
		int to = spath.get(sub.nodes * from + j).next;
		int linkid = get_sub_link_id(from,to);
	//	System.out.println(linkid);
		double min_bw=0.0;
		if(linkid == -1){//当linkid为-1时，from和to之间没有link，此时其可用带宽为0；
			min_bw = 0.0;
		}else{
			min_bw = sub.link[linkid].bw;
		}
		for (int k = 1; k < len; k ++)
		{
			from = to;
			to = spath.get(sub.nodes * from + j).next;
			linkid = get_sub_link_id(from,to);
			if (min_bw > sub.link[linkid].bw)
			{
				min_bw = sub.link[linkid].bw;
			}
		}
		return  min_bw;
	}
	
	public void add_node_map(ArrayList<S2V_node> s2v_n,ArrayList<ReqToSub> v2s,int snode,int reqid,int nodeid){
		v2s.get(reqid).snode[nodeid] = snode;
		sub.cpu[snode] -= req.get(reqid).cpu[nodeid];
		
		//System.out.println("v2s.get(reqid).snode[nodeid]="+v2s.get(reqid).snode[nodeid]);
		//System.out.println("sub.cpu[snode]="+sub.cpu[snode]+"  snode="+snode);
	}
	
	public void add_link_map(ArrayList<S2V_link> s2v_l, int slink, int reqid, int vlink){
		int index = v2s.get(reqid).spath[vlink].len;
		v2s.get(reqid).spath[vlink].len ++;
		v2s.get(reqid).spath[vlink].link[index] = slink;
		//资源的消耗
		
		DecimalFormat df = new DecimalFormat("#.000000");//Double保留六位小数
		Double temp = sub.link[slink].bw - req.get(reqid).link[vlink].bw;
		sub.link[slink].bw = Double.valueOf(df.format(temp));
		
		//sub.link[slink].bw -= req.get(reqid).link[vlink].bw;
	}
	
	public void updateNodeData(int snode,int vnode){
		mappedNodeList.add(vnode);
		mappedSubNodeList.add(snode);
		
		for(int k=0;k<unmappedNodeList.size();k++){
			if(unmappedNodeList.get(k) == vnode){
				unmappedNodeList.remove(k);
				break;
			}
		}
		
		for(int k=0;k<unmappedSubNodeList.size();k++){
			if(unmappedSubNodeList.get(k) == snode){
				unmappedSubNodeList.remove(k);
				break;
			}
		}
		
	}
	
	//从未映射链路集合中删除刚映射的链路
	public void updateLinkData(int vlink){
		mappedLinkList.add(vlink);
		for(int k=0;k<unmappedLinkList.size();k++){
			if(unmappedLinkList.get(k)==vlink){
				unmappedLinkList.remove(k);
				break;
			}
		}
	}
	
	public Boolean hasMapped(int v)
	{
		int i=0;
		for (i = 0; i < mappedNodeList.size(); i ++)
		{
			if (mappedNodeList.get(i) == v)
			{
				break;
			}
		}
		return !(i == mappedNodeList.size());
	}
	
	
	public void embedUnmappedNodes(){
		int i,j;
		int v;
		//映射v
		VN_Request r = req.get(currentIndex);
		Vector<Integer> candSubNodeList = new Vector<Integer>();
		//System.out.println("embedUnmappedNodes---unmappedNodeList"+unmappedNodeList.size());
		for (i = 0; i < unmappedNodeList.size(); i ++)
		{
			v = unmappedNodeList.get(i);
			candSubNodeList.clear();
			for (j = 0; j < unmappedSubNodeList.size(); j ++)
			{
				int b = unmappedSubNodeList.get(j);
				if (sub.cpu[b] > r.cpu[v])
				{
					candSubNodeList.add(b);
				}
			}
			//可选节点均满足CPU要求

			//System.out.println("embedUnmappedNodes()----v="+v+"可选"+candSubNodeList.size()+"个节点");
		
			//花费
			double bw_cost = 0;
			int flag = -1;
			double min_bw_cost = 99999;
			int selectedNode = -1;
			for (int b = 0; b < candSubNodeList.size(); b ++)
			{
				bw_cost = 0;
				//对于每一个可选的物理节点
				int cand_s_n = candSubNodeList.get(b);
				for (int c = 0; c < mappedNodeList.size(); c++)
				{
					//映射的虚拟节点
					int v_node = mappedNodeList.get(c);
					//该虚拟节点映射到的物理节点
					int s_node = mappedSubNodeList.get(c);
					for (int d = 0; d < r.links; d ++)
					{
						//节点v_node与节点v相连
						if ((r.link[d].from == v && r.link[d].to == v_node)||(r.link[d].from == v_node && r.link[d].to == v))
						{
							//printf("**********%d(%d,%d)\n",k,v,v_node);
							//计算可选节点cand_s_n与所有与v相连的虚拟节点所映射到的物理节点的带宽消耗，去最小的。
							//if (spath[sub.nodes * cand_s_n + s_node].bw >= r.link[d].bw)
							if (calc_resource_for(cand_s_n, s_node) >= r.link[d].bw)
							{
								bw_cost += spath.get(sub.nodes * cand_s_n + s_node).length * r.link[d].bw;
							}
							else
							{
	
								//printf("\n%lf,%lf",spath[sub.nodes * cand_s_n + s_node].bw,r.link[d].bw);
								//表示这个可选节点不可用
								flag = 1;
								break;
							}
						}
					}
					if (flag == 1)
					{
						break;
					}
				}
				
				if (flag == 1)
				{
					flag = -1;
					continue;
				}
				if (bw_cost < min_bw_cost)
				{
					min_bw_cost = bw_cost;
					selectedNode = cand_s_n;
				}
			}
			//遍历完所有的可选节点之后
			if (selectedNode != -1)
			{

				//printf("\n成功映射相连的%d-》%d",v,selectedNode);
	
				add_node_map(null,v2s,selectedNode,currentIndex,v);
				updateNodeData(selectedNode, v);
				i --;
			}
			else
			{
				System.out.println("\n----在映射相连的链路时，失败!----\n");
			}
			//  天才
		}
	}
	
	public void embedUnmappedLinks(){
		
		int vf,vt;
		int sf,st;
		VN_Request r = req.get(currentIndex);

		//未映射的链路:
		for (int i = 0; i < unmappedLinkList.size(); i ++)
		{
			vf = r.link[unmappedLinkList.get(i)].from;
			vt = r.link[unmappedLinkList.get(i)].to;
			//两种情况:
			//1.有刚好一个端点已经映射
			//2.两个端点都已经映射，但是链路还没有映射
			sf = v2s.get(currentIndex).snode[vf];		
			st = v2s.get(currentIndex).snode[vt];
			
			int tempsf = sf;
			int tempst = st;
			do
			{
				//spath[tempsf * sub.nodes + tempst].bw -= r.link[unmappedLinkList[i]].bw;
				//printf("\n%d,%d,linkid = %d;",tempsf,tempst,spath.get(tempsf * sub.nodes + tempst).linkid);
	
				add_link_map(null, spath.get(tempsf * sub.nodes + tempst).linkid,currentIndex,unmappedLinkList.get(i));
				tempsf = spath.get(tempsf * sub.nodes + tempst).next;
			}while (tempsf != tempst);
			updateLinkData(unmappedLinkList.get(i));
			i--;//这是因为上面的这个函数减小了向量的大小

		}
	}
	
	public SimulateResult simulate_emebeddRequest(int stime,String subfilename){
		
		int i,j;
		System.out.println("*********simulate_emebeddRequest***********");
		int flag;
		int[] iswrite = new int[1000];
		successList.clear();
		failList.clear();
		System.out.println("********0000000*********");
		
		String subfileSplit[] = new String[5];
		System.out.println("********1111111********* subfilename="+subfilename);
		subfileSplit = subfilename.split("\\.");//"."为特殊字符，需要加转义字符
		
		System.out.println("********222********* subfileSplit[0]="+subfileSplit[0]);
		String embedOutName = subfileSplit[0]+"-embed-out.xml";   //针对不同的底层拓扑文件需要设定不同的映射结果输出文件名
		
		System.out.println("********11133331111*********");
		
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		long start = 0L;//开始时间
		long end = 0L;//结束时间
		DecimalFormat df = new DecimalFormat("#.000000");//Double保留六位小数
				
		time = 0;
		success = 0;
		arrived = 0;
		successTime = 0.0;
		failTime = 0.0;
		rate = 0.0;
		vital_sum_fail = 0;
		vital_sum_success = 0;
		RCompareToC_sum = 0.0;

		R = 0.0;
		C = 0.0;
//		System.out.println("********11222221111*********");
		
		//创建document对象
		 Document document = DocumentHelper.createDocument();	//XML
		 //定义根节点Element
		 Element rootGen = document.addElement("root");			//XML
		
	//	System.out.println("********222222*********");
		System.out.println("n= "+n);
		while(true)
		{
			time ++;
			
			for (i = 0; i < n; i++)			//每一个时刻扫描所有的vn
			{
				//System.out.println("n= "+n);
				if (time == req.get(i).time)	//表示第i个请求到达了
				{
					arrived ++;
					System.out.println("\n**************************时间=time,"+i+"个请求到达..........");
					
					start = System.nanoTime();//记录开始时间
					test_embededRequest(i);
					end = System.nanoTime();//记录结束时间
					
					double f = (Double)((end-start)/1000000000.0);
					double elapsed = Double.valueOf(df.format(f));//将时间差保留六位小数记录到变量elapsed中
					
					flag = check_is_failed(i);
					if (flag != 1)
					{
						success ++;
						successList.add(i);
						successTime += elapsed;
						vital_sum_success += vital;
						print_map_to_file(i,rootGen);  //将映射结果写到输出文件中
						
						sub_Monitor(rootGen);
						
						R += calc_sum_revnue(i, 1.0);
						C += calc_sum_cost(i);
						RCompareToC_sum += (calc_bw_cost(i) / calc_sum_bw(i));
					}
					else
					{
						vital_sum_fail += vital;
						failTime += elapsed;
						failList.add(i);
					}
				}
				if (time == (req.get(i).time + req.get(i).duration))
				{
					//第i个请求达到了持续时间，需要移除
					j = successList.size();
					
					for(int it=0;it<j;it++){
						if(successList.get(it) == i){
							successList.remove(it);
							break;
						}
					}
					if (j > successList.size())
					{
						//如果是成功映射，那就移除把
						remove_map(i);
						write_leave_req(i,rootGen);
					}
				}

				if (time < req.get(i).time)		//时间还不够长，没有请求到达
				{
					break;
				}
			}
			
			if (time % 100 == 0 && arrived > 0 && iswrite[time / 100] == 0)
			{
				iswrite[time / 100] = 1;
				//fenxi_resourceUseRate(time);
				time_cost_output();
			}
			

			
			if (i == n && successList.size() == 0)
			{
				break;
			}
			rate = success / (arrived * 1.0);
			if (arrived >= 50)
			{
				if (rate < minRate)
				{
					minRate = rate;
				}
				if (rate > maxRate)
				{
					maxRate = rate;
				}
			}
		//	System.out.println("当前接受率:"+rate+", 最小接受率:"+minRate+"最大接受率:"+maxRate);
			
			//Sleep(stime);
	//		System.out.println("----------------------------时间流逝...");
	
		}
		
		//wuwuwuwuw
		SimulateResult sr = new SimulateResult();
		
		//将分析结果写入XML输出文件中
		sr = print_evaluation_XML(sr,rootGen);
		
		try{
			//开始将document写入XML文件中
			 OutputFormat format = null;							//XML
			 XMLWriter xmlwriter = null;							//XML
			 format = OutputFormat.createPrettyPrint();
			 //设定编码
			 format.setEncoding("UTF-8");
			 
			 /* 路径修改啊*/
			 xmlwriter = new XMLWriter(new FileOutputStream("D:\\flexW\\fangzhen4\\flex_src\\XML\\embedOut\\"+embedOutName), format);
			 System.out.println("映射输出文件为："+"D:\\flexW\\fangzhen4\\flex_src\\XML\\embedOut\\"+embedOutName);
			 
			 
			 xmlwriter.write(document);
		//	 xmlwriter.write(rootGen);
			 xmlwriter.flush();
			 xmlwriter.close();
		} catch (Exception e) {
			 e.printStackTrace();
			 System.out.println("-----------Exception occured during of create xmlfile -------");
		}
		print_fail_list_to_file();
		
		return sr;
		
	}
	
	public int print_map_to_file(int reqid,Element rootGen)
	{
		
		String subname;
		//subname =reqfileName+"count.embed.out.txt";
		subname = "D:\\flexW\\count.embed.out1.txt";
		//subname2 = "D:\\count.embed.out.xml";
		//subname2 = "./flex_src/embed-out.xml";
		
		int i,j, k;
		
		BufferedWriter writer = null;
		 
		//添加VN节点
		Element vnGen = rootGen.addElement("VN");				//XML
		vnGen.addAttribute("label",reqid+"");					//XML
		 
		int isfailed = -1;
		try{
			writer = new BufferedWriter(new FileWriter(new File(subname), true));
			for (i = reqid; i <= reqid; i ++)
			{
				writer.write("------------------------------"+i+"------------------------------");
				writer.newLine();
				
				//在文件中打印节点映射结果
				Element nodes = vnGen.addElement("nodes");					//XML
				for (j = 0; j < req.get(i).nodes; j ++)
				{
					writer.write(i+"."+j+"<-->"+v2s.get(i).snode[j]);
					writer.newLine();
					
					Element node = nodes.addElement("node");				//XML
					node.addAttribute("label",j+"" );						//XML
					node.addAttribute("x",sub.x[v2s.get(i).snode[j]]+"");	//XML
					node.addAttribute("y",sub.y[v2s.get(i).snode[j]]+"");	//XML
					node.addAttribute("toNode",v2s.get(i).snode[j]+"" );	//XML
				}
										
				//打印链路映射结果
				Element links = vnGen.addElement("links");					//XML
				for (j = 0; j < req.get(i).links; j ++)
				{
					int vfrom = req.get(i).link[j].from;
					int vto = req.get(i).link[j].to;
					int sfrom = v2s.get(i).snode[vfrom];
					int sto = v2s.get(i).snode[vto];
					//首先打印虚拟链路请求
					writer.write("link "+j+"("+req.get(i).link[j].from+","+req.get(i).link[j].to+";"+req.get(i).link[j].bw+") ");
					
					Element link = links.addElement("link");						//XML
					link.addAttribute("label", j+"");								//XML
					link.addAttribute("fromNode", req.get(i).link[j].from+"");		//XML
					link.addAttribute("toNode", req.get(i).link[j].to+"");			//XML
					link.addAttribute("BandwidthNeed", req.get(i).link[j].bw+"");	//XML
					
					for (k = 0; k < v2s.get(i).spath[j].len - 1; k ++) //如果映射到多条物理链路上，打印除第一条链路之外的剩余链路
					{
						writer.write(v2s.get(i).spath[j].link[k]+"("+sub.link[v2s.get(i).spath[j].link[k]].from+","+sub.link[v2s.get(i).spath[j].link[k]].to+")->");
						
						Element linkmap = link.addElement("linkmap");						//XML
						linkmap.addAttribute("toLink", v2s.get(i).spath[j].link[k]+"");		//XML
					}
					if (k == v2s.get(i).spath[j].len - 1)//答应映射的物理路径的第一个链路
					{
						writer.write(v2s.get(i).spath[j].link[k]+"("+sub.link[v2s.get(i).spath[j].link[k]].from+","+sub.link[v2s.get(i).spath[j].link[k]].to+");"+
								calc_resource_for(sfrom, sto)+"("+spath.get(sub.nodes * sfrom + sto).bw+")");
						
						Element linkmap = link.addElement("linkmap");						//XML
						linkmap.addAttribute("toLink", v2s.get(i).spath[j].link[k]+"");		//XML
					}
					if (calc_resource_for(sfrom, sto) < 0)
					{
						isfailed = 1;
						writer.write("请求路径分割");
					}
					writer.newLine();
				}
				double vn_sum_bw = 0;
				double consume_sum_bw = 0;
				int f,t;
				for (j = 0; j < req.get(i).links; j ++)
				{
					vn_sum_bw += req.get(i).link[j].bw;
					f = req.get(i).link[j].from;
					t = req.get(i).link[j].to;
					f = v2s.get(i).snode[f];		
					t = v2s.get(i).snode[t];
					consume_sum_bw += spath.get(sub.nodes * f + t).length * req.get(i).link[j].bw;
				}
				
				DecimalFormat df = new DecimalFormat("#.000000");//Double保留六位小数
				vn_sum_bw = Double.valueOf(df.format(vn_sum_bw));
				consume_sum_bw = Double.valueOf(df.format(consume_sum_bw));
				
				//fprintf(fp,"总共需要的带宽:%lf,总共消耗的带宽:%lf,消耗比例:%lf\n",vn_sum_bw,consume_sum_bw, consume_sum_bw / vn_sum_bw);
				writer.write("needed bw:"+vn_sum_bw+",cost bw:"+consume_sum_bw+",cost/needed:"+Double.valueOf(df.format(consume_sum_bw / vn_sum_bw)));
				writer.newLine();
				
				Element bw = vnGen.addElement("bw");										//XML
				bw.addAttribute("needed", vn_sum_bw+"");									//XML
				bw.addAttribute("cost", consume_sum_bw+"");									//XML
				bw.addAttribute("cost-needed", df.format(consume_sum_bw / vn_sum_bw));		//XML	
				
				//创建document对象
				 Document alarmDocument = DocumentHelper.createDocument();	//XML
				 //定义根节点Element
		//		 Element alarmRoot = alarmDocument.addElement("root");			//XML
				 Element alarmMy = alarmDocument.addElement("alrams");
				 Element alarmMyNodes = alarmMy.addElement("nodeAlarms");
				 Element alarmMyLinks = alarmMy.addElement("linkAlarms");
 				
				//在每个虚拟网被成功映射之后，遍历检测一次底层网络；
				Element alarms = vnGen.addElement("alarms");
				Element alarmNodes = alarms.addElement("alarmNodes");
				Element alarmLinks = alarms.addElement("alarmLinks");
				
				//物理节点、链路性能数据
				Document subnodeDoc = DocumentHelper.createDocument();
				Element rootNode = subnodeDoc.addElement("root");
				Element subnodeEl = rootNode.addElement("nodes");
				
				Document sublinkDoc = DocumentHelper.createDocument();
				Element rootLink = sublinkDoc.addElement("root");
				Element sublinkEl = rootLink.addElement("links");
				
				//首先遍历底层网络的节点信息
				double temp = 0.000000;
				double alarmLine = 0.95;
				int alarmCount = 0;
				for(int index=0;index<sub_monitor.nodes;index++){
					temp = (1.0 - (sub.cpu[index]/sub_monitor.cpu[index]));
					
					Element subnode = subnodeEl.addElement("node");
					subnode.addAttribute("id", index+"");
					subnode.addAttribute("cpu", sub.cpu[index]+"");
				    float tmp = (float) temp; 
					subnode.addAttribute("utilization",String.valueOf(tmp) );			
					
					if(temp>alarmLine){
						Element alarmNode = alarmNodes.addElement("alarmNode");
						alarmNode.addAttribute("label", index+"");
						alarmNode.addAttribute("utilization", temp+"");
						
						//在告警的xml文件中添加信息：节点告警信息
						Element alarmMynode = alarmMyNodes.addElement("nodeAlarm");
						alarmMynode.addAttribute("id", String.valueOf(alarmCount));
						alarmMynode.addAttribute("name", "告警"+String.valueOf(alarmCount));
						alarmMynode.addAttribute("type", "nodeAlarm");
						alarmMynode.addAttribute("nodeId", index +"");
						alarmMynode.addAttribute("utilization", String.valueOf(temp));
						
						alarmCount++;
					}
				}
				//添加计数
			//	alarmMyNodes.addAttribute("count", alarmCount+"");
				
				alarmNodes.addAttribute("count", alarmCount+"");
				alarmCount = 0;
				
				//再遍历底层网络的链路信息
				for(int index=0; index<sub_monitor.links;index++){
					temp = (1.0 - (sub.link[index].bw)/(sub_monitor.link[index].bw));
					
					Element sublink = sublinkEl.addElement("link");
					sublink.addAttribute("id", index+"");
					sublink.addAttribute("bw", sub.link[index].bw +"");
					sublink.addAttribute("linkFrom",sub.link[index].from+"" );
					sublink.addAttribute("linkTo",sub.link[index].to+"");
					sublink.addAttribute("utilization", String.valueOf(temp) );
					
					
					if(temp>alarmLine){
						Element alarmLink = alarmLinks.addElement("alarmLink");
						alarmLink.addAttribute("label", index+"");
						alarmLink.addAttribute("utilization", temp+"");
						
						//添加链路告警信息
						Element alarmMynode = alarmMyLinks.addElement("linkAlarm");
						alarmMynode.addAttribute("id", String.valueOf(alarmCount));
						alarmMynode.addAttribute("name", "告警"+String.valueOf(alarmCount));
						alarmMynode.addAttribute("type", "linkAlarm");
						alarmMynode.addAttribute("linkFrom", sub_monitor.link[index].from+"");
						alarmMynode.addAttribute("linkTo", sub_monitor.link[index].to+"");
						alarmMynode.addAttribute("utilization", temp+"");
						
						alarmCount++;
					}			
				}
				//添加计数
			//	alarmMyLinks.addAttribute("count", alarmCount+"");
				
				alarmLinks.addAttribute("count", alarmCount+"");
				
				 /* 路径修改啊*/
				//告警的xml文件保存
				try{
					//开始将document写入XML文件中
					 OutputFormat format = null;							//XML
					 XMLWriter xmlwriter = null;							//XML
					 format = OutputFormat.createPrettyPrint();
					 //设定编码
					 format.setEncoding("UTF-8");
					 xmlwriter = new XMLWriter(new FileOutputStream("D:\\flexW\\fangzhen4\\flex_src\\XML\\alarm\\"+"alramOut.xml"), format);
					 System.out.println("映射输出文件为："+"D:\\flexW\\fangzhen4\\flex_src\\XML\\alarm\\"+"alramOut.xml");
										 
					 xmlwriter.write(alarmDocument);
					 xmlwriter.flush();
					 xmlwriter.close();
					 
					 //写入物理节点和链路利用率信息
					 OutputFormat format1 = null;							//XML
					 XMLWriter subnode_xmlwriter = null;							//XML
					 format1 = OutputFormat.createPrettyPrint();
					 //设定编码
					 format1.setEncoding("UTF-8");
					 subnode_xmlwriter = new XMLWriter(new FileOutputStream("D:\\flexW\\fangzhen4\\flex_src\\XML\\utilization\\"+"subnodeUtil.xml"), format1);
					 
					 subnode_xmlwriter.write(subnodeDoc);
					 subnode_xmlwriter.flush();
					 subnode_xmlwriter.close();
					 
					 //链路
					 OutputFormat format2 = null;
					 XMLWriter sublink_xmlwriter = null;
					 format2 = OutputFormat.createPrettyPrint();
					 format2.setEncoding("UTF-8");
					 
					 sublink_xmlwriter = new XMLWriter(new FileOutputStream("D:\\flexW\\fangzhen4\\flex_src\\XML\\utilization\\"+"sublinkUtil.xml"), format2);
					 
					 sublink_xmlwriter.write(sublinkDoc);
					 sublink_xmlwriter.flush();
					 sublink_xmlwriter.close();
					 
				} catch (Exception e) {
					 e.printStackTrace();
					 System.out.println("-----------Exception occured during of create xmlfile -------");
				}
				
				
			}	
			
			 
		}catch (Exception e){
			e.printStackTrace();
		} finally {
            if (writer != null) {
                try {
                	writer.close();
                } catch (IOException e1) {
                	e1.printStackTrace();
                	System.out.println("文件关闭异常");
                }
            }
        }
		return isfailed;
	}
	
	public double calc_sum_revnue(int reqid,double r)//原C++的版本r是默认参数为1.0,此处的调用以后需要指定参数值
	{
		return this.calc_sum_bw(reqid) + this.calc_sum_cpu(reqid) * r;
	}
	
	public double calc_sum_bw(int reqid)
	{
		double bw = 0;
		for (int i = 0; i < req.get(reqid).links; i ++)
		{
			bw += req.get(reqid).link[i].bw;
		}
		return bw;
	}
	
	public double calc_sum_cpu(int reqid)
	{
		double cpu = 0;
		for (int i = 0; i < req.get(reqid).nodes; i ++)
		{
			cpu += req.get(reqid).cpu[i];
		}
		
		return cpu;

	}
	
	//计算映射的开销
	public double calc_sum_cost(int reqid)
	{
		int f,t;
		int i = reqid;
		double consume_sum_bw = 0.0,consume_sum_cpu = 0.0;
		for (int j = 0; j < req.get(i).links; j ++)
		{
			f = req.get(i).link[j].from;
			t = req.get(i).link[j].to;
			f = v2s.get(i).snode[f];		
			t = v2s.get(i).snode[t];
			consume_sum_bw += spath.get(sub.nodes * f + t).length * req.get(i).link[j].bw;
		}
		consume_sum_cpu = calc_sum_cpu(i);
		return consume_sum_bw + consume_sum_cpu;
	}
	
	public double calc_bw_cost(int reqid)
	{
		int f,t;
		int i = reqid;
		double consume_sum_bw = 0.0;
		for (int j = 0; j < req.get(i).links; j ++)
		{
			f = req.get(i).link[j].from;
			t = req.get(i).link[j].to;
			f = v2s.get(i).snode[f];		
			t = v2s.get(i).snode[t];
			consume_sum_bw += spath.get(sub.nodes * f + t).length * req.get(i).link[j].bw;
		}
		
		return consume_sum_bw;
	}
	
	public void remove_map(int reqid)
	{
		//恢复节点
		int snode,i;
		for (i = 0; i < req.get(reqid).nodes; i ++)
		{
			snode = v2s.get(reqid).snode[i];
			sub.cpu[snode] += req.get(reqid).cpu[i];
			v2s.get(reqid).snode[i] = -1;
			//remove_node_map(s2v_n, snode, reqid);
		}

		//恢复链路
		int slink;
		for (i = 0; i < req.get(reqid).links; i ++)
		{
			int len = v2s.get(reqid).spath[i].len;
			for (int j = 0; j < len; j ++)
			{
				slink = v2s.get(reqid).spath[i].link[j];
				sub.link[slink].bw += req.get(reqid).link[i].bw;
				//remove_link_map(s2v_l, slink, reqid);
			}
			v2s.get(reqid).spath[i].len = 0;
		}
	}
	
	public void write_leave_req(int reqid,Element rootGen)
	{
		
		
		//String subname =reqfileName+"count.embed.out.txt";
		String subname = "D:\\count.embed.out1.txt";
//		int i,j, k;
		
		BufferedWriter writer = null;
		Element vnLeaves = rootGen.addElement("vnLeaves");
		vnLeaves.addAttribute("reqId", reqid+"");
		try{
			writer = new BufferedWriter(new FileWriter(new File(subname), true));
			writer.write("		*********"+reqid+" leaves*********");
			writer.newLine();
		} catch (Exception e){
			e.printStackTrace();
		} finally {
            if (writer != null) {
                try {
                	writer.close();
                } catch (IOException e1) {
                	e1.printStackTrace();
                	System.out.println("文件关闭异常");
                }
            }
        }
				
	}
	
	public void time_cost_output()
	{	
		//String subname=reqfileName+"count.fenxi.time.txt";
		String subname="D:\\count.fenxi.time.txt";
		BufferedWriter writer = null;
		try{
			writer = new BufferedWriter(new FileWriter(new File(subname), true));
			int f_num = failList.size();	//失败的个数
			int s_num = arrived - f_num;	//成功的个数
			
			writer.newLine();
			writer.newLine();
			writer.write("========time="+time+"=======");
			writer.newLine();
			
			writer.write("accepted:"+s_num+",rejected:"+f_num+",arrived:"+(s_num + f_num));
			writer.newLine();
			writer.write("accept rate:"+1.0 * s_num / arrived);
			writer.newLine();
			writer.write("success_cost_time_avg:"+successTime / s_num+";fail_cost_time_avg:"+failTime / f_num+";success_backtrace_avg:"+1.0 * vital_sum_success / s_num+",fail_backtrace_avg:"+1.0 * vital_sum_fail/ f_num);
			writer.newLine();
			writer.write("revenu="+R+",cost:"+C+", revenu_by_time:"+R/time+", cost_by_time:"+C / time+", Cost/Revenue:"+C / R);
			writer.newLine();
			writer.write("cpu_avg_cost:"+calc_node_use_rate_avg()+", bw_avg_cost:"+calc_bw_use_rate_avg());
			writer.newLine();
			writer.write("R/C_avg:"+RCompareToC_sum / s_num);
			writer.newLine();
			writer.write("====================================");
			writer.newLine();
			
		} catch (Exception e){
			e.printStackTrace();
		} finally {
            if (writer != null) {
                try {
                	writer.close();
                } catch (IOException e1) {
                	e1.printStackTrace();
                	System.out.println("文件关闭异常");
                }
            }
        }
		
	}
	
	public double calc_node_use_rate_avg()
	{
		double r = 0.0;
		for (int i = 0; i < sub.nodes; i ++)
		{
			r += (jiedianbeifen[i] - sub.cpu[i]) / jiedianbeifen[i];
		}
		return r / sub.nodes;
	}
	
	public double calc_bw_use_rate_avg()
	{	
		double r = 0.0;
		for (int i = 0; i < sub.links; i ++)
		{
			int f = sub.link[i].from;
			int t = sub.link[i].to;
			r += (spath.get(f * sub.nodes + t).bw - calc_resource_for(f, t)) / spath.get(f * sub.nodes +t).bw;
		}
		return r / sub.links;
	}
	
	public void print_fail_list_to_file()
	{
		
		//String subname =reqfileName+"count.embed.out.txt";
		String subname = "D:\\flexW\\count.embed.out1.txt";
		
		int i,j;
		int index;

		BufferedWriter writer = null;
		try{
			writer = new BufferedWriter(new FileWriter(new File(subname), true));
			for (j = 0; j < failList.size(); j ++)
			{
				index = failList.get(j);
				writer.write("-----------------------"+index+"----------------------");
				writer.newLine();
				writer.write("nodes = "+req.get(index).nodes+", links = "+req.get(index).links);
				writer.newLine();
			
				for (i = 0; i < req.get(index).nodes; i ++)
				{
					writer.write("N("+i+") = "+req.get(index).cpu[i]);
					writer.newLine();
				}
				for (i = 0; i < req.get(index).links; i ++)
				{
					writer.write("L("+i+") = ("+req.get(index).link[i].from+","+req.get(index).link[i].to+";"+req.get(index).link[i].bw+")");
					writer.newLine();
			    }
			}
		}catch (Exception e){
			e.printStackTrace();
		} finally {
            if (writer != null) {
                try {
                	writer.close();
                } catch (IOException e1) {
                	e1.printStackTrace();
                	System.out.println("文件关闭异常");
                }
            }
        }	
	}
	
	//打印算法的分析结果到XML输出文件
	public SimulateResult print_evaluation_XML(SimulateResult sr,Element rootGen){
	
		int f_num = failList.size();
		int s_num = arrived - f_num;
		
		//添加Evaluation节点
		Element evaluation = rootGen.addElement("Evaluation");				//XML
		//Element successVN = evaluation.addElement("successVN");
		Element failVN = evaluation.addElement("failVN");
		
		/*
		for(int i=0;i<s_num;i++){
			Element VN_s_num = successVN.addElement("VN_s_num");
			VN_s_num.addAttribute("Num",successList.get(i)+"");
		}
		*/
		for(int i=0;i<f_num;i++){
			Element fail_num = failVN.addElement("fail_num");
			fail_num.addAttribute("Num",failList.get(i)+"");
		}
		
		Double C_R = C/R;
		DecimalFormat df = new DecimalFormat("#.000000");	//Double保留六位小数
		C_R = Double.valueOf(df.format(C_R));
		Double acceptRate = s_num/(s_num+f_num+0.0);
		
		evaluation.addAttribute("Arrived",arrived+"");
		evaluation.addAttribute("SuccessNum", s_num+"");
		evaluation.addAttribute("FailNum",f_num+"");
		evaluation.addAttribute("AcceptRate",acceptRate+"");
		evaluation.addAttribute("Revenue",R+"");
		evaluation.addAttribute("Cost",C+"");
		evaluation.addAttribute("CR",C_R+"");
		
		sr.setVirtual_net_num(arrived);
		sr.setSucc_num(s_num);
		sr.setFail_nul(f_num);
		sr.setAccept_rate(acceptRate);
		sr.setProfit(R);
		sr.setCost(C);
		return sr;
		
	}
	
	//底层网络的状态监控，每次有虚拟网络映射成功之后，遍历一次底层网络，
	//找出占用率超过85%的资源,并将其写到映射结果输出文件中
	//???将这一过程加入到print_map_to_file函数中？？？,将告警信息放到VN节点中
	public void sub_Monitor(Element rootGen){
		
	}
	

}
