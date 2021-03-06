package com;

import java.io.File;  
import java.io.IOException;  
import java.io.PrintWriter;  
import java.util.ArrayList;
import java.util.Iterator;  
import java.util.List;  
import javax.servlet.ServletException;    
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
import org.apache.commons.fileupload.FileItem;  
import org.apache.commons.fileupload.FileUploadException;  
import org.apache.commons.fileupload.disk.DiskFileItemFactory;  
import org.apache.commons.fileupload.servlet.ServletFileUpload;  

import utils.UpLoad;
@SuppressWarnings("serial")  


public class FileUpload extends UpLoad   
{  
    //瀹氫箟鏂囦欢涓婁紶鐨勮矾寰� 
    private String uploadPath = "D://flexW//fangzhen4//flex_src//XML//phyNets//";  
      
    private int maxPostSize = 100*1024*1024;  
   
    @SuppressWarnings("unchecked")
	public void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException  
    {  
        System.out.println("Access!");  
        
        //闃叉涓枃涔辩爜
        request.setCharacterEncoding("utf-8"); 
        response.setContentType("text/html;charset=UTF-8");
        
        PrintWriter out = response.getWriter();  
        out.print("宸茶繛鎺ヤ笂!");  
          
        //淇濆瓨鏂囦欢鍒版湇鍔″櫒涓� 
        DiskFileItemFactory factory = new DiskFileItemFactory();  
        factory.setSizeThreshold(4096);  
        ServletFileUpload upload = new ServletFileUpload(factory);  
        upload.setSizeMax(maxPostSize);  
        try  
        {  
            List fileItems = upload.parseRequest(request);  
            Iterator iter = fileItems.iterator();  
            while(iter.hasNext())  
            {  
                FileItem item = (FileItem)iter.next();  
                if(!item.isFormField())  
                {  
                    String name = item.getName();  
                    System.out.println(name);  
                    try  
                    {  
                        item.write(new File(uploadPath+name));  
                    }  
                    catch(Exception e)  
                    {  
                        e.printStackTrace();  
                    }  
                }  
            }  
        }  
        catch(FileUploadException e)  
        {  
            e.printStackTrace();  
            System.out.println(e.getMessage()+"缁撴潫");  
        }  
    }  
    
    //鑾峰緱鎵�湁鐨勭墿鐞嗘嫇鎵戠殑鏂囦欢鍚�
    @SuppressWarnings("unchecked")
	public ArrayList getFileName(){   
    	
    	String path = uploadPath;
        File file = new File(path);   
        File[] array = file.listFiles();   
        ArrayList list = new ArrayList();
        for(int i=0;i<array.length;i++){   
            if(array[i].isFile()){   
            	list.add(array[i].getName());
                System.out.println("--------Filename: " + array[i].getName());   
            }
        }  
        
        return list;
    }   
    
}  