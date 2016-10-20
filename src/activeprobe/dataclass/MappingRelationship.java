package activeprobe.dataclass;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Hashtable;

import activeprobe.diagnosis.JavaMat;

public class MappingRelationship {
	private Hashtable<String, String> NodeMap;// 前指物理，后指虚拟
	private Hashtable<Topo_Path, String> EdgeMap;
	private ArrayList<Topo_Path> SubstrateEdge;
	private static String filename = JavaMat.initUrl + "output\\repeatedge.txt";

	public MappingRelationship() {
		NodeMap = new Hashtable<String, String>();
		EdgeMap = new Hashtable<Topo_Path, String>();
		SubstrateEdge = new ArrayList<Topo_Path>();
	}

	public void init(String fileName) {
		try {
			RandomAccessFile readFile = new RandomAccessFile(fileName, "r");
			String line;
			line = readFile.readLine();
			String[] temp = line.split(" ");
			int nodeNum = Integer.parseInt(temp[0]);
			int edgeNum = Integer.parseInt(temp[1]);
			for (int i = 0; i != nodeNum; ++i) {
				line = readFile.readLine();
				String[] tempNode = line.split(" ");
				NodeMap.put(tempNode[1], tempNode[0]);
			}

			for (int i = 0; i != edgeNum; ++i) {
				line = readFile.readLine();
				Topo_Path substrate = new Topo_Path();
				String[] tempEdge = line.split("I"); // 虚拟链路
				String[] tempNode1 = tempEdge[0].split(" ");
				if (tempEdge[1].contains("#")) {
					String[] tempNode2 = tempEdge[1].split("#");
					EdgeToFile(nodeNum + i, tempNode2[0], tempNode2[1]);
					String[] tempNode3 = tempNode2[0].split(" ");
					String[] tempNode4 = tempNode2[1].split(" ");
					for (int j = 0; j != tempNode3.length; ++j) {
						substrate.add(tempNode3[j]);
					}
					for (int k = 0; k != tempNode4.length; ++k) {
						boolean check = false;
						for (int l = 0; l != substrate.size(); ++l) {
							if (substrate.get(l).equals(tempNode4[k])) {
								check = true;
							}
						}
						if (check == false) {
							substrate.add(tempNode4[k]);
						}
					}
				} else {
					String[] tempNode2 = tempEdge[1].split(" ");
					substrate.clear();
					for (int j = 0; j != tempNode2.length; ++j) {
						substrate.add(tempNode2[j]);
					}
				}
				SubstrateEdge.add(substrate);
				EdgeMap.put(substrate, tempNode1[0]); // 存储虚拟链路对应的物理链路
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found!!!topologyGenerate.readInetFile  ");
		} catch (IOException e) {
			System.out.print("IO error");
		}
	}

	public void EdgeToFile(int num, String EdgeA, String EdgeB) {
		File MyFile = new File(filename);
		try {
			MyFile.createNewFile();
			FileOutputStream txtfile = new FileOutputStream(MyFile);
			PrintStream p = new PrintStream(txtfile);
			p.println(num);
			p.println(EdgeA);
			p.println(EdgeB);
			p.close();
			txtfile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Hashtable<String, String> GetNode() {
		return NodeMap;
	}

	public Hashtable<Topo_Path, String> GetEdge() {
		return EdgeMap;
	}

	public ArrayList<Topo_Path> GetSubEdge() {
		return SubstrateEdge;
	}

	public int GetSize() {
		return NodeMap.size() + EdgeMap.size();
	}
}
