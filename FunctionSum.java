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


	public FunctionSum get_function_from_Symboltable(String function_name)
	{
		System.out.println("Psaxnw sto symboltable tin sunartisi "+function_name+"kai twra eimi stin "+this.name);
		if(this.name.replaceAll(" ","").equals(function_name.replaceAll(" ","")))
		{

			return this;
		}
		for(FunctionSum function:this.fun)
		{
			//Iterator<FunctionSum> itr=this.fun.iterator();
			//while(itr.hasNext())
			//{
			//	FunctionSum ret=itr.next();
				String  comp=function.name.replaceAll(" ","");
				if(comp.equals(function_name.replaceAll(" ","")))
				{
					return function;
				}
				else
					return function.get_function_from_Symboltable(function_name);   
			//}
		}
		return null;

	}

	public boolean get_variable_ref(int var)
	{
		System.out.println("Psaxnw stin sunartisi "+this.name+"tin parametro "+Integer.toString(var));
		int num=0;
		Iterator<VarSum> itr=this.arg.iterator();
		while(itr.hasNext())
		{
			VarSum ret=itr.next();
			if(num==var)
			{	
				if(!ret.ref.replaceAll(" ","").equals(""))
					return true;
				else
					return false;
			}   
			num++;
		}
		System.out.println("Den eprepe n bainw edw");
		return false;
	}



	public boolean findparameter(String var)
	{
		Iterator<VarSum> itr=this.arg.iterator();
		while(itr.hasNext())
		{
			VarSum ret=itr.next();
			String  comp=ret.name.replaceAll(" ","");
			if(comp.equals(var.replaceAll(" ","")))
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
			String comp=ret.name.replaceAll(" ","");
			if(comp.equals(var.replaceAll(" ","")))
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
			String comp=ret.name.replaceAll(" ","");
			if(comp.equals(var.replaceAll(" ","")))
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
			String comp=fa.name.replaceAll(" ","");
			if(comp.equals(na2))
			{
				return true;
			}
		}
		return false;
	}

}
