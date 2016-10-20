package activeprobe.dataclass;

public class Edge implements Comparable<Edge>{
	private String id = new String();
	private String startPoint = new String();
	private String endPoint = new String();
	private double probability=0;
	private boolean status = false;
	public Edge(){
	
	}
	
	@Override
	public String toString() {
		return this.id;
	}
	
	
	public Edge(String edgeNo, String sPoint, String ePoint){
		this.id = edgeNo;
		this.startPoint = sPoint;
		this.endPoint = ePoint;
	}
	
	public String getStartPoint(){
		return startPoint;
	}
	
	public String getEndPoint(){
		return endPoint;
	}
	
    public String getID(){
    	return id;
    }

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}
	
	public void setStatus(boolean s)
	{
		this.status = s;
	}
	
	public boolean getStatus()
	{
		return status;
	}
	
	public int compareTo(Edge o) {
	    if(this==o) return 0;	
	    int thisIDNo = Integer.parseInt(this.getID().substring(1));
	    int oIDNo = Integer.parseInt(o.getID().substring(1));
	    if(thisIDNo > oIDNo) return 1;
	    else if(thisIDNo < oIDNo) return -1;
	    	 else return 0;
	} 
}
