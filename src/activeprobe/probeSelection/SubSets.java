package activeprobe.probeSelection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;


class MySet<T extends Comparable<T>>
{
    HashSet<T> data;
    public MySet()
    {
    	this.data=new HashSet<T>();
    }
    //添加操作
    public void AddElement(T o)
    {   
    	if(this.data.contains(o)) return;        
        this.data.add(o);      
    }
    public void AddElement(ArrayList<T> objs)
    {
    	this.data.addAll(objs);
    }
    public void AddElement(T[] objs)
    {
    	for(int i=0;i<objs.length;i++)
    	{
    		T o=objs[i];
    		if(this.data.contains(o)) continue;
    		this.data.add(o);    		
    	}
    }
    public void AddElement(MySet<T> s)
    {
        HashSet<T> se=s.GetAllElements();
        this.data.addAll(se);
    }
    
    //删除操作
    public void DeleteElement(T o)
    {  
    	this.data.remove(o);    	
    }
    public void DeleteElement(MySet<T> s)
    {
    	this.data.removeAll(s.GetAllElements());
    }
    //访问操作：获取集合元素个数
    public int GetElementCount()
    {
    	return this.data.size();
    }
    
    //获取集合的所有元素
    public HashSet<T> GetAllElements()
    {
    	return this.data;
    }
    //判断集合是否包含某个元素
    public boolean ContainElement(T o)
    {
    	return this.data.contains(o);
    }
    
    //集合运算：并
    public MySet<T> union(MySet<T> s1)
    {
    	MySet<T> uSet=new MySet<T>();
    	uSet.AddElement(this);
    	uSet.AddElement(s1);
    	return uSet;
    }
//  集合运算：交
    public  MySet<T> interset(MySet<T> s2)
    {   
    	MySet<T> iSet=new MySet<T>();
    	iSet.AddElement(this);
    	iSet.data.retainAll(s2.GetAllElements());
    	return iSet;
    }
//  集合运算：减
    public  MySet<T> minus(MySet<T> s2)
    {   
    	MySet<T> mSet=new MySet<T>();
    	mSet.AddElement(this);
    	mSet.DeleteElement(s2);
    	return mSet;
    }
    //集合运算：判断是否子集
    public  boolean isSubSet(MySet<T> s2)
    {
      return this.data.containsAll(s2.data);
    }
    
    public void PrintElement()
    {
    	HashSet<T> subelements=this.GetAllElements();
		Iterator<T> itr=subelements.iterator();
    	while(itr.hasNext())
    	{
		  System.out.printf("%6s",itr.next());		  
		}
		System.out.print("\n");
    }
}

public class SubSets<T extends Comparable<T>>  {
	ArrayList<MySet<T>> AllSubSet;                     
	ArrayList<MySet<T>> TempSubSet;
	ArrayList<T>        Nodes;
	int ElementCount;
	
	//构造函数，参数为一个集合：初始化子集列表为一个子集，即为参数输入的集合
	public SubSets(ArrayList<T> initset)
	{
		this.AllSubSet=new ArrayList<MySet<T>>();
		this.Nodes    =new ArrayList<T>();
		MySet<T> s=new MySet<T>();
	    s.AddElement(initset);
	    this.AllSubSet.add(s);
	    this.ElementCount=initset.size();
	    this.Nodes.addAll(initset);
	}
	
	public SubSets()
	{
		 this.AllSubSet=new ArrayList<MySet<T>>();
		 this.Nodes    =new ArrayList<T>();
		 this.ElementCount=0;
	}
	
	public void SetElements(ArrayList<T> initset)
	{
		this.AllSubSet.clear();
		MySet<T> s=new MySet<T>();
	    s.AddElement(initset);
	    this.AllSubSet.add(s);
	    this.ElementCount=initset.size();
	    this.Nodes.addAll(initset);
	}
	/*
	 * 根据输入的集合，分裂所有的子集：如果某个子集包输入集合中的部分元素，那么这些元素将分裂为一个独立的子集；
	 * 如果一个子集中没有包含输入集合的元素或全部为输入集合中的元素，则不需要分裂
	*/
	public void SplitSubSet(ArrayList<T> input)
	{
		MySet<T> inputset=new MySet<T>();
		inputset.AddElement(input);
		ArrayList<MySet<T>> newAllSubSet=new ArrayList<MySet<T>>();
		for(int i=0;i<AllSubSet.size();i++)
		{
			MySet<T> subset=AllSubSet.get(i);
			MySet<T> mset=subset.minus(inputset);
			if(mset.GetElementCount()!=0&&mset.GetElementCount()<subset.GetElementCount())
			{
				MySet<T> mmset=subset.minus(mset);
				newAllSubSet.add(mset);
				newAllSubSet.add(mmset);
			}
			else{
				newAllSubSet.add(subset);
			}
		}
		this.AllSubSet=newAllSubSet;
	}
	public void SplitSubSet(T[] input)
	{
		MySet<T> inputset=new MySet<T>();
		inputset.AddElement(input);
		ArrayList<MySet<T>> newAllSubSet=new ArrayList<MySet<T>>();
		for(int i=0;i<AllSubSet.size();i++)
		{
			MySet<T> subset=AllSubSet.get(i);
			MySet<T> mset=subset.minus(inputset);
			if(mset.GetElementCount()!=0&&mset.GetElementCount()<subset.GetElementCount())
			{
				MySet<T> mmset=subset.minus(mset);
				newAllSubSet.add(mset);
				newAllSubSet.add(mmset);
			}
			else{
				newAllSubSet.add(subset);
			}
		}
		this.AllSubSet=newAllSubSet;
	}
	
	public void TrySplitSubSet(ArrayList<T> input)
	{
		MySet<T> inputset=new MySet<T>();
		inputset.AddElement(input);
	    this.TempSubSet=new ArrayList<MySet<T>>();
		for(int i=0;i<AllSubSet.size();i++)
		{
			MySet<T> subset=AllSubSet.get(i);
			MySet<T> mset=subset.minus(inputset);
			if(mset.GetElementCount()!=0&&mset.GetElementCount()<subset.GetElementCount())
			{
				MySet<T> mmset=subset.minus(mset);
				this.TempSubSet.add(mset);
				this.TempSubSet.add(mmset);
			}
			else{
				this.TempSubSet.add(subset);
			}
		}		
	}
	
	public void ReSet()
	{
		this.AllSubSet.clear();
		MySet<T> set=new MySet<T>();
	    set.AddElement(this.Nodes);
	    this.AllSubSet.add(set);
	}
	
	public void PrintAllElements()
	{
		
		for(int i=0;i<this.AllSubSet.size();i++){
		 
			MySet<T> s=AllSubSet.get(i);	
			HashSet<T> subelements=s.GetAllElements();
			Iterator<T> itr=subelements.iterator();
			while(itr.hasNext())
			{
			  System.out.print(itr.next());			  
			}
			System.out.print("\n");
		}
	}
	//计算分组引起的熵值
	public double CaculateEntropy()
	{ 
		double entropy=0;
		for(int i=0;i<this.AllSubSet.size();i++)
		{
			MySet<T> tempset=this.AllSubSet.get(i);
			int ni=tempset.GetElementCount();
			entropy+=((double)ni/this.ElementCount)*Math.log10(ni);
		}
		return entropy;
	}
	public double TryCaculateEntropy()
	{ 
		double entropy=0;
		for(int i=0;i<this.TempSubSet.size();i++)
		{
			MySet<T> tempset=this.TempSubSet.get(i);
			int ni=tempset.GetElementCount();
			entropy+=((double)ni/this.ElementCount)*Math.log10(ni);
		}
		return entropy;
	} 
	
//	public SubSets<T> Clone()
//	{
//		SubSets<T> s=new SubSets<T>();
//	}
	/*
   public static void main(String[] args)
   {  
	   ArrayList<String> a1=new ArrayList<String>();
	   a1.add("happy");
	   a1.add("good");
	   a1.add("shit");
	   a1.add("good");
	   a1.add("how");
	   SubSets<String> subsets=new SubSets<String>(a1);
	   
	   subsets.SplitSubSet(new String[]{"good","shit"});
	   subsets.SplitSubSet(new String[]{"good","baby"});
	   subsets.PrintAllElements(); 
   }
	*/
}
