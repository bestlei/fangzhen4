package activeprobe.probeSelection;

import java.util.ArrayList;
import java.util.HashSet;

import	activeprobe.dataclass.*;
import activeprobe.diagnosis.JavaMat;

public class ProbeSelect {
	private DependencyMatrix dMatrix;
	
	public ProbeSelect(DependencyMatrix dMatrix){
		this.dMatrix=dMatrix;
	}
	/**
	 * from Diagnosis Probes Package To Detection Probes Package:greedy()算法
	 * @return
	 */
	public ArrayList<Probe> detectionProbesByGreedy(ArrayList<Probe> probes){
		System.out.println("用greedy方法选出detection包集合,请稍候.....");
		ArrayList<Node> allNodes=dMatrix.GetAllNodes();		   //全部节点
		ArrayList<Probe> probesSelected=new ArrayList<Probe>(); //记录已选择的探测
		ArrayList<Node> passNodes=new ArrayList<Node>();
		while(passNodes.size()!=allNodes.size()){
			int maxNum=0;											//经过节点的数量
			int probeNo=0;              //探针序号
			int maxProbeNo=0;
			Probe selectedProbe=new Probe();						
			ArrayList<Node> selectedNodes=new ArrayList<Node>();
			for(Probe probe:probes){
				
				ArrayList<Node> nodes=dMatrix.GetNodesforProbe(probe);
				nodes.removeAll(passNodes);                          //节点集合相减
				probeNo++;
				if(maxNum<nodes.size()){							//判断经过节点最多的探针
					maxNum=nodes.size();
					selectedProbe=probe;
					selectedNodes=nodes;
					maxProbeNo=probeNo;
				}
				
				
			}
			//记录经过节点最多的探针
			if(selectedProbe!=null){
				probesSelected.add(selectedProbe);
				JavaMat.probesNo.add(maxProbeNo);//记录detection中的探针在所有探针里的序号
				for(Node node:selectedNodes){
					passNodes.add(node);
				}
			}
		}
		System.out.printf("%d Detection Probe Selected\n",probesSelected.size());
		return probesSelected;
	}
	
	/**
	  * 求解Diagnosis Probes Package：Greedy近似算法
	  * @param probeIgnore
	  * @return
	  */
	public ArrayList<Probe> diagnosisProbesByGreedy(){
		System.out.println("用greedy方法选出diagnosis包集合,请稍候.....");
		ArrayList<Probe> allProbes=dMatrix.GetAllProbes();     //全部探针
		ArrayList<Node> allNodes=dMatrix.GetAllNodes();		   //全部节点
		ArrayList<Probe> probesSelected=new ArrayList<Probe>(); //记录已选择的探测
		
		HashSet<Node> nodesFlag=new HashSet<Node>();
		
		double beginDP=Math.log10(allNodes.size());            //初始的熵值
		
		SubSets<Node> subsets=new SubSets<Node>(allNodes);     //子集的集合类  
		while(true){
			Probe selectedProbe=null;	                       //记录选择的节点，熵值最小
			double DP=beginDP;
			//循环求熵，求出最小的熵
			for(int i=0;i<allProbes.size();i++){
				Probe probe=allProbes.get(i);                  //获得当前探针
				ArrayList<Node> passNode=dMatrix.GetNodesforProbe(probe); //获得探针经过节点集合			
				subsets.TrySplitSubSet(passNode);                    //当前探针分割节点
				DP=subsets.TryCaculateEntropy();
				if(DP<beginDP){                            //找出熵值最小的
					beginDP=DP;
					selectedProbe=allProbes.get(i);
				}
			}
			if(selectedProbe!=null){							//如不选择不出能减少熵值的探针则退出循环
				probesSelected.add(selectedProbe);
				nodesFlag.addAll(dMatrix.GetNodesforProbe(selectedProbe));     //标记经过的节点
				subsets.SplitSubSet(dMatrix.GetNodesforProbe(selectedProbe));
				allProbes.remove(selectedProbe);               //移除选择过的探针
			}else{
				break;
			}
		}	
		//额外添加一个探针，使diagnosis probes不会出现全0的列,即所有节点都经过
out:		for(Node node:allNodes){
			if(!nodesFlag.contains(node)){									//选出当前探针集合未经过的节点
				for(Probe probe:allProbes){
					if(dMatrix.GetNodesforProbe(probe).contains(node)){    //判断是否经过该节点
						probesSelected.add(probe);
						break out;
					}
				}
			}
		}
		System.out.printf("%d Diagnosis Probe Selected\n",probesSelected.size());
		return probesSelected;
	}

}