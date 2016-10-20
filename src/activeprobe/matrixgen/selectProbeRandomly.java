package activeprobe.matrixgen;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
import activeprobe.dataclass.*;

public class selectProbeRandomly {
	private EdgeDependencyMatrix edm = new EdgeDependencyMatrix();
	private EdgeDependencyMatrix nedm = new EdgeDependencyMatrix();
	private Topology topo = new Topology();

    private ArrayList<String> varEdges = new ArrayList<String>();
    private Hashtable<String,Integer> weightOfEdge = new Hashtable<String,Integer>();
	
	public selectProbeRandomly(){
	}
	
	public selectProbeRandomly(Topology tp){
		this.topo = tp;
		this.addEdgesToEDM(topo);
		this.addEdgesToNEDM(topo);
		for(int i = 0; i < this.edm.getEdgeCount(); i++){
	    	weightOfEdge.put(this.edm.GetAllEdges().get(i).getID(), 0);
		}
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
	
	private Edge[] getOptimizedPath(ArrayList<Edge[]> shortPaths ){
		if(shortPaths.size() > 0){
			Edge[] path = new Edge[shortPaths.get(0).length];
		    ArrayList<Edge[]> optimizedPaths = new ArrayList<Edge[]>();
		    int minWeight = 0;
		    int tempWeight = 0;
		    int maxNumOfZero = 0;
		    int tempNumOfZero = 0;
		    int newWeight = 0;
		
		    //ѡ��������·������ı����
		    for(int i = 0, p = shortPaths.size(); i < p; i++){
			    for(int j = 0, q = shortPaths.get(i).length; j < q; j++){
				    tempWeight = tempWeight + weightOfEdge.get((shortPaths.get(i))[j].getID());	
			    }
			    if(i == 0){
				    minWeight = tempWeight;
				    optimizedPaths.add(shortPaths.get(i));
			    }
			    else{
				    if(tempWeight < minWeight){
					    optimizedPaths.clear();
					    optimizedPaths.add(shortPaths.get(i));
					    minWeight = tempWeight;
				    }
				    else if(tempWeight == minWeight){
					    optimizedPaths.add(shortPaths.get(i));
				    }
			    }
			    tempWeight = 0;			
		    }
		
	    	//��ֻ��һ������������·�������
		    if(optimizedPaths.size() == 1){
			    path = optimizedPaths.get(0);
			    for(int a = 0, p = path.length; a < p; a++){
				    newWeight = weightOfEdge.get(path[a].getID()) + 1;
				    weightOfEdge.remove(path[a].getID());
				    weightOfEdge.put(path[a].getID(), newWeight);
			    }
			    return path;
		    }
		
		    //���г���һ������������·������ͨ��·��ͨ����±ߵ���Ŀ��ѡ������·��
		    else{
			    for(int i = 0, p = optimizedPaths.size(); i < p; i++){
				    for(int j = 0, q = optimizedPaths.get(i).length; j < q; j++){
					    if(weightOfEdge.get((optimizedPaths.get(i))[j].getID()) == 0){
						    tempNumOfZero++;
					    }
				    }
				    if(tempNumOfZero >= maxNumOfZero){
					    path = optimizedPaths.get(i);
					    maxNumOfZero = tempNumOfZero;
				    }
			    }
			    for(int a = 0, p = path.length; a < p; a++){
				    newWeight = weightOfEdge.get(path[a].getID()) + 1;
				    weightOfEdge.remove(path[a].getID());
				    weightOfEdge.put(path[a].getID(), newWeight);
			    }
			    return path;
		    }
		}
		else
			return null;
	}
	
	private boolean isNodeAddible(String[] nextHop, Hashtable<String,ArrayList> tempPT){
		boolean result = true;
		if(tempPT.get(nextHop[1]).contains(nextHop[0]))
			result = false;
		return result;
	}
	
	private String getInputProbeFile(){
		String fileName = "inputFile.txt";
		String probeFile = new String();
		try{
			RandomAccessFile readFile = new RandomAccessFile(fileName,"r");
			for(int i = 0; i < 2; i++){
				probeFile = readFile.readLine();
			}
			readFile.close();
		}    
		catch(FileNotFoundException e){
			System.out.print("File not found");
		}
		catch(IOException e){
			System.out.print("IO error");
		}
		return probeFile;
	}
	
	private ArrayList<String[]> getProbeEndpoints(Topology topo){
		ArrayList<String[]> endPoints = new ArrayList<String[]>();
		String fileName = this.getInputProbeFile();
		String[] temp = new String[2];
		try{
			RandomAccessFile readFile = new RandomAccessFile(fileName,"r");
			String line = readFile.readLine();
			while(line != null){
				//System.out.println(line);
				temp = line.split(" ");
				temp[0] = "n" + temp[0];
				temp[1] = "n" + temp[1];
				endPoints.add(temp);
				line = readFile.readLine();
			}
			readFile.close();
		}    
		catch(FileNotFoundException e){
			System.out.print("File not found");
		}
		catch(IOException e){
			System.out.print("IO error");
		}
		return endPoints;
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
		String[] nodeProbe = new String[3];
		int ppNum = 0;
		nodeProbe[0] = nodeA;
		nodeProbe[1] = "0";
		nodeProbe[2] = "0";
		int removedNodeDegree = 0;
		int pathLevel = 1000000000;
		ArrayList<Edge[]> shortPaths = new ArrayList<Edge[]>();
        int exitFlag = 0;
		String[] endPoints = new String[2];
		endPoints[0] = nodeA;
		endPoints[1] = nodeB;
	
		
		if(shortPath.offer(nodeProbe)){
			String[] startNode = (String[])shortPath.poll();
			removedNodeDegree = topo.getNode(startNode[0]).getDegree();
			
			for(int j = 0; j < removedNodeDegree; j++){
				String[] nextHop = new String[3];
				nextHop[0] = (String) topo.getNode(startNode[0]).getNeighborNodes().get(j);
				nextHop[1] = new Integer(ppNum).toString();
				nextHop[2] = "1";
				shortPath.offer(nextHop);
				nextHop = null;	
			}
			
			while(shortPath.size() != 0){
				String[] head = (String[])shortPath.poll();
				
				int level = new Integer(head[2]).intValue();
				if(level > pathLevel){					
				    String pID = "p" + new Integer(probeNumber).toString();
				    Probe probe = new Probe(pID,endPoints);				    
				    max.AddProbe(probe,this.getOptimizedPath(shortPaths));
					exitFlag = 1;
				    break;
				}
				
			    int addedNodeDegree = 0;
		  	    ppNum++;
		  	    if(head[1].equalsIgnoreCase("0")){
	    	        String ID = new Integer(ppNum).toString();
		    	    ArrayList<String> tempNodes = new ArrayList<String>();
		    	    tempNodes.add(0,nodeA);
			        tempNodes.add(1,head[0]);
			        tempPT.put(ID, tempNodes);
			        if(head[0].equals(nodeB)){
			        	pathLevel = new Integer(head[2]).intValue();
			    	    Edge[] tempEdges = new Edge[tempPT.get(ID).size() - 1];
				        String EdgeID = topo.getEdgeID(nodeA, head[0]);
				        tempEdges[0] = topo.getEdge(EdgeID);
				        shortPaths.add(tempEdges);
				        tempEdges = null;
			        }
			        tempNodes = null;
			    }
				else{ 
			        String ID = new Integer(ppNum).toString();
				    ArrayList<String> tempNodes = new ArrayList<String>();
				    String preProbeID = head[1];
				  
					if (tempPT.containsKey(preProbeID)){
					    int a, p;
					    for (a = 0,p = tempPT.get(preProbeID).size(); a < p; a++){
					        tempNodes.add(a, (String)tempPT.get(preProbeID).get(a));
					    }
					    tempNodes.add(a,head[0]);
					    tempPT.put(ID, tempNodes);
					}
				    else{
					    System.out.println("Error, preProbe is null");
					}
					if(head[0].equals(nodeB)){
						pathLevel = new Integer(head[2]).intValue();
					    Edge[] tempEdges = new Edge[tempPT.get(ID).size() - 1];
					    for (int b = 0, p = tempPT.get(ID).size() - 1; b < p; b++){
					        String EdgeID = topo.getEdgeID((String)tempPT.get(ID).get(b), (String)tempPT.get(ID).get(b + 1));
					        tempEdges[b] = topo.getEdge(EdgeID);
					    }
					    shortPaths.add(tempEdges);
					    tempEdges = null;
					}
					
					tempNodes = null;
				}			
		  	    
		  	    addedNodeDegree = topo.getNode(head[0]).getDegree();
		  	    
				if (addedNodeDegree != 1){
					
					for(int k = 0; k < addedNodeDegree; k++){
						String[] nextHop = new String[3];
				        String nextNode = (String)topo.getNode(head[0]).getNeighborNodes().get(k);
				        nextHop[0] = nextNode;
				    	nextHop[1] = new Integer(ppNum).toString();
				    	nextHop[2] = new Integer(new Integer(head[2]).intValue() + 1).toString();
				    	if(this.isNodeAddible(nextHop, tempPT))
				    		shortPath.offer(nextHop);
				        nextHop = null;
					}
				}
				
				head = null;	
			}
			if(exitFlag == 0){
			    String pID = "p" + new Integer(probeNumber).toString();
			    Probe probe = new Probe(pID,endPoints);				    
			    max.AddProbe(probe,this.getOptimizedPath(shortPaths));
			}
		}	
		
		shortPath = null;
		shortPaths = null;
		tempPT = null;
    }
    
	public EdgeDependencyMatrix selectPSet(){
        int probeNumber = 1;  
        ArrayList<String[]> endPoints = new ArrayList<String[]>();
        endPoints = this.getProbeEndpoints(topo);
        System.gc();
        System.out.println("������ɱ������������Ժ�......");
		int pNum = endPoints.size();
		for(int i = 0; i < pNum; i++){
			this.getShortestPath(edm, probeNumber, endPoints.get(i)[0], endPoints.get(i)[1]);
			System.gc();
			probeNumber++;
		}
		return edm;
	}
	
	private void getVaribleEdge(){
		varEdges = topo.getCycleEdge();		
		String[] nodePoints = new String[2];
		ArrayList<String> removedEdge = new ArrayList<String>();
		int edgevalue = 0;
				
		//ȥ����һ�β�����ı�
		int[][] matrix = edm.getMatrixAll();
		for(int j = 0, p = edm.getEdgeCount(); j < p; j++){
			for(int i = 0, q = edm.getProbeCount(); i < q; i++){
				edgevalue = edgevalue + matrix[i][j];
			}
			if (edgevalue == 0){
				String edgeID = "e" + new Integer(j+1).toString();
				varEdges.remove(edgeID);
			}
			edgevalue = 0;
		}
        
		//	ȥ��ֻ��Ψһ·���ı�
		for(int i = 0, p = varEdges.size(); i < p; i++){
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
		
		System.out.print("The edges can be changed: ");
		for(int a = 0; a < varEdges.size(); a++){
			System.out.print(" "+ varEdges.get(a));
		}
		System.out.println();//����
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
		String[] nodeProbe = new String[3];
		int ppNum = 0;
		nodeProbe[0] = nodeA;
		nodeProbe[1] = "0";
		nodeProbe[2] = "0";
		int removedNodeDegree = 0;
		int nodeFlag = 0;
		String[] endPoints = new String[2];
		endPoints[0] = nodeA;
		endPoints[1] = nodeB;
		int exitFlag = 0;
		
		int pathLevel = 1000000000;
		ArrayList<Edge[]> shortPaths = new ArrayList<Edge[]>();
		
		if(shortPath.offer(nodeProbe)){
			
			String[] startNode = (String[])shortPath.poll();
			removedNodeDegree = topo.getNode(startNode[0]).getDegree();
			
			for(int j = 0; j < removedNodeDegree; j++){
				String[] nextHop = new String[3];
				nextHop[0] = (String) topo.getNode(startNode[0]).getNeighborNodes().get(j);
				nextHop[1] = new Integer(ppNum).toString();
				nextHop[2] = "1";
				
				if(startNode[0].equals(sn[0]) && nextHop[0].equals(sn[1])){
					nodeFlag = 1;
				}
				else{
				    if(startNode[0].equals(sn[1]) && nextHop[0].equals(sn[0])){
				    	nodeFlag = 1;
				    }
				}
				
				if(nodeFlag != 1)
	    			shortPath.offer(nextHop);
				
				nodeFlag = 0;
				nextHop = null;
			}
					
		    while(shortPath.size() != 0){
		    
				String[] head = (String[])shortPath.poll();
				
                int level = new Integer(head[2]).intValue();
				
				if(level > pathLevel){
				    String pID = "p" + new Integer(probeNumber).toString();
				    Probe probe = new Probe(pID,endPoints);
				    nedm.AddProbe(probe,this.getOptimizedPath(shortPaths));
				    exitFlag = 1;
					break;
				}
				
				int addedNodeDegree = 0;
			  	ppNum++;
			  	if(head[1].equalsIgnoreCase("0")){
		    	    String ID = new Integer(ppNum).toString();
			    	ArrayList<String> tempNodes = new ArrayList<String>();
			    	tempNodes.add(0,nodeA);
				    tempNodes.add(1,head[0]);
				    tempPT.put(ID, tempNodes);
				    if(head[0].equals(nodeB)){
				    	pathLevel = new Integer(head[2]).intValue();
				    	Edge[] tempEdges = new Edge[tempPT.get(ID).size() - 1];
					    String EdgeID = topo.getEdgeID(nodeA, head[0]);
					    tempEdges[0] = topo.getEdge(EdgeID);
					    shortPaths.add(tempEdges);
					    tempEdges = null;
				    }
				    tempNodes = null;
				}
			    else{ 
				    String ID = new Integer(ppNum).toString();
					ArrayList<String> tempNodes = new ArrayList<String>();
					String preProbeID = head[1];
					
					if (tempPT.containsKey(preProbeID)){
						int a,p;
						for (a = 0, p = tempPT.get(preProbeID).size(); a < p; a++){
						    tempNodes.add(a, (String)tempPT.get(preProbeID).get(a));
						}
						tempNodes.add(a,head[0]);
						tempPT.put(ID, tempNodes);
					}
					else{
						System.out.println("Error, preProbe is null");
					}
					
					if(head[0].equals(nodeB)){
						pathLevel = new Integer(head[2]).intValue();
						Edge[] tempEdges = new Edge[tempPT.get(ID).size() - 1];
						for (int b = 0, p = tempPT.get(ID).size() - 1; b < p; b++){
						    String EdgeID = topo.getEdgeID((String)tempPT.get(ID).get(b), (String)tempPT.get(ID).get(b + 1));
						    tempEdges[b] = topo.getEdge(EdgeID);
						}
						shortPaths.add(tempEdges);
						tempEdges = null;
					}
					
					tempNodes = null;
					
				}				
			  	
			  	addedNodeDegree = topo.getNode(head[0]).getDegree();
			
			  	if (addedNodeDegree != 1){
					for(int k = 0; k < addedNodeDegree; k++){
					    String nextNode = (String)topo.getNode(head[0]).getNeighborNodes().get(k);
					    
					    if(head[0].equals(sn[0]) && nextNode.equals(sn[1])){
					    	nodeFlag = 1;
					    }
					    else{
					    	if(head[0].equals(sn[1]) && nextNode.equals(sn[0])){
					    		nodeFlag = 1;
					    	}
					    }
					    	
					    if(nodeFlag != 1){		  
					    	String[] nextHop = new String[3];
					    	nextHop[0] = nextNode;
					    	nextHop[1] = new Integer(ppNum).toString();
                            nextHop[2] = new Integer(new Integer(head[2]).intValue() + 1).toString();
					    	
					    	if(this.isNodeAddible(nextHop, tempPT))
					    		shortPath.offer(nextHop);
					    	nextHop = null;
					    }
					    nodeFlag = 0;
					}
				}
			 
			  	head = null;
			}

		    if(exitFlag == 0){
			    String pID = "p" + new Integer(probeNumber).toString();
			    Probe probe = new Probe(pID,endPoints);		
			    if(this.getOptimizedPath(shortPaths) != null)
			        nedm.AddProbe(probe,this.getOptimizedPath(shortPaths));
			}
		}	
		
		shortPath = null;
		shortPaths = null;
		tempPT = null;
	}

	public EdgeDependencyMatrix selectNewPSet(){
		
		int probeNumber = 1; 
		ArrayList<String[]> endPoints = new ArrayList<String[]>();
        endPoints = this.getProbeEndpoints(topo);
		int pNum = endPoints.size();
		System.out.println("����Ѱ�ҿɸ��µıߣ����Ժ�......");
		String selectedEdge = this.getEdgeRandomly();
		System.out.println("The edge to be changed is " + selectedEdge);
		System.out.println("���ڸ��±������������Ժ�......");
		String[] selNodes = new String[2];
		selNodes = this.getSelNodes(selectedEdge);
		
		//System.out.println("the selected nodes is " + selNodes[0] + " and " + selNodes[1]);

        ArrayList<Integer> probeChanged  = new ArrayList<Integer>();
		this.getProbeChanged(probeChanged, selectedEdge);
		
		for(int i = 0; i < pNum; i++){
			if(probeChanged.contains(probeNumber)){
				this.getShortestPathRes(probeNumber, endPoints.get(i)[0], endPoints.get(i)[1], selNodes);
			    System.gc();
			}
			else{
				this.getShortestPath(nedm, probeNumber, endPoints.get(i)[0], endPoints.get(i)[1]);
				System.gc();
			}
			probeNumber++;
		}		
		return nedm;
    }
}
