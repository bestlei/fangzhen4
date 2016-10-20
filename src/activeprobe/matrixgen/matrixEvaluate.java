package activeprobe.matrixgen;

import java.io.*;
import activeprobe.dataclass.*;

public class matrixEvaluate {
	private void calculateSparsity(EdgeDependencyMatrix matrix, int[] probeNums, boolean append){
		File file = new File("Sparsity.txt");
		FileWriter fw = null;
		
		int pNum = probeNums.length;
		int eNum = matrix.getEdgeCount();
		double sqrtD = Math.sqrt(eNum);
		int[][] max = new int[pNum][eNum];
	    for(int i = 0; i < pNum; i++){
	    	max[i] = matrix.getMatrixAll()[probeNums[i] - 1];	    	
	    }
	    
	    int[] value = new int [pNum];
	    for(int i = 0; i < pNum; i++){
	    	value[i] = 0;
	    }
	    for(int i = 0; i < pNum; i++){
	    	for(int j = 0; j < eNum; j++){
	    		value[i] = value[i] + max[i][j];
	    	}
	    }
	    
		double[] probeSparsity = new double[pNum];
		for(int i = 0; i < pNum; i++){
			probeSparsity[i] = (sqrtD - Math.sqrt(value[i])) / (sqrtD - 1);
		}
	
		try{
			fw = new FileWriter(file, append);
		}
		catch(IOException ex){
			System.out.println("file error");
		}
		PrintWriter bw = new PrintWriter(new BufferedWriter(fw));

		for (int i = 0; i < pNum; i++) {
			bw.print(probeNums[i] + "  " + probeSparsity[i]);
			bw.println();
		}
		for (int i = 0; i < pNum; i++){
			bw.print("****");
		}
		bw.println();
		bw.close();
	}
	
	private void calEdgesForProbe(EdgeDependencyMatrix matrix, boolean append){
		File file = new File("EdgesForProbe.txt");
		FileWriter fw = null;
		
		int pNum = matrix.getProbeCount();
		int eNum = matrix.getEdgeCount();
		int[] edgesForProbe = new int[pNum];
		int[][] max = new int[pNum][eNum];
		
		max = matrix.getMatrixAll(); 
		
		for(int i = 0; i < pNum; i++){
			for(int j = 0; j < eNum; j++){
				edgesForProbe[i] = edgesForProbe[i] + max[i][j];
			}
		}
		
		try{
			fw = new FileWriter(file, append);
		}
		
		catch(IOException ex){
			System.out.println("file error");
		}
		
		PrintWriter bw = new PrintWriter(new BufferedWriter(fw));

		for (int i = 0; i < pNum; i++) {
			bw.print((i+1) + "  " + edgesForProbe[i]);
			bw.println();
		}
		
		bw.print("********");
		
		bw.println();
		bw.close();
	}
	
	private void calProbesForEdge(EdgeDependencyMatrix matrix, boolean append){
		File file = new File("ProbesForEdge.txt");
		FileWriter fw = null;
		
		int pNum = matrix.getProbeCount();
		int eNum = matrix.getEdgeCount();
		int[] probesForEdge = new int[eNum];
		int[][] max = new int[pNum][eNum];
		
		int zero = 0;
		
		max = matrix.getMatrixAll();
		
		for(int j = 0; j < eNum; j++){
			for(int i = 0; i < pNum; i++){
				probesForEdge[j] = probesForEdge[j] + max[i][j];
			}
			
			if(probesForEdge[j] == 0){
				zero++;
			}
		}
		
		System.out.println("all zero edge:" + zero);
		
		try{
			fw = new FileWriter(file, append);
		}
		
		catch(IOException ex){
			System.out.println("file error");
		}
		
		PrintWriter bw = new PrintWriter(new BufferedWriter(fw));

		for (int i = 0; i < eNum; i++) {
			bw.print((i+1) + "  " + probesForEdge[i]);
			bw.println();
		}
		
		bw.print("********");
		
		bw.println();
		bw.close();
	}
	
	public void maxEvaluate(EdgeDependencyMatrix edm, EdgeDependencyMatrix nedm, int[] probeNums){
		
		//this.calculateSparsity(edm, probeNums, false);
		//this.calculateSparsity(nedm, probeNums, true);
		this.calEdgesForProbe(edm, false);
		this.calEdgesForProbe(nedm, true);
		this.calProbesForEdge(edm, false);
		this.calProbesForEdge(nedm, true);
		
	}
}
