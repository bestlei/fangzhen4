package embed;
//��������������
//import embed.Request_Relate;
public class VN_Request {
	
	public int nodes;			//�ڵ����
	public int links;			//��·����
	public int split;			//�Ƿ�����ָ�
	public int time;			//����ʱ��
	public int topo;			//��������
	public int duration;		//����ʱ��	
	
	public double revenue;		//��λʱ�����������
	public double[] cpu;		//�ڵ��CPU,
	public Link[] link;			//��·����
//public Request_Relate[] relate;
	
	public VN_Request(){
		cpu = new double[100];		//ԭC++ʹ���˺궨��MAX_REQ_NODES = 100;
		link = new Link[100];		//ԭC++ʹ���˺궨��MAX_REQ_LinkS = 100;
		
	}
}