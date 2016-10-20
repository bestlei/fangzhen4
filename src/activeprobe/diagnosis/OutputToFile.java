package activeprobe.diagnosis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;

import activeprobe.dataclass.DependencyMatrix;
import activeprobe.dataclass.Edge;
import activeprobe.dataclass.MappingRelationship;
import activeprobe.dataclass.Node;
import activeprobe.dataclass.Probe;
import activeprobe.dataclass.Topo_Path;

public class OutputToFile{
	public static void relationshipToFile(MappingRelationship map,int nodenum,String filename){
		File file=new File(JavaMat.initUrl+"output\\"+filename);
		FileWriter fw=null;
		try{
			fw=new FileWriter(file);
		}catch(IOException ex){
			System.out.println("file error");
		}
		PrintWriter bw=new PrintWriter(new BufferedWriter(fw));
		int[][] intFile=new int[map.GetSize()][nodenum];
		for(int i=0;i!=map.GetSize();++i){
			for(int j=0;j!=nodenum;++j){
				intFile[i][j]=0;
			}
		}
		Hashtable<String,String>node=map.GetNode();
		Hashtable<Topo_Path,String>edge=map.GetEdge();
		ArrayList<Topo_Path>subedge=map.GetSubEdge();
	    for(int j=0;j!=nodenum;++j){
	    	String index=node.get(String.valueOf(j));  //寻找物理节点对应的虚拟节点
			if(index!=null){
				intFile[Integer.parseInt(index)][j]=1;
			}else{                                //如果未找到物理节点对应的虚拟节点,找包含当前节点的物理链路对应的虚拟链路编号
			    for(int k=0;k!=subedge.size();++k){
				    if(subedge.get(k).contains(String.valueOf(j))){
					     index=edge.get(subedge.get(k));
					     intFile[Integer.parseInt(index)][j]=1;
				    }
			    }
			}
	    }
	    for (int a = 0; a <map.GetSize(); a++) {
			for (int b = 0; b <nodenum; b++) {
					bw.print(intFile[a][b]);
			}
			bw.println();
		}
		bw.close();
	}
	/**
	 * @param index  代指物理网络或者虚拟网络
	 * @param dMatrix   拓扑结构
	 * @param probes   探针
	 * @param fileName   输出文件
	 */
	public static void arrayListToFile(int index,DependencyMatrix dMatrix,ArrayList<Probe> probes,String fileName){
		File file=new File(JavaMat.initUrl+"output\\"+fileName);
		FileWriter fw = null;
		try{
			fw = new FileWriter(file);
		}
		catch(IOException ex){
			System.out.println("file error");
		}
		PrintWriter bw = new PrintWriter(new BufferedWriter(fw));
		ArrayList<Node> nodes=dMatrix.GetAllNodes();
		ArrayList<Edge> edges=dMatrix.GetAllEdges();
		int []edgeSize=new int[2];
		edgeSize[0]=edges.size();
		edgeSize[1]=0;
		int[][] intFile=new int[probes.size()][nodes.size()+edgeSize[index-1]];
		for(int i=0;i<probes.size();i++){
			ArrayList<Node> passNodes =dMatrix.GetNodesforProbe(probes.get(i));
			for(int j=0;j<nodes.size();j++){
				if(passNodes.contains(nodes.get(j))) 
    	    		intFile[i][j] = 1;
    	    	else 
    	    		intFile[i][j] = 0;
			}
			if(index==1){
				ArrayList<Edge> passEdges = dMatrix.GetEdgesforProbe(probes.get(i));
				for(int j=0;j!=edges.size();++j){
					if(passEdges.contains(edges.get(j)))
						intFile[i][nodes.size()+j]=1;
					else
						intFile[i][nodes.size()+j]=0;
				}
			}
		}
		for (int a = 0; a <probes.size(); a++) {
			for (int b = 0; b < nodes.size()+edgeSize[index-1]; b++) {
					bw.print(intFile[a][b]);
			}
			bw.println();
		}
		bw.close();
	}
	public static void stringArrayListToFile(ArrayList<String> faultNo,String filename){
		File file=new File(JavaMat.initUrl+"output\\"+filename);
		FileWriter fw = null;
		try{
			fw = new FileWriter(file);
		}
		catch(IOException ex){
			System.out.println("file error");
		}
		PrintWriter bw = new PrintWriter(new BufferedWriter(fw));
		for(String str:faultNo){
			bw.println(str);
		}
		bw.close();
	}
	public static void stringToFile(String faultProbability,String filename){
		File file=new File(JavaMat.initUrl+"output\\"+filename);
		FileWriter fw = null;
		try{
			fw = new FileWriter(file);
		}
		catch(IOException ex){
			System.out.println("file error");
		}
		PrintWriter bw = new PrintWriter(new BufferedWriter(fw));
		bw.println(faultProbability);
		bw.close();
	}
	/**
	 * 
	 * @param faultNo
	 */
	public static void stringArrayListToShrinkFile(String faultNo){
		File file=new File(JavaMat.initUrl+"output\\failededges.txt");
		FileWriter fw = null;
		try{
			fw = new FileWriter(file);
		}
		catch(IOException ex){
			System.out.println("file error");
		}
		PrintWriter bw = new PrintWriter(new BufferedWriter(fw));
		bw.println(faultNo);
		bw.close();
	}
	/**
	 * 
	 * @param faultNo
	 */
	public static void detetctionToShrinkFile(ArrayList<Probe> probes){
		File file=new File(JavaMat.initUrl+"output\\detectresultfornegative.txt");
		FileWriter fw = null;
		StringBuffer stb=new StringBuffer();
		try{
			fw = new FileWriter(file);
		}
		catch(IOException ex){
			System.out.println("file error");
		}
		PrintWriter bw = new PrintWriter(new BufferedWriter(fw));
		for(int i=0;i<probes.size();i++){
			bw.print(JavaMat.probesNo.get(i)+"+"+33+"+"+33+"+"+(JavaMat.detResult.get(i)-1)+"~");
		}
		bw.println();
		for(String fNode:JavaMat.suspectNodes){
			stb.append(fNode);
		}
		bw.print(stb);
		bw.close();
	}
}
