/****************************************************************************/
/*                  Copyright 2001, Trustees of Boston University.          */
/*                               All Rights Reserved.                       */
/*                                                                          */
/* Permission to use, copy, or modify this software and its documentation   */
/* for educational and research purposes only and without fee is hereby     */
/* granted, provided that this copyright notice appear on all copies and    */
/* supporting documentation.  For any other uses of this software, in       */
/* original or modified form, including but not limited to distribution in  */
/* whole or in part, specific prior permission must be obtained from Boston */
/* University.  These programs shall not be used, rewritten, or adapted as  */
/* the basis of a commercial software or hardware product without first     */
/* obtaining appropriate licenses from Boston University.  Boston University*/
/* and the author(s) make no representations about the suitability of this  */
/* software for any purpose.  It is provided "as is" without express or     */
/* implied warranty.                                                        */
/*                                                                          */
/****************************************************************************/
/*                                                                          */
/*  Author:     Alberto Medina                                              */
/*              Anukool Lakhina                                             */
/*  Title:     BRITE: Boston university Representative Topology gEnerator   */
/*  Revision:  2.0         4/02/2001                                        */
/****************************************************************************/

package brite.Export;

import brite.Topology.*;
import brite.Model.*;
import brite.Graph.*;
import brite.Util.*;
import utils.*;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
   Export.BriteExport provides functionality to export a topology into
   a BRITE format file.  The BRITE format looks like:
   <br>
   
   <pre>
   Topology: ( [numNodes] Nodes, [numEdges] Edges )
   Model ( [ModelNum] ):  [Model.toString()]
   
   Nodes: ([numNodes]):
   [NodeID]  [x-coord]  [y-coord]  [inDegree] [outDegree] [ASid]  [type]
   [NodeID]  [x-coord]  [y-coord]  [inDegree] [outDegree] [ASid]  [type]
   [NodeID]  [x-coord]  [y-coord]  [inDegree] [outDegree] [ASid]  [type]
   ...

   Edges: ([numEdges]):
   [EdgeID]  [fromNodeID]  [toNodeID]  [Length]  [Delay]  [Bandwidth]  [ASFromNodeID]  [ASToNodeID]  [EdgeType]  [Direction]
   [EdgeID]  [fromNodeID]  [toNodeID]  [Length]  [Delay]  [Bandwidth]  [ASFromNodeID]  [ASToNodeID]  [EdgeType]  [Direction]
   [EdgeID]  [fromNodeID]  [toNodeID]  [Length]  [Delay]  [Bandwidth]  [ASFromNodeID]  [ASToNodeID]  [EdgeType]  [Direction]
   ...
   </pre>
   <br>
   Please see the BRITE User Manual (at http://www.cs.bu.edu/brite/docs.htm) for more information.
 */
public class BriteExport {
  
  private Topology t;
  //private BufferedWriter bw;
  //******
  private BufferedWriter phyBw;
  private BufferedWriter reqBw;
  float dValue;
  float theCpuMinValue;
  private String typeofFile;
  //******
  private Graph g;
  private String modelStr;
    /**
       Class Constructor: Returns a BriteExport object which your code
       my keep around.  Does not actually write the topology to the
       file.  You must explicitly call the <code>export()</code> method of this
       object in order to write to the file.
       
       @param t the topology object to export
       @param outFile the destination file to write the topology to.
    */
    public BriteExport(Topology t, File outFile,String outformalFile,float cpuMinValue, float cpuMaxValue,String typeOfFile) {
	this.t = t;
	//******
	dValue = Math.abs(cpuMaxValue-cpuMinValue);
	theCpuMinValue = cpuMinValue;
	typeofFile = typeOfFile;
	String phyNetPath = AllPath.topoGeneratePath;
	String virReqpath = AllPath.virReqPath;
	
	//int randNum = rand.nextInt(22)+5;
    //******
	
	
	//******
	//物理拓扑文件
	//String outformalFile = "zhang";
	
	
	try {
		if(typeofFile.equals("generatedphy")){
			File phyOutFile = new File(phyNetPath+outformalFile+".xml");
		
			phyBw = new BufferedWriter(new FileWriter(phyOutFile));
		}
		if(typeofFile.equals("node_link")){
			reqBw = new BufferedWriter(new FileWriter(virReqpath+outformalFile+".txt"));
		}
	}
	catch (IOException e) {
	    Util.ERR(" Error creating BufferedWriter in BriteExport: " + e);
	}
	//虚拟请求格式的文件
	//File virReqFile = new File("E://fangzhenpingtai//top//topo-500-5-10-10//link-node//"+outformalFile+".txt");
	//try {
	//	
	//}
  //	catch (IOException e) {
	//    Util.ERR(" Error creating BufferedWriter in BriteExport: " + e);
	//}
	
	//******
	g = t.getGraph();
	modelStr = t.getModel().toString();
    }

    
    /**
       Writes the contents of the topolgy in the BRITE format to the
       destination file specified in the constructor.  
    */
    public void export() {
	Util.MSG("Exporting to BRITE...");
     	try { 
	    /*output nodes*/
	    // ArrayList nodes = g.getNodesVector();
	    
	    Node[] nodes = g.getNodesArray();
	    Arrays.sort(nodes, Node.IDcomparator);
	    
	    Edge[] edges = g.getEdgesArray();
	    
	    //******
	    //phyNet
	    //phyBw.write("nihao");
	    if(typeofFile.equals("generatedphy")){
	    	phyBw.write("<"+"?xml "+"version="+"\"1.0\""+" encoding="+"\"UTF-8\""+"?>");
	    	phyBw.newLine();
	    	phyBw.newLine();
	    	phyBw.write("<root nodes="+"\""+nodes.length+"\""+" links="+"\""+edges.length+"\">");
	    	phyBw.newLine();
	    	phyBw.write("  <nodes>");
	    	phyBw.newLine();
	    }
	    
	    //virNet
	    //reqBw.write("nihao");
	    DecimalFormat dcmFmt = new DecimalFormat("0.000000");
	    DecimalFormat dcmfmt = new DecimalFormat("0");
	    if(typeofFile.equals("node_link")){
	    	int arrivenum = (int) Math.random()*100 +20;
			String arriveTime= dcmfmt.format(arrivenum);
			//int arrivetime = Integer.parseInt(arriveTime);
			//String str=Integer.toHexString(a);
	    	int virnum = (int) Math.random()*8000 +2000;
			String continuedTime= dcmfmt.format(virnum);
			
	    	reqBw.write(nodes.length+" "+edges.length+" "+"1 "+arriveTime+" "+continuedTime+" "+"1");
	    	reqBw.flush();
	    	reqBw.newLine();
	    }
		
	    //******
	    
	    for (int i=0; i< nodes.length; ++i) {
		Node n =  nodes[i];
		int x = (int) ((NodeConf) n.getNodeConf()).getX();
		int y = (int)  ((NodeConf)n.getNodeConf()).getY();
		int specificNodeType=-1;
		int ASid = -1;
		int outdegree = n.getOutDegree();
		int indegree = n.getInDegree();
		int nodeID = n.getID();
		
		if (n.getNodeConf() instanceof RouterNodeConf) { 
		    ASid = ((RouterNodeConf)n.getNodeConf()).getCorrAS();
		    specificNodeType = ((RouterNodeConf)n.getNodeConf()).getType();
		}
		if (n.getNodeConf() instanceof ASNodeConf) {
		    specificNodeType = ((ASNodeConf)n.getNodeConf()).getType();
		    ASid = nodeID;
		}
		
		
		//******
		float num = (float) Math.random()*dValue +theCpuMinValue;
		
		String cpuValue= dcmFmt.format(num);
		if(typeofFile.equals("generatedphy")){
			phyBw.write("    <node label=\""+nodeID+"\" x="+"\""+x+"\""+" y="+"\""+y+"\""+" CPU="+"\""+cpuValue+"\""+"/>");
		}
		if(typeofFile.equals("node_link")){
			reqBw.write(cpuValue);
		}
		//******
	
		
		if(typeofFile.equals("generatedphy")){
			phyBw.newLine();
		}
		 if(typeofFile.equals("node_link")){
		    	reqBw.newLine();
		    }
		
	    }
	    //******
	    if(typeofFile.equals("generatedphy")){
	    	phyBw.write("  </nodes>");
	    	phyBw.flush();
	    }
	    if(typeofFile.equals("node_link")){
	    	reqBw.flush();
	    }
	    //******
	   
	    /*output edges*/
	    //******
	    //Edge[] edges = g.getEdgesArray();
	    //******此行加了注释
	    //ArrayList edges = g.getEdgesVector();
	   
	    //******
	    if(typeofFile.equals("generatedphy")){
	    	phyBw.newLine();
	    	phyBw.write("  <links>");
	    	phyBw.newLine();
	    }
	    //******
	    
	    Arrays.sort(edges, Edge.IDcomparator);
	    for (int i=0; i<edges.length; ++i) {
	    	Edge e = (Edge) edges[i];
	    	Node src = e.getSrc();
	    	Node dst = e.getDst();
	    	double dist = e.getEuclideanDist();
	    	double delay = e.getDelay();
	    	int asFrom= src.getID();
	    	int asTo = dst.getID();
	    	if (src.getNodeConf() instanceof RouterNodeConf)
	    		asFrom  =((RouterNodeConf) src.getNodeConf()).getCorrAS();
	    	if (dst.getNodeConf() instanceof RouterNodeConf)
	    		asTo  =((RouterNodeConf) dst.getNodeConf()).getCorrAS();
		
	    	
		
	    	String distVaue = dcmFmt.format(dist);
	    	if(typeofFile.equals("generatedphy")){
	    		phyBw.write("    <link label="+"\""+e.getID()+"\""+" fromNode="+"\""+src.getID()+"\""+" toNode="+"\""+dst.getID()+"\""+" Bandwidth="+"\""+distVaue+"\""+"/>");
	    	}
	    	if(typeofFile.equals("node_link")){
	    		reqBw.write(src.getID()+" "+dst.getID()+" "+distVaue);
	    	}
		
	    	
	       
	    	
	    	if(typeofFile.equals("generatedphy")){
	    		phyBw.newLine();
	    	}
	    	if(typeofFile.equals("node_link")){
	    		reqBw.newLine();
	    	}
	    	
	    	}
	    	if(typeofFile.equals("generatedphy")){
	    		phyBw.write("  </links>");
	    		phyBw.newLine();
	    		phyBw.write("</root>");
	    		phyBw.flush();
	    		phyBw.close();
	    	}
	    	if(typeofFile.equals("node_link")){
	    		reqBw.flush();
	    		reqBw.close();
	    	}
     	}
     	catch (Exception e) {
     		System.out.println("[BRITE ERROR]: Error exporting to file. " + e);
     		e.printStackTrace();
     		System.exit(0);
     		
     	}
     	Util.MSG("... DONE.");
    }
    
    //******
    public void exportToTxt() {
    	Util.MSG("Exporting to BRITE...");
         	try {

    	    Node[] nodes = g.getNodesArray();
    	    Arrays.sort(nodes, Node.IDcomparator);
    	    
    	    Edge[] edges = g.getEdgesArray();
    	    
    	   
    		reqBw.write(nodes.length+" "+edges.length+" ");
    		reqBw.newLine();
    		reqBw.flush();
    		DecimalFormat dcmFmt = new DecimalFormat("0.000000");
    	    //******
    	    
    	    for (int i=0; i< nodes.length; ++i) {
    	    	Node n =  nodes[i];
    	    	int x = (int) ((NodeConf) n.getNodeConf()).getX();
    	    	int y = (int)  ((NodeConf)n.getNodeConf()).getY();
    	    	int specificNodeType=-1;
    	    	int ASid = -1;
    	    	int outdegree = n.getOutDegree();
    	    	int indegree = n.getInDegree();
    	    	int nodeID = n.getID();
    		
    	    	if (n.getNodeConf() instanceof RouterNodeConf) { 
    	    		ASid = ((RouterNodeConf)n.getNodeConf()).getCorrAS();
    		    	specificNodeType = ((RouterNodeConf)n.getNodeConf()).getType();
    	    	}
    	    	if (n.getNodeConf() instanceof ASNodeConf) {
    	    		specificNodeType = ((ASNodeConf)n.getNodeConf()).getType();
    	    		ASid = nodeID;
    	    	}
    	    	
    		
    	    	float num = (float) Math.random()*dValue +theCpuMinValue;
    		
    			String cpuValue= dcmFmt.format(num);
    			
    			reqBw.write(cpuValue);

    			reqBw.newLine();
    		
    	    }
    	    //******
    	    
    	  
    	    Arrays.sort(edges, Edge.IDcomparator);
    	    
    	    for (int i=0; i<edges.length; ++i) {
    	    	Edge e = (Edge) edges[i];
    	    	Node src = e.getSrc();
    	    	Node dst = e.getDst();
    	    	double dist = e.getEuclideanDist();
    	    	double delay = e.getDelay();
    	    	int asFrom= src.getID();
    	    	int asTo = dst.getID();
    		
    	    	if (src.getNodeConf() instanceof RouterNodeConf)
    	    		asFrom  =((RouterNodeConf) src.getNodeConf()).getCorrAS();
    	    	
    	    	if (dst.getNodeConf() instanceof RouterNodeConf)	
    	    		asTo  =((RouterNodeConf) dst.getNodeConf()).getCorrAS();
    		
    	    	String distVaue = dcmFmt.format(dist);
    	    	reqBw.write(distVaue);
    	    	reqBw.newLine();
    	    	}
    	    
    	    	reqBw.flush();
    	    	reqBw.close();
    	      
         	}
         	catch (Exception e) {
         		System.out.println("[BRITE ERROR]: Error exporting to file. " + e);
         		e.printStackTrace();
         		System.exit(0);
    	    
         	}
         	Util.MSG("... DONE.");
    }
    //******
    
}

  
    








