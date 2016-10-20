package utils;



import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public abstract class UpLoad extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6230309682787209266L;
	public UpLoad()   
    {  
    	super();
         
    }  
    public void destroy()   
    {  
        super.destroy();   
    }  
     
   
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException   
    {  
        processRequest(request, response);  
    }  
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException   
    {  
        processRequest(request, response);  
    }  
 @SuppressWarnings("unchecked")
public abstract ArrayList getFileName();
 public abstract void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
 

}
