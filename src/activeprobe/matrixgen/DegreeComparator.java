package activeprobe.matrixgen;

import java.util.Comparator;
import activeprobe.dataclass.*;

public class DegreeComparator implements Comparator {

	public int compare(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		Node n1 = (Node)arg0;
		Node n2 = (Node)arg1;
        if (n1.getDegree() > n2.getDegree()){
        	return 1;
        }
        else{
        	if (n1.getDegree() == n2.getDegree()){
        		return 0;
        		}
        	else
        		return -1;       	
        	}
	}
}
