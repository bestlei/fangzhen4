package activeprobe.matrixgen;

import java.util.ArrayList;

import activeprobe.dataclass.*;

public class matrixGenerate{
    /**	
     * @param index 1 代表虚拟网络，2 代表物理网络
     * @param filename  网络节点文件
     * @param nodeStationsNum   探针数量
     */
	public static DependencyMatrix getMatrix(int index,String filename,int nodeStationsNum){
		Topology newTopo = null;
		topologyGenerate tG = new topologyGenerate();
		newTopo = tG.genTopo(filename);
		DependencyMatrix dm=null;
		if (newTopo != null){
			selectProbeStation sPS = new selectProbeStation();
			if(sPS != null){
				String[] probeStations = sPS.selectPSRandomly(index,newTopo, nodeStationsNum);    //探测站点队列
				selectProbeSet sPSet = new selectProbeSet(newTopo);
				dm= new DependencyMatrix();
				dm=sPSet.selectPSet(probeStations);
			}
		}
		else{
			
			System.out.println("Topo is null");
		}       
		return dm;
	}
	
	public static void DeleteNodeStation (DependencyMatrix dMatrix){
		ArrayList<Node> nodeStations=dMatrix.getNodeStations();
		for(Node node:nodeStations){
			dMatrix.DeleteNode(node);
		}
	}
}
