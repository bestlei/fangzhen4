package activeprobe.matrixgen;

import java.util.*;

import activeprobe.dataclass.*;


public class selectProbeSet {
	
	private DependencyMatrix dm = new DependencyMatrix();
	private Queue shortPath = new LinkedList();
	private Topology topo = new Topology();
	
	public selectProbeSet(){
		
	}
	
	public selectProbeSet(Topology tp){
		this.topo = tp;
		this.addToDM(topo);
	}
	
	private void addToDM(Topology topo){
		 List nodeList = new ArrayList();
	     nodeList.addAll(topo.getNodes());
	     NodeComparator comp1 = new NodeComparator();
	     Collections.sort(nodeList,comp1);       
	     for(int i = 0; i < nodeList.size(); i++){
	    	    dm.AddNode((Node)nodeList.get(i));
	     }
	     List edgeList = new ArrayList();
	     edgeList.addAll(topo.getEdges());
	     EdgeComparator comp2 = new EdgeComparator();
	     Collections.sort(edgeList,comp2);
	     for(int i=0;i!=edgeList.size();++i){
	    	 dm.AddEdge((Edge)edgeList.get(i));
	     }
	}
	
	private boolean isNodeChecked(String nodeID){
		boolean flag = false;
        if (topo.getNode(nodeID).getStatus())
        	flag = true;
		return flag;
	}
	

    private void getShortestPath(DependencyMatrix max, int probeNumber, String nodeA, String nodeB){
    	String tempNode = new String();
		if(topo.getNode(nodeA).getDegree() > topo.getNode(nodeB).getDegree()){
			tempNode = nodeA;
			nodeA = nodeB;
			nodeB = tempNode;
		}
    	Queue shortPath = new LinkedList();
		Hashtable<String,ArrayList> tempPT = new Hashtable<String,ArrayList>();  
		String[] nodeProbe = new String[2];
		int ppNum = 0;
		nodeProbe[0] = nodeA;
		nodeProbe[1] = "0";
		int removedNodeDegree = 0;
		int flag = 0;
		String[] endPoints = new String[2];
		endPoints[0] = nodeA;
		endPoints[1] = nodeB;
		if(shortPath.offer(nodeProbe)){
			topo.getNode(nodeA).setStatus(true);
			String[] startNode = (String[])shortPath.poll();
			removedNodeDegree = topo.getNode(startNode[0]).getDegree();
			for(int j = 0; j < removedNodeDegree; j++){
				String[] nextHop = new String[2];
				nextHop[0] = (String) topo.getNode(startNode[0]).getNeighborNodes().get(j);
				nextHop[1] = new Integer(ppNum).toString();
				if(!this.isNodeChecked(nextHop[0])){
				    shortPath.offer(nextHop);
				    topo.getNode(nextHop[0]).setStatus(true);
				}		
			}	
			while(shortPath.size() != 0 && flag == 0){
				String[] head = (String[])shortPath.poll();
			    int addedNodeDegree = 0;
		  	    ppNum++;
		  	    if(head[1].equalsIgnoreCase("0")){
	    	        String ID = new Integer(ppNum).toString();
		    	     ArrayList<String> tempNodes = new ArrayList<String>();
		    	     tempNodes.add(0,nodeA);
			        tempNodes.add(1,head[0]);
			        tempPT.put(ID, tempNodes);
			        if(head[0].equals(nodeB)){
				 	       String pID = "p" + new Integer(probeNumber).toString();
				        Probe probe = new Probe(pID,endPoints);
				        Node[] nodes = new Node[2];
				        String[] key = new String[2];
				        for(int nodeNum = 0; nodeNum < 2; nodeNum++){
				        	nodes[nodeNum] = topo.getNode(tempNodes.get(nodeNum));
				         key[nodeNum]=tempNodes.get(nodeNum);
				        }
				        Edge edge=topo.getEdge(topo.getEdgeID(key[0],key[1]));
				        Edge[] edges=new Edge[1];
				        edges[0]=edge;
				        max.AddProbe(probe, nodes,edges);
				        flag = 1;
			        }
			    }
				else{ 
			        String ID = new Integer(ppNum).toString();
				    ArrayList<String> tempNodes = new ArrayList<String>();
				    String preProbeID = head[1];
					if (tempPT.containsKey(preProbeID)){
					    int a;
					    for (a = 0; a < tempPT.get(preProbeID).size(); a++){
					        tempNodes.add(a, (String)tempPT.get(preProbeID).get(a));
					    }
					    tempNodes.add(a,head[0]);
					    tempPT.put(ID, tempNodes);
					}
				    else{
					    System.out.println("Error, preProbe is null");
					}
					if(head[0].equals(nodeB)){
					    String pID = "p" + new Integer(probeNumber).toString();
					    Probe probe = new Probe(pID,endPoints);
					    Node[] nodes = new Node[tempNodes.size()];
					    Edge[] edges = new Edge[tempNodes.size()-1];
					    for (int b = 0; b < tempNodes.size(); b++){
					        nodes[b] = topo.getNode(tempNodes.get(b));
					        if(b!=0){
					        	String[] key=new String[2];
					        	key[0]=tempNodes.get(b-1);
					        	key[1]=tempNodes.get(b);
					        	edges[b-1]=topo.getEdge(topo.getEdgeID(key[0],key[1]));
					        }
					    }
					    max.AddProbe(probe, nodes,edges);
					    flag = 1;
					}
				}				  	    
		  	    addedNodeDegree = topo.getNode(head[0]).getDegree();
				if (addedNodeDegree != 1 && flag == 0){
				    for(int k = 0; k < addedNodeDegree; k++){
				        String[] nextHop = new String[2];
				        String nextNode = (String)topo.getNode(head[0]).getNeighborNodes().get(k);
				        if(!this.isNodeChecked(nextNode)){
				    	    nextHop[0] = nextNode;
				    	    nextHop[1] = new Integer(ppNum).toString();
				    	    shortPath.offer(nextHop);
				    	    topo.getNode(nextHop[0]).setStatus(true);
				    	}
				    }
				}
			}			
		}	
		topo.resetNodeStatus();
    }
    
	
public DependencyMatrix selectPSet(String[] probeStation){
    int probeNum = 1;  
    int probeStationNum = probeStation.length;
    int nodeNum = this.topo.getNodeNum();
    int flag = 0;

	String[] nodes = new String[nodeNum];
	
	for (int a = 0; a < nodeNum; a++){
		nodes[a] = "n" + new Integer(a).toString();	
	}
	
	for(int i = 0; i < probeStationNum; i++){
		for(int j = 0; j < nodeNum; j++){
			flag = 0;
			for(int a = 0; a < i + 1; a++){
				if(probeStation[a].equals(nodes[j])){
					flag = 1;
				}
			}
			if(flag == 0){
				this.getShortestPath(dm, probeNum, probeStation[i], nodes[j]);
				probeNum++;
			}
		}		
	}
	ArrayList<Node> nodeStations=new ArrayList<Node>();
	for(String nodeId:probeStation){
		for(Node node:dm.GetAllNodes()){
			if(node.getID().equals(nodeId)){
				nodeStations.add(node);
				break;
			}
		}
	}
	dm.setNodeStations(nodeStations);
	return dm;
  }
}
