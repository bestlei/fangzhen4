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
		
		//ӳ���������ļ�����ʽΪ���ײ�������+"-embed-out.xml"
		//�ײ�����Ϊsubfile.xml ������ļ�Ϊsubfile-embed-out.xml
		
	}	
	*/
	//˵����MapAlgorithom�㷨�У��������������еĵ���ʱ��Ĵ����������ģ��Ե�һ����������ĵ���ʱ��Ϊ��׼�����жϺ�����������ĵ���
	//ʱ�䣬�������ʱ���ڵ�ǰʱ���ǰ�棬����������������ڵ�ǰʱ��֮���������������ӳ��
	//�����ڸ��Ʋ����н����жϣ�������������������������ķ�ʽ����
	//��������������ӳ�������ǣ�ͨ������Ĳ���ѡ����������
	//filename��ʾ������������ƣ�snet��mnet��bnet�ֱ��ʾС���У���������������ĸ���
	public SimulateResult virnet_deal(String filename , int  snet, int mnet, int bnet) throws Exception{
		//������topo��ѡ��������������
		//�����Ǹ��Ƶ��µ��ļ����ڣ�Ȼ�������������ӳ��
		//·��
		String src ="D:\\flexW\\top\\topo-500-5-10-10";
		String des ="D:\\flexW\\top\\topo-500-5-10-10\\topo_for_use";
	   	int s =snet;
	   	int m =mnet;
	   	int b = bnet;
	   	int count = s+m+b;
	   	System.out.println("count= "+count);
	   	//����smalltopo����������Ӹ��ļ�����ѡ������ļ�
	   int[] s_set = randomCommon(0,smalltopo_num,s);
	   for(int i=0;i<s;i++){
		   System.out.println("s_set["+i+"]=  "+ s_set[i]  );
		   String src1 = src+"\\small\\sreq"+s_set[i]+".txt";
		   copyfile(src1, des, i);
	   }
	   	//����middletopo����������Ӹ��ļ��������ѡ����Ҫ�������ļ�
	   int[] m_set  = randomCommon(0, middletopo_num, m);
	   for(int i =0;i<m;i++){
		   System.out.println("m_set["+i+"]=  "+ m_set[i]  );
		   String src2 = src +"\\middle\\mreq"+m_set[i]+".txt";
		   copyfile(src2,des,i+s);
	   }
	//����bigtopo����������Ӹ��ļ�����ѡ����Ҫ�������ļ�
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
	//����������Ĵ��룬ָ����Χ�ڵ�n�����ظ��������
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
	
	//����txt�ļ��ĺ���,src ��Ҫ���Ƶ�Դ�ļ����ڵ�λ�ã������ļ�������des���Ƶ���Ŀ¼��filename����position��ʾ���Ƶ�λ��
	public void copyfile(String src, String des , int position) throws Exception{
		     File file1= new File(src);
		     File file2 = new File(des +"\\req"+position +".txt");
		     if(file1 ==null  || file2==null){
		    	 System.out.println("�ļ��򿪴���");
		     }
		     else{
		    	BufferedReader  br = new BufferedReader( new FileReader(file1));
		    	BufferedWriter bw = new BufferedWriter(new FileWriter(file2));
		    	String line = null;
		    	line=br.readLine();
		    	String a=line.split(" ")[3];
		    	int b = Integer.valueOf(a);
		    	System.out.println("&&&&&&&&&&&arrived_time= "+arrived_time +"  b= "+b);
		    	if(arrived_time== 0){ //��һ������
		    		arrived_time=b;
		    	}
		    	else {
		    		if(b<=arrived_time){ //��ǰ��������ǰһ����֮ǰ����ģ��޸ĵ���ʱ��
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
	     
	     //��ѡ�������ļ�������link-node�ļ�����
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
			embed.MapAlgorithm alg = new embed.MapAlgorithm(subname,reqfilename,1);//��Ĭ��ֻ��һ����������
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
