package compiler;

import java.util.ArrayList;
import java.util.*;

public class FunctionSum 
{
	public String name;
	public String type;
	public String belongs;
	public ArrayList<FunctionSum> fun;
	public ArrayList<VarSum> arg;
	public ArrayList<VarSum> vars;
	public FunctionSum(String n)
	{
		name=n;
		vars = new ArrayList<VarSum>();
		arg= new ArrayList<VarSum>();
		fun= new ArrayList<FunctionSum>();
	}

	public boolean findparameter(String var)
	{
		Iterator<VarSum> itr=this.arg.iterator();
		while(itr.hasNext())
		{
			VarSum ret=itr.next();
			if(ret.name.equals(var))
			{
				return true;
			}   
		}
		return false;
	}

	public boolean findvariable(String var)
	{
		Iterator<VarSum> itr=this.vars.iterator();
		while(itr.hasNext())
		{
			VarSum ret=itr.next();
			if(ret.name.equals(var))
			{
				return true;
			}   
		}
		return false;
	}
	
	public boolean findfunction(FunctionSum fun)
	{
		Iterator<FunctionSum> itr=this.fun.iterator();
		while(itr.hasNext())
		{
			FunctionSum ret=itr.next();
			if(ret.name.equals(fun.name))
			{
				return true;
			}   
		}
		return false;
	}
	public boolean exist_name(String na)
	{
		if(name.equals(na))
			return true;
		for(FunctionSum fa:fun)
		{
			if(fa.name.equals(na))
				return true;
		}
		return false;
	}

}