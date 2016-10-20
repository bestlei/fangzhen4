package activeprobe.dataclass;
import java.util.*;


public class DependencyMatrix{
	 
	 
	 private ArrayList<Node> Nodes;
	 private ArrayList<Edge> Edges;
	 private Hashtable<Probe,ProbeInfo> ProbeTable;	//存储探针信息，探针ID，探针信息，包括探针路径上的边和节点的信息
	 private ArrayList<Node> nodeStations;		
	 
	 public ArrayList<Node> getNodeStations() {
		return nodeStations;
	}
	public void setNodeStations(ArrayList<Node> nodeStations) {
		this.nodeStations = nodeStations;
	}
	 class ProbeInfo
	 {
		 Probe probe;
		 ArrayList<Node> nodes=new ArrayList<Node>();
		 ArrayList<Edge> edges=new ArrayList<Edge>();
		 public ProbeInfo(Probe p, ArrayList<Node> ns,ArrayList<Edge> es)
		 {
			 this.probe=p;
			 this.nodes=ns;
			 this.edges=es;
		 }
		 public boolean IsNodePasted(Node n)
		 {
			return this.nodes.contains(n);
		 }
		 public boolean IsEdgePasted(Edge e)
		 {
			 return this.edges.contains(e);
		 }
		 public void AddPastNode(Node n)
		 {
			 if(!this.IsNodePasted(n)) this.nodes.add(n);
		 }
		 public void AddPastEdge(Edge e)
		 {
			 if(!this.IsEdgePasted(e))this.edges.add(e);
		 }
		 public void DeletePastNode(Node n)
		 {
			 this.nodes.remove(n);
		 }
		 public void DeletePastEdge(Edge e)
		 {
			 this.edges.remove(e);
		 }
	 }
    public DependencyMatrix()
    {  
    	this.Nodes=new ArrayList<Node>();
    	this.Edges=new ArrayList<Edge>();
    	this.ProbeTable=new Hashtable<Probe,ProbeInfo>();
    }
     
	public void ResetNodes(ArrayList<Node> nodes)
    {
        this.Nodes.clear();
        this.ProbeTable.clear();
        for(int i=0;i<nodes.size();i++)
        {
        	if(this.IsNodeExist(nodes.get(i))) continue;
        	this.Nodes.add(nodes.get(i));
        }     
    }
	public void ResetEdges(ArrayList<Edge> edges)
    {
        this.Edges.clear();
        this.ProbeTable.clear();
        for(int i=0;i<edges.size();i++)
        {
        	if(this.IsEdgeExist(edges.get(i))) continue;
        	this.Edges.add(edges.get(i));
        }     
    }
	/**
	 * 
	 * @param p
	 * @param nodes  探测路径上的节点
	 * @param edges  探测路径上的边
	 * @return
	 */
	public boolean AddProbe(Probe p,Node[] nodes,Edge[] edges)
	{
		if(this.IsProbeExist(p)) return false;
		ArrayList<Node> ns=new ArrayList<Node>();		
		ArrayList<Edge> es=new ArrayList<Edge>();
		for(int i=0;i<nodes.length;i++)
		{    
			Node n=this.GetNode(nodes[i]);
			if(n==null||ns.contains(nodes[i])) continue;
			ns.add(n);
		}
		for(int i=0;i<edges.length;i++)
		{    
			Edge e=this.GetEdge(edges[i]);
			if(e==null||es.contains(edges[i])) continue;
			es.add(e);
		}
		ProbeInfo pi=new ProbeInfo(p,ns,es);
		this.ProbeTable.put(p,pi);
		return true;		
	}
	public boolean AddProbe(Probe p,ArrayList<Node> nodes,ArrayList<Edge> edges)
	{
		if(this.IsProbeExist(p)) return false;
		ArrayList<Node> ns=new ArrayList<Node>();	
		ArrayList<Edge> es=new ArrayList<Edge>();	
		for(int i=0;i<nodes.size();i++)
		{    
			Node n=this.GetNode(nodes.get(i));
			if(n==null||ns.contains(nodes.get(i))) continue;
			ns.add(n);
		}
		for(int i=0;i<edges.size();i++)
		{    
			Edge e=this.GetEdge(edges.get(i));
			if(e==null||es.contains(edges.get(i))) continue;
			es.add(e);
		}
		ProbeInfo pi=new ProbeInfo(p,ns,es);
		this.ProbeTable.put(p,pi);
		return true;
	}
    public ArrayList<Node> GetAllNodes()
    {
    	ArrayList<Node> ns=new ArrayList<Node>();
    	ns.addAll(this.Nodes);
    	return ns;
    }
    public ArrayList<Edge> GetAllEdges()
    {
    	ArrayList<Edge> es=new ArrayList<Edge>();
    	es.addAll(this.Edges);
    	return es;
    }
    public Probe getProbe(String pID){
		Enumeration<ProbeInfo> enu=this.ProbeTable.elements();
		while(enu.hasMoreElements())
		{
		    ProbeInfo pi=enu.nextElement();
		    Probe p = pi.probe;
		    if (p.getID().equalsIgnoreCase(pID)){
		    	return pi.probe;
		    }
		}
		return null;
	}
    public ArrayList<Probe> GetAllProbes()
    {
    	ArrayList<Probe> ps=new ArrayList<Probe>();
    	
    	Enumeration<Probe> enu=this.ProbeTable.keys();
 	    while(enu.hasMoreElements())
 	    {
 	      Probe p=enu.nextElement();
 	      ps.add(p);	     
 	    }
 	   
 	   return ps;
    }  
    public ArrayList<Probe> GetAllProbesRandom()
    {
    	ArrayList<Probe> ps=new ArrayList<Probe>();
    	Random rn=new Random();
    	Enumeration<Probe> enu=this.ProbeTable.keys();
 	    while(enu.hasMoreElements())
 	    {
 	      Probe p=enu.nextElement(); 
 	      if(ps.size()>0)
 	        ps.add(rn.nextInt(ps.size()),p);
 	      else
 	    	ps.add(p);
 	    }
    	return ps;
    }

	public boolean IsProbeNodeMatch(Probe p,Node n) 
	{
	  ProbeInfo pi=this.ProbeTable.get(p);
	  if(pi==null) {return false;}
	 
	  return  pi.IsNodePasted(n);
	}
	public boolean IsProbeEdgeMatch(Probe p,Edge e)
	{
		ProbeInfo pi=this.ProbeTable.get(p);
		if(pi==null){return false;}
		return pi.IsEdgePasted(e);
	}
	public  ArrayList<Node> GetNodesforProbe (Probe p) 
	{
	  ProbeInfo pi=this.ProbeTable.get(p);
	  if(pi==null) return new ArrayList<Node>();
	  return pi.nodes;	  
	}
	public ArrayList<Edge> GetEdgesforProbe (Probe p)
	{
		  ProbeInfo pi=this.ProbeTable.get(p);
		  if(pi==null) return new ArrayList<Edge>();
		  return pi.edges;	
	}
	public  ArrayList<Probe> GetProbesforNode(Node node)
	{
		ArrayList<Probe> probes=new ArrayList<Probe>(); 
		Enumeration<ProbeInfo> enu=this.ProbeTable.elements();
		while(enu.hasMoreElements())
		{
		    ProbeInfo pi=enu.nextElement();
		    if(pi.IsNodePasted(node)) probes.add(pi.probe);	     
		}
		return probes;
	}
	public  ArrayList<Probe> GetProbesforEdge(Edge edge)
	{
		ArrayList<Probe> probes=new ArrayList<Probe>(); 
		Enumeration<ProbeInfo> enu=this.ProbeTable.elements();
		while(enu.hasMoreElements())
		{
		    ProbeInfo pi=enu.nextElement();
		    if(pi.IsEdgePasted(edge)) probes.add(pi.probe);	     
		}
		return probes;
	}
	public int getProbeCount()
	{
		return this.ProbeTable.size();
	}
	public int getNodeCount()
	{
		return this.Nodes.size();
	}
	public int getEdgeCount(){
		return this.Edges.size();
	}
	public boolean AddNode(Node node)
	{
	  if(this.IsNodeExist(node)) return false;
	  this.Nodes.add(node);
	  return true;
	}
	public void DeleteNode(Node node)
	{
	   Enumeration<ProbeInfo> enu=this.ProbeTable.elements();
	   while(enu.hasMoreElements())
	   {
	      ProbeInfo pi=enu.nextElement();
	      pi.DeletePastNode(node);	     
	   }
	   this.Nodes.remove(node);
	}
	public boolean AddEdge(Edge edge)
	{
	  if(this.IsEdgeExist(edge)) return false;
	  this.Edges.add(edge);
	  return true;
	}
	public void DeleteEdge(Edge edge)
	{
	   Enumeration<ProbeInfo> enu=this.ProbeTable.elements();
	   while(enu.hasMoreElements())
	   {
	      ProbeInfo pi=enu.nextElement();
	      pi.DeletePastEdge(edge);	     
	   }
	   this.Edges.remove(edge);
	}
	public void ReservTheseNodes(ArrayList<Node> nodes)
	{   
     	ArrayList<Node> nodeToDelete=new ArrayList<Node>();
     	Collections.sort(nodes);
	  	for(int i=0;i<this.Nodes.size();i++)
	   	{
	  	   Node n=this.Nodes.get(i);
	   	   if(Collections.binarySearch(nodes,n)<0)
	   	   {
	  		   nodeToDelete.add(this.Nodes.get(i));
	   	   }
	   	}
	      	
	   	for(int i=0;i<nodeToDelete.size();i++)
	  	{
	   		this.DeleteNode(nodeToDelete.get(i));
	   	}
	   	ArrayList<Probe> probeEmpty=new ArrayList<Probe>();
	   	Enumeration<ProbeInfo> enu=this.ProbeTable.elements();
	   	while(enu.hasMoreElements())
	   	{
	   		ProbeInfo pi=enu.nextElement();
	   		if(pi.nodes.size()==0)
	   		{
	   			probeEmpty.add(pi.probe);
	   		}
	   	}
	   	for(int i=0;i<probeEmpty.size();i++)
	   	{
	   		this.ProbeTable.remove(probeEmpty.get(i));
	   	}	   	
	}
	public boolean AddProbe(Probe p)
	{
		if(this.IsProbeExist(p)) return false;
		ArrayList<Node> ns=new ArrayList<Node>();
		ArrayList<Edge> es=new ArrayList<Edge>();
		ProbeInfo pi=new ProbeInfo(p,ns,es);
		this.ProbeTable.put(p,pi);
		return true;
	}
	public void DeleteProbe(Probe p)
    {
       if(this.IsProbeExist(p)) this.ProbeTable.remove(p);
    }
    public void ExtendMatrix(int k)
    {
  	  ArrayList<Node>  singleNode=this.GetAllNodes();
  	  ArrayList<Node>  nodeCon=new ArrayList<Node>();  	  
  	  this.ChoseKNode(1, k,0,singleNode,nodeCon);  	 
    }
    public DependencyMatrix Clone()
    {
    	DependencyMatrix newMatrix=new DependencyMatrix();
    	
    	newMatrix.Nodes.addAll(this.Nodes);
    	newMatrix.Edges.addAll(this.Edges);
    	Enumeration<ProbeInfo> enu=this.ProbeTable.elements();
    	while(enu.hasMoreElements())
    	{   
    		   ProbeInfo pi=enu.nextElement();    	    
    	    ArrayList<Node> nodes=new ArrayList<Node>();
    	    ArrayList<Edge> edges=new ArrayList<Edge>();
    	    nodes.addAll(pi.nodes);
    	    edges.addAll(pi.edges);
    	    ProbeInfo piNew=new ProbeInfo(pi.probe,nodes,edges); 
    	    newMatrix.ProbeTable.put(pi.probe, piNew);
    	}    	
    	
    	return newMatrix;
    }
    
    private void ConverNode(ArrayList<Node> nodeCon)
    {
  	  String id="";
  	
  	  for(int i=0;i<nodeCon.size();i++)
  	  {
  		  id+=nodeCon.get(i).getID();
  	  }
  	  Node n=new Node(id);
  	  n.setStatus(true);
  	  this.Nodes.add(n);
  	  
  	  Enumeration<ProbeInfo>  enu=this.ProbeTable.elements();
  	 
  	  while(enu.hasMoreElements())
  	  {  
  		  ProbeInfo pi=enu.nextElement();
  		  for(int i=0;i<nodeCon.size();i++)
  		  {
  			  if(pi.IsNodePasted(nodeCon.get(i))) pi.AddPastNode(n);
  			  break;
  		  }
  	  }
    }
    private void ChoseKNode(int m,int k,int start,ArrayList<Node> singleNode,ArrayList<Node> nodeCon)
    {     
          for(int i=start;i<singleNode.size();i++)
          {          	
          	nodeCon.add(singleNode.get(i));  
          	
          	if(m>1) this.ConverNode(nodeCon);
          	
          	if(m<k)
          	  {this.ChoseKNode(m+1,k,i+1,singleNode,nodeCon);}
          	
          	nodeCon.remove(nodeCon.size()-1);
          }
    }   
    private Node GetNode(Node n)
	{
		for(int i=0;i<this.Nodes.size();i++)
		{  
			if(this.Nodes.get(i).equals(n)) return  this.Nodes.get(i);
		}
		return null;
	}
    private Edge GetEdge(Edge e)
	{
		for(int i=0;i<this.Edges.size();i++)
		{  
			if(this.Edges.get(i).equals(e)) return  this.Edges.get(i);
		}
		return null;
	}
    private boolean IsNodeExist(Node node)
    {
    	return this.Nodes.contains(node);    	
    }
    private boolean IsEdgeExist(Edge edge)
    {
    	return this.Edges.contains(edge);    	
    }
    private boolean IsProbeExist(Probe p)
    {
    	return this.ProbeTable.containsKey(p);
    }    
}
