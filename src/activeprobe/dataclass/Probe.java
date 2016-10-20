package activeprobe.dataclass;

public class Probe implements Comparable<Probe> {
	
	String[] endPoints = new String[2];
	
    @Override
	public boolean equals(Object obj) {
		if(super.equals(obj)) return true;
		if(this.getClass().isInstance(obj)){
		   return this.id.equals(((Probe)obj).id);
		}
		return false;
	}
	@Override
	public String toString() {
		return this.id;
	}
	
	private String id;
    private ProbeStatus status;     //UKnow Success Failed
    private double noisy;
    
    public Probe(){
    	
    }
    public Probe(String s)
    {  
    	this.id=s;
    	this.status=ProbeStatus.Success;
    	this.noisy=0;
    }    
    
    public Probe(String s,String[] endPoint)
    {  
    	this.id=s;
    	this.status=ProbeStatus.Success;
    	this.noisy=0;
    	this.endPoints = endPoint;
    }    
    
	public int compareTo(Probe o) 
	{
		if(this==o) return 0;
		int thisIDNo = Integer.parseInt(this.getID().substring(1));
		int oIDNo = Integer.parseInt(o.getID().substring(1));
		if(thisIDNo > oIDNo) 
			return 1;
		else if(thisIDNo < oIDNo)
				return -1;
			 else 
				 return 0;	
	}
	public ProbeStatus getStatus() {
		return status;
	}
	public void setStatus(ProbeStatus status) 
	{   
		if(status==null){this.status=ProbeStatus.Success;}
		else { this.status = status;}
	}
	public String getID() 
	{
		return id;
	}
	public double getNoisy() {
		return noisy;
	}
	public void setNoisy(double noisy) {
		this.noisy = noisy;
	}
	public String getStartPoint()
	{
		return endPoints[0];
	}
	public String getEndPoint()
	{
		return endPoints[1];
	}
	
	public Object clone()
	{
		Probe p = new Probe();
		p.endPoints = this.endPoints;
		p.id = this.id;
		p.noisy = this.noisy;
		p.status = this.status;
		return p;
	}
}
