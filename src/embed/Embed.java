package embed;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;

import org.eclipse.jdt.internal.compiler.batch.Main;
import org.junit.Test;

import com.sun.java.swing.plaf.nimbus.ArrowButtonPainter;

import embed.VNSort;
import embed.Request_Relate;
public class Embed {
	
	int bigtopo_num=5;
	int middletopo_num=8;
	int smalltopo_num=8;
	int arrived_time=0;
	public static Request_Relate relate;
	public static Request_Relate target;
	public static SN_Network sub = new SN_Network();
	public static MapAlgorithm map = new MapAlgorithm();
	
	public Embed(){
		relate = new Request_Relate();
		target = new Request_Relate();
		for(int i=0;i<500;i++){
			relate.node_num[i]=0;
			relate.link_num[i]=0;
			relate.req_id[i]=-1;
			
			target.node_num[i]=0;
			target.link_num[i]=0;
			target.req_id[i]=-1;
		}
	}
	
	/*
	public void startEmbed(){
		//String reqfilename = "D:\\work\\top\\requests-500-0-10-10\\";
		String reqfilename = "E:\\fangzhenpingtai\\requests-500-0-10-10\\";
		//String subname = "D:\\work\\top\\sub30-50.txt";
		String subname = "E:\\fangzhenpingtai\\fangzhen\\flex_src\\XML\\phyNet\\sub30-50xy.xml";
		//String reqfilename = "D:\\work\\top\\request-50\\";
		
		MapAlgorithm alg = new MapAlgorithm(subname,reqfilename);
		alg.simulate_emebeddRequest(100,"sub30-50xy.xml");
	}
	
	public void selectEmbed(String filename){
		//String reqfilename = "D:\\work\\top\\requests-500-0-10-10\\";
		String reqfilename = "E:\\fangzhenpingtai\\requests-500-0-10-10\\";
		String subname = "E:\\fangzhenpingtai\\fangzhen\\flex_src\\XML\\phyNets\\"+filename;
		
		System.out.println("--------selectEmbed--------"+filename);
		embed.MapAlgorithm alg = new embed.MapAlgorithm(subname,reqfilename);
		System.out.println("---111111-----");
		alg.simulate_emebeddRequest(100,filename);
		
		//映锟斤拷锟斤拷锟斤拷锟斤拷锟侥硷拷锟斤拷锟斤拷式为锟斤拷锟阶诧拷锟斤拷锟斤拷锟斤拷+"-embed-out.xml"
		//锟阶诧拷锟斤拷锟斤拷为subfile.xml 锟斤拷锟斤拷锟斤拷募锟轿猻ubfile-embed-out.xml
		
	}	
	*/
	//说明：MapAlgorithom算法中，对于虚拟请求中的到达时间的处理是这样的，以第一个虚拟请求的到达时间为基准，来判断后续虚拟请求的到达
	//时间，如果到达时间在当前时间的前面，则忽略这个请求，如何在当前时间之后，则进行虚拟网络映射
	//我们在复制操作中进行判断，将后续的请求按照随机数递增的方式增加
	//当进行虚拟网络映射设置是，通过保存的参数选择虚拟请求
	//filename表示物理网络的名称，snet，mnet，bnet分别表示小，中，大虚拟网络请求的个数
	public SimulateResult virnet_deal(String filename , int  snet, int mnet, int bnet) throws Exception{
		//从现有topo中选择出虚拟请求，随机
		//将他们复制到新的文件夹内，然后进行虚拟网络映射
		//路径
		String src ="D:\\flexW\\top\\topo-500-5-10-10";
		String des ="D:\\flexW\\top\\topo-500-5-10-10\\topo_for_use";
	   	int s =snet;
	   	int m =mnet;
	   	int b = bnet;
	   	int count = s+m+b;
	   	System.out.println("count= "+count);
	   	//产生smalltopo的随机数，从该文件夹中选择随机文件
	   int[] s_set = randomCommon(0,smalltopo_num,s);
	   for(int i=0;i<s;i++){
		   System.out.println("s_set["+i+"]=  "+ s_set[i]  );
		   String src1 = src+"\\small\\sreq"+s_set[i]+".txt";
		   copyfile(src1, des, i);
	   }
	   	//产生middletopo的随机数，从该文件夹中随机选择需要的拓扑文件
	   int[] m_set  = randomCommon(0, middletopo_num, m);
	   for(int i =0;i<m;i++){
		   System.out.println("m_set["+i+"]=  "+ m_set[i]  );
		   String src2 = src +"\\middle\\mreq"+m_set[i]+".txt";
		   copyfile(src2,des,i+s);
	   }
	//产生bigtopo的随机数，从该文件夹中选择需要的拓扑文件
	   int[] b_set = randomCommon(0, bigtopo_num, b);
	   for(int i=0;i<b;i++){
		   System.out.println("b_set["+i+"]=  "+ b_set[i]  );
		   String src3 = src+"\\big\\breq"+b_set[i]+".txt";
		   copyfile(src3, des, i+s+m);
	   }
	   String reqfilename = "D:\\flexW\\top\\topo-500-5-10-10\\topo_for_use\\";
	   String subname = "D:\\flexW\\fangzhen4\\flex_src\\XML\\phyNets\\"+filename;
	   
		//System.out.println("--------selectEmbed--------"+subname);
		embed.MapAlgorithm alg = new embed.MapAlgorithm(subname,reqfilename,count);
		System.out.println("---111111-----");
		SimulateResult sr = alg.simulate_emebeddRequest(100,filename);
		
		System.out.println("**************************************&&&"+sr.getSucc_num()+" , " + sr.getFail_nul());
		return sr;
	}
	
	
	public SimulateResult virnet_deal(String filename,String reqfilename) throws Exception{
		String subname = "D:\\flexW\\fangzhen4\\flex_src\\XML\\phyNets\\"+filename;
		System.out.println(reqfilename);
		embed.MapAlgorithm alg = new embed.MapAlgorithm(subname,reqfilename,0);
		System.out.println("---111111-----");
		SimulateResult sr = alg.simulate_emebeddRequest(100,filename);
		
		System.out.println("**************************************&&&"+sr.getSucc_num()+" , " + sr.getFail_nul());
		return sr;
		
	}
	//产生随机数的代码，指定范围内的n个不重复的随机数
	public static int[] randomCommon(int min, int max, int n){  
	    if (n > (max - min + 1) || max < min) {  
	           return null;  
	       }  
	    int[] result = new int[n];  
	    int count = 0;  
	    while(count < n) {  
	        int num = (int) (Math.random() * (max - min)) + min;  
	        boolean flag = true;  
	        for (int j = 0; j < n; j++) {  
	            if(num == result[j]){  
	                flag = false;  
	                break;  
	            }  
	        }  
	        if(flag){  
	            result[count] = num;  
	            count++;  
	        }  
	    }  
	    return result;  
	}  
	
	//复制txt文件的函数,src 需要复制的源文件所在的位置（包括文件名），des复制到的目录（filename），position表示复制的位置
	public void copyfile(String src, String des , int position) throws Exception{
		     File file1= new File(src);
		     File file2 = new File(des +"\\req"+position +".txt");
		     if(file1 ==null  || file2==null){
		    	 System.out.println("文件打开错误");
		     }
		     else{
		    	BufferedReader  br = new BufferedReader( new FileReader(file1));
		    	BufferedWriter bw = new BufferedWriter(new FileWriter(file2));
		    	String line = null;
		    	line=br.readLine();
		    	String a=line.split(" ")[3];
		    	int b = Integer.valueOf(a);
		    	System.out.println("&&&&&&&&&&&arrived_time= "+arrived_time +"  b= "+b);
		    	if(arrived_time== 0){ //第一个请求
		    		arrived_time=b;
		    	}
		    	else {
		    		if(b<=arrived_time){ //当前请求是在前一请求之前到达的，修改到达时间
		    			int k=(int) (Math.random() * (50 - 0)) + 1;
		    			System.out.println("k=" +k);
		    			b= arrived_time +k;
		    			arrived_time=b;
		    		}
		    		else{
		    			if(b>arrived_time){
		    				arrived_time=b;
		    			}
		    		}
		    	}
		    	line=line.split(" ")[0]+" "+line.split(" ")[1]+" "+line.split(" ")[2]+" "+String.valueOf(b)+" "+line.split(" ")[4]+" "+line.split(" ")[5];
		    	bw.write(line); 
		    	bw.write("\n");
		    	bw.flush();
		    	line=br.readLine();
		    	while( line   !=null){
		    		bw.write(line);
		    		bw.newLine();
		    		bw.flush();
		    		line=br.readLine();
		    	} 
		    	 bw.close();
		    	 br.close();
		     }
	}
	public void deal_nodelink_req(String filename, int node_num,int link_num) throws Exception{
	      VNSort	 vnsort = new VNSort();
	      String s1="D;\\flexW\\top\\topo-500-5-10-10\\";
	      relate =vnsort.get_node_num(s1, 500);
	      System.out.println("relate-conten--------------");
	      for(int i=0;i<relate.link_num.length;i++){
	    	  System.out.println(relate.req_id[i]+" ,  "+relate.node_num[i]+" , "+relate.link_num[i]);
	      }
	     target= vnsort.findReq(relate, node_num,link_num );
	     
	     //将选出来的文件拷贝到link-node文件夹中
	     String src="D;\\flexW\\top\\topo-500-5-10-10\\";
	     String des="D;\\flexW\\top\\topo-500-5-10-10\\link-node\\";
	     int count=0;
	     for(int i=0;i<target.link_num.length;i++){
	    	 if(target.node_num[i]!=0){
	    		   String src1=src+"req"+target.req_id[i]+".txt";
	    		    copyfile(src1, des, count);
	    		    count++;
	    	 }
	     }
	     String reqfilename = "D:\\flexW\\top\\topo-500-5-10-10\\link-node\\";
		  String subname = "D:\\flexW\\fangzhen4\\flex_src\\XML\\phyNets\\"+filename;
		   
			//System.out.println("--------selectEmbed--------"+subname);
			embed.MapAlgorithm alg = new embed.MapAlgorithm(subname,reqfilename,1);//先默认只有一个虚拟请求
			System.out.println("---111111-----");
			alg.simulate_emebeddRequest(100,filename);
	     
	}
	
	/*
	@Test
	public  void main() throws Exception{
	//	String filename="E:\\fangzhenpingtai\\fangzhen\\flex_src\\XML\\phyNets\\sub20-30xy.xml";
		String filename="sub20-30xy.xml";
		Embed  eb = new Embed();
		eb.virnet_deal(filename, 3, 2, 2);
	//	eb.deal_nodelink_req(filename, 2, 1);
	}
	*/
	
	public SimulateResult customVirnet_deal(String filename, ArrayList virReqfile,int customCount) throws Exception{
		
		   String reqfilename = "D:\\flexW\\top\\topo-500-5-10-10\\topo_for_use\\";
		   String subname = "D:\\flexW\\fangzhen4\\flex_src\\XML\\phyNets\\"+filename;
		   
			//System.out.println("--------selectEmbed--------"+subname);
			embed.MapAlgorithm alg = new embed.MapAlgorithm(subname,reqfilename,virReqfile,customCount);
			System.out.println("----------"+customCount+"2016---------------");
			System.out.println("---111111-----");
			SimulateResult sr = alg.simulate_emebeddRequest(100,filename);
			
			System.out.println("**************************************&&&"+sr.getSucc_num()+" , " + sr.getFail_nul());
			return sr;
		}
	
public int print(String fileName){
		
		map.readFromTXT(fileName);
	    sub = map.virSub;
		System.out.println("this is a demo");
		System.out.println(sub.nodes);
		int nodes = sub.nodes;
		return nodes;
	}
}
