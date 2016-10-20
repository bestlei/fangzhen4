package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

public class SAXxmlUtil {
	public void saxXml(String xml,String path){
		String result="";
		int nodeCount=0;
		int linkCount=0;
		
	System.out.println(xml);
	     String str="";
        SAXReader reader = new SAXReader();
       Document document = null;
	try {
		document = reader.read(new InputSource(new StringReader(xml)));
	} catch (DocumentException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
         
        //获取XML文档的根元素
       Element elementResult = document.getRootElement();
       //获取根元素下的所有一级子元素
       List<Element> firstSubElements = elementResult.elements();
        
       //获取Value子元素
      
       for(int i=1;i<firstSubElements.size();i++){
    	   Element elementData = firstSubElements.get(i);
           String type=elementData.attributeValue("type");
       if(type.equals("twaver.Node")){
    	   List<Element> secondSubElements = elementData.elements();
    	   Element elementCPU = secondSubElements.get(0);
    	   String CPU=elementCPU.getStringValue();
    	   str+=CPU+"\n";
    	   nodeCount++;
       }
       else if(type.equals("twaver.Link")){
    	   List<Element> secondSubElements = elementData.elements();
    	   Element elementBoard = secondSubElements.get(0);
    	   String board=elementBoard.getStringValue();
    	   Element elementFrom = secondSubElements.get(2);
    	   String from=elementFrom.attributeValue("ref");
    	   Element elementTo = secondSubElements.get(3);
    	   String to=elementTo.attributeValue("ref");
    	   str+=(from+" "+to+" "+board+"\n");
    	   linkCount++;
       }
       }
       result+=nodeCount+" "+linkCount+" "+"1"+" "+"10"+" "+"76"+" "+"1"+"\n"+str;
       try {
    	   System.out.println(path+"-----------");
    	   File file=new File(path);
    	   if (!file.exists()) 
    		   file.getParentFile().mkdirs();
    		   file.createNewFile();
           FileOutputStream fos = new FileOutputStream(file);
           fos.write(result.getBytes());
           fos.close();
       } catch (Exception e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
     
        
   }

}

		



