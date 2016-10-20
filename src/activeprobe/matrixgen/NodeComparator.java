package activeprobe.matrixgen;

import java.util.Comparator;
import activeprobe.dataclass.*;

public class NodeComparator implements Comparator {

	public int compare(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		Node n1 = (Node)arg0;
		Node n2 = (Node)arg1;
		String[] idString1 = n1.getID().split("n");
		String[] idString2 = n2.getID().split("n");
		int id1 = new Integer(idString1[1]).intValue();
		int id2 = new Integer(idString2[1]).intValue();
        if (id1 > id2){
        	return 1;
        }
        else{
        	if (id1 == id2){
        		return 0;
        		}
        	else
        		return -1;       	
        	}
	}
}
