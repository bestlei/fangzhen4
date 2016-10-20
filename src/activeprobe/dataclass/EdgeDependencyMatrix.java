package activeprobe.dataclass;

import java.util.*;
public class EdgeDependencyMatrix{
	 
	 
	 private ArrayList<Edge> Edges;
	 private Hashtable<Probe,ProbeInfo> ProbeTable;	
	
	 
	
	 class ProbeInfo
	 {   
		 Probe     probe;
		 ArrayList<Edge> edges;
		 public ProbeInfo(Probe p, ArrayList<Edge> es)
		 {
			 this.probe=p;
			 this.edges=es;
		 }
		 
		 public boolean IsEdgePasted(Edge e)
		 {
			return this.edges.contains(e);
		 }
		 
		 public void AddPastEdge(Edge e)
		 {
			 if(!this.IsEdgePasted(e)) 
				 this.edges.add(e);
		 }
		 
		 public void DeletePastEdge(Edge e)
		 {
			 this.edges.remove(e);
		 }
	 }
	 
	 
      public EdgeDependencyMatrix()
      {  
    	 this.Edges=new ArrayList<Edge>();
    	 this.ProbeTable=new Hashtable<Probe,ProbeInfo>();
    	
      }
       
    public boolean IsEdgeExist(Edge edge)
    {
    	return this.Edges.contains(edge);    	
    }
    
    public ArrayList<Edge> GetAllEdges()
    {
    	ArrayList<Edge> es=new ArrayList<Edge>();
    	es.addAll(this.Edges);
    	return es;
    }
    
    public boolean IsProbeExist(Probe p)
    {
    	return this.ProbeTable.containsKey(p);
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
    

	public boolean IsProbeEdgeMatch(Probe p,Edge e) 
	{
	  ProbeInfo pi=this.ProbeTable.get(p);
	  if(pi==null) {return false;}
	 
	  return  pi.IsEdgePasted(e);
	}
	

	public void ResetEdges(Edge[] edges)
    {
        this.Edges.clear();
        this.ProbeTable.clear();
        for(int i=0;i<edges.length;i++)
        {
        	if(this.IsEdgeExist(edges[i])) continue;
        	this.Edges.add(edges[i]);
        }        
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
	}
	
	public boolean AddProbe(Probe p,Edge[] edges)
	{
		if(this.IsProbeExist(p)) return false;
		ArrayList<Edge> es=new ArrayList<Edge>();		
		
		for(int i=0;i<edges.length;i++)
		{
			if(!this.IsEdgeExist(edges[i])||es.contains(edges[i])) continue;
			es.add(edges[i]);
		}
		ProbeInfo pi=new ProbeInfo(p,es);
		this.ProbeTable.put(p,pi);
		return true;
		
	}
	
	public boolean AddProbe(Probe p)
	{
		if(this.IsProbeExist(p)) return false;
		ArrayList<Edge> es=new ArrayList<Edge>();
		ProbeInfo pi=new ProbeInfo(p,es);
		this.ProbeTable.put(p,pi);
		return true;
	}
	
	public void DeleteProbe(Probe p)
    {
       if(this.IsProbeExist(p)) this.ProbeTable.remove(p);
    }
	
	
	public boolean SetProbeToPastEdge(Probe p,Edge e,boolean past) 
	{
	   if(!this.ProbeTable.containsKey(p)) return false;
	   if(!this.Edges.contains(e)) return false;
	  
	   ProbeInfo pi=this.ProbeTable.get(p);
	   if(past)
	   {
		   pi.AddPastEdge(e);
	   }
	   else
	   {
		   pi.DeletePastEdge(e);
	   }
	   return true;
	}
	public boolean IsProbePastEdge(Probe p,Edge e)
	{
		if(!this.ProbeTable.containsKey(p)) return false;
		if(!this.Edges.contains(e)) return false;
		  
		ProbeInfo pi=this.ProbeTable.get(p);
		return pi.IsEdgePasted(e);
	}
	
	public  ArrayList<Edge> GetEdgesforProbe (Probe p)
	{
	    ProbeInfo pi=this.ProbeTable.get(p);
	    if(pi == null)
	    	System.out.println("null");
	    if(pi==null) return new ArrayList<Edge>();
	  
	    ArrayList<Edge> es=pi.edges;	
	    return es;	  
	}
	
	public ArrayList<Probe> GetProbesforEdge(Edge edge)
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
	
	public int getEdgeCount() {
		return this.Edges.size();
	}

	public int getProbeCount() {
		return this.ProbeTable.size();
	}
	
	public Probe getProbe(String pID){
		Enumeration<ProbeInfo> enu=this.ProbeTable.elements();
		while(enu.hasMoreElements())
		{
		    ProbeInfo pi=enu.nextElement();
		    Probe p = (Probe)pi.probe;
		    if (p.getID().equalsIgnoreCase(pID)){
		    	return pi.probe;
		    }
		}
		return null;
	}
	
	public int[][] getMatrixAll() {
		int[][] intMatrix = new int[this.getProbeCount()][this.getEdgeCount()];
		for(int i = 0; i < this.getProbeCount(); i++){
			String probeID = "p" + new Integer(i + 1).toString();			
			ArrayList<Edge> pastEdges = new ArrayList<Edge>();			
			if(this.getProbe(probeID) != null){
				pastEdges = this.GetEdgesforProbe(this.getProbe(probeID));
			}
			else{
				System.out.println(probeID);
				System.out.println("Probe Error");
			}
			
			for(int j=0; j < this.getEdgeCount();j++)
        	{
    	    	if(pastEdges.contains(this.GetAllEdges().get(j))){
    	    		intMatrix[i][j] = 1;
    	    	}
    	    	else 
    	    		intMatrix[i][j] = 0;
        	}	
		}
		return intMatrix;
		
	}
	
 
    public void PrintMatrix() {
    	System.out.print("       ");
      	System.out.println("\n");
      	int averageEdge = 0;
    	for(int i=0;i<this.Edges.size();i++)
    	{
      		//System.out.print(String.format("%6s",this.Nodes.get(i).toString());
      	    Edge tempEdge = new Edge();
      	    tempEdge = (Edge)this.Edges.get(i);
      	    System.out.print(String.format("%8s",tempEdge.getID()));
    	}
    	Enumeration<ProbeInfo> enu=this.ProbeTable.elements();
    	
    	while(enu.hasMoreElements())
    	{   
    		System.out.print("\n");
    		
    	    ProbeInfo pi=enu.nextElement();
    	    //System.out.print(String.format("%6s",pi.probe.toString()));
    	    Probe tempProbe = new Probe();
    	    tempProbe = (Probe)pi.probe;
    	    System.out.print(tempProbe.getID());
    	    ArrayList<Edge> edges=pi.edges;
    	   
    	    for(int i=0;i<this.Edges.size();i++)
        	{
    	    	if(edges.contains(this.Edges.get(i))){
    	    		System.out.print(String.format("%7d",1));
    	    		averageEdge++;
    	    	}
    	    	else System.out.print(String.format("%7d",0));
        	}
    	}
    	System.out.print("\n");
    	System.out.println(ProbeTable.size());
    	System.out.println(averageEdge/ProbeTable.size());
    	
    }

}