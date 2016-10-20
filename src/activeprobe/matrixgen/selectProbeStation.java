package activeprobe.matrixgen;


import java.util.*;
import activeprobe.dataclass.*;
import activeprobe.diagnosis.Parameter;;

public class selectProbeStation {
	public String[] selectPSByDegree(Topology topo,int psn) {
		String[] probeStation = new String[psn];
		int totalNum = topo.getNodeNum();
        List nodeDegree = new ArrayList();
        nodeDegree.addAll(topo.getNodes());
        DegreeComparator comp = new DegreeComparator();
        Collections.sort(nodeDegree,comp);
        
        for (int i = totalNum - 1; i >= 0; i--){
        	Node temp = null;
        	temp = (Node)nodeDegree.get(i);
        }
        System.out.println("There are " + psn + " probe stations:");
        for (int i = 0; i < psn; i++){
            Node temp = null;
            temp = (Node)nodeDegree.get(totalNum - i - 1);
            probeStation[i] = temp.getID();
            System.out.println(probeStation[i]);
        }
        
		return probeStation;
	}
	/**
	 * @param index   1为虚拟网络   2位物理网络
	 * @param topo    网络拓扑结构（只是用来获取网络节点数）
	 * @param psn     探针数量
	 * @return
	 */
	public String[] selectPSRandomly(int index,Topology topo,int psn){
		String[] probeStation = new String[psn];
		ArrayList<Integer> psNum = new ArrayList<Integer>();    //当前探针队列
		int totalNum = topo.getNodeNum();     //网络节点数量
		Random rn = new Random();
		
		for(int i = 0; i < psn; i++){
			int temp = rn.nextInt(totalNum) + 1;
			if(psNum.contains(temp)){
				i--;
			}
			else{
				psNum.add(temp);
			}
		}
		if(index==1){
		    System.out.println("虚拟网络中有  " + psn + "个探测站点:");
		    for(int j = 0; j < psn; j++){
				String psID = "n" + psNum.get(j).toString();
				Parameter.virProbesNo.add(psNum.get(j));
				probeStation[j] = psID;
				System.out.println(probeStation[j]);
			}
		}else{
			  System.out.println("物理网络中有  " + psn + "个探测站点:");
			  for(int j = 0; j < psn; j++){
					String psID = "n" + psNum.get(j).toString();
					Parameter.phyProbesNo.add(psNum.get(j));
					probeStation[j] = psID;
					System.out.println(probeStation[j]);
				}
		}
		
		return probeStation;
	}
}
