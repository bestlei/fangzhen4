package embed;
//����������

public class SN_Network {
	
	public int nodes;	//�ڵ����
	public int links;	//��·����
	public double[] cpu;	//�ڵ��CPU,ԭC++ʹ���˺궨��MAX_REQ_NODES = 100;
	public Link[] link;//��·����ԭC++ʹ���˺궨��MAX_REQ_LinkS = 100;
	public int[] x;
	public int[] y;
	
	
	public SN_Network(){
		cpu = new double[1000];
		link = new Link[10000];
		x = new int[1000];
		y = new int[1000];
	}
}
