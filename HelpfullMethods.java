package compiler;

import java.util.ArrayList;
import java.util.*;

public class HelpfullMethods 
{
	public int instruction_num;
	public ArrayList<String> instruction_list;
	public HelpfullMethods()
	{
		instruction_num=1;
		instruction_list=new ArrayList<String>();
	}

	public int nextquad()
	{
		return instruction_num;
	}

	public String genquad(String op,String x,String y,String z)
	{	
		String value=Integer.toString(instruction_num)+": "+op+","+x+","+y+","+z+"\n";
		instruction_num++;
		return value;
	}

	public ArrayList<String> backpatch(ArrayList<String> list,String z)
	{
		Iterator<String> itr=list.iterator();
		ArrayList<String> modified=new ArrayList<String>();
		while(itr.hasNext())
		{
			String ret=itr.next();
			modified.add(ret.replaceAll("_",z));
			//String fun=ret.name.replaceAll(" ","");
		}
		return modified;
	}

	public void modifiyquad(String num,String value)
	{
		Iterator<String> itr=this.instruction_list.iterator();
		while(itr.hasNext())
		{
			String ret=itr.next();
			if(ret.substring(0,1).equals(num))
			{
				ret=ret.replaceAll("\\*",value);
				this.instruction_list.set(Integer.parseInt(num)-1,ret);
				return;
			}
		}
		return;
	}

	public ArrayList<String> makelist(String x)
	{
		ArrayList<String> list=new ArrayList<String>();
		list.add(x);
		return list;
	}

	public void print_instructions()
	{
		Iterator<String> itr=this.instruction_list.iterator();
		while(itr.hasNext())
		{
			String ret=itr.next();
			System.out.print(ret);
		}
		return;
	}
	
}
