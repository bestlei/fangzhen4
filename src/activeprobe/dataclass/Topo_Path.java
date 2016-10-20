package activeprobe.dataclass;
import java.util.ArrayList;
public class Topo_Path {
	private ArrayList<String> path;
	public Topo_Path(){
		path=new ArrayList<String>();
	}
	public Topo_Path(ArrayList<String> path){
		this.path=path;
	}
	public int size(){
		return path.size();
	}
	public String get(int index){
		return path.get(index);
	}
	public void add(String edge){
		path.add(edge);
	}
	public void clear(){
		path.clear();
	}
	public boolean contains(String edge){
		return path.contains(edge);
	}
}
