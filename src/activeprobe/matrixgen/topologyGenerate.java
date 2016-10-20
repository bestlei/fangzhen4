package activeprobe.matrixgen;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import activeprobe.dataclass.*;

public class topologyGenerate{
	public Topology genTopo(String fileName){
		Topology netTopo = null;
		System.out.println("filename----------------------------------"+fileName);
		netTopo = this.readInetFile(netTopo, fileName);
		return netTopo;
	}
	
	private Topology readInetFile(Topology netTopo, String fileName){
		try{
			RandomAccessFile readFile = new RandomAccessFile(fileName,"r");
			String edgeNo = new String();
			String line = readFile.readLine();
			String[] temp = line.split(" ");
			int totalNum,totalEdge;
			totalNum = new Integer(temp[0]).intValue();
            totalEdge = new Integer(temp[1]).intValue();
            
            //System.out.println(totalNum);
         
			netTopo = new Topology(totalNum,totalEdge);	
			for (int i = 0; i < totalEdge; i++){
				edgeNo = "e" + new Integer(i).toString();
				line = readFile.readLine();
				temp = line.split(" ");
				//System.out.println("size = "+temp.length);
				netTopo.addEdge(temp[0],temp[1],edgeNo);
			}
		
			netTopo.genLeafEdge();
			netTopo.genCycleEdge();
			readFile.close();
		}    
		catch(FileNotFoundException e){
			e.printStackTrace();
			System.out.println("File not found!!!topologyGenerate.readInetFile  ");
		}
		catch(IOException e){
			System.out.print("IO error");
		}
		return netTopo;
	}
	
}
