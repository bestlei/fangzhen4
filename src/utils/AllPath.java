package utils;

public class AllPath {
	
		public static String topoGeneratePath = "D://flexW//fangzhen4//flex_src//XML//phyNets//";
		
		public static String virReqPath = "D://flexW//top//topo-500-5-10-10//topo_for_use//";
		
		public static String subPath = "D://flexW//fangzhen4//flex_src//XML//phyNets//";
		
		public static String embedOutPath = "D://flexW//fangzhen4//flex_src//XML//embedOut//";
		
		public static String programPath = "D://flexW//fangzhen4//";
		
		public static String downloadSubNetPath = "D://flexW//fangzhen4//flex_src//XML//phyNets";
		
		public String getTopoGeneratePath(){
			return this.topoGeneratePath;
		}
		
		public String getVirReqPath(){
			return this.virReqPath;
		}
		
		public String getSubPath(){
			return this.subPath;
		}
		
		public String getEmbedOutPath(){
			return this.embedOutPath;
		}
		
		public String getProgramPath(){
			return this.programPath;
		}
		
		public String getdownloadPath(){
			return this.downloadSubNetPath;
		}
		
		public String getprogramAndtopoForUse(){
			System.out.println(programPath+"&"+ virReqPath+"&"+topoGeneratePath);
			return programPath+"&"+ virReqPath+"&"+topoGeneratePath;
		}

}
