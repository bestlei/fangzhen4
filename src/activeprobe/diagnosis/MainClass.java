package activeprobe.diagnosis;
import java.util.ArrayList;

public class MainClass {
	/**
	 * fileNo1虚拟节点文件，初始为20.txt
	 * probeStationNum虚拟探针数
	 * fileNo2物理网络文件，初始为100.txt
	 * probStationNum2物理探针数
	 * fileNo3为映射的结果文件，示例为20-100.txt
	 * f1,f2,f3,f4故障生成中的高斯参数
	 * f5 节点噪声率，f6探头噪声故障率
	 */
	public ArrayList<String> probeEntrance(int fileNo1,int probeStation,int fileNo2,int probStationNum2,int virMaxNum, int phyMaxNum,String f1,String f2,String f3,String f4) {
		double F1 = Double.parseDouble(f1);
		double F2 = Double.parseDouble(f2);
		double F3 = Double.parseDouble(f3);
		double F4 = Double.parseDouble(f4);
		Parameter.virProbePath = new ArrayList<String>();
		Parameter.phyProbePath = new ArrayList<String>();
		ArrayList<String> result = JavaMat.Initial(fileNo1,probeStation,fileNo2, probStationNum2,fileNo1 + "-" + fileNo2, F1,F2,F3,F4,0.01f,0.01f,virMaxNum,phyMaxNum);
		//getVirProbeResult();    //将探测结果写入文件
		//getPhyProbeResult();
		return result;    //返回故障生成信息
	}
	/*public void getVirProbeResult(){    //将虚拟网络探测故障结果写入文档
		ArrayList<String> virresult = Parameter.virProbePath;
		String filepath = JavaMat.initUrl + "output" + File.separator + "VirProbeResult.txt";
		writeProbeResult(virresult, filepath);
	}
	public void getPhyProbeResult(){   //将物理网络探测故障结果写入文档
		ArrayList<String> phyresult = Parameter.phyProbePath;
		String filepath = JavaMat.initUrl + "output" + File.separator + "PhyProbeResult.txt";
		writeProbeResult(phyresult, filepath);
	}
	public static void writeProbeResult(ArrayList<String> result,String filepath){//file 为写入的文件
		try {
			File file = new File(filepath);
			if (file.exists())
				file.deleteOnExit();
		    file.createNewFile();
			BufferedWriter bw=new BufferedWriter(new FileWriter(file));
	        for(int i=0;i<result.size();i++){
	            bw.write(result.get(i));
	            bw.newLine();
	        }
	        bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		}*/
	/*public ArrayList<String> backResults(){
		ArrayList<String> results = new ArrayList<String>();
		results.add(readProbeResult(Parameter.virProbePath));
		results.add(readProbeResult(Parameter.phyProbePath));
		return results;
	}
    public static String readProbeResult(ArrayList<String> results){    //将探测结果整理为字符串
    	String result = null;
    	for(int i =0;i<results.size();i++){
    		result += results.get(i).toString();
    	}
		return result;
    	
    }*/
}