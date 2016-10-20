package activeprobe.dataclass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;
public class Path {
	private String pathID;
	private String startPoint;
	private String endPoint;
	private ArrayList<String> pathNodes = new ArrayList<String>();
	private ArrayList<String> pathEdges = new ArrayList<String>();
	public Path(){}
	public Path(String ID,String start,String end,ArrayList<String> nodes,ArrayList<String> edges)
	{
		this.pathID = ID;
		this.startPoint = start;
		this.endPoint = end;
		this.pathNodes.addAll(nodes);
		this.pathEdges.addAll(edges);
	}
	
	public Path(String ID,String start,String end,ArrayList<String> nodes)
	{
		this.pathID = ID;
		this.startPoint = start;
		this.endPoint = end;
		this.pathNodes.addAll(nodes);
	}
	
	public void setPathID(String ID)
	{
		this.pathID = ID;
	}
	public String getPathID()
	{
		return pathID;
	}
	
	public void setStartPoint(String start)
	{
		this.startPoint = start;
	}
	public String getStartPoint()
	{
		return startPoint;
	}
	
	public String getEndPoint()
	{
		return endPoint;
	}
	public void setEndPoint(String end)
	{
		this.endPoint = end;
	}
	
	public void setPathNodes(ArrayList<String> nodes)
	{
		this.pathNodes.clear();
		this.pathNodes.addAll(nodes);
	}
	public ArrayList<String> getPathNodes()
	{
		return pathNodes;
	}
	
	public ArrayList<String> getPathEdges()
	{
		return pathEdges;
	}
	public void setPathEdges(ArrayList<String> edges)
	{
		this.pathEdges.addAll(edges);
	}
	public void addEdge(String s)
	{
		this.pathEdges.add(s);
	}
	
	public boolean isSimplePath()
	{
		int length = pathNodes.size();
		for(int i=0;i<length;i++)
		{
			String s = pathNodes.remove(i);
			if(pathNodes.contains(s)) 
				return false;
			
			pathNodes.add(s);
		}
		return true;
	}

	public static boolean isSimplePath(ArrayList<String> nodeKeys)
	{
		int length = nodeKeys.size();
		for(int i=0;i<length;i++)
		{
			if(Collections.frequency(nodeKeys, nodeKeys.get(i))>1) return false;
		}
		return true;
	}
	
	public static ArrayList<String> getPathFromStack(HashMap<String,String[]> stack,String[] currentNode)
	{
		ArrayList<String> pathNodeRecord = new ArrayList<String>();
		String[] tempArray = currentNode[0].split("_");
		pathNodeRecord.add(tempArray[0]);
		String[] prevous = stack.get(currentNode[1]);
		while(prevous!=null&&prevous[1]!=null)
		{
			tempArray = prevous[0].split("_");
			pathNodeRecord.add(tempArray[0]);
			prevous = stack.get(prevous[1]);
		}
		if(prevous!=null)
		{
			tempArray = prevous[0].split("_");
			pathNodeRecord.add(prevous[0]);
		}
		Collections.reverse(pathNodeRecord);
		return pathNodeRecord;
	}
	
	public static ArrayList<String> getPathEgesFromStack(HashMap<String,String[]> stack,String[] currentNode)
	{
		ArrayList<String> pathNodeRecord = new ArrayList<String>();
		String[] tempArray = currentNode[0].split("_");
		pathNodeRecord.add(tempArray[0]);
		String[] prevous = stack.get(currentNode[1]);
		while(prevous!=null&&prevous[1]!=null)
		{
			tempArray = prevous[0].split("_");
			pathNodeRecord.add(tempArray[0]);
			prevous = stack.get(prevous[1]);
		}
		if(prevous!=null)
		{
			tempArray = prevous[0].split("_");
			pathNodeRecord.add(prevous[0]);
		}
		Collections.reverse(pathNodeRecord);
		return pathNodeRecord;
	}
	
	public static boolean isInQueue(Queue<String[]> q,String s)
	{
		Queue<String[]> queue = new LinkedList<String[]>();
		queue.addAll(q);
		String[] temp = null;
		while(queue.size()>0)
		{
			temp = queue.poll();
			if(temp[0]!=null&&s.equalsIgnoreCase(temp[0]))
			{
				return true;
			}
		}
		return false;
	}
	
}
