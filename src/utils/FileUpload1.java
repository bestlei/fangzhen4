/*
 * 功能：处理故障探测部分的物理网络拓扑上传
 * */
package utils;

import java.io.File;  
import java.io.IOException;  
import java.io.PrintWriter;  
import java.util.ArrayList;
import java.util.Iterator;  
import java.util.List;  
import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
import org.apache.commons.fileupload.FileItem;  
import org.apache.commons.fileupload.FileUploadException;  
import org.apache.commons.fileupload.disk.DiskFileItemFactory;  
import org.apache.commons.fileupload.servlet.ServletFileUpload;  
@SuppressWarnings("serial")  


public class FileUpload1 extends HttpServlet   
{  
    //瀹氫箟鏂囦欢涓婁紶鐨勮矾寰� 
    private String uploadPath = "D://flexW//fangzhen4//flex_src//XML//probesubNet//";  
      
    private int maxPostSize = 100*1024*1024;  
    public FileUpload1()   
    {  
        super();  
    }  
    public void destroy()   
    {  
        super.destroy();   
    }  
      
    @SuppressWarnings("unchecked")  
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException  
    {  
        System.out.println("Access--fileupload1!");  
        
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
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException   
    {  
        processRequest(request, response);  
    }  
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException   
    {  
        processRequest(request, response);  
    }  
    
    //鑾峰緱鎵�湁鐨勭墿鐞嗘嫇鎵戠殑鏂囦欢鍚�
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