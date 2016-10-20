package activeprobe.dataclass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

public class Topology {
	private int nodeNum;      //拓扑网络节点数
	private int edgeNum;      //拓扑网络中的边数
	private Hashtable<String,Node> topoNodes = new Hashtable<String,Node>();
	private Hashtable<String,Edge> topoEdges1 = new Hashtable<String,Edge>();    //拓扑网络的边信息，key为边名，value为边
	private Hashtable<String,String> leafEdgeTable = new Hashtable<String,String>();
	private Hashtable<String,String> edgeNoTable = new Hashtable<String,String>();    //点边表，key为fkey+tokey，value为边名
	private ArrayList<String> cycleEdges = new ArrayList<String>();        //拓扑网络中的边集
	
	public Topology(){
	
	}
	
	public Topology(int nnum, int ednum ){
		nodeNum = nnum;
		edgeNum = ednum;
	}
	public int getNodeNum(){
		return nodeNum;
	}
	
	public int getEdgeNum(){
		return edgeNum;
	}
	
	public Node getNode(String key){
		return (Node)topoNodes.get(key);				
	}
	
	public Edge getEdge(String key){
		return (Edge)topoEdges1.get(key);
	}

	public Collection<Node> getNodes(){
		return topoNodes.values();
	}
	
	public Collection<Edge> getEdges(){
		return topoEdges1.values();
	}
	
	public void resetNodeStatus(){
		ArrayList<Node> nodes = new ArrayList<Node>();
		nodes.addAll(topoNodes.values());
		for(int i = 0; i < this.getNodeNum(); i++){
			nodes.get(i).setStatus(false);
		}
	}
	
	public void addEdge(String from, String to, String edgeNo)
	{
		String fKey = "n" + new Integer(new Integer(from).intValue()).toString();
		String tKey = "n" + new Integer(new Integer(to).intValue()).toString();
        String endPoints = new String();
        endPoints = fKey + tKey;
        
		Node fromNode = null;
		Node toNode = null;
		
		Edge edge = new Edge(edgeNo, fKey, tKey);
		topoEdges1.put(edgeNo, edge);
		edgeNoTable.put(endPoints, edgeNo);
		if (topoNodes.containsKey(fKey)){
			fromNode = topoNodes.get(fKey);
		}
		else{
			fromNode = new Node(fKey);
			topoNodes.put(fKey, fromNode);
		}
		
		if (topoNodes.containsKey(tKey)){
			toNode = topoNodes.get(tKey);
		}
		else{
			toNode = new Node(tKey);
			topoNodes.put(tKey,toNode);
		}		
		
		fromNode.addNeighbor( tKey );  
		toNode.addNeighbor( fKey );
	}
	
	public String getEdgeID(String nodeId1, String nodeId2){
		String edgeId1 = new String();
		String edgeId2 = new String();
		edgeId1 = nodeId1 + nodeId2;
		edgeId2 = nodeId2 + nodeId1;
		
 		if (edgeNoTable.containsKey(edgeId1)){
			return edgeNoTable.get(edgeId1);
		}
		else{
			if(edgeNoTable.containsKey(edgeId2)){
				return edgeNoTable.get(edgeId2);
			}
			else{
				System.out.println("get edge number error,no this edge exists");
				return null;
			}
		}
	}
	
	public void genLeafEdge(){
		 String node = new String();
		 String edge = new String();
	     for(int i = 0; i < nodeNum; i++){
	    	 node = "n" + new Integer(i).toString();
	    	 if(topoNodes.get(node).getDegree() == 1){
	    		 for(int j = 0; j < edgeNum; j++){
	    			 edge = "e" + new Integer(j).toString();
	    			 if(topoEdges1.get(edge).getStartPoint().equalsIgnoreCase(node) || 
	    					 topoEdges1.get(edge).getEndPoint().equalsIgnoreCase(node)){
	    				 leafEdgeTable.put(node, edge);
	    			 }
	    		 }
	    	 }
	     }
	}
	
	public Hashtable getLETable(){
		return leafEdgeTable;
	}
	
	public void genCycleEdge(){
		String node = new String();
		String edge = new String();
		String neighborID = new String();
		int[] nodeDegree = new int[nodeNum];
	    ArrayList<String> speNodes = new ArrayList<String>();
		speNodes.addAll(leafEdgeTable.keySet());
		
		for(int i = 0; i < nodeNum; i++){
			node = "n" + new Integer(i).toString();
			//System.out.println(node);
			nodeDegree[i] = topoNodes.get(node).getDegree();
			if(topoNodes.get(node).getDegree() != 1){
			    for(int j = 0; j < topoNodes.get(node).getDegree(); j++){
				    neighborID = (String)topoNodes.get(node).getNeighborNodes().get(j);				    
				    if (speNodes.contains(neighborID)){
					        nodeDegree[i]--;
				    }
				}    
			    if(nodeDegree[i] == 1){
				   speNodes.add(node);
				   this.getSpeNode(i, nodeDegree, speNodes);
			    }
			    
			}
		}
		for(int i = 0; i < edgeNum; i++){
		    edge = "e" + new Integer(i).toString();
		    String startPoint = topoEdges1.get(edge).getStartPoint();
		    String endPoint = topoEdges1.get(edge).getEndPoint();
		    if(!speNodes.contains(startPoint) && !speNodes.contains(endPoint)){
			    cycleEdges.add(edge);
			}
		}
	}

    public void getSpeNode(int sn, int[] nd, ArrayList<String> sns){
    	String nodeID = new String();
    	String speNode = "n" + new Integer(sn).toString();
        for (int i = 0; i < sn; i++){
        	nodeID = "n" + new Integer(i).toString();
    	    if(nd[i] != 1 && topoNodes.get(nodeID).getNeighborNodes().contains(speNode)){
    	    	nd[i]--;
    	    	if(nd[i] == 1){
    	    		sns.add(nodeID);
    	    		this.getSpeNode(i, nd, sns);
    	    	}
    	    }
		}
    }
    
    public ArrayList<String> getCycleEdge(){
    	return cycleEdges;
    }
    
    public void printNodeDegree(){
    	String nodeID = new String();
    	for(int i = 0, p = this.nodeNum ; i < p; i++ ){
    		nodeID = "n" + new Integer(i).toString();
    		System.out.println(this.topoNodes.get(nodeID).getDegree());
    	}
    }
    
}
