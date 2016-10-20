package embed;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;
import embed.Request_Relate;
//�������������ͳ�ƣ�Ȼ�󻮷ֳɴ��͡����͡�С�����ˣ��ֱ�ֵ���ͬ���ļ�����
public class VNSort {
           public static   int node[]; //�洢�ڵ�����
            public static int link[]; //�洢��·��
            public Request_Relate relate; 
            public Request_Relate target;//�洢ѡ��ŵ�������������
            
             public static int small_num;
             public static int middle_num;
             public static int big_num;
             public static int maxnode;
             public static int minnode;
             public static int maxlink;
             public static int minlink;
             public VNSort(){
            	 int node[]= new int[500];
            	 int link[]=new int[500];
            	 int node_sort[] = new int[500];
            	 for(int i=0;i<500;i++){
            		 node[i]=-1;
            		 link[i]=-1;
            		 node_sort[i]=-1;
            	 }
            	 Request_Relate relate=new Request_Relate();          	
            	 for(int i=0;i<500;i++){         		
            		 relate.link_num[i]=0;
            		 relate.node_num[i]=0;
            		 relate.req_id[i]=-1;
            	 }
            	 
            	Request_Relate target= new Request_Relate();
            	
         /*   	for(int i=0;i<100;i++){
            		 target[i].link_num=0;
            		 target[i].node_num=0;
            		 target[i].req_id=-1;
            	 }
            	 */
            	 small_num=0;
            	 middle_num=0;
            	 big_num=0;
             }
	
    public Request_Relate get_node_num(String filename ,int num) throws IOException{ //����·����,����������ܵĸ�������ýڵ���������
    	int i=0;
    	node= new int[500];
    	link=new int[500];
    	relate= new Request_Relate();
    	while(i<num){
    		String src=filename+"req"+i+".txt";  
    	    File file= new File(src);   
    		BufferedReader br = new BufferedReader(new FileReader(file));
    		String line=null;
    		line=br.readLine();
    		String a=line.split(" ")[0];  //��ȡ�ڵ���
    		int b = Integer.valueOf(a);
    		int c=Integer.valueOf(line.split(" ")[1]);//��ȡ��·��
    		//System.out.println(i +"," +c);
    		link[i]=c;
    		//System.out.println("i= "+i);
    		node[i]=b;
    		relate.node_num[i]=b;
    		relate.link_num[i]=c;
    		relate.req_id[i]=i;
    		
    		br.close();
    		i++;
    	}
  /*  	
    for(int j=0;j<num;j++){
    	System.out.print(node[j]+" ");
    }	
      System.out.println();
      for(int j=0;j<num;j++){
    	  System.out.print(link[j]+" ");
      }
      System.out.println();
     
    	System.out.println("relate-content");
    	for(int j=0;j<relate.link_num.length;j++){
    		System.out.println(relate.req_id[j]+" , "+relate.node_num[j]+" , "+relate.link_num[j]);
    	} */
    return relate;
    }	
  public int get_average(int node[],int num){//��ȡ�ڵ������ƽ����
    	int average=0,sum=0;
       	for(int i=0;i<num;i++){
       		sum+=node[i];
       	}  
       	average=sum/num;
    	return average;
    }
  //���ڸ����Ľڵ�������·�����������󣬽��д���ѡ�����ʸ����������������������������ӳ��
   public Request_Relate findReq( Request_Relate relate,int targ_nodenum, int targ_linknum){
	     int i=0,count=0;   
	     target = new Request_Relate();
	     for(int k=0;k<500;k++){
	    	 target.link_num[k]=0;
	    	 target.node_num[k]=0;
	    	 target.req_id[k]=-1;
	     }
	   System.out.println("target-nodenum= "+targ_nodenum +"target-linknum= "+targ_linknum);
	   while(i<relate.node_num.length){
		   // System.out.println("i= "+i);
		     if(relate.node_num[i]==targ_nodenum){
		    	 if(relate.link_num[i]== targ_linknum){
		    		 System.out.println("find a target------------------------");
		    		 target.link_num[count]=targ_linknum;
		    		 target.node_num[count]= targ_nodenum;
		    		 target.req_id[count] = i;
		    		 count++;
		    	 }
		    	 
		     }
		   i++;
	   }
	 
	   int j=0;
	   System.out.println("target-conten*************************");
	   while(j<target.link_num.length){
		   System.out.println("nodenum= "+target.node_num[j]+", linknum="+target.link_num[j]+", req-id= "+target.req_id[j]);
		   j++;
	   }
	   return target;
	   
   }
   //ð������,�����
   public static void sort(int[] values ,int len){

	    int temp;
	    for(int i=0;i<len;i++){//����
	      for(int j=0;j<len-i-1;j++){//�Ƚϴ���
	        if(values[j]>values[j+1]){
	          temp=values[j];
	          values[j]=values[j+1];
	          values[j+1]=temp;
	        }
	      }
	    }
	   }
   //���ݻ�ȡ�����ݽ����������ļ���������
   public  void set_num(int[] node,int average,int max,int min,int count) throws IOException{
	   int total=max-min+1;
	   int set=total/3;
	   //���ݽڵ�ĸ����������ݷ�Ϊ�����֣���һ������min~average,�ڶ�������average+set�����������ǵ����
	  String org_src="D:\\flexW\\top\\topo-500-5-10-10\\";
	   String small_src="D:\\flexW\\top\\topo-500-5-10-10\\small\\";
	   String middle_src="D:\\flexW\\top\\topo-500-5-10-10\\middle\\";
	   String big_src="D:\\flexW\\top\\topo-500-5-10-10\\big\\";
	  
	  int i=0;
	   while(i<count){
		   String src1=org_src+"req"+i+".txt";
		    File file1= new File(src1);
		   BufferedReader br=new BufferedReader(new FileReader(file1));
		   String line=null;
		   line=br.readLine();
		   int a=Integer.valueOf(line.split(" ")[0]);//��ȡ�ڵ����
		   if(a>=min && a<average){ //С������
			   String src2=small_src+"sreq"+small_num+".txt";
			   File file2=new File(src2);
			   BufferedWriter bw= new BufferedWriter(new FileWriter(file2));
			   while(line!=null){
				   bw.write(line);
				   bw.newLine();
				   bw.flush();
				   line=br.readLine();
			   }
			   br.close();
			   bw.close();
			   small_num++;
		   }
		   else if(a>=average && a<= average+set){ //��������
			   String src3=middle_src+"mreq"+middle_num+".txt";
			   File file3=new File(src3);
			   BufferedWriter bw= new BufferedWriter(new FileWriter(file3));
			   while(line!=null){
				   bw.write(line);
				   bw.newLine();
				   bw.flush();
				   line=br.readLine();
			   }
			   br.close();
			   bw.close();
			   middle_num++;
			   
		   }
		   else if(a>average+set && a <=max){ //��������
			   String src4=big_src+"breq"+big_num+".txt";
			   File file4=new File(src4);
			   BufferedWriter bw= new BufferedWriter(new FileWriter(file4));
			   while(line!=null){
				   bw.write(line);
				   bw.newLine();
				   bw.flush();
				   line=br.readLine();
			   }
			   br.close();
			   bw.close();
			   big_num++;
			   
		   }
		   i++;
	   }
	   
   }
    @Test
    public void main() throws IOException {  
    	VNSort sort= new VNSort();
    	 relate= new Request_Relate();
            
   	for(int i=0;i<500;i++){
    		relate.node_num[i]=0;
    		relate.link_num[i]=0;
    		relate.req_id[i]=-1;
    	}
    	
        String s1="D:\\flexW\\top\\topo-500-5-10-10\\";
 
    	relate=sort.get_node_num(s1, 500);
    	 sort.findReq(relate, 4,3 );
    /*	 
    	System.out.println("relate-content");
    	for(int j=0;j<relate.link_num.length;j++){
    		System.out.println(relate.req_id[j]+" , "+relate.node_num[j]+" , "+relate.link_num[j]);
    	} */
 /*      	for(int i=0;i<38;i++){
    		System.out.print(node1[i]+" ");
    	}
        System.out.println();
        */
    	
    	int average_node_num=sort.get_average(node, 500);
    	System.out.println("average_node_num= "+average_node_num);
    	sort.sort(node, 500);
    	sort.sort(link,500);
    	maxlink=link[499];
    	minlink=link[0];
    	System.out.println("maxlink= "+maxlink +" ,minlink=  "+minlink);
      //   sort.node_quickSort(node, 0, 9);
     /* 
    	for(int i=0;i<500;i++){  
             System.out.print(node[i]+" ");  
         }   */
         System.out.println();  
         maxnode=node[499];
         minnode=node[0];
         System.out.println("min=" +minnode+",max= "+maxnode);
         sort.set_num(node, average_node_num, maxnode, minnode, 500);
         
       //  sort.findReq(relate, 5,6 );
         
    }  
}
