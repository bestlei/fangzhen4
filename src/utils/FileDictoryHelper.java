package utils;

import java.io.File;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import flex.messaging.io.ArrayCollection;

/**
 * 文件下载是用于提供文件目录信息，由于flex前台获取不了，转后台获取
 * @author lenovo
 *
 */
public class FileDictoryHelper {
       private String  filePath;           //文件路径
       
       public  ArrayList getFileDectory(String path){
    	 
    	      File file = new File(path);   
              File[] array = file.listFiles();  
      
              ArrayList   fileDictory = new ArrayList();
             for(int i=0;i<array.length;i++){   
                  if(array[i].isFile()){                 	 
                	  fileDictory.add(array[i].getName());
                     System.out.println("--------Filename: " + fileDictory.get(i));   
               }
           }   	   
    	   return fileDictory;
       }
       /*
       public static void main(String[] args) {
		        String path ="E://fangzhenpingtai//fangzhen//flex_src//XML//phyNets//";
		        getFileDectory(path);
	}
	*/
}
