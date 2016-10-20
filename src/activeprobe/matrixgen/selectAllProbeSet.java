package activeprobe.matrixgen;

import java.util.*;

import activeprobe.dataclass.*;


public class selectAllProbeSet {
	private EdgeDependencyMatrix edm = new EdgeDependencyMatrix();
	private EdgeDependencyMatrix nedm = new EdgeDependencyMatrix();
	private Topology topo = new Topology();

    private ArrayList<String> varEdges = new ArrayList<String>();
	
	public selectAllProbeSet(){
		
	}
	
	public selectAllProbeSet(Topology tp){
		this.topo = tp;
		this.addEdgesToEDM(topo);
		this.addEdgesToNEDM(topo);
	}
	
	private void addEdgesToEDM(Topology topo){
		 List<Edge> edgeList = new ArrayList<Edge>();
	     edgeList.addAll(topo.getEdges());
	     EdgeComparator comp = new EdgeComparator();
	     Collections.sort(edgeList,comp);
	     for(int i = 0; i < edgeList.size(); i++){
	    	 edm.AddEdge((Edge)edgeList.get(i));
	     }
	}
	
	private void addEdgesToNEDM(Topology topo){
		 List<Edge> edgeList = new ArrayList<Edge>();
	     edgeList.addAll(topo.getEdges());
	     EdgeComparator comp = new EdgeComparator();
	     Collections.sort(edgeList,comp);
	     for(int i = 0; i < edgeList.size(); i++){
	    	 nedm.AddEdge((Edge)edgeList.get(i));
	     }
	}
	
	private boolean isNodeChecked(String nodeID){
		boolean flag = false;
        if (topo.getNode(nodeID).getStatus())
        	flag = true;
		return flag;
	}
	

    private void getShortestPath(EdgeDependencyMatrix max, int probeNumber, String nodeA, String nodeB){
     	
    	//ѡ������С�Ľڵ���Ϊ·�������
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
			    	    Edge[] tempEdges = new Edge[tempPT.get(ID).size() - 1];
				        String EdgeID = topo.getEdgeID(nodeA, head[0]);
				        tempEdges[0] = topo.getEdge(EdgeID);
				        max.AddProbe(probe, tempEdges);
				        flag = 1;
			        }
			    }
				else{ 
			        String ID = new Integer(ppNum).toString();
				    ArrayList<String> tempNodes = new ArrayList<String>();
				    String preProbeID = head[1];
				    //System.out.println(preProbeID);
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
					    //System.out.println(pID);
					    Probe probe = new Probe(pID,endPoints);
					    Edge[] tempEdges = new Edge[tempPT.get(ID).size() - 1];
					    //System.out.println(tempPT.get(ID).size());
					    for (int b = 0; b < tempPT.get(ID).size() - 1; b++){
					        String EdgeID = topo.getEdgeID((String)tempPT.get(ID).get(b), (String)tempPT.get(ID).get(b + 1));
					        //System.out.println(EdgeID);
					        tempEdges[b] = topo.getEdge(EdgeID);
					    }
					    max.AddProbe(probe, tempEdges);
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
    
	public EdgeDependencyMatrix selectPSet(){
		
        int probeNumber = 1;  
        int nodeNum = this.topo.getNodeNum();

		String[] nodes = new String[nodeNum];
		
		for (int a = 0; a < nodeNum; a++){
			nodes[a] = "n" + new Integer(a + 1).toString();	
		}
		
		for(int i = 0; i < nodeNum; i++){
			for(int j = i+1; j < nodeNum; j++){
				this.getShortestPath(edm, probeNumber, nodes[i], nodes[j]);
				probeNumber++;
			}		
		}
		return edm;
	}
	
	private void getVaribleEdge(){
		varEdges = topo.getCycleEdge();		
		String[] nodePoints = new String[2];
		int edgeNum = varEdges.size();
		ArrayList<String> removedEdge = new ArrayList<String>();
		
		for (int i = 0; i < edgeNum; i++){
			nodePoints[0] = topo.getEdge(varEdges.get(i)).getStartPoint();
			nodePoints[1] = topo.getEdge(varEdges.get(i)).getEndPoint();
			this.getShortestPathRes(1, nodePoints[0], nodePoints[1],nodePoints);
			if(nedm.getProbeCount() == 0){
				removedEdge.add(varEdges.get(i));
			}
			else{
				nedm.DeleteProbe(nedm.getProbe("p1"));
			}
		}
		
		for(int j = 0, p = removedEdge.size(); j < p; j++){
			varEdges.remove(removedEdge.get(j));
		}
		
		System.out.println("The edges can be changed: ");
		for(int a = 0; a < varEdges.size(); a++){
			System.out.println(" "+ varEdges.get(a));
		}
		
	}
	
	private String getEdgeRandomly(){
		this.getVaribleEdge();
		String edgeID = new String();
		Random rn = new Random();
		edgeID = varEdges.get(rn.nextInt(varEdges.size()));
		//System.out.println(edgeID);
		return edgeID;
	}
	
	private String[] getSelNodes(String edge){
		String[] selNodes = new String[2];
		selNodes[0] = topo.getEdge(edge).getStartPoint();
		selNodes[1] = topo.getEdge(edge).getEndPoint();
		return selNodes;
	}
	
	/* ������������һЩ�ı���ѡ����Ҫ�仯��̽�� */
	
	private void getProbeChanged(ArrayList<Integer> probeChanged, String edge){
		int eID = new Integer(edge.substring(1)).intValue();
		int pID = 0;
		int[][] matrix = new int[edm.getProbeCount()][edm.getEdgeCount()];
		matrix = edm.getMatrixAll();
		for (int i = 0; i < edm.getProbeCount(); i++){
			if (matrix[i][eID-1] == 1){
				pID = i + 1;
				probeChanged.add(pID);
				//System.out.println(pID);
			}
		}
	}
	
	private void getShortestPathRes(int probeNumber, String nodeA, String nodeB, String[] sn){
	    
	 	//ѡ������С�Ľڵ���Ϊ·�������
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
		int nodeFlag = 0;
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
				
				if(startNode[0].equals(sn[0]) && nextHop[0].equals(sn[1])){
					nodeFlag = 1;
				}
				else{
				    if(startNode[0].equals(sn[1]) && nextHop[0].equals(sn[0])){
				    	nodeFlag = 1;
				    }
				}
				if(!this.isNodeChecked(nextHop[0]) && nodeFlag != 1){
					shortPath.offer(nextHop);
					topo.getNode(nextHop[0]).setStatus(true);
				}		
				nodeFlag = 0;
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
				    	Edge[] tempEdges = new Edge[tempPT.get(ID).size() - 1];
					    String EdgeID = topo.getEdgeID(nodeA, head[0]);
					    tempEdges[0] = topo.getEdge(EdgeID);
					    nedm.AddProbe(probe, tempEdges);
					    flag = 1;
				    }
				}
			    else{ 
				    String ID = new Integer(ppNum).toString();
					ArrayList<String> tempNodes = new ArrayList<String>();
					String preProbeID = head[1];
					//System.out.println(preProbeID);
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
						//System.out.println(pID);
					    Probe probe = new Probe(pID,endPoints);
						Edge[] tempEdges = new Edge[tempPT.get(ID).size() - 1];
						//System.out.println(tempPT.get(ID).size());
						for (int b = 0; b < tempPT.get(ID).size() - 1; b++){
						    String EdgeID = topo.getEdgeID((String)tempPT.get(ID).get(b), (String)tempPT.get(ID).get(b + 1));
						    //System.out.println(EdgeID);
						    tempEdges[b] = topo.getEdge(EdgeID);
						}
						nedm.AddProbe(probe, tempEdges);
						flag = 1;
					}
				}				
			  	
			  	addedNodeDegree = topo.getNode(head[0]).getDegree();
			
			  	if (addedNodeDegree != 1 && flag == 0){
					for(int k = 0; k < addedNodeDegree; k++){
					    String[] nextHop = new String[2];
					    String nextNode = (String)topo.getNode(head[0]).getNeighborNodes().get(k);
					    if(head[0].equals(sn[0]) && nextNode.equals(sn[1])){
					    	nodeFlag = 1;
					    }
					    else{
					    	if(head[0].equals(sn[1]) && nextNode.equals(sn[0])){
					    		nodeFlag = 1;
					    	}
					    }
					    	
					    if(!this.isNodeChecked(nextNode) && nodeFlag != 1){		    
					    	nextHop[0] = nextNode;
					    	nextHop[1] = new Integer(ppNum).toString();
					    	shortPath.offer(nextHop);
					    	topo.getNode(nextHop[0]).setStatus(true);
					    }
					    nodeFlag = 0;
					}
				}
			}			
		}	
		topo.resetNodeStatus();
	}

	public EdgeDependencyMatrix selectNewPSet(){
		
		int nodeNum = this.topo.getNodeNum();
		
	    String[] nodes = new String[nodeNum];
			
	    for (int a = 0; a < nodeNum; a++){
			nodes[a] = "n" + new Integer(a + 1).toString();	
		}
		
		String selectedEdge = this.getEdgeRandomly();
		System.out.println("The edge to be changed is " + selectedEdge);
		
		String[] selNodes = new String[2];
		selNodes = this.getSelNodes(selectedEdge);
		
		//System.out.println("the selected nodes is " + selNodes[0] + " and " + selNodes[1]);
		int probeNumber = 1;  
		
        ArrayList<Integer> probeChanged  = new ArrayList<Integer>();
		this.getProbeChanged(probeChanged, selectedEdge);
		
		for(int i = 0; i < nodeNum; i++){
			for(int j = i+1; j < nodeNum; j++){
				if(probeChanged.contains(probeNumber)){
					this.getShortestPathRes(probeNumber, nodes[i], nodes[j], selNodes);
				}
				else{
					this.getShortestPath(nedm, probeNumber, nodes[i], nodes[j]);
				}
				probeNumber++;
			}		
		}
		
		return nedm;
    }
}
