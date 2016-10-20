package com;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;



public class Test {
	 public void embed(String filename,String virName,String subName) {
	        
		 URL[] urls = new URL[] {};
	        MyClassLoader classLoader = new MyClassLoader(urls, null);
	        String filename1="D://flexW//fangzhen4//flex_src//XML//jars//"+filename;
	        
	        try {
	            classLoader.addJar(new File(filename1).toURI().toURL());
	            Class<?> clazz = classLoader.loadClass("embed.Embed");
	            Method method = clazz.getDeclaredMethod("startEmbed",new Class[]{String.class,String.class});
	            method.invoke(clazz.newInstance(),virName,subName);
	         
	          // classLoader.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	    }

	 static class MyClassLoader extends URLClassLoader {

	        public MyClassLoader(URL[] urls) {
	            super(urls);
	        }

	        public MyClassLoader(URL[] urls, ClassLoader parent) {
	            super(urls, parent);
	        }

	        public void addJar(URL url) {
	            this.addURL(url);
	        }

	    }
}
