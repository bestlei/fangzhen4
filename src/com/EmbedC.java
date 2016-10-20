package com;
public class EmbedC{
	public native void callEmbed(String virName,String subName,int count);
	public void na(){
		System.load("C://Users//admin//Desktop//embedDLL//embed.dll");
	}
	public static void embed(String filename,String virName,String subName,int count){
		System.out.println("C main start");
		
		String filename1="D://flexW//fangzhen4//flex_src//XML//dlls//"+filename;
		String filename2="D://flexW//fangzhen4//flex_src//XML//dlls//libgcc_s_dw2-1.dll";
		String filename3="D://flexW//fangzhen4//flex_src//XML//dlls//libstdc++-6.dll";
		String filename4="D://flexW//fangzhen4//flex_src//XML//dlls//libgcc_s_sjlj-1.dll";
		System.out.println(filename4);
		System.out.println(filename2);
		System.out.println(filename3);
		//System.out.println(filename1);
		System.out.println(virName+"     "+subName);
		System.load(filename4);
		System.load(filename2);
		System.load(filename3);
		System.load(filename1);
		EmbedC ec = new EmbedC();
		
		ec.callEmbed(virName,subName,count);
		System.out.println("C main end");	
	}
	public static void main(String args[]){
		embed("embed.dll","D:\\flexW\\top\\requests-500-0-10-10\\","D:\\flexW\\top\\sub20-30.txt",10);
	}
}