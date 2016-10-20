package activeprobe.matrixgen;

import java.util.Comparator;

import activeprobe.dataclass.*;

public class EdgeComparator implements Comparator<Edge> {
	public int compare(Edge arg0, Edge arg1) {
		// TODO Auto-generated method stub
		Edge e1 = arg0;
		Edge e2 = arg1;
		String[] idString1 = e1.getID().split("e");
		String[] idString2 = e2.getID().split("e");
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
