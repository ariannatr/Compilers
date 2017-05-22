package compiler;

import java.util.ArrayList;
import java.util.*;

public class FunctionSum 
{
	public String name;
	public String type;
	public FunctionSum belongs;
	public ArrayList<FunctionSum> fun;
	public ArrayList<VarSum> arg;
	public ArrayList<VarSum> vars;
	public boolean dec;
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
	public String findparametertype(String var)
	{
		Iterator<VarSum> itr=this.arg.iterator();
		while(itr.hasNext())
		{
			VarSum ret=itr.next();
			if(ret.name.equals(var))
			{
				return ret.type;
			}   
		}
		return "NULL";
	}

	public String findvariabletype(String var)
	{
		Iterator<VarSum> itr=this.vars.iterator();
		while(itr.hasNext())
		{
			VarSum ret=itr.next();
			if(ret.name.equals(var))
			{
				return ret.type;
			}   
		}
		return "NULL";
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
	public FunctionSum getFunction(String name)
	{
		Iterator<FunctionSum> itr=this.fun.iterator();
		while(itr.hasNext())
		{
			FunctionSum ret=itr.next();
			String fun=ret.name.replaceAll(" ","");
			if(fun.equals(name.replaceAll(" ","")))
			{
				return ret;
			}   
		}
		return null;
	}
	public boolean exist_name(String na)
	{
		String na2=na.replaceAll(" ","");
		if(name.equals(na))
			return true;
		for(FunctionSum fa:fun)
		{
			System.out.println("fun1 "+fa.name+ " to compare "+na);
			String comp=fa.name.replaceAll(" ","");
			if(comp.equals(na2))
			{
				System.out.println("found");
				return true;
			}
		}
		return false;
	}

}