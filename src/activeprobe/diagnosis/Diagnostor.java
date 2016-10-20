package activeprobe.diagnosis;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

import activeprobe.diagnosis.OutputToFile;

import activeprobe.dataclass.*;

public class Diagnostor {
	private static int pk = 2;// 同时发生的物理故障数上限
	private static int vk = 3;// 同时发生的虚拟故障数上限
	public static int faultSymptom = 0;
	public double average_substrate = 0.005; // 物理故障节点正态分布均值
	public double average_virtual = 0.005; // 虚拟故障节点正态分布均值
	public double variance_substrate = 0.003; // 物理故障节点正态分布方差
	public double variance_virtual = 0.002; // 虚拟故障节点正态分布方差
	public static float vir_rateofnodenoise = 0.1f; // 虚拟网络故障概率
	public static float phy_rateofnodenoise = 0.1f;  //  物理网络故障概率
	public static float vir_faultrateoprobenoise = 0.01f;   //虚拟网络泄露概率
	public static float phy_faultrateonprobenoise = 0.01f; // 物理网络泄露概率
	private DependencyMatrix[] dMatrix;
	private ArrayList<String> faultNodes_substrate;// 物理故障节点
	private ArrayList<String> faultNodes_virtual;// 虚拟故障节点
	private ArrayList<String> faultEdges_virtual;// 虚拟故障链路

	public Diagnostor() {
	}

	public Diagnostor(DependencyMatrix dMatrix1, DependencyMatrix dMatrix2,
			double average_virtual, double variance_virtual,double average_substrate, double variance_substrate,int virMaxNum,int phyMaxNum) {
		dMatrix = new DependencyMatrix[2];
		this.dMatrix[0] = dMatrix1;
		this.dMatrix[1] = dMatrix2;
		faultNodes_substrate = new ArrayList<String>();
		faultNodes_virtual = new ArrayList<String>();
		faultEdges_virtual = new ArrayList<String>();
		this.average_substrate = average_substrate;
		this.average_virtual = average_virtual;
		this.variance_substrate = variance_substrate;
		this.variance_virtual = variance_virtual;
		Diagnostor.pk = phyMaxNum;
		Diagnostor.vk = virMaxNum;
	}

	/**
	 * 初始化网络状态
	 */
	public void InitialNetStatus() {
		ResetNodeStatus(); // 重置节点状态
		ResetProbeStatus(); // 重置指针状态
	
	}
	/**
	 * 初始化故障探测概率
	 */
	public static void InitialDetectStatus(float vir_rateofnodenoise, float vir_faultrateoprobenoise, float phy_rateofnodenoise, float phy_faultrateonprobenoise){
		Diagnostor.vir_rateofnodenoise = vir_rateofnodenoise;
		Diagnostor.vir_faultrateoprobenoise = vir_faultrateoprobenoise;
		Diagnostor.phy_rateofnodenoise = phy_rateofnodenoise;
		Diagnostor.phy_faultrateonprobenoise = phy_faultrateonprobenoise;
	}
	/**
	 * 探测函数
	 * 
	 * @param dMatrix
	 */
	public String DiagnostRound(int index, ArrayList<Probe> probes) {
		
		String diagnostResult = SendDetectionProbes(index, probes); // 发送探针
		
		/*Vector<Integer> result = new Vector<Integer>(probes.size());
		// 获得探针状态并返回
		for (Probe probe : probes) {
			if (probe.getStatus() == ProbeStatus.Failed) {
				result.add(2); // 失败

			} else
				result.add(1); // 成功
		}*/
		return diagnostResult;
	}

	/**
	 * 重置节点状态
	 */
	private void ResetNodeStatus() {
		/*
		 * for(int i=0;i!=2;++i){ ArrayList<Node>
		 * nodes=dMatrix[i].GetAllNodes(); for(Node node:nodes){
		 * node.setStatus(true); } if(i==0){ ArrayList<Edge>
		 * edges=dMatrix[i].GetAllEdges(); for(Edge edge:edges){
		 * edge.setStatus(true); } } }
		 */
		/*
		ArrayList<Node> nodes = dMatrix[1].GetAllNodes();
		for (Node node : nodes) {
			node.setStatus(true);
		}
		*/
	}

	private void ResetProbeStatus() {
		/*
		 * for(int i=0;i!=2;++i){ ArrayList<Probe>
		 * probes=dMatrix[i].GetAllProbes(); for(Probe probe:probes){
		 * probe.setStatus(ProbeStatus.Success); } }
		 
		ArrayList<Probe> probes = dMatrix[1].GetAllProbes();
		for (Probe probe : probes) {
			probe.setStatus(ProbeStatus.Success);
		}
		*/
	}

	public void CreateFaultNodes_Virtual() {
		ArrayList<Node> allNodes = dMatrix[0].GetAllNodes();      //虚拟链路节点
		Random rm = new Random();
		float[] rateByGauss = new float[allNodes.size()];
		StringBuffer spp = new StringBuffer();// 存取先验概率
		System.out.println("生成虚拟故障............");
		for (int i = 0; i < allNodes.size(); i++) {
			double rate = rm.nextGaussian() * (Math.sqrt(variance_virtual))
					+ average_virtual; // 正态分布随机概率
			rate = rate < 0 ? 0 : rate; // 概率范围［0,1］
			rate = rate > 1 ? 1 : rate;
			rate = dMatrix[0].getNodeStations().contains(allNodes.get(i)) ? 0
					: rate;// 探测基站的概率为0
			rateByGauss[i] = (float) rate;
			if (rm.nextDouble() < rate && faultNodes_virtual.size() < vk) {
				allNodes.get(i).setStatus(true);
				String faultNo = allNodes.get(i).getID().toString()
						.substring(1);
				faultNodes_virtual.add(faultNo);
				System.out.println("节点" + faultNo+"发生故障；");
			}
			spp.append(rate + "+");
		}// 独立节点故障
		// 写入文件
		OutputToFile.stringToFile(spp.toString(), "VirtualProbability.txt");// 写入虚拟先验概率
		OutputToFile.stringArrayListToFile(faultNodes_virtual, "VirtualFaultNodes.txt");// 写入虚拟故障节点
		OutputToFile.stringArrayListToFile(faultEdges_virtual, "VirtualFaultEdges.txt");// 写入虚拟故障链路
	}

	public String getvirFaultInformation(){
		String virFault_Result ="";
		for(String str:faultNodes_virtual)
			virFault_Result += "节点" + str + "发生故障\n";
		return virFault_Result;
	}
	public String getphyFaultInformation(){
		String phyFault_Result = "";
		for(String str:faultNodes_substrate)
			phyFault_Result += "节点 " + str + " 发生故障\n";
		return phyFault_Result;
	}
	/**
	 * 通过正态分布产生不固定的物理故障节点概率
	 */

	public void CreateFaultNodes_Substrate() {
		ArrayList<Node> allNodes = dMatrix[1].GetAllNodes();
		Random rm = new Random();
		float[] rateByGauss = new float[allNodes.size()];
		StringBuffer spp = new StringBuffer();// 存取先验概率
		System.out.println("生成物理故障............");
		for (int i = 0; i < allNodes.size(); i++) {
			double rate = rm.nextGaussian() * (Math.sqrt(variance_substrate)) + average_substrate; // 正态分布随机概率
			rate = rate < 0 ? 0 : rate; // 概率范围［0,1］
			rate = rate > 1 ? 1 : rate;
			rate = dMatrix[1].getNodeStations().contains(allNodes.get(i)) ? 0 : rate;// 探测基站的概率为0
			rateByGauss[i] = (float) rate;
			if (rm.nextDouble() < rate && faultNodes_substrate.size() < pk) {
				allNodes.get(i).setStatus(true);
				String faultNo = allNodes.get(i).getID().toString()
						.substring(1);
				faultNodes_substrate.add(faultNo);
				int tempint=Integer.parseInt(faultNo);
				Parameter.phyFaultNo.add(tempint);
				System.out.println("节点" + faultNo+"发生故障；");
			}
			spp.append(rate + "+");
		}
		// 写入文件
		OutputToFile.stringToFile(spp.toString(), "SubstrateProbability.txt");// 写入物理先验概率
		OutputToFile.stringArrayListToFile(faultNodes_substrate, "SubStrateFaultNodes.txt");// 写入物理故障节点
	}

	/**
	 * 发送探测包并加入噪声处理
	 */
	private String SendDetectionProbes(int index, ArrayList<Probe> detectionProbe) {
		StringBuffer buffer = new StringBuffer();
		if(index== 0){      //虚拟网络故障探测
			int x= 0;
			for (Probe probe : detectionProbe) {
				buffer.append("探针 " + probe.getID() + ": ");
				ArrayList<Node> nodes = dMatrix[index].GetNodesforProbe(probe);
				for (int i = 0; i < nodes.size(); i++) {
					Node node = nodes.get(i);
					if (i < nodes.size() - 1) {
						buffer.append(node.getID().toString().substring(1) + "-");
					} else {
						buffer.append(node.getID().toString().substring(1)+", ");
					}
					if (node.getStatus() == true) {
						probe.setStatus(ProbeStatus.Failed);
					}
				}
				AddNoise(buffer, probe, vir_faultrateoprobenoise,vir_rateofnodenoise);
				Parameter.virProbePath.add(buffer.toString());
			}// 加入探针噪声处理
		}
		if(index== 1){     //物理网络故障探测
			for (Probe probe : detectionProbe) {
				buffer.append("探针 " + probe.getID() + ": ");
				ArrayList<Node> nodes = dMatrix[index].GetNodesforProbe(probe);
				for (int i = 0; i < nodes.size(); i++) {
					Node node = nodes.get(i);
					if (i < nodes.size() - 1) {
						buffer.append(node.getID().toString().substring(1) + "-");
					} else {
						buffer.append(node.getID().toString().substring(1)+", ");
					}
					if (node.getStatus() == true) {
						probe.setStatus(ProbeStatus.Failed);
					}
				}
				AddNoise(buffer, probe,phy_faultrateonprobenoise,phy_rateofnodenoise);
				Parameter.phyProbePath.add(buffer.toString());
			}// 加入探针噪声处理
		}
		return buffer.toString();
	}

	/**
	 * 加入探针路径的噪声处理
	 */
	private void AddNoise(StringBuffer buffer, Probe probe, float faultrateonprobenoise, float rateofnodenoise) {
		Random rd = new Random();
		if (probe.getStatus() == ProbeStatus.Success
				&& rd.nextDouble() < faultrateonprobenoise) {
			probe.setStatus(ProbeStatus.Failed);
		}
		if (probe.getStatus() == ProbeStatus.Failed
				&& rd.nextDouble() > (1 - rateofnodenoise)) {
			probe.setStatus(ProbeStatus.Success);
		}
		if (probe.getStatus() == ProbeStatus.Success) {
			buffer.append("探测成功！\n");
		} else if (probe.getStatus() == ProbeStatus.Failed) {
			buffer.append("探测失败！\n");
		}
	}
}
