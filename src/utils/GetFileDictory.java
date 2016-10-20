package utils;

import java.io.File;
import java.util.ArrayList;

import flex.messaging.io.ArrayCollection;

/*
 * 获取文件夹下文件列表的帮助类
 * */
public class GetFileDictory {
   
        public ArrayCollection getFileName(){   
     	  String path ="D:/flexW/fangzhen4/flex_src/XML/phyNets/";
            File file = new File(path);   
            File[] array = file.listFiles();   
            ArrayCollection arrayCol = new ArrayCollection();
            for(int i=0;i<array.length;i++){   
                if(array[i].isFile()){                 
                	arrayCol.add(array[i].getName());
                    System.out.println("-getFile-------Filename: " + array[i].getName());   
                }
            }  
            
            return arrayCol;
        }   
	
}
