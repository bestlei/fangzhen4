package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import flex.messaging.io.ArrayCollection;

public class FileDownLoad {

	/**
	 * 下载文件的后台函数
	 * @param filename   要下载的文件名
	 * @param path     下载的文件要保存的路径
	 * @throws IOException 
	 * @throws IOException 
	 */
	public boolean downLoadList(ArrayCollection list,String path) throws IOException
	{
		boolean flag = false;
	  	  for(int i = 0; i < list.size(); i++)
	  	  {
	  		  String fileName = list.get(i).toString();
	  		//  System.out.println("fileName:"+fileName);
	  		  flag = downLoadFile(fileName, path);
	  	  }
		
		  return flag;
	}
	
	public boolean downLoadFile(String filename ,String path) throws IOException{
		
		    System.out.println("into file download");
		  //拓扑文件存储的路径
		   String basePath =  "D:/flexW/fangzhen4/flex_src/XML/phyNets/";  
		   File  downLoadFile = new File(basePath + filename);   //要下载的文件
		   File saveFile = new File(path + filename);                        //保存的路径下创建文件
		   
		   if(downLoadFile == null ){
			      System.out.println("下载路径错误");
			      return false;
		   }else if(saveFile == null){
			     System.out.println("保存路径错误");
			     return false;
		   }else{
			 	BufferedReader br;
				try {
					br = new BufferedReader( new FileReader(downLoadFile));
					BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile));
			    	String line = null;
			    	line=br.readLine();
			    	while( line   !=null){
			    		bw.write(line);
			    		bw.newLine();
			    		bw.flush();
			    		line=br.readLine();
			    	    } 
			    	 bw.close();
			    	 br.close();
			  				
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	
		   }
			   return true;
		   }

	
	@SuppressWarnings("unused")
	private void downLoad(HttpServletResponse response) throws Exception {  
		 
	    BufferedOutputStream bos = null;  
	    BufferedInputStream  bis = null;  
	    try {  
	            bis = new BufferedInputStream(new FileInputStream("/head.PNG"));              
	            bos = new BufferedOutputStream(response.getOutputStream());  
	              
	            byte[] buff = new byte[2048];  
	            int bytesRead;  
	 
	            while(-1 != (bytesRead = bis.read(buff, 0, buff.length))) {  
	                bos.write(buff,0,bytesRead);  
	            }  
	        } catch(final IOException e) {  
	            e.printStackTrace();  
	        } catch(Exception e) {  
	            e.printStackTrace();  
	        }finally {  
	            if (bis != null)  
	                bis.close();  
	            if (bos != null)  
	            {  
	                bos.flush();  
	                bos.close();  
	                bos=null;  
	            }  
	        }  
	        response.flushBuffer();  
	} 
}
