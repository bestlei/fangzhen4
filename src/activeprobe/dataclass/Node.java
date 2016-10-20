package activeprobe.dataclass;

import java.util.ArrayList;

public class Node implements Comparable<Node> {
	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj)) return true;
		if(this.getClass().isInstance(obj)){
		   return this.id.equals(((Node)obj).id);
		}
		return false;
	}
	@Override
	public String toString() {
		return this.id;
	}

	  private String id;
    private boolean status;
    private int degree;
    private boolean isStation;
    private double   probability;
    private ArrayList<String> neighborNodes = new ArrayList<String>();

    public Node(String s)
    {
       this.id=s;
       this.status=false;
       this.degree=0;
       this.isStation=false;
    }
    
	public int compareTo(Node o) {
	    if(this==o) return 0;	
	    int thisIDNo = Integer.parseInt(this.getID().substring(1));
	    int oIDNo = Integer.parseInt(o.getID().substring(1));
	    if(thisIDNo > oIDNo) return 1;
	    else if(thisIDNo < oIDNo) return -1;
	    	 else return 0;
	}

    
	public int getDegree(){
	   	return degree;
    }
	  
    public ArrayList<String> getNeighborNodes(){
    	return neighborNodes;
    }
    
    public void addNeighbor(String neighbor)
    {
    	if(!neighborNodes.contains(neighbor)){
    		neighborNodes.add(neighbor);
    		degree++;
    	}
    }
    
	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getID() {
		return id;
	}
	public boolean isStation() {
		return isStation;
	}
	public void setStation(boolean isStation) {
		this.isStation = isStation;
	}
	public double getProbability() {
		return probability;
	}
	public void setProbability(double probability) {
		this.probability = probability;
	}
}
